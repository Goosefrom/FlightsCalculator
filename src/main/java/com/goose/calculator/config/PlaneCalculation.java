package com.goose.calculator.config;

import com.goose.calculator.exception.AirplaneException;
import com.goose.calculator.exception.ErrorType;
import com.goose.calculator.model.Airplane;
import com.goose.calculator.model.TemporaryPoint;
import com.goose.calculator.model.WayPoint;
import com.goose.calculator.model.AirplaneCharacteristics;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.util.*;

import static java.lang.Math.*;

@Slf4j
@Configuration
public class PlaneCalculation {

    public List<TemporaryPoint> calculateRoute(Airplane airplane, List<WayPoint> wayPoints) {

        AirplaneCharacteristics characteristics = airplane.getAirplaneCharacteristics();

        handleExceptions(wayPoints, characteristics);

        List<TemporaryPoint> flightPath = new ArrayList<>();
        TemporaryPoint currentPosition = airplane.getPosition();
        flightPath.add(currentPosition);

        double distance;
        for (WayPoint nextWayPoint : wayPoints) {

            //get plane current position in space
            currentPosition = flightPath.get(flightPath.size() - 1);

            //calculate distance between current position and next waypoint position
            distance = calculateDistance(currentPosition, nextWayPoint);
            log.info("s={}", distance);

            //if distance = 0 add same point to flight path
            if (distance == 0) flightPath.add(currentPosition);
            else {
                //calculate angles for Ox, Oy, Oz
                double angleX = calculateAngle(currentPosition.getLatitude(), nextWayPoint.getLatitude(), distance);
                double angleY = currentPosition.getCourse();
                double angleZ = calculateAngle(currentPosition.getFlightAltitude(), nextWayPoint.getFlightAltitude(), distance);

                //calculate course for next waypoint and delta between current and next courses
                double nextWayPointCourse = calculateCourse(currentPosition, nextWayPoint, distance);
                double dCourse = nextWayPointCourse - currentPosition.getCourse();
                if (abs(nextWayPointCourse + 360 - currentPosition.getCourse()) < abs(dCourse)) dCourse += 360;

                //calculate time need to pass distance
                double timeToReachV = calculateTime(currentPosition.getFlightSpeed(), nextWayPoint.getFlightSpeed(), distance);

                //calculate acceleration to reach next waypoint with those speed and time to reach Ox, Oy, Oz based on variants of acceleration`s existing
                double timeToReachX, timeToReachY;
                double acceleration = calculateAcceleration(currentPosition.getFlightSpeed(), nextWayPoint.getFlightSpeed(), timeToReachV, characteristics);
                if (acceleration == 0) {
                    timeToReachV = 0;

                    timeToReachX = (nextWayPoint.getLatitude() - currentPosition.getLatitude())
                            / (nextWayPoint.getFlightSpeed() * cos(toRadians(angleX)));

                    timeToReachY = (nextWayPoint.getLongitude() - currentPosition.getLongitude())
                            / (nextWayPoint.getFlightSpeed() * cos(toRadians(nextWayPointCourse)));
                }
                else {
                    if (abs(acceleration) > characteristics.getMaxAcceleration()) {
                        acceleration = characteristics.getMaxAcceleration() * acceleration / abs(acceleration);
                        timeToReachV = (nextWayPoint.getFlightSpeed() - currentPosition.getFlightSpeed()) / acceleration;
                    }

                    timeToReachX = calculateTimeToReachO(cos(toRadians(angleX)) * acceleration / 2.0,
                            currentPosition.getFlightSpeed() * cos(toRadians(angleX)),
                            nextWayPoint.getLatitude() - currentPosition.getLatitude());

                    timeToReachY = calculateTimeToReachO(cos(toRadians(nextWayPointCourse)) * acceleration / 2.0,
                            currentPosition.getFlightSpeed() * cos(toRadians(nextWayPointCourse)),
                            nextWayPoint.getLongitude() - currentPosition.getLongitude());
                }

                double timeToReachZ = (nextWayPoint.getFlightAltitude() - currentPosition.getFlightAltitude())
                        / (characteristics.getRateAltitudeChange() * cos(toRadians(angleZ)));


                //choose maximum time for loop
                double time = max(max(timeToReachV, timeToReachZ), max(timeToReachX, timeToReachY));
                double a = calculateAcceleration(currentPosition.getFlightSpeed(), nextWayPoint.getFlightSpeed(), time, characteristics);
                double aX = calculateAccelerationO(currentPosition.getLatitude(), nextWayPoint.getLatitude(), currentPosition.getFlightSpeed(), time, cos(toRadians(angleX)));
                double aY = calculateAccelerationO(currentPosition.getLongitude(), nextWayPoint.getLongitude(), currentPosition.getFlightSpeed(), time, cos(toRadians(nextWayPointCourse)));
                double aZ = calculateAccelerationO(currentPosition.getFlightAltitude(), nextWayPoint.getFlightAltitude(), currentPosition.getFlightSpeed(), time, cos(toRadians(angleZ)));
                log.info("t={}", time);

                //create variables for calculation
                double x1, y1, z1, v, sZ, dCourseNow, dt = 0;

                //go to points calculation
                while (dt != time) {
                    //trip time
                    dt += min(1, time - dt);

                    //calculate course delta and course for this dt
                    dCourseNow = abs(dCourse) < characteristics.getRateCourseChange()
                            ? dCourse
                            : characteristics.getRateCourseChange() * dCourse / abs(dCourse);
                    angleY += dCourseNow;
                    angleY %= 360;
                    dCourse -= dCourseNow;

                    v = currentPosition.getFlightSpeed() + a * dt;
                    x1 = currentPosition.getLatitude() + currentPosition.getFlightSpeed() * dt * cos(toRadians(angleX)) + 0.5 * aX * pow(dt, 2);
                    y1 = currentPosition.getLongitude() + currentPosition.getFlightSpeed() * dt * cos(toRadians(angleY)) + 0.5 * aY * pow(dt, 2);
                    sZ = currentPosition.getFlightSpeed() * dt * cos(toRadians(angleZ)) + 0.5 * aZ * pow(dt, 2);
                    z1 = currentPosition.getFlightAltitude() + (abs(sZ) <= (characteristics.getRateAltitudeChange() * dt) ? sZ : characteristics.getRateAltitudeChange() * dt * sZ / abs(sZ));

                    //add new point to list
                    System.out.println(x1 + " " + y1 + " " + z1 + " " + v + " " + angleY + ";  dcourse " + dCourse + "; a " + acceleration);
                    flightPath.add(new TemporaryPoint(formatValue(x1), formatValue(y1), formatValue(z1), formatValue(v), angleY));
                }
            } 
        }
        return flightPath;
    }



