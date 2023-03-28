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
abstract class Workplace extends Zone {
    protected Workplace(int capacity, int annualTax, double refund, double chanceOfFire, int x, int y, int width, int height, Image image) {
        super(capacity, 0, true, annualTax, refund, chanceOfFire ,x, y, width, height, image);
    }
}
