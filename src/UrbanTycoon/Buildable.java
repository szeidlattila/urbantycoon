/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package UrbanTycoon;

import java.awt.Image;

/**
 *
 * @author Felhasználó
 */
abstract class Buildable extends Sprite {
    protected Buildable(int x, int y, int width, int height, Image image){
        super(x, y, width, height, image);
    }
    public void progressBuilding(int progressInDays){}
    
    protected abstract int destroy();
    
    protected int getDistance(Buildable other) {
        /* TODO: Visszaadja 'this' és 'other' közti távolságot */
        return 100; // át lesz majd írva, most csak azért 100, hogy ne dobjon hibát
    }
    
    protected int getDistanceAlongRoad(Buildable other) {
        /* TODO: Visszaadja 'this' és 'other' közti távolságot a LEGRÖVIDEBB utak mentén. Ha nincs összekötve úttal -1-et ad vissza */
        return -1;
    }
    
    protected int getAnnualFee() {
        return 0;
    }
    
    protected int getAnnualTax() {
        return 0;
    }
}
