/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package UrbanTycoon;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author ati
 */
public class ConflictDemolition {
    
    private final int FIELDSIZE = 20;
    private final int FIELDROWSNUM = 8;
    private final int FIELDCOLSNUM = 16;
    private final int INITIALMONEY = 100000;
    private final int INITIALRESIDENT = 20;
    private final int ZONEPRICE = 250;
    private final int ROADPRICE = 75;
    private final int STADIUMPRICE = 1000;
    private final int POLICESTATIONPRICE = 750;
    private final int FIRESTATIONPRICE = 750;
    private final int FORESTPRICE = 500;
    private final double ANNUALFEEPERCENTAGE = 0.3; // playerBuildIt annualFee = price * ANNUALFEEPERCENTAGE
    private final int RESIDENTCAPACITY = 5;
    private final int WORKPLACECAPACITY = 18;
    private final double REFUND = 0.4;
    private final int RADIUS = 3;
    private final int CRITSATISFACTION = -5;
    private final int MOVEINATLEASTSATISFACTION = 5;
    private final double CHANCEOFFIRE = 0.05;
    
    static City city;
    
    @BeforeEach
    public void newCity() {
        city = new City(INITIALRESIDENT, FIELDSIZE, FIELDROWSNUM, FIELDCOLSNUM, CRITSATISFACTION,
                MOVEINATLEASTSATISFACTION, INITIALMONEY,
                ZONEPRICE, ROADPRICE, STADIUMPRICE, POLICESTATIONPRICE, FIRESTATIONPRICE, FORESTPRICE,
                ANNUALFEEPERCENTAGE,
                RESIDENTCAPACITY, WORKPLACECAPACITY, REFUND, CHANCEOFFIRE, RADIUS, null);
    }
    
    /**
     * all false
     *      [2] [3] [4] [5] [6] [7]
     * [2]  rz     
     * [3]  r   f   f
     * [4]  r   r   r   r   r   rz
     * [5]          iz  
     */
    @Test
    public void canDeleteRoadTest1() {
        assertFalse(city.canDeleteRoad(city.getFields()[3][2]));
        assertFalse(city.canDeleteRoad(city.getFields()[4][2]));
        assertFalse(city.canDeleteRoad(city.getFields()[4][3]));
        assertFalse(city.canDeleteRoad(city.getFields()[4][4]));
        assertFalse(city.canDeleteRoad(city.getFields()[4][5]));
        assertFalse(city.canDeleteRoad(city.getFields()[4][6]));
    }
    
    /**
     * all false except (3; 5)
     *      [2] [3] [4] [5] [6] [7]
     * [2]  rz     
     * [3]  r   f   f   r
     * [4]  r   r   r   r   r   rz
     * [5]          iz  
     */
    @Test
    public void canDeleteRoadTest2() {
        assertFalse(city.canDeleteRoad(city.getFields()[3][2]));
        assertFalse(city.canDeleteRoad(city.getFields()[4][2]));
        assertFalse(city.canDeleteRoad(city.getFields()[4][3]));
        assertFalse(city.canDeleteRoad(city.getFields()[4][4]));
        assertFalse(city.canDeleteRoad(city.getFields()[4][5]));
        assertFalse(city.canDeleteRoad(city.getFields()[4][6]));
        
        assertTrue(city.canDeleteRoad(city.getFields()[3][5]));
    }
    
