
package UrbanTycoon;

import java.awt.Image;

abstract class Workplace extends Zone {
    protected Workplace(int capacity, int selectPrice, int annualTax, int safety, int satisfactionBonus, double refund, double chanceOfFire, int x, int y, int width, int height, Image image) {
        super(capacity, selectPrice, annualTax, safety, satisfactionBonus, refund, chanceOfFire ,x, y, width, height, image);
    }
}
