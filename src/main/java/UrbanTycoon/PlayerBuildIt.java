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
    
    @Override
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
    
    @Override
    public final int destroy(){
        return buildingPrice;
    }
    @Override
    public String asString(){
        return super.asString() + buildingPrice + ";" + annualFee + ";";
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PlayerBuildIt other = (PlayerBuildIt) obj;
        
        if (Double.doubleToLongBits(this.refund) != Double.doubleToLongBits(other.refund)) {
            return false;
        }
        if (this.burning != other.burning) {
            return false;
        }
        if(Double.doubleToLongBits(this.chanceOfFire) != Double.doubleToLongBits(other.chanceOfFire)){
            return false;
        }
        return this.x == other.x && this.y == other.y && this.annualFee == other.annualFee && this.buildingPrice == other.buildingPrice;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + this.buildingPrice;
        hash = 53 * hash + this.annualFee;
        return hash;
    }
}
