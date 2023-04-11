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
public class ServiceZone extends Workplace{
    public ServiceZone(int capacity, int selectPrice, int annualTax, double refund, double chanceOfFire,int x,int y,int width, int height,Image image) {
        super(capacity, selectPrice, annualTax, refund, chanceOfFire, x, y, width, height, image);
    }
    
    protected String type(){
        return "serviceZone";
    }
}
