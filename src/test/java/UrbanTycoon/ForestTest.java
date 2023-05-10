package UrbanTycoon;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ForestTest {

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
    
	@BeforeEach
	public void setUp() {
		city = new City(INITIALRESIDENT, FIELDSIZE, FIELDROWSNUM, FIELDCOLSNUM, CRITSATISFACTION,
                MOVEINATLEASTSATISFACTION, INITIALMONEY,
                ZONEPRICE, ROADPRICE, STADIUMPRICE, POLICESTATIONPRICE, FIRESTATIONPRICE, FORESTPRICE,
                ANNUALFEEPERCENTAGE,
                RESIDENTCAPACITY, WORKPLACECAPACITY, REFUND, CHANCEOFFIRE, RADIUS, null);
	}
	
	@Test
	public void calculateForestBonusResZoneTest1(){
		city.fieldSelect(1, 1);
		city.build(Forest.class);
								// FOREST  0   0
								//    0  HOME 0
		assertEquals(10, city.calculateForestBonusResZone()[2][2]);
	}
	
	@Test
	public void calculateForestBonusResZoneTest2(){
		city.fieldSelect(1, 2);
		city.build(Forest.class);
								// 0 FOREST 0
								// 0  HOME  0
		assertEquals(10, city.calculateForestBonusResZone()[2][2]);
	}
	
	@Test
	public void calculateForestBonusResZoneTest3(){
		city.fieldSelect(0, 0);
		city.build(Forest.class);
								// FOREST 0  0
								//    0   0  0
								//    0   0 HOME
		assertEquals(10, city.calculateForestBonusResZone()[2][2]);
	}
	
	@Test
	public void calculateForestBonusResZoneTest4(){
		city.fieldSelect(1, 1);
		city.build(Forest.class);
		city.fieldSelect(2, 1);
		city.build(PoliceStation.class);
								// FOREST  0   0
								//   PS  HOME 0   (blocked)
		assertEquals(0, city.calculateForestBonusResZone()[2][2]);
	}
	
	@Test
	public void calculateForestBonusResZoneTest5(){
		city.fieldSelect(1, 1);
		city.build(Forest.class);
		city.fieldSelect(1, 2);
		city.build(Forest.class);
		city.fieldSelect(1, 3);
		city.build(Forest.class);
								// FOREST  FOREST FOREST
								//    0     HOME     0
		assertEquals(10, city.calculateForestBonusResZone()[2][2]); // 10 marad, mert nem többszöröződik
	}
	
	@Test
	public void calculateForestBonusResZoneTest6(){
		city.fieldSelect(1, 1);
		city.build(Forest.class); //(0 éves)
		city.yearElapsed();
		city.yearElapsed();
								// FOREST  0   0  (forest 2 éves)
								//    0  HOME 0
		assertEquals(20, city.calculateForestBonusResZone()[2][2]);
	}
	
	@Test
	public void forestAgingTest1(){
		city.fieldSelect(1, 1);
		city.build(Forest.class);
		for(int i=0;i<3;i++)
			city.yearElapsed();
		assertEquals(3, ((Forest)city.getFields()[1][1].getBuilding()).getAge());
		assertEquals(3, ((Forest)city.getFields()[1][1].getBuilding()).getBonusMultiplier());
		assertEquals(Math.ceil(FORESTPRICE * ANNUALFEEPERCENTAGE), ((Forest)city.getFields()[1][1].getBuilding()).getAnnualFee());
	}
	
	@Test
	public void forestAgingTest2(){
		city.fieldSelect(1, 1);
		city.build(Forest.class);
		for(int i=0;i<11;i++)
			city.yearElapsed();
		assertEquals(11, ((Forest)city.getFields()[1][1].getBuilding()).getAge());
		assertEquals(10, ((Forest)city.getFields()[1][1].getBuilding()).getBonusMultiplier());
		assertEquals(0,((Forest)city.getFields()[1][1].getBuilding()).getAnnualFee());
	}
	
	/* Ha apply-olva lesz a forest bonus a resident satisfaction-ökre akkor futtathatók:
	 * (úgy van megcsinálva, hogy a rálátás 1-el növeli a satisfaction-t,
	 * és felére csökkenti a gyár penalty-t a zónára
	 * 
	@Test
	public void forestEffectTest1() {
		ResidentialZone home = (ResidentialZone)city.getFields()[4][14].getBuilding();
		Resident personInZone = city.getZonePeople(home).get(0);
		int satPrior = personInZone.getSatisfaction();
		// ekörül alapból nincs IndustrialZone
		
		city.fieldSelect(3, 14);
		city.build(Forest.class);
		assertEquals(satPrior + 1, personInZone.getSatisfaction());
	}
	
	@Test
	public void forestEffectTest2() {
		ResidentialZone home = (ResidentialZone)city.getFields()[4][14].getBuilding();
		Resident personInZone = city.getZonePeople(home).get(0);
		// ekörül alapból nincs IndustrialZone
		
		
		city.fieldSelect(4, 15);
		city.selectField(IndustrialZone.class);
		int satPrior = personInZone.getSatisfaction();
		
		city.fieldSelect(2, 14);
		city.build(Forest.class);
		
		city.fieldSelect(3, 14);
		city.build(Road.class); //PoliceStation a kettő közé

									// FOREST 0
									//   PS   0
									//  HOME GYÁR
		
		assertEquals(satPrior, personInZone.getSatisfaction());
	}
	
	@Test
	public void forestEffectTest3() {
		ResidentialZone home = (ResidentialZone)city.getFields()[4][14].getBuilding();
		Resident personInZone = city.getZonePeople(home).get(0);
		int satPrior = personInZone.getSatisfaction();
		// ekörül alapból nincs IndustrialZone
		
		city.fieldSelect(4, 15);
		city.selectField(IndustrialZone.class);
		assertEquals(satPrior - 5, personInZone.getSatisfaction());
		satPrior = personInZone.getSatisfaction();
		
		//INDUSTRIALZONENEGATIVEEFFECT = 6 - Math.max(távolságVízszintesen, távolságFüggőlegesen)
		//								(Minden 5 sugarú környezetben levő IndustrialZone-ra levonódik)
		city.fieldSelect(3, 14);
		city.build(Forest.class);
		assertEquals(satPrior + 1 + INDUSTRIALZONENEGATIVEEFFECT/2, personInZone.getSatisfaction());
	}
	*/
}
