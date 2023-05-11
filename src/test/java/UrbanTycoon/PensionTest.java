/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package UrbanTycoon;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Felhasználó
 */
public class PensionTest {
    
    static Resident r;
    
    @BeforeEach
    public void setUp() {
        r = new Resident( 22, null, null);
    }
    
    @Test
    public void agingTest(){
        r.setAge(60);
        int ageBefore = r.getAge();
        r.increaseAge();
        assertEquals(ageBefore+1, r.getAge());
        r.increaseAge();
        r.increaseAge();
        r.increaseAge();
        assertFalse(r.isRetired());
        r.increaseAge();
        assertTrue(r.isRetired());
        boolean l = false;
        for(int i=0;i<10;i++){
            l = l || r.increaseAge();
        }
        assertTrue(l); // meghalt-e 10 év után. chanceOfDeath += 0.1 minden évben
    }
    
    @Test
    public void paidTaxesFor20Years(){
        r.setAge(44);
        IndustrialZone iz = new IndustrialZone(5, 200, 500, 2, 2, 0.3, 0.1, 0, 0, 0, 0, null);
        iz.incrementPeopleNum();
        r.setWorkplace(iz);
        ResidentialZone rz = new ResidentialZone(0.4,5,200,500,2,2,0.2,0.1,0,0,0,0,null);
        rz.incrementPeopleNum();
        r.setHome(rz);
        
        assertEquals(r.getPaidTaxesBeforeRetired(),0);
        assertEquals(r.getWorkedYearsBeforeRetired(), 0);
        
        for(int i=0;i<21;i++){ // az első évben csak 44 éves, így ekkor nem növekszik a két érték
            r.tax();
            r.increaseAge();
        }
        
        assertEquals(r.getPaidTaxesBeforeRetired(), 20 * 1000);
        assertEquals(r.getWorkedYearsBeforeRetired(), 20);
        assertTrue(r.isRetired());
        assertEquals(r.getYearlyRetirement(), 20 * 1000 / 20 / 2);
    }
    
    @Test
    public void paidTaxesFor10Years(){
        r.setAge(55);
        
        IndustrialZone iz = new IndustrialZone(5, 200, 500, 2, 2, 0.3, 0.1, 0, 0, 0, 0, null);
        iz.incrementPeopleNum();
        r.setWorkplace(iz);
        ResidentialZone rz = new ResidentialZone(0.4,5,200,500,2,2,0.2,0.1,0,0,0,0,null);
        rz.incrementPeopleNum();
        r.setHome(rz);
        
        assertEquals(r.getPaidTaxesBeforeRetired(),0);
        assertEquals(r.getWorkedYearsBeforeRetired(), 0);
        for(int i=0;i<10;i++){
            r.tax();
            r.increaseAge();
        }
        assertEquals(r.getPaidTaxesBeforeRetired(), 10 * 1000);
        assertEquals(r.getWorkedYearsBeforeRetired(), 10);
        assertTrue(r.isRetired());
        assertEquals(r.getYearlyRetirement(), 10 * 1000 / 10 / 2);
    }
}