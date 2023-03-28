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
public class IndustrialZone extends Workplace{
    public IndustrialZone(int capacity, int annualTax, double refund, double chanceOfFire, int x, int y, int width, int height, Image image){
        super(capacity, annualTax, refund, chanceOfFire*2, x, y, width, height, image);
    }
}
