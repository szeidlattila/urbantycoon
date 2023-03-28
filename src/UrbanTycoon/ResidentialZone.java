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
class ResidentialZone extends Zone {
    double moveInChance = 0.0;
    
    public ResidentialZone(double moveInChance, int capacity, int annualTax, double refund, double chanceOfFire, int x, int y, int width, int height, Image image) {
        super(capacity, 0, true, annualTax, refund, chanceOfFire, x, y, width, height, image);
        
        if (0.0 < moveInChance && moveInChance < 1.0) {
            this.moveInChance = moveInChance;
        } else {
            throw new IllegalArgumentException("Invalid value! Move in chance must be greater than 0.0 and lower than 1.0!");
        }
    }

    public void setMoveInChance(double moveInChance) {
        this.moveInChance = moveInChance;
    }

    public double getMoveInChance() {
        return moveInChance;
    }
    
}
