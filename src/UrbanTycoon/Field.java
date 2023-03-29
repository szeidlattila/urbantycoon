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
    
    public Field(Buildable building) {
        this.building = building;
        if (building == null) {
            this.free = true;
        } else {
            this.free = false;
        }
    }
    
    public boolean isFree() {
        return free;
    }
    
    public Buildable getBuilding() {
        return building;
    }
    
    public void setBuilding(Buildable building) {
        this.building = building;
        if (building == null) {
            this.free = true;
        } else {
            this.free = false;
        }
    }
    public String getInfo(){
        // TODO
        return null;
    }
    public int destroyOrDenominate(){
        if(!free){
            int d = building.destroy();
            if(d!=0)
                setBuilding(null);
            return d;
        }
        return 0;
    }
}
