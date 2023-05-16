
package UrbanTycoon;

import java.awt.Image;

public class Forest extends PlayerBuildIt{
    private int age=0;

    public Forest(int buildingPrice, int annualFee, int x, int y, int width, int height, Image image, double refund, double chanceOfFire) {
        super(buildingPrice, annualFee, x, y, width, height, image, refund, chanceOfFire);
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getBonusMultiplier() {
        if (age == 0) {
            return 1;
        } else if (age >= 10) {
            return 10;
        } else {
            return age;
        }

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

    @Override
    protected String type(){
        return (burning ? "burning" : "notBurning") + "/forest";
    }
    
    @Override
    public String asString(){
        return "for;" + super.asString() + age;
    }
}
