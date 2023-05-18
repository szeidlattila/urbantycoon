
package UrbanTycoon;

import java.awt.Image;

public class IndustrialZone extends Workplace{
    public IndustrialZone(int capacity, int selectPrice, int annualTax, int safety, int satisfactionBonus, double refund, double chanceOfFire, int x, int y, int width, int height, Image image){
        super(capacity, selectPrice, annualTax, safety, satisfactionBonus, refund, chanceOfFire*2.0, x, y, width, height, image);
    }

    @Override
    protected String type() {
        if (peopleNum == 0)         return (burning ? "burning" : "notBurning") + "/industrialZoneEmpty";
        if (peopleNum == capacity)  return (burning ? "burning" : "notBurnint") + "/industrialZoneFull";
        return (burning ? "burning" : "notBurning") + "/industrialZone";
    }
    
    @Override
    public String asString(){
        return "iz;" + super.asString();
    }
}
