import { useState, useEffect, useRef } from 'react';
import { useLocation } from "react-router-dom";
import axios from 'axios';
import * as THREE from 'three';
import { ArcballControls } from 'three/addons/controls/ArcballControls.js';
import { CSS2DRenderer, CSS2DObject } from 'three/addons/renderers/CSS2DRenderer.js';
import { GUI } from 'three/addons/libs/lil-gui.module.min.js';
import disc from './textures/disc.png';

let graphRef, canvas, scene, camera, renderer, controls, labelRenderer, 
    texture, gui, guiSettings,
    airplaneOnGraph, labelObject;
    let airplaneObjectNow = {
        latitude: 0.0,
        longitude: 0.0,
        flightAltitude: 0.0,
        flightSpeed: 0.0,
        course: 0.0
    }

function Flight() {

    //Get Params for request
    const params = useLocation();
    const { airplaneId, flightNumber } = params.state;

    //Request
    const [data, setData] = useState({
        number : '',
        wayPoints : [],
        passedPoints : []
    });
    useEffect(() => {
        axios.get(`http://localhost:8080/airplanes/${airplaneId}/flights/${flightNumber}`)
            .then((response) => setData(response.data))
            .catch(error => console.log(error));
    }, [airplaneId, flightNumber]);

    // textures
    const loader = new THREE.TextureLoader();
    texture = loader.load( disc );
    texture.colorSpace = THREE.SRGBColorSpace;

    //Plot graph
    graphRef = useRef(null);
    useEffect(() => {
        if (!graphRef.current) return;
        const airplanePositionNow = data.passedPoints.at(-1);
        if (airplanePositionNow == null) return;

        canvas = graphRef.current;

        scene = new THREE.Scene();

        //axes
        const axesForMoreThanZero = new THREE.AxesHelper( 1000 );
        scene.add( axesForMoreThanZero );

        //camera
        camera = new THREE.OrthographicCamera( window.innerWidth / - 100, window.innerWidth / 100, window.innerHeight / 100, window.innerHeight / - 100, -1000, 1000 );
        camera.position.set( 3, -3, 2 );
        camera.up = new THREE.Vector3( 0, 0, 1 );

        //renderer
        renderer = new THREE.WebGLRenderer({ canvas: canvas, antialias: true, alpha: true });
        renderer.domElement.style.background = 'linear-gradient( 180deg, rgba(0, 0, 0, 0.8) 0%, rgba( 0,0,0,1 ) 100% )';
        renderer.setPixelRatio( window.devicePixelRatio );
        renderer.setSize( window.innerWidth, window.innerHeight );
        document.body.appendChild( renderer.domElement );
        
        // //label render
        labelRenderer = new CSS2DRenderer();
        labelRenderer.setSize( window.innerWidth, window.innerHeight );
        labelRenderer.domElement.style.position = 'absolute';
        labelRenderer.domElement.style.top = '0px';
        labelRenderer.domElement.style.pointerEvents = 'none';
        document.body.appendChild( labelRenderer.domElement );

        // Add plane object
        airplaneObjectNow = airplanePositionNow;

        // Add waypoints
        const wayPoints = [];
        data.wayPoints.forEach((wayPoint) => {
            const point = new THREE.Vector3(wayPoint.latitude, wayPoint.longitude, wayPoint.flightAltitude);
            wayPoints.push(point);
        });
        const wayPointsGeometry = new THREE.BufferGeometry().setFromPoints(wayPoints);
        const wayPointsMaterial = new THREE.PointsMaterial( { color : "red", map : texture, size : 10, sizeAttenuation : false } );
        const wayPointsOnGraph = new THREE.Points(wayPointsGeometry, wayPointsMaterial);
        scene.add(wayPointsOnGraph);

        // Add passedpoints
        const passedPoints = [];
        data.passedPoints.forEach(passedPoint => {
            const point = new THREE.Vector3(passedPoint.latitude, passedPoint.longitude, passedPoint.flightAltitude);
            passedPoints.push(point);
        });
        const passedPointsGeometry = new THREE.BufferGeometry().setFromPoints(passedPoints);
        const lineMaterial = new THREE.LineBasicMaterial({ color: "white" });
        const passedPointsMaterial = new THREE.PointsMaterial( { color : "white", map : texture, size : 5, sizeAttenuation : false} );
        const passedTraectory = new THREE.Line(passedPointsGeometry, lineMaterial);
        const passedPointsOnGraph = new THREE.Points(passedPointsGeometry, passedPointsMaterial);
        scene.add(passedTraectory);
        scene.add(passedPointsOnGraph);

        // Controls
        controls = new ArcballControls( camera, renderer.domElement, scene );
        controls.addEventListener( 'change', render );
        controls.cursorZoom = true;

        //gui with controls
        guiSettings = { 
            gizmoVisible: true,
            time: passedPoints.length - 1,
            airplaneVisible: false,
            labelVisible: function () {
                camera.layers.toggle(1);
            }
        };

        gui = new GUI();
        gui.add( controls, 'enabled' ).name( 'Enable controls' );
        gui.add( controls, 'enableRotate' ).name( 'Enable rotate' );
        gui.add( controls, 'enableZoom' ).name( 'Enable zoom' );
        gui.add( controls, 'cursorZoom' ).name( 'Cursor zoom' );
        gui.add( guiSettings, 'gizmoVisible' ).name( 'Show gizmos' ).onChange( function () {
            controls.setGizmosVisible( guiSettings.gizmoVisible );
        } );
        const airplaneControls = gui.addFolder( 'Airplane Controls' );
        airplaneControls.add(guiSettings, 'airplaneVisible' )
            .name( 'Show airplane' )
            .onChange(function () {
                if (guiSettings.airplaneVisible) {
                    putAirplaneOnGraph();
                    airplaneControls.controllersRecursive().at(1).enable();
                    airplaneControls.controllersRecursive().at(2).enable();    
    
                }
                else {
                    labelObject.removeFromParent();
                    airplaneOnGraph.removeFromParent();
                    airplaneControls.controllersRecursive().at(1).disable();
                    airplaneControls.controllersRecursive().at(2).disable();    

                }
            });
        airplaneControls.add(guiSettings, 'time', 0, passedPoints.length - 1, 1)
            .name( `Plane position on time ` )
            .onChange( function (timeStep) {
                labelObject.removeFromParent();
                airplaneOnGraph.removeFromParent();
                airplaneObjectNow = data.passedPoints.at(timeStep);
                putAirplaneOnGraph();
            } ).disable();
        airplaneControls.add(guiSettings, 'labelVisible').name( 'Toggle label' ).disable();
        gui.close();

        //Animate
        const animate = () => {
            requestAnimationFrame(animate);
            render();
        };
        animate();

    }, [data, ]);

    window.addEventListener( 'resize', onWindowResize );
    window.addEventListener( 'popstate', handleNavigationButtons );

    return (
        <div>
            <canvas ref={graphRef}></canvas>
        </div>
        
    );
}



