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
public abstract class PlayerBuildIt extends Buildable {
    protected int buildingPrice;
    protected int annualFee;
    
    protected PlayerBuildIt(int buildingPrice, int annualFee,int x,int y,int width, int height,Image image, double refund, double chanceOfFire) {
        super(x, y, width, height, image, refund, chanceOfFire); 
        
        if (buildingPrice >= 0) {
            this.buildingPrice = buildingPrice;
        } else {
            throw new IllegalArgumentException("Invalid value! Building price must be at least 0!");
        }
        
        if (annualFee > 0) {
            this.annualFee = annualFee;
        } else {
            throw new IllegalArgumentException("Invalid annual fee! Annual fee must be greater than 0!");
        }
    }
    
    public boolean progressBuilding(int progressInDays){
        return false;
    }
    
    public int getBuildingPrice() {
        return buildingPrice;
    }
    
    @Override
    public int getAnnualFee() {
        return annualFee;
    }
    
    public final int destroy(){
        return buildingPrice;
    }
    @Override
    public String asString(){
        return super.asString() + buildingPrice + ";" + annualFee + ";";
    }
}
