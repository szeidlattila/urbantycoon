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
class FireEngine extends Sprite{
    private boolean available = true;

    public FireEngine(int x, int y, int width, int height, Image image) {
        super(x, y, width, height, image);
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
    
    public void fireFight(Zone zoneOnFire){
        
    }
}
