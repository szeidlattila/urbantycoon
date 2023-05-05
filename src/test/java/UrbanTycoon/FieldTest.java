/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package UrbanTycoon;

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
public class FieldTest {
    
    private static Buildable residentialZone;
    private static Buildable workplaceZone;
    private static Buildable road;
    private static Buildable policeStation;
    private static Buildable stadium;
    private static Buildable forest;
    private static Buildable fireStation;
    private static Field fieldNull;
    private static Field fieldResidentialZone;
    private static Field fieldWorkplaceZone;
    private static Field fieldRoad;
    private static Field fieldPoliceStation;
    private static Field fieldStadium;
    private static Field fieldForest;
    private static Field fieldFireStation;
    
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
        // Zone:
        residentialZone = new ResidentialZone(0.0, 1, 1, 1, 1, 1, 0.0, 0.0, 1, 1, 1, 1, null);
        workplaceZone = new IndustrialZone(1, 1, 1, 1, 1, 0.0, 0.0, 1, 1, 1, 1, null);
        // PlayerBuildIt:
        road = new Road(1, 1, 1, 1, 1, 1, null, 0.0);
        // PlayerBuildIt -> ServiceBuilding:
        policeStation = new PoliceStation(1, 1, 1, 1, 1, 1, 1, null, 0.0, 0.0);
        stadium = new Stadium(1, 1, 1, 1, 1, 1, 1, null, 0.0, 0.0);
        forest = new Forest(1, 1, 1, 1, 1, 1, null, 0.0, 0.0);
        fireStation = new FireStation(1, 1, 1, 1, 1, 1, 1, null, 0.0);
        
