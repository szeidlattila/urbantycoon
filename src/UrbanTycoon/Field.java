/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package UrbanTycoon;

/**
 *
 * @author Felhasználó
 */
class Field {
    
    private boolean free;
    private Buildable building;
    
    public Field(boolean free, Buildable building) {
        this.building = building;
        this.free = free;
    }
    
    public boolean isFree() {
        return free;
    }
    
    public Buildable getBuilding() {
        return building;
    }
    
    public void setFree(boolean free) {
        this.free = free;
    }
    
    public void setBuilding(Buildable building) {
        this.building = building;
    }
}
