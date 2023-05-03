package UrbanTycoon;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */

import java.util.ArrayList;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author ati
 */
public class FireStationTest {
    
    static FireStation fireStation;
    static FireEngine fireEngine;
    static ArrayList<Road> route;
    static ResidentialZone residentialZone;
    static IndustrialZone industrialZone;
    
    @BeforeAll
    public static void setUpClass() {
       
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
        fireStation = new FireStation(1, 1, 1, 0, 0, 1, 1, null, 0.0);
        fireEngine = fireStation.getFireEngine();
        route = new ArrayList<>();
        Road firstRoad = new Road(1, 1, 1, 1, 1, 1, null, 0.0);
        Road secondRoad = new Road(2, 2, 2, 2, 2, 2, null, 0.0);
        Road lastRoad = new Road(3, 3, 3, 3, 3, 3, null, 0.0);
        route.add(firstRoad);
        route.add(secondRoad);
        route.add(lastRoad);
        residentialZone = new ResidentialZone(0.1, 1, 1, 1, 1, 1, 0.0, 0.0, 0, 0, 1, 1, null);
        industrialZone = new IndustrialZone(1, 1, 1, 1, 1, 0.0, 0.0, 0, 0, 1, 1, null);  
    }
    
    @AfterEach
    public void tearDown() {
        route.clear();
    }
    
    /**
     * route is null
     */
    @Test
    public void setRouteAndDestinationTest1() {
        fireEngine.setRouteAndDestination(null, residentialZone);
        assertTrue(fireEngine.isAvailable());
        assertNull(fireEngine.getDestination());
        assertFalse(fireEngine.isMovingBack());
    }
    
    /**
     * destination is null
     */
    @Test
    public void setRouteAndDestinationTest2() {
        fireEngine.setRouteAndDestination(route, null);
        assertTrue(fireEngine.isAvailable());
        assertNull(fireEngine.getDestination());
        assertFalse(fireEngine.isMovingBack());
    }
    
    /**
     * route and destination is not null
     */
    @Test
    public void setRouteAndDestinationTest3() {
        fireEngine.setRouteAndDestination(route, residentialZone);
        assertFalse(fireEngine.isAvailable());
        assertNotNull(fireEngine.getDestination());
        assertFalse(fireEngine.isMovingBack());
    }
    
    /**
     * route has next road
     */
    @Test
    public void moveNextRoadTest1() {
       fireEngine.setRouteAndDestination(route, residentialZone);
       
       // firstRoad
       assertFalse(fireEngine.moveNextRoad());
       assertFalse(fireEngine.isAvailable());
       assertNotNull(fireEngine.getDestination());
       assertFalse(fireEngine.isMovingBack());
       
       // secondRoad
       assertFalse(fireEngine.moveNextRoad());
       assertFalse(fireEngine.isAvailable());
       assertNotNull(fireEngine.getDestination());
       assertFalse(fireEngine.isMovingBack());
       
       // lastRoad
       assertFalse(fireEngine.moveNextRoad());
       assertFalse(fireEngine.isAvailable());
       assertNotNull(fireEngine.getDestination());
       assertFalse(fireEngine.isMovingBack());
    }
    
    /**
     * route has no next road
     */
    @Test
    public void moveNextRoadTest2() {
       fireEngine.setRouteAndDestination(route, residentialZone);
       
       // firstRoad
       assertFalse(fireEngine.moveNextRoad());
       assertFalse(fireEngine.isAvailable());
       assertNotNull(fireEngine.getDestination());
       assertFalse(fireEngine.isMovingBack());
       
       // secondRoad
       assertFalse(fireEngine.moveNextRoad());
       assertFalse(fireEngine.isAvailable());
       assertNotNull(fireEngine.getDestination());
       assertFalse(fireEngine.isMovingBack());
       
       // lastRoad
       assertFalse(fireEngine.moveNextRoad());
       assertFalse(fireEngine.isAvailable());
       assertNotNull(fireEngine.getDestination());
       assertFalse(fireEngine.isMovingBack());
       
       // no more road
       assertTrue(fireEngine.moveNextRoad());
       assertFalse(fireEngine.isAvailable());
       assertNotNull(fireEngine.getDestination());
       assertTrue(fireEngine.isMovingBack());
    }
    
    /**
     * route back has next road
     */
    @Test
    public void moveBackNextRoadTest1() {
       fireEngine.setRouteAndDestination(route, residentialZone);
       
       // move to destination:
       fireEngine.moveNextRoad();
       fireEngine.moveNextRoad();
       fireEngine.moveNextRoad();
       fireEngine.moveNextRoad();
       
       // move back:
       // lastRoad
       fireEngine.moveBackNextRoad();
       assertFalse(fireEngine.isAvailable());
       assertNotNull(fireEngine.getDestination());
       assertTrue(fireEngine.isMovingBack());
       
       // secondRoad
       fireEngine.moveBackNextRoad();
       assertFalse(fireEngine.isAvailable());
       assertNotNull(fireEngine.getDestination());
       assertTrue(fireEngine.isMovingBack());
       
       // firstRoad
       fireEngine.moveBackNextRoad();
       assertFalse(fireEngine.isAvailable());
       assertNotNull(fireEngine.getDestination());
       assertTrue(fireEngine.isMovingBack());
    }
    
    /**
     * route back has no next road
     */
    @Test
    public void moveBackNextRoadTest2() {
       fireEngine.setRouteAndDestination(route, residentialZone);
       
       // move to destination:
       fireEngine.moveNextRoad();
       fireEngine.moveNextRoad();
       fireEngine.moveNextRoad();
       fireEngine.moveNextRoad();
       
       // move back:
       // lastRoad
       fireEngine.moveBackNextRoad();
       assertFalse(fireEngine.isAvailable());
       assertNotNull(fireEngine.getDestination());
       assertTrue(fireEngine.isMovingBack());
       
       // secondRoad
       fireEngine.moveBackNextRoad();
       assertFalse(fireEngine.isAvailable());
       assertNotNull(fireEngine.getDestination());
       assertTrue(fireEngine.isMovingBack());
       
       // firstRoad
       fireEngine.moveBackNextRoad();
       assertFalse(fireEngine.isAvailable());
       assertNotNull(fireEngine.getDestination());
       assertTrue(fireEngine.isMovingBack());
       
       // no more road (arrived back to fire station)
       fireEngine.moveBackNextRoad();
       assertTrue(fireEngine.isAvailable());
       assertNull(fireEngine.getDestination());
       assertFalse(fireEngine.isMovingBack());
    }
    
    /**
     * industrial zone chance of fire is higher (2 times) than other fields
     */
    @Test
    public void chanceOfFireTest() {
        assertTrue(industrialZone.getChanceOfFire() == 2.0 * residentialZone.getChanceOfFire());
    }
}
