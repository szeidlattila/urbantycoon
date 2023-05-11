
package UrbanTycoon;

import java.awt.Image;

public class PoliceStation extends ServiceBuilding {
    public PoliceStation(int buildingPrice, int annualFee,int radius,int x,int y,int width, int height,Image image, double refund, double chanceOfFire) {
        super(buildingPrice, annualFee, radius, x, y, width, height, image, refund, chanceOfFire);
    }

    protected String type() {
        return (burning ? "burning" : "notBurning") + "/policeStation";
    }
    @Override
    public String asString(){
        return "ps;" + super.asString();
    }
}
