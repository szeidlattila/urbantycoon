
package UrbanTycoon;

import java.awt.Image;


public class Road extends PlayerBuildIt {
    boolean fireTrackOnTheRoad = false;

    public Road(int buildingPrice, int annualFee, int x, int y, int width, int height, Image image, double refund) {
        super(buildingPrice, annualFee, x, y, width, height, image, refund, 0.0);
    }

    public boolean isFireTrackOnTheRoad() {
        return fireTrackOnTheRoad;
    }

    public void setFireTrackOnTheRoad(boolean fireTrackOnTheRoad) {
        this.fireTrackOnTheRoad = fireTrackOnTheRoad;
    }

    @Override
    protected String type() {
        return "notBurning/" + (fireTrackOnTheRoad ? "fireTruck" : "road");
    }
    @Override
    public String asString(){
        return "r;" + super.asString() + fireTrackOnTheRoad;
    }
}
