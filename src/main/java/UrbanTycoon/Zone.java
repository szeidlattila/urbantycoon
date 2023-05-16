
package UrbanTycoon;

import java.awt.Image;
import javax.swing.ImageIcon;

abstract class Zone extends Buildable {

    protected final int capacity;
    protected int peopleNum = 0;
    protected int safety;
    protected int satisfactionBonus;
    protected int selectPrice;
    protected int annualTaxPerPerson;
    protected int forestBonus=0;
    protected int buildProgress = 0;
    protected boolean builtUp;

    protected Zone(int capacity, int selectPrice, int annualTaxPerPerson, int safety, int satisfactionBonus,
            double refund,
            double chanceOfFire, int x, int y, int width, int height, Image image) {
        super(x, y, width, height, image, refund, chanceOfFire);
        this.selectPrice = selectPrice;
        if (capacity > 0) {
            this.capacity = capacity;
        } else {
            throw new IllegalArgumentException("Invalid value! Capacity must be greater than 0!");
        }

        this.safety = safety;
        this.satisfactionBonus = satisfactionBonus;

        if (annualTaxPerPerson >= 0) {
            this.annualTaxPerPerson = annualTaxPerPerson;
        } else {
            throw new IllegalArgumentException("Invalid value! Annual tax must be at least 0!");
        }
    }

    @Override
    public String asString() {
        StringBuilder b = new StringBuilder(super.asString());
        b.append(annualTaxPerPerson);
        b.append(';');
        b.append(buildProgress);
        b.append(';');
        b.append(builtUp);
        b.append(';');
        b.append(capacity);
        b.append(';');
        b.append(peopleNum);
        b.append(';');
        b.append(safety);
        b.append(';');
        b.append(satisfactionBonus);
        b.append(';');
        b.append(selectPrice);
        b.append(';');
        return b.toString();
    }

    public int getSatisfactionBonus() {
        return satisfactionBonus + forestBonus;
    }

    public void setSatisfactionBonus(int satisfactionBonus) {
        if (satisfactionBonus > 10 || satisfactionBonus < -10)
            throw new IllegalArgumentException("Satisfaction bonus out of range!");
        else
            this.satisfactionBonus = satisfactionBonus;
    }

    public void setForestBonus(int bonus) {
        if (satisfactionBonus + forestBonus > 10) {
            forestBonus = 10 - satisfactionBonus;
        } else {
            forestBonus = bonus;
        }
    }

    

    /**
     * 
     * @return the annual tax (depends on how many people are in the field)
     */
    @Override
    public int getAnnualTax() {
        return annualTaxPerPerson;
    }

    public void setAnnualTaxPerPerson(int annualTaxPerPerson) {
        this.annualTaxPerPerson = annualTaxPerPerson;
    }

    public void setBuildProgress(int buildProgress) {
        this.buildProgress = buildProgress;
    }

    public void setAnnualTax(int annualTaxPerPerson) {
        if (annualTaxPerPerson >= 0) {
            this.annualTaxPerPerson = annualTaxPerPerson;
        } else {
            throw new IllegalArgumentException("Invalid value! Annual tax must be at least 0!");
        }
    }

    public int getCapacity() {
        return capacity;
    }

    @Override
    public void progressBuilding(int progressInDays) {
        if (!builtUp) {
            buildProgress += progressInDays * 25;
            if (buildProgress >= 100) {
                buildProgress = 100;
                builtUp = true;
                image = new ImageIcon("data/graphics/field/unselected/" + type() + ".png").getImage();
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

    public void incrementPeopleNum() {
        if (peopleNum + 1 > capacity) {
            throw new IllegalArgumentException("peopleNum cannot be more than the capacity");
        }
        peopleNum++;
    }

    public void decreasePeopleNum() {
        if (peopleNum - 1 < 0) {
            throw new IllegalArgumentException("peopleNum cannot be less than 0");
        }
        peopleNum--;
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
        return (int) Math.ceil(selectPrice * refund);
    }

    @Override
    public boolean isBuiltUp() {
        return builtUp;
    }

    public void setBuiltUp(boolean builtUp) {
        this.builtUp = builtUp;
    }

    /**
     * 
     * @return true if the zone is full otherwise false
     */
    public boolean isFull() {
        return capacity == peopleNum;
    }

    @Override
    public int getRefundMoney() {
        return selectPrice;
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
        hash = 79 * hash + (this.burning ? 1 : 0);
        hash = 79 * hash + (int) (Double.doubleToLongBits(this.chanceOfFire)
                ^ (Double.doubleToLongBits(this.chanceOfFire) >>> 32));
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
        if (this.burning != other.burning) {
            return false;
        }
        if (Double.doubleToLongBits(this.chanceOfFire) != Double.doubleToLongBits(other.chanceOfFire)) {
            return false;
        }
        return this.x == other.x && this.y == other.y;
    }

}
