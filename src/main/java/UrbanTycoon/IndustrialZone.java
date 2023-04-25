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
public class IndustrialZone extends Workplace{
    public IndustrialZone(int capacity, int selectPrice, int annualTax, int safety, int satisfactionBonus, double refund, double chanceOfFire, int x, int y, int width, int height, Image image){
        super(capacity, selectPrice, annualTax, safety, satisfactionBonus, refund, chanceOfFire*2.0, x, y, width, height, image);
    }

    protected String type() {
        if (peopleNum == 0)         return (burning ? "burning" : "notBurning") + "/industrialZoneEmpty";
        if (peopleNum == capacity)  return (burning ? "burning" : "notBurnint") + "/industrialZoneFull";
        return (burning ? "burning" : "notBurning") + "/industrialZone";
    }
}
