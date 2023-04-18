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
class Field extends Sprite {

    private boolean free;
    private Buildable building;

    public Field(Buildable building, int x, int y, int width, int height, Image image) {
        super(x, y, width, height, image);
        this.building = building;
        this.free = building == null;
    }

    public boolean isFree() {
        return free;
    }

    public Buildable getBuilding() {
        return building;
    }

    public void setBuilding(Buildable building) {
        this.building = building;
        this.free = building == null;
    }

    public String getInfo() {
        String out;
        if (building instanceof Zone zone) {
            out = "Residents: " + zone.getCapacity() + "/" + zone.getPeopleNum() + "\nSafety: " + zone.getSafety() + "\nSatBonus: " + zone.getSatisfactionBonus();
            return out;
        }
        if (building instanceof PlayerBuildIt pbi) {
            out = "Annual Fee: " + pbi.getAnnualFee() + " Build Price: " + pbi.getBuildingPrice() + " Refund: "
                    + pbi.getBuildingPrice() * pbi.getRefund();
            return out;
        }
        return null;
    }

    public int destroyOrDenominate() {
        if (!free) {
            int d = building.destroy();
            if (d != 0) {
                setBuilding(null);
            }
            return d;
        }
        return 0;
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
    }

    public void select() {
        image = new ImageIcon("data/graphics/selectedField.png").getImage(); // hex: #FF9E2A
    }

    public void unselect() {
        image = new ImageIcon("data/graphics/field.png").getImage();
    }
}