    /**
     * some false, some true
     *      [2] [3] [4] [5] [6] [7]
     * [2]  rz  r   r   r   r   r     
     * [3]  r   f   f   r   r   r
     * [4]  r   r   r   r   r   rz
     * [5]  r   r   iz  r   r   r
     */
    @Test
    public void canDeleteRoadTest3() {
        assertFalse(city.canDeleteRoad(city.getFields()[3][2]));
        assertFalse(city.canDeleteRoad(city.getFields()[4][2]));
        assertFalse(city.canDeleteRoad(city.getFields()[4][3]));
        assertFalse(city.canDeleteRoad(city.getFields()[4][4]));
        assertFalse(city.canDeleteRoad(city.getFields()[4][5]));
        assertFalse(city.canDeleteRoad(city.getFields()[4][6]));
        
        assertTrue(city.canDeleteRoad(city.getFields()[2][3]));
        assertTrue(city.canDeleteRoad(city.getFields()[2][4]));
        assertTrue(city.canDeleteRoad(city.getFields()[2][5]));
        assertTrue(city.canDeleteRoad(city.getFields()[2][6]));
        assertTrue(city.canDeleteRoad(city.getFields()[2][7]));
        assertTrue(city.canDeleteRoad(city.getFields()[3][5]));
        assertTrue(city.canDeleteRoad(city.getFields()[3][6]));
        assertTrue(city.canDeleteRoad(city.getFields()[3][7]));
        assertTrue(city.canDeleteRoad(city.getFields()[5][2]));
        assertTrue(city.canDeleteRoad(city.getFields()[5][3]));
        assertTrue(city.canDeleteRoad(city.getFields()[5][5]));
        assertTrue(city.canDeleteRoad(city.getFields()[5][6]));
        assertTrue(city.canDeleteRoad(city.getFields()[5][7]));
    }
    
    /**
     * all true
     *      [5] [6] [7]
     * [2]  r   r   r     
     * [3]  r   r   r
     * [4]  r   r   rz  =>  rz -> r
     * [5]  r   r   r
     */
    @Test
    public void canDeleteRoadTest4() {
        city.getFields()[4][7].setBuilding(new Road(1, 1, 1, 1, 1, 1, null, 0.1));
        for (int i = 2; i <= 5; i++) {
            for (int j = 5; j <= 7; j++) {
                assertTrue(city.canDeleteRoad(city.getFields()[i][j]));
            }
        }
    }
    
    /**
     * try to delete null
     */
    @Test
    public void canDeleteRoadTest5() {
        assertFalse(city.canDeleteRoad(null));
    }
    
    /**
     * try to delete field with null buildable
     */
    @Test
    public void canDeleteRoadTest6() {
        assertTrue(city.canDeleteRoad(new Field(null, 1, 1, 1, 1, null)));
    }
    
    /**
     * try to delete field which is not road
     */
    @Test
    public void canDeleteRoadTest7() {
        assertTrue(city.canDeleteRoad(new Field(new Forest(1, 1, 1, 1, 1, 1, null, 0.1, 0.1), 1, 1, 1, 1, null)));
    }
    
    /**
     * initialize r resident with a home which is outside of city
     * search home inside city for r
     * but city is full -> return null
     */
    @Test
    public void initHomeTest1() {
        ResidentialZone notHome = new ResidentialZone(0.1, 10, 1, 1, 1, 1, 0.1, 0.1, 1, 1, 1, 1, null);
        ResidentialZone previousHome = new ResidentialZone(0.1, 30, 1, 1, 1, 1, 0.1, 0.1, 1, 1, 1, 1, null);
        Resident r = new Resident(30, previousHome, null);
        
        assertEquals(previousHome, r.getHome());
        assertNotEquals(notHome, r.getHome());
        
        city.initHome(r);
        
        assertNotEquals(previousHome, r.getHome());
        assertNull(r.getHome());   
    }
    
