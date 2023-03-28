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
public abstract class ServiceBuilding extends PlayerBuildIt{
    protected int radius;
    
    protected ServiceBuilding(int buildingPrice, int annualFee, int radius, int x, int y, int width, int height, Image image) {
        super(buildingPrice, annualFee, x, y, width, height, image);
        
        if (radius > 0) {
            this.radius = radius;
        } else {
            throw new IllegalArgumentException("Invalid radius! radius must be greater than 0!");
        }
    }

    public int getRadius() {
        return radius;
    }
    
    public void destroy() {
        return;
    }
}
