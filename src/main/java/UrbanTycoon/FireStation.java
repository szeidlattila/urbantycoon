
package UrbanTycoon;

import java.awt.Image;
import javax.swing.ImageIcon;

public class FireStation extends ServiceBuilding {
    private final FireEngine fireEngine;

    public FireStation(int buildingPrice, int annualFee, int radius, int x, int y, int width, int height, Image image, double refund) {
        super(buildingPrice, annualFee, radius, x, y, width, height, image, refund, 0.0);
        fireEngine = new FireEngine(x, y, width, height, new ImageIcon("data/graphics/field/selected/fireTruck.png").getImage());
    }

    public FireEngine getFireEngine() {
        return fireEngine;
    }

    @Override
    protected String type() {
        return "notBurning/fireStation";
    }
    
    public String asString(){
        return "fs;" + super.asString();
    }
}