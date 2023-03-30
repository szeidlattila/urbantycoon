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
abstract class Zone extends Buildable {
    
    protected final int capacity;
    protected int peopleNum = 0;
    protected int safety;
    protected int satisfactionBonus = 0;
    protected int selectPrice;
    protected int annualTaxPerPerson;
    protected double refund;
    protected int buildProgress = 0;
    protected boolean builtUp;
    protected boolean onFire = false;
    protected double chanceOfFire;
    
    protected Zone(int capacity, int selectPrice, boolean builtUp, int annualTaxPerPerson, double refund, double chanceOfFire, int x, int y, int width, int height, Image image) {
        super(x ,y, width, height, image);
        
        if (capacity > 0) {
            this.capacity = capacity;
        } else {
            throw new IllegalArgumentException("Invalid value! Capacity must be greater than 0!");
        }
        
        this.safety = calculateSafety();
        
        if (annualTaxPerPerson >= 0) {
            this.annualTaxPerPerson = annualTaxPerPerson;
        } else {
            throw new IllegalArgumentException("Invalid value! Annual tax must be at least 0!");
        }
        
        if (0.0 <= refund && refund <= 1.0) {
            this.refund = refund;
        } else {
            throw new IllegalArgumentException("Invalid value! Refund must be greater than 0.0 and lower than 1.0!");
        }
        
        if (0.0 <= chanceOfFire && chanceOfFire <= 0.5) {
            this.chanceOfFire = chanceOfFire;
        } else {
            throw new IllegalArgumentException("Invalid value! Chance of fire must be greater than 0.0 and lower than 0.5!");
        }    
    }

    public int getSatisfactionBonus() {
        return satisfactionBonus;
    }

    public void setSatisfactionBonus(int satisfactionBonus) {
        if(satisfactionBonus > 10 || satisfactionBonus < -10) throw new IllegalArgumentException("Satisfaction Bonus out of range");
        else this.satisfactionBonus = satisfactionBonus;
    }

    /**
     * 
     * @return the annual tax (depends on how many people are in the field)
     */
    @Override
    public int getAnnualTax() {
        return annualTaxPerPerson * peopleNum;
    }
    
    public void setAnnualTax(int annualTaxPerPerson) {
        if (annualTaxPerPerson >= 0) {
            this.annualTaxPerPerson = annualTaxPerPerson;
        } else {
            throw new IllegalArgumentException("Invalid value! Annual tax must be at least 0!");
        }
    }
    
    @Override
    public void progressBuilding(int progressInDays){
        if(!builtUp){
            buildProgress += progressInDays * 25;
            if(buildProgress >= 100){
                buildProgress = 100;
                builtUp=true;
            }
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
    
    /**
     * 
     * @return money for refund
     */
    public int getRefundValue() {
        return (int)Math.ceil(selectPrice * refund);
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
    
    /**
     * 
     * @return true if the zone is full otherwise false
     */
    public boolean isFull() {
        return capacity == peopleNum; 
    }
    
    @Override
    public int destroy() {
        if(builtUp) return 0;
        return selectPrice;
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

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + this.capacity;
        hash = 79 * hash + this.peopleNum;
        hash = 79 * hash + this.safety;
        hash = 79 * hash + this.satisfactionBonus;
        hash = 79 * hash + this.selectPrice;
        hash = 79 * hash + this.annualTaxPerPerson;
        hash = 79 * hash + (int) (Double.doubleToLongBits(this.refund) ^ (Double.doubleToLongBits(this.refund) >>> 32));
        hash = 79 * hash + this.buildProgress;
        hash = 79 * hash + (this.builtUp ? 1 : 0);
        hash = 79 * hash + (this.onFire ? 1 : 0);
        hash = 79 * hash + (int) (Double.doubleToLongBits(this.chanceOfFire) ^ (Double.doubleToLongBits(this.chanceOfFire) >>> 32));
        return hash;
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
        final Zone other = (Zone) obj;
        if (this.capacity != other.capacity) {
            return false;
        }
        if (this.peopleNum != other.peopleNum) {
            return false;
        }
        if (this.safety != other.safety) {
            return false;
        }
        if (this.satisfactionBonus != other.satisfactionBonus) {
            return false;
        }
        if (this.selectPrice != other.selectPrice) {
            return false;
        }
        if (this.annualTaxPerPerson != other.annualTaxPerPerson) {
            return false;
        }
        if (Double.doubleToLongBits(this.refund) != Double.doubleToLongBits(other.refund)) {
            return false;
        }
        if (this.buildProgress != other.buildProgress) {
            return false;
        }
        if (this.builtUp != other.builtUp) {
            return false;
        }
        if (this.onFire != other.onFire) {
            return false;
        }
        return Double.doubleToLongBits(this.chanceOfFire) == Double.doubleToLongBits(other.chanceOfFire);
    }
    
    
}
