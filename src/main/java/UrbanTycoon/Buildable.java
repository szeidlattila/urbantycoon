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
abstract class Buildable extends Sprite {
    protected double refund;
    protected double chanceOfFire;
    protected int burnsDownAfterNDays = 3*31;   // 3 hónap
    protected boolean burning = false;
    public Date burningStartDate = null;

    protected Buildable(int x, int y, int width, int height, Image image, double refund, double chanceOfFire) {
        super(x, y, width, height, image);
        
        if (0.0 <= refund && refund <= 1.0) {
            this.refund = refund;
        } else {
            throw new IllegalArgumentException("Invalid value! Refund must be greater than 0.0 and lower than 1.0!");
        }
        
        if (0.0 <= chanceOfFire && chanceOfFire <= 1.0) {
            this.chanceOfFire = chanceOfFire;
        } else {
            throw new IllegalArgumentException("Invalid value! Chance of fire must be between 0.0 and 1.0!");
        }
    }

    public abstract boolean progressBuilding(int progressInDays);

    protected abstract int destroy();

    protected final void select(boolean accessible) {
        if (isBuiltUp())
            image = new ImageIcon("data/graphics/field/selected/" + type() + ".png").getImage();
        else
            image = new ImageIcon("data/graphics/field/selected/notBurning/" + (accessible ? "build" : "unableBuild") + ".png").getImage();
    }

    protected final void unselect(boolean accessible) {
        if (isBuiltUp())
            image = new ImageIcon("data/graphics/field/unselected/" + type() + ".png").getImage();
        else
            image = new ImageIcon("data/graphics/field/unselected/notBurning/" + (accessible ? "build" : "unableBuild") + ".png").getImage();
    }

    public boolean isBuiltUp() {
        return true;
    }

    protected int getAnnualFee() {
        return 0;
    }

    protected int getAnnualTax() {
        return 0;
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

    public double getChanceOfFire() {
        return chanceOfFire;
    }

    public void setChanceOfFire(double chanceOfFire) {
        if (0.0 <= chanceOfFire && chanceOfFire <= 1.0) {
            this.chanceOfFire = chanceOfFire;
        } else {
            throw new IllegalArgumentException("Invalid value! Chance of fire must be greater between 0.0 and 1.0!");
        }
    }
    
    public boolean isBurning() {
        return burning;
    }

    public void startBurning(Date burningStartDate) {
        if (!burning && this.burningStartDate == null) {
            this.burning = true;
            this.burningStartDate = burningStartDate;
            //System.out.println("Égés kezdete: " + this.burningStartDate.toString());
        }
    }
    
    public void stopBurning() {
        this.burning = false;
        this.burningStartDate = null;
    }
    
    public boolean isBurntDown(Date currentDate) {
        if (!isBurning())   return false;
        //System.out.println("Eltelt órák égés óta: " + burningStartDate.hoursElapsed(currentDate));
        //System.out.println("Égés start: " + burningStartDate.toString() + ", current: " + currentDate.toString());
        return burningStartDate.hoursElapsed(currentDate) > burnsDownAfterNDays*24;
    }
    
    public void fireSpread() {
        /* RÉSZFELADAT: Tűzoltóság */
    }
    
    protected abstract String type();
}
