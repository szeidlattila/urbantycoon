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
public class PoliceStation extends ServiceBuilding {
    public PoliceStation(int buildingPrice, int annualFee,int radius,int x,int y,int width, int height,Image image, double refund, double chanceOfFire) {
        super(buildingPrice, annualFee, radius, x, y, width, height, image, refund, chanceOfFire);
    }

    protected String type() {
        return (burning ? "burning" : "notBurning") + "/policeStation";
    }
}