function handleNavigationButtons() {    
    window.location.reload(true);
}

function onWindowResize() {
    camera.aspect = window.innerWidth / window.innerHeight;
    camera.updateProjectionMatrix();
    renderer.setSize( window.innerWidth, window.innerHeight );
    labelRenderer.setSize( window.innerWidth, window.innerHeight );
    render();
}

function createPointMesh(name, vector, size, color) {
    const geometry = new THREE.SphereGeometry(size);
    const material = new THREE.MeshBasicMaterial({color: color});
    const mesh = new THREE.Mesh(geometry, material);
    mesh.position.copy(vector);
    mesh.name = name;
    return mesh;
}

function putAirplaneOnGraph() {

    const airplaneVectorNow = new THREE.Vector3(
        airplaneObjectNow.latitude,
        airplaneObjectNow.longitude,
        airplaneObjectNow.flightAltitude
    );
    airplaneOnGraph = createPointMesh('airplane', airplaneVectorNow, 0.15, 'gray');
    scene.add(airplaneOnGraph);

    //label with characteristics
    const labelDiv = document.createElement('div');
    labelDiv.innerHTML = `Airplane characteristics` 
                            + `\nLatitude: ${airplaneObjectNow.latitude}`
                            + `\nLongitude: ${airplaneObjectNow.longitude}`
                            + `\nFlight altitude: ${airplaneObjectNow.flightAltitude}`
                            + `\nFlight speed: ${airplaneObjectNow.flightSpeed} m/s`
                            + `\nCourse: ${airplaneObjectNow.course} deg`;
    //labelDiv.innerHTML.style.width = '10px';
    labelDiv.style.whiteSpace = 'pre';
    labelDiv.style.padding = '2px';
    labelDiv.style.fontSize = '80%';
    labelDiv.style.border = 'thin dotted gray';
    labelDiv.style.color = 'white';
    labelDiv.style.backgroundColor = 'transparent';

    labelObject = new CSS2DObject(labelDiv);
    labelObject.position.set(1.5, 1.5, -1);
    airplaneOnGraph.add(labelObject);
    labelObject.layers.set(1);

}

function render() {
    renderer.render(scene, camera);
    labelRenderer.render( scene, camera );
}

export default Flight;