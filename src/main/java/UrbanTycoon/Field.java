
package UrbanTycoon;

import java.awt.Image;
import java.util.Objects;
import javax.swing.ImageIcon;

class Field extends Sprite {

    private boolean free;
    private Buildable building;
    private boolean burntDown;

    public Field(Field other) {
        super(other.getX(), other.getY(), other.getWidth(), other.getHeight(), other.getImage());
        this.free = other.free;
        this.building = other.building;
        this.burntDown = other.burntDown;
    }

    public Field(Buildable building, int x, int y, int width, int height, Image image) {
        super(x, y, width, height, image);
        this.building = building;
        this.free = building == null;
        this.burntDown = false;
    }


    public boolean isFree() {
        return free;
    }

    public boolean isCoordinateInsideCell(int x, int y) {
        return x >= this.x && x <= this.x + this.width && y >= this.y && y <= this.y + this.height;
    }

    public boolean isBurntDown() {
        return burntDown;
    }

    public Buildable getBuilding() {
        return building;
    }

    /**
     * set this.building to building, if buildin is null then set free to true otherwise false
     * @param building
     */
    public void setBuilding(Buildable building) {
        this.building = building;
        this.free = building == null;
    }
    
    public void setBurntDown(boolean burntDown) {
        this.burntDown = burntDown;
    }

    /** 
     * 
     * @return zone info if zone is empty return null
     */
    public String getInfo() {
        String out;
        if (building instanceof Zone zone) {
            out = "Residents: " + zone.getCapacity() + "/" + zone.getPeopleNum() + "\nSafety: " + zone.getSafety()
                    + "\nSatBonus: " + zone.getSatisfactionBonus();
            return out;
        }
        if (building instanceof PlayerBuildIt pbi) {
            out = "Annual Fee: " + pbi.getAnnualFee() + " Build Price: " + pbi.getBuildingPrice() + " Refund: "
                    + pbi.getBuildingPrice() * pbi.getRefund();
            return out;
        }
        return null;
    }
    
    /**
     * 
     * @return money from destroy
     */
    public int getDestroyMoney() {
    	if (free) throw new IllegalArgumentException("Accessing destroy money, when field is free. call isFree() first.");
    	return this.building.getRefundMoney();
    }
    
    /**
     * set building to null
     */
    public void destroyOrDenominate() {
    	
    	if (free) throw new IllegalArgumentException("Trying to destroy, when field is free. call isFree() first.");
    	setBuilding(null);
    }

    /**
     * set a buildable building on a free field
     * if the field is not free does not do anything
     * 
     * @param newBuilding
     * @param buildingPrice
     * @param annualFee
     * @param radius
     */
    public void build(Buildable newBuilding) {
        if (!this.free)
            return;
        setBuilding(newBuilding);
        unselect();
        burntDown = false;
    }

    /**
     * change image to selected
     */
    public void select() {
        image = new ImageIcon("data/graphics/field/selected/notBurning/" + type() + ".png").getImage();
    }

    /**
     * change image to unselected
     */
    public void unselect() {
        image = new ImageIcon("data/graphics/field/unselected/notBurning/" + type() + ".png").getImage();
    }

    /**
     * set building to null and set burntDown to true
     */
    public void burnsDown() {
        setBuilding(null);
        burntDown = true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
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
        final Field other = (Field) obj;
        if (this.free != other.free) {
            return false;
        }
        if (this.burntDown != other.burntDown) {
            return false;
        }
        return Objects.equals(this.building, other.building);
    }

    public String type() {
        return burntDown ? "burntDownField" : "field";
    }

    public String asString() {
        return burntDown + ";" + (free ? "empty" : building.asString());
    }
}
