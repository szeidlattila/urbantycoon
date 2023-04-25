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
public class Road extends PlayerBuildIt{

    public Road(int buildingPrice, int annualFee, int x, int y, int width, int height, Image image, double refund) {
        super(buildingPrice, annualFee, x, y, width, height, image, refund, 0.0);
    }

    protected String type(){
        return "notBurning/road";
    }
}
