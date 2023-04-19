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
public class ResidentTest {
    
    public ResidentTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
    }
    
    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of setSatisfaction method, of class Resident.
     */
    @Test
    public void testSetSatisfaction() {
        System.out.println("setSatisfaction");
        int satisfaction = 0;
        Resident instance = null;
        instance.setSatisfaction(satisfaction);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getAge method, of class Resident.
     */
    @Test
    public void testGetAge() {
        System.out.println("getAge");
        Resident instance = null;
        int expResult = 0;
        int result = instance.getAge();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isRetired method, of class Resident.
     */
    @Test
    public void testIsRetired() {
        System.out.println("isRetired");
        Resident instance = null;
        boolean expResult = false;
        boolean result = instance.isRetired();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getChanceOfDeath method, of class Resident.
     */
    @Test
    public void testGetChanceOfDeath() {
        System.out.println("getChanceOfDeath");
        Resident instance = null;
        double expResult = 0.0;
        double result = instance.getChanceOfDeath();
        assertEquals(expResult, result, 0);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getSatisfaction method, of class Resident.
     */
    @Test
    public void testGetSatisfaction() {
        System.out.println("getSatisfaction");
        Resident instance = null;
        int expResult = 0;
        int result = instance.getSatisfaction();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getHome method, of class Resident.
     */
    @Test
    public void testGetHome() {
        System.out.println("getHome");
        Resident instance = null;
        ResidentialZone expResult = null;
        ResidentialZone result = instance.getHome();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getWorkplace method, of class Resident.
     */
    @Test
    public void testGetWorkplace() {
        System.out.println("getWorkplace");
        Resident instance = null;
        Workplace expResult = null;
        Workplace result = instance.getWorkplace();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of increaseAge method, of class Resident.
     */
    @Test
    public void testIncreaseAge() {
        System.out.println("increaseAge");
        Resident instance = null;
        instance.increaseAge();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of increaseSatisfaction method, of class Resident.
     */
    @Test
    public void testIncreaseSatisfaction() {
        System.out.println("increaseSatisfaction");
        Resident instance = null;
        instance.increaseSatisfaction();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of decreaseSatisfaction method, of class Resident.
     */
    @Test
    public void testDecreaseSatisfaction() {
        System.out.println("decreaseSatisfaction");
        Resident instance = null;
        instance.decreaseSatisfaction();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setHome method, of class Resident.
     */
    @Test
    public void testSetHome() {
        System.out.println("setHome");
        ResidentialZone home = null;
        Resident instance = null;
        instance.setHome(home);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setWorkplace method, of class Resident.
     */
    @Test
    public void testSetWorkplace() {
        System.out.println("setWorkplace");
        Workplace workplace = null;
        Resident instance = null;
        instance.setWorkplace(workplace);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of retire method, of class Resident.
     */
    @Test
    public void testRetire() {
        System.out.println("retire");
        Resident instance = null;
        instance.retire();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of die method, of class Resident.
     */
    @Test
    public void testDie() {
        System.out.println("die");
        Resident instance = null;
        instance.die();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of hashCode method, of class Resident.
     */
    @Test
    public void testHashCode() {
        System.out.println("hashCode");
        Resident instance = null;
        int expResult = 0;
        int result = instance.hashCode();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of equals method, of class Resident.
     */
    @Test
    public void testEquals() {
        System.out.println("equals");
        Object obj = null;
        Resident instance = null;
        boolean expResult = false;
        boolean result = instance.equals(obj);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of toString method, of class Resident.
     */
    @Test
    public void testToString() {
        System.out.println("toString");
        Resident instance = null;
        String expResult = "";
        String result = instance.toString();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