    //meter
    private double calculateDistance(TemporaryPoint currentPoint, WayPoint nextPoint) {
        double dx = nextPoint.getLatitude() - currentPoint.getLatitude();
        double dy = nextPoint.getLongitude() - currentPoint.getLongitude();
        double dz = nextPoint.getFlightAltitude() - currentPoint.getFlightAltitude();
        return sqrt(pow(dx, 2.0) + pow(dy, 2) + pow(dz, 2.0));
    }

    //degree
    private double calculateCourse(TemporaryPoint point1, WayPoint point2, double distance) {
        double dy = point2.getLongitude() - point1.getLongitude();
        if (dy != 0) {
            double course;
            if(point2.getLatitude() >= point1.getLatitude()) course = toDegrees(acos(dy / distance));
            else course = toDegrees(acos(abs(dy) / distance) + PI);
            return course;
        }
        else return 90.0;
    }

    //degree
    private double calculateAngle(double point1, double point2, double distance) {
        double d = point2 - point1;
        return d != 0 ? toDegrees(acos(d / distance)) : 90.0;
    }

    //second
    private double calculateTime(double v0, double v1, double distance) {
        double time = 2.0 * distance / (v0 + v1);
        return Double.isFinite(time) ? time : 0;
    }

    // meter/second^2
    private double calculateAcceleration(double v0, double v1, double time, AirplaneCharacteristics characteristics) {
        double a = (v1 - v0) / time;
        return Double.isFinite(a) ? a : characteristics.getMaxAcceleration();
    }

    //second
    private double calculateTimeToReachO(double a, double b, double c) {
        double d = pow(b, 2) + 4 * a * c;
        if (d < 0) return 0;
        double sqrtD = sqrt(d);
        double t1 = ((-1) * b + sqrtD) / (2 * a);
        double t2 = ((-1) * b - sqrtD) / (2 * a);
        if (t1 < 0) return t2;
        else if (t2 < 0) return t1;
        else return min(t1, t2);
    }

    private double calculateAccelerationO(double p0, double p, double v0, double t, double cos) {
        return 2 * (p - p0 - v0 * t * cos) / pow(t, 2);
    }



    //0.000000000000 -> 0.000
    private static double formatValue(double n) {
        NumberFormat numberFormat = new DecimalFormat("#0.000");
        return Double.parseDouble(numberFormat.format(n).replace(',', '.'));
    }

    private static void handleExceptions(List<WayPoint> wayPoints, AirplaneCharacteristics characteristics) {
        for(WayPoint wayPoint : wayPoints) {
            //check waypoints for requirements
            //altitude cant be less than 0
            if (wayPoint.getFlightAltitude() < 0) {
                throw new AirplaneException(ErrorType.INTERNAL_ERROR,
                        MessageFormat.format("Waypoint N:{0} has negative altitude, it could be a miss click so change to positive value.",
                                wayPoints.indexOf(wayPoint)));
            }
            //flight speed cant be negative
            if (wayPoint.getFlightSpeed() < 0) {
                throw new AirplaneException(ErrorType.INTERNAL_ERROR,
                        MessageFormat.format("Waypoint N:{0} has negative speed, it could be a miss click so change to positive value.",
                                wayPoints.indexOf(wayPoint)));
            }
            //flight speed cant be more than characteristics.maxSpeed
            if (wayPoint.getFlightSpeed() > characteristics.getMaxSpeed()) {
                throw new AirplaneException(ErrorType.INTERNAL_ERROR,
                        MessageFormat.format("Waypoint N:{0} cant have such flight speed, change to max speed.",
                                wayPoints.indexOf(wayPoint)));
            }
        }
    }
}
