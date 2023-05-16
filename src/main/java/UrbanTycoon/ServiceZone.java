
package UrbanTycoon;

import java.awt.Image;

public class ServiceZone extends Workplace{
    public ServiceZone(int capacity, int selectPrice, int annualTax, int safety, int satisfactionBonus, double refund, double chanceOfFire,int x,int y,int width, int height,Image image) {
        super(capacity, selectPrice, annualTax, safety, satisfactionBonus, refund, chanceOfFire, x, y, width, height, image);
    }
    
    @Override
    protected String type(){
        if (peopleNum == 0)         return (burning ? "burning" : "notBurning") + "/serviceZoneEmpty";
        if (peopleNum == capacity)  return (burning ? "burning" : "notBurning") + "/serviceZoneFull";
        return (burning ? "burning" : "notBurning") + "/serviceZone";
    }
    @Override
    public String asString(){
        return "sz;" + super.asString();
    }
}
