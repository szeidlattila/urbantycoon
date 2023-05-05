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
public class CityTest {
    
    private final int WIDTH = 80;
    private final int HEIGHT = 80;
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
    
    public CityTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void newCity() {
        city = new City(INITIALRESIDENT, FIELDSIZE, FIELDROWSNUM, FIELDCOLSNUM, CRITSATISFACTION,
                MOVEINATLEASTSATISFACTION, INITIALMONEY,
                ZONEPRICE, ROADPRICE, STADIUMPRICE, POLICESTATIONPRICE, FIRESTATIONPRICE, FORESTPRICE,
                ANNUALFEEPERCENTAGE,
                RESIDENTCAPACITY, WORKPLACECAPACITY, REFUND, CHANCEOFFIRE, RADIUS, WIDTH, HEIGHT);
    }
    
    @AfterEach
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    @Test
    public void fieldSelectTest(){
        city.fieldSelect(0, 0);
        assertEquals(city.getFields()[0][0], city.selectedField);
        
        city.fieldSelect(0,1);
        assertEquals(city.getFields()[0][1], city.selectedField);
        
        city.fieldSelect(0, 1);
        assertEquals(null, city.selectedField);
    }
    
    @Test
    public void buildTest() {
        
        city.fieldSelect(0,0);
        city.build(PoliceStation.class);
        assertTrue(city.getFields()[0][0].getBuilding() instanceof PoliceStation);
        
        city.fieldSelect(0,1);
        city.build(FireStation.class);
        assertTrue(city.getFields()[0][1].getBuilding() instanceof FireStation);
        
        city.fieldSelect(2,1);
        city.build(Stadium.class);
        for(int i = 0; i < 2; i++)
            assertTrue(city.getFields()[i/3+1][i%3].getBuilding() instanceof Stadium);
        
        // Ugyanarra a mezőre próbál építeni
        city.build(Road.class);
        assertFalse(city.getFields()[2][1].getBuilding() instanceof Road);
    }
    
    @Test
    public void selectTest(){
        city.fieldSelect(0,0);
        city.selectField(ServiceZone.class);
        assertTrue(city.getFields()[0][0].getBuilding() instanceof ServiceZone);
        
        city.fieldSelect(0,1);
        city.selectField(ResidentialZone.class);
        assertTrue(city.getFields()[0][1].getBuilding() instanceof ResidentialZone);
        
        city.fieldSelect(2,1);
        city.selectField(IndustrialZone.class);
        assertTrue(city.getFields()[2][1].getBuilding() instanceof IndustrialZone);
    }
    
    private int yearlyProfit(City city){
        int annualTaxes = city.getResidents().size() * 2 * city.getTax();
        for(Resident r : city.getResidents())
            if(r.isRetired())
                annualTaxes -= city.getTax()*2 + r.getYearlyRetirement();
        
        int annualFees = 0;
        for(Field[] row : city.getFields())
            for(Field f : row)
                if(!f.isFree() && f.getBuilding().isBuiltUp())
                    annualFees += f.getBuilding().getAnnualFee();
        annualFees -= city.countField(Stadium.class) * 3 * Math.ceil(STADIUMPRICE * ANNUALFEEPERCENTAGE);
        return annualTaxes - annualFees;
    }
    
    @Test 
    public void moneyTest1(){
        assertEquals(100, city.getTax());
        assertEquals( INITIALMONEY,city.getBudget());
        
        city.increaseTax();
        assertEquals(150, city.getTax());
        
        city.lowerTax();
        assertEquals(100, city.getTax());

        city.yearElapsed();
        assertEquals(INITIALMONEY + yearlyProfit(city), city.getBudget());
    }
    
    @Test
    public void moneyTest2(){
        city.fieldSelect(0,0);
        city.build(PoliceStation.class);
        city.fieldSelect(0,1);
        city.build(FireStation.class);
        city.fieldSelect(2,1);
        city.build(Stadium.class);
        long budgetBefore = city.getBudget();
        city.yearElapsed();
        assertEquals(budgetBefore + yearlyProfit(city), city.getBudget());
        
    }
    /**
     * profit nem változik, ha nem felépült zónával ér véget az év
     */
    @Test
    public void moneyTest3(){
        int profitBefore = yearlyProfit(city);
        long budgetBefore = city.getBudget();
        city.fieldSelect(0,  2);
        city.selectField(IndustrialZone.class);
        city.yearElapsed();
        assertEquals(budgetBefore - ZONEPRICE + profitBefore, city.getBudget());
    }
    
    /**
     * profit változik minden felépült zónával
     */
    @Test
    public void moneyTest4(){
        long budgetBefore = city.getBudget();
        int profitBefore = yearlyProfit(city);
        city.fieldSelect(0,0);
        city.build(PoliceStation.class);
        city.yearElapsed();
        assertNotEquals(budgetBefore - POLICESTATIONPRICE + profitBefore, city.getBudget());
    }
}
