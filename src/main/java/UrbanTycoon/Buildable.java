
package UrbanTycoon;

import java.awt.Image;
import javax.swing.ImageIcon;

abstract class Buildable extends Sprite {
    protected double refund;
    protected double chanceOfFire;
    protected final int burnsDownAfterNDays = 60;
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
            System.out.println(chanceOfFire);
            throw new IllegalArgumentException("Invalid value! Chance of fire must be between 0.0 and 1.0!");
        }
    }

    public abstract void progressBuilding(int progressInDays);

    protected abstract int destroy();

    protected final void select(boolean accessible) {
        if (isBuiltUp())
            image = new ImageIcon("data/graphics/field/selected/" + type() + ".png").getImage();
        else
            image = new ImageIcon(
                    "data/graphics/field/selected/notBurning/" + (accessible ? "build" : "unableBuild") + ".png")
                    .getImage();
    }

    protected final void unselect(boolean accessible) {
        if (isBuiltUp())
            image = new ImageIcon("data/graphics/field/unselected/" + type() + ".png").getImage();
        else
            image = new ImageIcon(
                    "data/graphics/field/unselected/notBurning/" + (accessible ? "build" : "unableBuild") + ".png")
                    .getImage();
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
            throw new IllegalArgumentException("Invalid value! Chance of fire must be between 0.0 and 1.0!");
        }
    }

    public boolean isBurning() {
        return burning;
    }

    public void startBurning(Date burningStartDate) {
        if (!burning && this.burningStartDate == null) {
            this.burning = true;
            this.burningStartDate = burningStartDate;
        }
    }

    public void stopBurning() {
        this.burning = false;
        this.burningStartDate = null;
    }

    public boolean isBurntDown(Date currentDate) {
        if (!isBurning())
            return false;
        return burningStartDate.hoursElapsed(currentDate) > burnsDownAfterNDays * 24;
    }

    public void setBurning(boolean burning) {
        this.burning = burning;
    }

    public void setBurningStartDate(Date burningStartDate) {
        this.burningStartDate = burningStartDate;
    }

    protected abstract String type();

    public String asString() {
        StringBuilder b = new StringBuilder();
        b.append(refund);
        b.append(';');
        b.append(chanceOfFire);
        b.append(';');
        b.append(burning);
        b.append(';');
        b.append(burningStartDate == null ? "null" : burningStartDate.toString());
        b.append(';');
        return b.toString();
    }
}
