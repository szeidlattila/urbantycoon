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
public class Forest extends PlayerBuildIt{
    private int age=0;

    public Forest(int buildingPrice, int annualFee, int x, int y, int width, int height, Image image, double refund) {
        super(buildingPrice, annualFee, x, y, width, height, image, refund);
    }

    public int getAge() {
        return age;
    }
    
    public void increaseAgeBy1(){
        age++;
        if(age==10) setAnnualFeeTo0();
    }
    
    private void setAnnualFeeTo0(){
        annualFee=0;
    }
    
    public void increaseResidentSatisfaction(){
        
    }
    
    public void increaseMoveInChance(){
        
    }

    protected String type(){
        return "forest";
    }
}
