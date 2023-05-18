
package UrbanTycoon;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CityTest {
    
    private final int FIELDSIZE = 60;
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
     * évi költségek változnak minden felépült zónával
     */
    @Test
    public void moneyTest3(){
        long budgetBefore = city.getBudget();
        int profitBefore = yearlyProfit(city);
        city.fieldSelect(0,0);
        city.build(PoliceStation.class);
        city.yearElapsed();
        assertNotEquals(budgetBefore - POLICESTATIONPRICE + profitBefore, city.getBudget());
    }
    
    /**
     * adó növelése
     */
    @Test
    public void satisfactionTest1() {
    	int satisfactionBefore = city.getSatisfaction();
    	city.increaseTax();
    	city.increaseTax();
    	assertTrue(city.getSatisfaction() < satisfactionBefore);
    }
    
    /**
     * népesség növekedés biztonság
     */
    @Test
    public void satisfactionTest2() {
    	int satisfactionBefore = city.getSatisfaction();
    	for(int i=0;i<10;i++) {
    		city.addResident(new Resident(20, (ResidentialZone)city.getFields()[2][2].getBuilding(), (Workplace)city.getFields()[5][4].getBuilding()));
    		city.addResident(new Resident(20, (ResidentialZone)city.getFields()[4][14].getBuilding(), (Workplace)city.getFields()[5][11].getBuilding()));    		
    	}
    	// ugyanannyi resident ment serviceZone-ba és IndustrialZone-ba, ezért a szolg-ipar dolgozók aránya nem változott
    	// csak a safety változott
    	city.performTicks(1); //ilyenkor refresh-elődik a sat
    	assertTrue(city.getSatisfaction() < satisfactionBefore);
    }
    
    /**
     * lakóépületek közelébe ipari zónák kerültek
     */
    @Test
    public void satisfactionTest3() {
    	int satisfactionBefore = city.getSatisfaction();
    	city.fieldSelect(3, 1);
    	city.selectField(IndustrialZone.class);
    	city.fieldSelect(3, 6);
    	city.selectField(IndustrialZone.class);
     	city.fieldSelect(3, 13);
     	city.selectField(IndustrialZone.class);
     	city.performTicks(1); //ilyenkor refresh-elődik a sat
    	assertTrue(city.getSatisfaction() < satisfactionBefore);
    }
    
    /**
     * ipar-szolgáltatás arány
     */
    @Test
    public void satisfactionTest4() {
    	int prevUniSat = city.universialSatisfaction;
    	for(Resident r: city.getResidents()) {
    		r.setWorkplace((Workplace)city.getFields()[5][4].getBuilding());
    		r.setHome((ResidentialZone)city.getFields()[2][2].getBuilding());
    	}
    	city.performTicks(1); //ilyenkor refresh-elődik a sat
    	// a resident szám nem változik, csak most mindenki iparban dolgozik, szolgáltatásban senki.
    	assertTrue(city.universialSatisfaction < prevUniSat);
    }
    
    /**
     * negatív büdzsé
     */
    @Test
    public void satisfactionTest5() {
    	
    	int prevUniSat = city.universialSatisfaction;
    	city.setBudget(-7616);
    	city.yearElapsed();
    	assertTrue(city.getBudget() == -4308);
    	assertTrue(city.universialSatisfaction == prevUniSat - 4);// 1*(-4308/1000)
    	
    	city.yearElapsed();
    	assertTrue(city.getBudget() == -1000);
    	assertTrue(city.universialSatisfaction == prevUniSat - 2); // 2*(-1000/1000) (2 éve negatív büdzsé)
    }
    
    /**
     * munkahely közelsége
     */
    
    @Test
    public void satisfactionTest6() {
    	// ez inkább resident
    	Resident r = new Resident(20, (ResidentialZone)city.getFields()[2][2].getBuilding(), (Workplace)city.getFields()[5][4].getBuilding());
    	city.addResident(r);
    	city.performTicks(1); //ilyenkor refresh-elődik a sat
    	int prevSat = r.getSatisfaction();
    	
    	city.fieldSelect(5, 2);
    	city.selectField(ServiceZone.class);
    	
    	r.setWorkplace((Workplace)city.getFields()[5][2].getBuilding());
    	city.performTicks(1); // ilyenkor refresh-elődik a sat
    	
    	assertTrue(r.getSatisfaction() == prevSat + 2); // 2 - vel közelebb van a munkahelye
    }
    
    /**
     * getZonePeople method test
     */
    @Test
    public void residentTest1() {
        ResidentialZone home1 = new ResidentialZone(0.1, 10, 1, 1, 1, 1, 1, 0.1, 1, 1, 1, 1, null);
        ResidentialZone home2 = new ResidentialZone(0.1, 10, 1, 1, 1, 1, 1, 0.1, 2, 2, 1, 1, null);
        ServiceZone workplace = new ServiceZone(15, 1, 1, 1, 1, 0.1, 0.1, 1, 1, 1, 1, null);
        city.getFields()[0][0].setBuilding(home1);
        city.getFields()[0][1].setBuilding(home2);
        city.getFields()[1][1].setBuilding(workplace);
        Resident r1 = new Resident(30, home1, workplace);
        Resident r2 = new Resident(35, home1, workplace);
        Resident r3 = new Resident(40, home1, workplace);
        Resident r4 = new Resident(45, home1, workplace);
        Resident r5 = new Resident(50, home2, workplace);
        Resident r6 = new Resident(55, home2, workplace);
        city.addResident(r1);
        city.addResident(r2);
        city.addResident(r3);
        city.addResident(r4);
        city.addResident(r5);
        city.addResident(r6);
        
        assertIterableEquals(List.of(r1, r2, r3, r4, r5, r6), city.getZonePeople(workplace));
        assertIterableEquals(List.of(r1, r2, r3, r4), city.getZonePeople(home1));
        assertIterableEquals(List.of(r5, r6), city.getZonePeople(home2));
    }
    
    /**
     * moveInOneResident method test: at least resident can move in (city is not full)
     */
    @Test
    public void residentTest2() {
        ResidentialZone home = new ResidentialZone(0.9, 100, 1, 1, 1, 1, 1, 0.1, 2, 2, 1, 1, null);
        ServiceZone workplace = new ServiceZone(150, 1, 1, 1, 1, 0.1, 0.1, 1, 1, 1, 1, null);
        city.getFields()[0][0].setBuilding(home);
        city.getFields()[0][1].setBuilding(new Road(1, 1, 1, 1, 1, 1, null, 0.1));
        city.getFields()[0][2].setBuilding(workplace);
        ((Zone)city.getFields()[0][0].getBuilding()).setBuiltUp(true);
        ((Zone)city.getFields()[0][2].getBuilding()).setBuiltUp(true);
        
        int prevCityResidentsNum = city.getResidentsNum();
        city.moveInOneResident(false);
        
        assertEquals(prevCityResidentsNum + 1, city.getResidentsNum());
    }
    
    /**
     * moveInOneResident method test: resident cannot move in (city is full)
     */
    @Test
    public void residentTest3() {
        int prevCityResidentsNum = city.getResidentsNum();
        city.moveInOneResident(false);
        
        assertEquals(prevCityResidentsNum, city.getResidentsNum());
    }
    
    /**
     * moveInOneResident method test: 4 residents can move in (city is not full)
     */
    @Test
    public void residentTest4() {
        ResidentialZone home = new ResidentialZone(0.9, 4, 1, 1, 1, 1, 1, 0.1, 2, 2, 1, 1, null);
        ServiceZone workplace = new ServiceZone(150, 1, 1, 1, 1, 0.1, 0.1, 1, 1, 1, 1, null);
        city.getFields()[0][0].setBuilding(home);
        city.getFields()[0][1].setBuilding(new Road(1, 1, 1, 1, 1, 1, null, 0.1));
        city.getFields()[0][2].setBuilding(workplace);
        ((Zone)city.getFields()[0][0].getBuilding()).setBuiltUp(true);
        ((Zone)city.getFields()[0][2].getBuilding()).setBuiltUp(true);
        
        int prevCityResidentsNum = city.getResidentsNum();
        assertEquals(prevCityResidentsNum, city.getResidentsNum());
        city.moveInOneResident(false); // 1
        assertEquals(prevCityResidentsNum + 1, city.getResidentsNum());
        city.moveInOneResident(false); // 2
        assertEquals(prevCityResidentsNum + 2, city.getResidentsNum());
        city.moveInOneResident(false); // 3
        assertEquals(prevCityResidentsNum + 3, city.getResidentsNum());
        city.moveInOneResident(false); // 4     -> last move in
        assertEquals(prevCityResidentsNum + 4, city.getResidentsNum());
        city.moveInOneResident(false); // 5     -> city is full, no more move in
        assertEquals(prevCityResidentsNum + 4, city.getResidentsNum());
        city.moveInOneResident(false); // 6
        assertEquals(prevCityResidentsNum + 4, city.getResidentsNum());
    }
    
    /**
     * moveOut method test: not empty zone
     */
    @Test
    public void residentTest5() {
        ResidentialZone home = new ResidentialZone(0.9, 4, 1, 1, 1, 1, 1, 0.1, 2, 2, 1, 1, null);
        ServiceZone workplace = new ServiceZone(150, 1, 1, 1, 1, 0.1, 0.1, 1, 1, 1, 1, null);
        city.getFields()[0][0].setBuilding(home);
        city.getFields()[0][1].setBuilding(new Road(1, 1, 1, 1, 1, 1, null, 0.1));
        city.getFields()[0][2].setBuilding(workplace);
        ((Zone)city.getFields()[0][0].getBuilding()).setBuiltUp(true);
        ((Zone)city.getFields()[0][2].getBuilding()).setBuiltUp(true);
        city.moveInOneResident(false);
        city.moveInOneResident(false);
        city.moveInOneResident(false);
        city.moveInOneResident(false);
        int prevCityResidentsNum = city.getResidentsNum();
        
        assertEquals(4, home.getPeopleNum());
        assertEquals(4, workplace.getPeopleNum());
        assertEquals(prevCityResidentsNum, city.getResidentsNum());
        city.moveOut(home);
        assertEquals(0, home.getPeopleNum());
        assertEquals(0, workplace.getPeopleNum());
        assertEquals(prevCityResidentsNum - 4, city.getResidentsNum());
    }
    
    /**
     * moveOut method test: empty zone
     */
    @Test
    public void residentTest6() {
        ResidentialZone home = new ResidentialZone(0.9, 4, 1, 1, 1, 1, 1, 0.1, 2, 2, 1, 1, null);
        ServiceZone workplace = new ServiceZone(150, 1, 1, 1, 1, 0.1, 0.1, 1, 1, 1, 1, null);
        city.getFields()[0][0].setBuilding(home);
        city.getFields()[0][1].setBuilding(new Road(1, 1, 1, 1, 1, 1, null, 0.1));
        city.getFields()[0][2].setBuilding(workplace);
        ((Zone)city.getFields()[0][0].getBuilding()).setBuiltUp(true);
        ((Zone)city.getFields()[0][2].getBuilding()).setBuiltUp(true);
        int prevCityResidentsNum = city.getResidentsNum();
        
        assertEquals(0, home.getPeopleNum());
        assertEquals(0, workplace.getPeopleNum());
        assertEquals(prevCityResidentsNum, city.getResidentsNum());
        city.moveOut(home);
        assertEquals(0, home.getPeopleNum());
        assertEquals(0, workplace.getPeopleNum());
        assertEquals(prevCityResidentsNum, city.getResidentsNum());
    }
    
    /**
     * home and workplace are accessible on road
     */
    @Test
    public void isAccessibleOnRoadTest1() {
        ResidentialZone home = new ResidentialZone(0.9, 4, 1, 1, 1, 1, 1, 0.1, 2, 2, 1, 1, null);
        ServiceZone workplace = new ServiceZone(150, 1, 1, 1, 1, 0.1, 0.1, 1, 1, 1, 1, null);
        city.getFields()[0][0].setBuilding(home);
        city.getFields()[0][1].setBuilding(new Road(1, 1, 1, 1, 1, 1, null, 0.1));
        city.getFields()[0][2].setBuilding(new Road(1, 1, 1, 1, 1, 1, null, 0.1));
        city.getFields()[0][3].setBuilding(new Road(1, 1, 1, 1, 1, 1, null, 0.1));
        city.getFields()[1][3].setBuilding(new Road(1, 1, 1, 1, 1, 1, null, 0.1));
        city.getFields()[2][3].setBuilding(new Road(1, 1, 1, 1, 1, 1, null, 0.1));
        city.getFields()[2][4].setBuilding(new Road(1, 1, 1, 1, 1, 1, null, 0.1));
        city.getFields()[2][5].setBuilding(new Road(1, 1, 1, 1, 1, 1, null, 0.1));
        city.getFields()[2][6].setBuilding(workplace);
;
        ((Zone)city.getFields()[0][0].getBuilding()).setBuiltUp(true);
        ((Zone)city.getFields()[2][6].getBuilding()).setBuiltUp(true);
        
        assertTrue(city.isAccessibleOnRoad(city.getFields()[0][0]));
        assertTrue(city.isAccessibleOnRoad(city.getFields()[2][6]));
    }
    
    /**
     * home and workplace are not accessible on road
     */
    @Test
    public void isAccessibleOnRoadTest2() {
        ResidentialZone home = new ResidentialZone(0.9, 4, 1, 1, 1, 1, 1, 0.1, 2, 2, 1, 1, null);
        ServiceZone workplace = new ServiceZone(150, 1, 1, 1, 1, 0.1, 0.1, 1, 1, 1, 1, null);
        city.getFields()[0][0].setBuilding(home);
        city.getFields()[2][6].setBuilding(workplace);
;
        ((Zone)city.getFields()[0][0].getBuilding()).setBuiltUp(true);
        ((Zone)city.getFields()[2][6].getBuilding()).setBuiltUp(true);
        
        assertFalse(city.isAccessibleOnRoad(city.getFields()[0][0]));
        assertFalse(city.isAccessibleOnRoad(city.getFields()[2][6]));
    }
    
    /**
     * rendőrségek építése növeli a biztonságot és az elégedettséget a közeli zónákban
     */
    @Test
    public void safetyTest1() {
    	
    	Zone zone = (Zone)city.getFields()[2][2].getBuilding();
    	Zone zoneFar = (Zone)city.getFields()[4][14].getBuilding();
    	assertEquals(zone.getSafety(), 0);
    	assertEquals(zoneFar.getSafety(), 0);
    	
    	Resident personInZone = city.getZonePeople(zone).get(0);
    	int satPrior = personInZone.getSatisfaction();
    	
    	//épít egy úton elérhető rendőrséget
    	city.fieldSelect(3,1);
    	city.build(PoliceStation.class);
    	
    	assertEquals(zone.getSafety(), 1);
    	assertEquals(zoneFar.getSafety(), 0);
    	assertTrue(satPrior < personInZone.getSatisfaction());
    	
    	// mégegyet
    	satPrior = personInZone.getSatisfaction();
    	city.fieldSelect(4,1);
    	city.build(PoliceStation.class);
    	
    	assertEquals(zone.getSafety(), 2);
    	assertTrue(satPrior < personInZone.getSatisfaction());	
    }
    
    /**
     * úton nem elérhető rendőrség építése nem változtat
     */
    @Test
    public void safetyTest2() {

    	Zone zone = (Zone)city.getFields()[2][2].getBuilding();
    	assertEquals(zone.getSafety(), 0);
    	
    	Resident personInZone = city.getZonePeople(zone).get(0);
    	int satPrior = personInZone.getSatisfaction();
    	
    	//épít egy úton nem elérhető rendőrséget
    	city.fieldSelect(3,0);
    	city.build(PoliceStation.class);
    	
    	assertEquals(zone.getSafety(), 0);
    	assertEquals(satPrior, personInZone.getSatisfaction());
    	
    }
    
    /**
     * rendőrség építése és lerombolása
     */
    @Test
    public void safetyTest3() {
    	
    	Zone zone = (Zone)city.getFields()[2][2].getBuilding();
    	assertEquals(zone.getSafety(), 0);
    	
    	Resident personInZone = city.getZonePeople(zone).get(0);
    	int satPrior = personInZone.getSatisfaction();
    	
    	//épít egy úton elérhető rendőrséget
    	city.fieldSelect(3,1);
    	city.build(PoliceStation.class);
    	
    	assertEquals(zone.getSafety(), 1);
    	assertTrue(satPrior < personInZone.getSatisfaction());
    	
    	// lerombolja
    	city.tryDenominateOrDestroyZone();
    	
    	assertEquals(zone.getSafety(), 0);
    	assertEquals(satPrior, personInZone.getSatisfaction());
    }
    
    /**
     * stadionok építése növeli a biztonságot és az elégedettséget
     */
    @Test
    public void satBonusTest1() {
    	
    	Zone zone = (Zone)city.getFields()[2][2].getBuilding();
    	Zone zoneFar = (Zone)city.getFields()[4][14].getBuilding();
    	assertEquals(zone.getSatisfactionBonus(), 0);
    	assertEquals(zoneFar.getSatisfactionBonus(), 0);
    	
    	Resident personInZone = city.getZonePeople(zone).get(0);
    	int satPrior = personInZone.getSatisfaction();
    	
    	//épít egy úton elérhető stadiont
    	city.fieldSelect(3,1);
    	city.build(Stadium.class);
    	
    	assertEquals(1, zone.getSatisfactionBonus());
    	assertEquals(zoneFar.getSatisfactionBonus(), 0);
    	assertTrue(satPrior < personInZone.getSatisfaction());
    	
    	// mégegyet
    	satPrior = personInZone.getSatisfaction();
    	city.fieldSelect(5,1);
    	city.build(Stadium.class);
    	
    	assertEquals(2, zone.getSatisfactionBonus());
    	assertTrue(satPrior < personInZone.getSatisfaction());	
    }
    
    /**
     * úton nem elérhető stadion építése nem változtat
     */
    @Test
    public void satBonusTest2() {

    	Zone zone = (Zone)city.getFields()[2][2].getBuilding();
    	assertEquals(zone.getSatisfactionBonus(), 0);
    	
    	Resident personInZone = city.getZonePeople(zone).get(0);
    	int satPrior = personInZone.getSatisfaction();
    	
    	//épít egy úton nem elérhető stadiont
    	city.fieldSelect(2,5);
    	city.build(Stadium.class);
    	
    	assertEquals(zone.getSatisfactionBonus(), 0);
    	assertEquals(satPrior, personInZone.getSatisfaction());
    	
    }
    
    /**
     * stadion építése és lerombolása
     */
    @Test
    public void satBonusTest3() {
    	
    	Zone zone = (Zone)city.getFields()[2][2].getBuilding();
    	assertEquals(zone.getSafety(), 0);
    	
    	Resident personInZone = city.getZonePeople(zone).get(0);
    	int satPrior = personInZone.getSatisfaction();
    	
    	//épít egy úton elérhető stadiont
    	city.fieldSelect(3,1);
    	city.build(Stadium.class);
    	
    	assertEquals(1, zone.getSatisfactionBonus());
    	assertTrue(satPrior < personInZone.getSatisfaction());
    	
    	// lerombolja
    	city.tryDenominateOrDestroyZone();
    	
    	assertEquals(zone.getSatisfactionBonus(), 0);
    	assertEquals(satPrior, personInZone.getSatisfaction());
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