# FLight Calculator app
Web Application for calculating paths through point list in three-dimensional flat space and saving it as temporal point list. 
Calculations use constants:
1. precision for saving temporal plane point through path = 1 second
2. airplane characteristics are individual for each plane and declaring in model class [AirplaneCharacteristics](src/main/java/com/goose/calculator/model/AirplaneCharacteristics.java)

## Stack
1. mongodb as database
2. spring boot as backend
3. react, mui and three.js as frontend

## Run 
0. open terminal
####
1. in app folder insert
    ```docker
    docker compose up -d
    ```
2. to Demo Tab

### Demo
1. open [localhost:3000](http://localhost:3000) page in browser
    ![](examples/Airplanes_page.gif)
There are some features like popup plane characteristics and position or adding/deleting/downloading functionality on this page.
####
2. double-click on plane row
    ![](examples/Flights_page.gif)
There are the same features on this one and a new one: "Calculate Flight" which do calculations that needed for next page. 
####
3. double-click on a flight row
    ![](examples/Flight_page.gif)
This page is for step by step flight`s schematic visualisation through waypoints. 
User have configs to see details airplane characteristics through way.
####
4. Also you can work with endpoints directly in swagger or with postman for example - [localhost:8080](http://localhost8080/)