        fieldNull = new Field(null, 0, 0, 1, 1, null);
        fieldResidentialZone = new Field(residentialZone, 0, 0, 1, 1, null);
        fieldWorkplaceZone = new Field(workplaceZone, 0, 0, 1, 1, null);
        fieldRoad = new Field(road, 0, 0, 1, 1, null);
        fieldPoliceStation = new Field(policeStation, 0, 0, 1, 1, null);
        fieldStadium = new Field(stadium, 0, 0, 1, 1, null);
        fieldForest = new Field(forest, 0, 0, 1, 1, null);
        fieldFireStation = new Field(fireStation, 0, 0, 1, 1, null);
    }
    
    @AfterEach
    public void tearDown() {
    }

    /**
     * build on not empty field
     */
    @Test
    public void buildTest1() {
        assertEquals(road, fieldRoad.getBuilding());
        fieldRoad.build(residentialZone);
        assertEquals(road, fieldRoad.getBuilding());
    }
    
    /**
     * build on empty field
     */
    @Test
    public void buildTest2() {
        assertEquals(null, fieldNull.getBuilding());
        fieldNull.build(residentialZone);
        assertEquals(residentialZone, fieldNull.getBuilding());
    }
    
    /**
     * progressInDays is negative
     */
    @Test
    public void progressBuildingTest1() {
        assertFalse(residentialZone.progressBuilding(-1));
    }
    
    /**
     * progressInDays is lower than 4
     */
    @Test
    public void progressBuildingTest2() {
        assertFalse(residentialZone.progressBuilding(3));
    }
    
    /**
     * progressInDays is equal to 4
     */
    @Test
    public void progressBuildingTest3() {
        assertTrue(residentialZone.progressBuilding(4));
    }
    
    /**
     * progressInDays is greater than 4
     */
    @Test
    public void progressBuildingTest4() {
        assertTrue(residentialZone.progressBuilding(10000));
    }
    
    /**
     * call method twice on same Zone
     */
    @Test
    public void progressBuildingTest5() {
        assertTrue(residentialZone.progressBuilding(4));
        assertFalse(residentialZone.progressBuilding(4));
    }
    
    /**
     * call method more times with different progressInDays on same Zone
     */
    @Test
    public void progressBuildingTest6() {
        assertFalse(residentialZone.progressBuilding(-1));
        assertFalse(residentialZone.progressBuilding(0));
        assertFalse(residentialZone.progressBuilding(1));
        assertFalse(residentialZone.progressBuilding(2));
        assertFalse(residentialZone.progressBuilding(1));
        assertTrue(residentialZone.progressBuilding(1));
        assertFalse(residentialZone.progressBuilding(3));
        assertFalse(residentialZone.progressBuilding(-4));
        assertFalse(residentialZone.progressBuilding(4));
        assertFalse(residentialZone.progressBuilding(5));
    }
    
    /**
     * progressBuilding call on PlayerBuildIt
     */
    @Test
    public void progressBuildingTest7() {
        assertFalse(policeStation.progressBuilding(5));
        assertFalse(policeStation.progressBuilding(10));
        assertFalse(stadium.progressBuilding(5));
        assertFalse(stadium.progressBuilding(10));
        assertFalse(policeStation.progressBuilding(5));
        assertFalse(policeStation.progressBuilding(10));
        assertFalse(fireStation.progressBuilding(5));
        assertFalse(fireStation.progressBuilding(10));
        assertFalse(forest.progressBuilding(5));
        assertFalse(forest.progressBuilding(10));
    }
    
    /**
     * refund is less
     */
    @Test
    public void refundIllegalArgumentExceptionTest1() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            workplaceZone.setRefund(0.0);
        });

        String expectedMessage = "Invalid value! Refund must be more than 0.0 and less than 1.0!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
    
    /**
     * refund is more
     */
    @Test
    public void refundIllegalArgumentExceptionTest2() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            workplaceZone.setRefund(1.0);
        });

        String expectedMessage = "Invalid value! Refund must be more than 0.0 and less than 1.0!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
    
    /**
     * chanceOfFire is less
     */
    @Test
    public void chanceOfFireIllegalArgumentExceptionTest1() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            workplaceZone.setChanceOfFire(-0.001);
        });

        String expectedMessage = "Invalid value! Chance of fire must be between 0.0 and 1.0!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
    
    /**
     * chanceOfFire is more
     */
    @Test
    public void chanceOfFireIllegalArgumentExceptionTest2() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            workplaceZone.setChanceOfFire(1.1);
        });

        String expectedMessage = "Invalid value! Chance of fire must be between 0.0 and 1.0!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
    
    /**
     * buildingPrice is less
     */
    @Test
    public void buildingPriceIllegalArgumentExceptionTest1() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Stadium(-1, 1, 1, 1, 1, 1, 1, null, 1, 0.0);
        });

        String expectedMessage = "Invalid value! Building price must be at least 0!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
    
    /**
     * annualFee is less
     */
    @Test
    public void annualFeeIllegalArgumentExceptionTest1() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new PoliceStation(1, 0, 1, 1, 1, 1, 1, null, 1, 0.0);
        });

        String expectedMessage = "Invalid annual fee! Annual fee must be greater than 0!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
    
    /**
     * capacity is less
     */
    @Test
    public void capacityIllegalArgumentExceptionTest1() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new ResidentialZone(0.1, 0, 1, 1, 1, 1, 0.1, 0.1, 1, 1, 1, 1, null);
        });

        String expectedMessage = "Invalid value! Capacity must be greater than 0!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
    
    /**
     * annualTax is less
     */
    @Test
    public void annualTaxIllegalArgumentExceptionTest1() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new ResidentialZone(0.1, 1, 1, -1, 1, 1, 0.1, 0.1, 1, 1, 1, 1, null);
        });

        String expectedMessage = "Invalid value! Annual tax must be at least 0!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
    
    /**
     * satisfactionBonus is less
     */
    @Test
    public void satisfactionBonusIllegalArgumentExceptionTest1() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            ((Zone)residentialZone).setSatisfactionBonus(-11);
        });

        String expectedMessage = "Satisfaction bonus out of range!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
    
    /**
     * satisfactionBonus is more
     */
    @Test
    public void satisfactionBonusIllegalArgumentExceptionTest2() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            ((Zone)workplaceZone).setSatisfactionBonus(11);
        });

        String expectedMessage = "Satisfaction bonus out of range!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
    
    /**
     * peopleNum is negative
     */
    @Test
    public void peopleNumIllegalArgumentExceptionTest1() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            ((Zone)workplaceZone).setPeopleNum(-1);
        });

        String expectedMessage = "Invalid value! People number must be at least 0!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
    
    /**
     * incrementPeopleNum
     */
    @Test
    public void peopleNumIllegalArgumentExceptionTest2() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            ((Zone)workplaceZone).setPeopleNum(((Zone)workplaceZone).getCapacity());
            ((Zone)workplaceZone).incrementPeopleNum();
        });

        String expectedMessage = "peopleNum cannot be more than the capacity";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
    
    /**
     * decreasePeopleNum
     */
    @Test
    public void peopleNumIllegalArgumentExceptionTest3() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            ((Zone)workplaceZone).setPeopleNum(0);
            ((Zone)workplaceZone).decreasePeopleNum();
        });

        String expectedMessage = "peopleNum cannot be less than 0";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
    
    /**
     * safety is less
     */
    @Test
    public void selectPriceIllegalArgumentExceptionTest1() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            ((Zone)residentialZone).setSafety(-11);
        });

        String expectedMessage = "Invalid value! Safety must be at least 0 and at most 10!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
    
    /**
     * safety is less
     */
    @Test
    public void safetyIllegalArgumentExceptionTest1() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            ((Zone)residentialZone).setSafety(-11);
        });

        String expectedMessage = "Invalid value! Safety must be at least 0 and at most 10!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
    
    /**
     * safety is more
     */
    @Test
    public void safetyIllegalArgumentExceptionTest2() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            ((Zone)workplaceZone).setSafety(11);
        });

        String expectedMessage = "Invalid value! Safety must be at least 0 and at most 10!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
    
    /**
     * moveInChance is less
     */
    @Test
    public void moveInChanceIllegalArgumentExceptionTest1() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new ResidentialZone(-0.1, 1, 1, 1, 1, 1, 0.1, 0.1, 1, 1, 1, 1, null);
        });

        String expectedMessage = "Invalid value! Move in chance must be between 0.0 and 1.0!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
    
    /**
     * moveInChance is more
     */
    @Test
    public void moveInChanceIllegalArgumentExceptionTest2() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new ResidentialZone(1.01, 1, 1, 1, 1, 1, 0.1, 0.1, 1, 1, 1, 1, null);
        });

        String expectedMessage = "Invalid value! Move in chance must be between 0.0 and 1.0!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}
