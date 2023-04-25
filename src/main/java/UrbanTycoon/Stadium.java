/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package UrbanTycoon;

import java.awt.Image;
import javax.swing.ImageIcon;

/**
 *
 * @author Felhasználó
 */
public class Stadium extends ServiceBuilding{
    public Field[] fields = new Field[4];


    public Stadium(int buildingPrice, int annualFee, int radius, int x, int y, int width, int height, Image image, double refund, double chanceOfFire) {
        super(buildingPrice, annualFee, radius, x, y, width, height, image, refund, chanceOfFire); // divide by 4 because stadium size is 2x2
    }

    protected String type() {
        return (burning ? "burning" : "notBurning") + "/stadium";
    }
    
}
