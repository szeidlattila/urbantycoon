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
    protected Buildable(int x, int y, int width, int height, Image image){
        super(x, y, width, height, image);
    }
    public abstract boolean progressBuilding(int progressInDays);
    
    protected abstract int destroy();
    protected final void select(boolean accessible){
        if(isBuiltUp())
            image = new ImageIcon("data/graphics/selected"+ type().substring(0,1).toUpperCase() + type().substring(1)+".png").getImage();
        else
            image = new ImageIcon("data/graphics/selected" + (accessible?"build":"unableBuild") + ".png").getImage();
    }   // hex: #FF9E2A
    protected final void unselect(boolean accessible){
        if(isBuiltUp())
            image = new ImageIcon("data/graphics/"+type()+".png").getImage();
        else
            image = new ImageIcon("data/graphics/" + (accessible?"build":"unableBuild") + ".png").getImage();
    }
    public boolean isBuiltUp(){return true;}
    protected int getAnnualFee() {
        return 0;
    }
    protected abstract String type();
    
    protected int getAnnualTax() {
        return 0;
    }
}
