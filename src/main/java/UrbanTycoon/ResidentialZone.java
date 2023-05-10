/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package UrbanTycoon;

import java.awt.Image;
import java.util.Objects;

import javax.swing.ImageIcon;

/**
 *
 * @author Felhasználó
 */
class ResidentialZone extends Zone {
    double moveInChance;
    int industrialPenalty = 0;
    int forestBonus = 0;
    public ResidentialZone(double moveInChance, int capacity, int selectPrice, int annualTax, int safety, int satisfactionBonus, double refund, double chanceOfFire, int x, int y, int width, int height, Image image) {
        super(capacity, selectPrice, annualTax, safety, satisfactionBonus, refund, chanceOfFire, x, y, width, height, image);
        
        if (0.0 <= moveInChance && moveInChance <= 1.0) {
            this.moveInChance = moveInChance;
        } else {
            throw new IllegalArgumentException("Invalid value! Move in chance must be between 0.0 and 1.0!");
        }
    }

    public void setMoveInChance(double moveInChance) {
        this.moveInChance = moveInChance;
    }
    
    public void setForestBonus(int bonus) {
        if (satisfactionBonus + forestBonus > 10) {
            forestBonus = 10 - satisfactionBonus;
        } else {
            forestBonus = bonus;
        }
    }
    
    @Override
    public int getSatisfactionBonus() {
    	return satisfactionBonus;
    }
    
    public int getForestBonus() {
    	return forestBonus;
    }
    
    public int getIndustrialPenalty() {
    	return industrialPenalty;
    }
    
    public void setIndustrialPenalty(int ip) {
    	if(ip >= -10 && ip <= 0)
    		industrialPenalty = ip;
    }
    
    public double getMoveInChance() {
        return moveInChance;
    }
    @Override
    public int getAnnualTax(){
        return annualTaxPerPerson;
    }
    protected String type(){
        if (peopleNum == 0)         return (burning ? "burning" : "notBurning") + "/residentialZoneEmpty";
        if (peopleNum == capacity)  return (burning ? "burning" : "notBurning") + "/residentialZoneFull";
        return (burning ? "burning" : "notBurning") + "/residentialZone";
    }
    @Override
    public String asString(){
        return "rz;" + super.asString() + moveInChance + ";" + forestBonus + ";" + industrialPenalty;
    }
    
    @Override
    public boolean equals(Object other) {
    	if(super.equals(other)) {
    		ResidentialZone rOther = (ResidentialZone) other;
    		return this.moveInChance == rOther.moveInChance && this.forestBonus == rOther.forestBonus && this.industrialPenalty == rOther.industrialPenalty;
    	}
    	return false;
    }
}
