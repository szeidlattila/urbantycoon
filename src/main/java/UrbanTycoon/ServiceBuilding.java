
package UrbanTycoon;

import java.awt.Image;

public abstract class ServiceBuilding extends PlayerBuildIt{
    protected int radius;
    
    protected ServiceBuilding(int buildingPrice, int annualFee, int radius, int x, int y, int width, int height, Image image, double refund, double chanceOfFire) {
        super(buildingPrice, annualFee, x, y, width, height, image, refund, chanceOfFire);
        
        if (radius > 0) {
            this.radius = radius;
        } else {
            throw new IllegalArgumentException("Invalid radius! radius must be greater than 0!");
        }
    }

    public int getRadius() {
        return radius;
    }
    public String asString(){
        return super.asString() + radius;
    }
}