    /**
     * initialize r resident with a home which is outside of city
     * search home inside city for r
     * city is not full -> return home field
     */
    @Test
    public void initHomeTest2() {
        ResidentialZone notHome = new ResidentialZone(0.1, 10, 1, 1, 1, 1, 0.1, 0.1, 1, 1, 1, 1, null);
        ResidentialZone previousHome = new ResidentialZone(0.1, 30, 1, 1, 1, 1, 0.1, 0.1, 1, 1, 1, 1, null);
        Resident r = new Resident(30, previousHome, null);
        
        assertEquals(previousHome, r.getHome());
        assertNotEquals(notHome, r.getHome());
        
        // move out all residents 
        for (Field[] fields : city.getFields()) {
            for (Field field : fields) {
                if (!field.isFree() && field.getBuilding() instanceof ResidentialZone rz) {
                    city.moveOut(rz);
                }
            }
        }
        
        city.initHome(r);
        
        boolean foundHome = false;
        for (Field[] fields : city.getFields()) {
            for (Field field : fields) {
                if (!field.isFree() && field.getBuilding() instanceof ResidentialZone rz) {
                    city.moveOut(rz);
                    if (r.getHome().equals(rz)) {
                        foundHome = true;
                    }
                }
            }
        }
        
        assertNotEquals(previousHome, r.getHome());
        assertTrue(foundHome);   
    }
    
    /**
     * initialize r resident with a workplace which is outside of city
     * search workplace inside city for r
     * but city has no workplace -> IllegalArgumentException
     */
    @Test
    public void initWorkplaceTest1() {
        IndustrialZone notWorkplace = new IndustrialZone(10, 1, 1, 1, 1, 0.1, 0.1, 1, 1, 1, 1, null);
        ServiceZone previousWorkplace = new ServiceZone(30, 1, 1, 1, 1, 0.1, 0.1, 1, 1, 1, 1, null);
        Resident r = new Resident(30, null, previousWorkplace);
        
        assertEquals(previousWorkplace, r.getWorkplace());
        assertNotEquals(notWorkplace, r.getWorkplace());
        
        for (Field[] fields : city.getFields()) {
            for (Field field : fields) {
                if (!field.isFree() && field.getBuilding() instanceof ResidentialZone rz) {
                    city.moveOut(rz);
                }
            }
        }
        city.initHome(r);   // find home in city
        
        // delete all workplaces in city:
        for (Field[] fields : city.getFields()) {
            for (Field field : fields) {
                if (!field.isFree() && field.getBuilding() instanceof Workplace) {
                    field.setBuilding(null);
                }
            }
        }  
        
        // IllegalArgumentExceptioin:
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            city.initWorkplace(r);
        });

        String expectedMessage = "peopleNum cannot be less than 0";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
    
    /**
     * initialize r resident with a workplace which is outside of city
     * search workplace inside city for r
     * city is not full -> numbor of workers increase by one
     */
    @Test
    public void initWorkplaceTest2() {
        IndustrialZone notWorkplace = new IndustrialZone(10, 1, 1, 1, 1, 0.1, 0.1, 1, 1, 1, 1, null);
        ServiceZone previousWorkplace = new ServiceZone(30, 1, 1, 1, 1, 0.1, 0.1, 1, 1, 1, 1, null);
        Resident r = new Resident(30, null, previousWorkplace);
        
        assertEquals(previousWorkplace, r.getWorkplace());
        assertNotEquals(notWorkplace, r.getWorkplace());
        
        
        for (Field[] fields : city.getFields()) {
            for (Field field : fields) {
                if (!field.isFree() && field.getBuilding() instanceof ResidentialZone rz) {
                    city.moveOut(rz);
                }
            }
        }
        city.initHome(r);   // find home in city
        
        int cityWorkplacePeopleNum = 0;
        for (Field[] fields : city.getFields()) {
            for (Field field : fields) {
                if (!field.isFree() && field.getBuilding() instanceof Workplace wp) {
                    cityWorkplacePeopleNum += wp.getPeopleNum();
                }
            }
        }
        
        city.initWorkplace(r);
        
        int cityWorkplacePeopleNumAfterInitWorkplace = 0;
        for (Field[] fields : city.getFields()) {
            for (Field field : fields) {
                if (!field.isFree() && field.getBuilding() instanceof Workplace wp) {
                    cityWorkplacePeopleNumAfterInitWorkplace += wp.getPeopleNum();
                }
            }
        }
        
        assertNotEquals(previousWorkplace, r.getWorkplace());
        assertEquals(1, cityWorkplacePeopleNumAfterInitWorkplace - cityWorkplacePeopleNum);
    }
}