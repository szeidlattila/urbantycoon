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
abstract class Zone extends Buildable{
    protected final int capacity;
    protected int peopleNum = 0;
    protected int safety;
    protected int selectPrice;
    protected int annualTax;
    protected double refund;
    protected boolean builtUp;
    protected boolean onFire = false;
    protected double chanceOfFire;
    protected Zone(int capacity, int selectPrice, boolean builtUp, int annualTax, double refund, double chanceOfFire, int x, int y, int width, int height, Image image) {
        super(x ,y, width, height, image);
        
        if (capacity > 0) {
            this.capacity = capacity;
        } else {
            throw new IllegalArgumentException("Invalid value! Capacity must be greater than 0!");
        }
        
        this.safety = calculateSafety();
        
        if (annualTax >= 0) {
            this.annualTax = annualTax;
        } else {
            throw new IllegalArgumentException("Invalid value! Annual tax must be at least 0!");
        }
        
        if (0.0 < refund && refund < 1.0) {
            this.refund = refund;
        } else {
            throw new IllegalArgumentException("Invalid value! Refund must be greater than 0.0 and lower than 1.0!");
        }
        
        if (0.0 < refund && refund < 0.5) {
            this.chanceOfFire = chanceOfFire;
        } else {
            throw new IllegalArgumentException("Invalid value! Chance of fire must be greater than 0.0 and lower than 0.5!");
        }    
    }

    @Override
    public int getAnnualTax() {
        return annualTax;
    }
    
    public void setAnnualTax(int annualTax) {
        if (annualTax >= 0) {
            this.annualTax = annualTax;
        } else {
            throw new IllegalArgumentException("Invalid value! Annual tax must be at least 0!");
        }
    }
    
    public int getPeopleNum() {
        return peopleNum;
    }

    public void setPeopleNum(int peopleNum) {
        if (peopleNum >= 0) {
            this.peopleNum = peopleNum;
        } else {
            throw new IllegalArgumentException("Invalid value! People number must be at least 0!");
        }
    }

    public int getSafety() {
        return safety;
    }

    public void setSafety(int safety) {
        if (-10 <= safety && safety <= 10) {
            this.safety = safety;
        } else {
            throw new IllegalArgumentException("Invalid value! Safety must be at least 0 and at most 10!");
        }
    }

    public int getSelectPrice() {
        return selectPrice;
    }

    public void setSelectPrice(int selectPrice) {
        if (selectPrice > 0) {
            this.selectPrice = selectPrice;
        } else {
            throw new IllegalArgumentException("Invalid value! Select price must be more than 0!");
        }
    }

    public double getRefund() {
        return refund;
    }

    public void setRefund(double refund) {
        if (0.0 < refund && refund < 1.0) {
            this.refund = refund;
        } else {
            throw new IllegalArgumentException("Invalid value! Refund must be more than 0.0 and less than 1.0!");
        }
    }

    public boolean isBuiltUp() {
        return builtUp;
    }

    public void setBuiltUp(boolean builtUp) {
        this.builtUp = builtUp;
    }

    public boolean isOnFire() {
        return onFire;
    }

    public void setOnFire(boolean isOnFire) {
        this.onFire = isOnFire;
    }

    public double getChanceOfFire() {
        return chanceOfFire;
    }

    public void setChanceOfFire(double chanceOfFire) {
        if (0.0 <= chanceOfFire && chanceOfFire < 1.0) {
            this.chanceOfFire = chanceOfFire;
        } else {
            throw new IllegalArgumentException("Invalid value! Chance of fire must be at least 0.0 and less than 1.0!");
        }
    }
    
    public void destroy() {
        if(builtUp) return;
        /* TODO: Lebontani + refund */
    }
    
    protected int calculateSafety() {
        /* TODO: kiszámolni a biztonságot */
        return 1;
    }
    
    public void fireSpread() {
        /* RÉSZFELADAT: Tűzoltóság */
    }
    
    public void burnsDown() {
        /* RÉSZFELADAT: Tűzoltóság */
    }
}
