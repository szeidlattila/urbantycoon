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
public class Road extends PlayerBuildIt{

    public Road(int buildingPrice, int annualFee, int x, int y, int width, int height, Image image) {
        super(buildingPrice, annualFee, x, y, width, height, image);
    }

    @Override
    protected int destroy() {
        if (false /* TODO: Ha egy felépített épület nem lenne elérhető bontás után */) {
            return 0;
        } else {
            return buildingPrice;
            /* TODO: destroy */
        }  
    }

    protected String type(){
        return "road";
    }
}
