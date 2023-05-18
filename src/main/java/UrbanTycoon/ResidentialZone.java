
package UrbanTycoon;

import java.awt.Image;

class ResidentialZone extends Zone {
    double moveInChance;
    int industrialPenalty = 0;
    public ResidentialZone(double moveInChance, int capacity, int selectPrice, int annualTax, int safety, int satisfactionBonus, double refund, double chanceOfFire, int x, int y, int width, int height, Image image) {
        super(capacity, selectPrice, annualTax, safety, satisfactionBonus, refund, chanceOfFire, x, y, width, height, image);
        
        if (0.0 <= moveInChance && moveInChance <= 1.0) {
            this.moveInChance = moveInChance;
        } else {
            throw new IllegalArgumentException("Invalid value! Move in chance must be between 0.0 and 1.0!");
        }
    }

    public void setMoveInChance(double moveInChance) {
        this.moveInChance = moveInChance;
    }
    
    public double getMoveInChance() {
        return moveInChance;
    }
    
    public void setIndustrialPenalty(int industrialPenalty) {
    	this.industrialPenalty = industrialPenalty;
    }
    
    public int getIndustrialPenalty() {
    	return industrialPenalty;
    }
    
    @Override
    public int getAnnualTax(){
        return annualTaxPerPerson;
    }
    
    @Override
    protected String type(){
        if (peopleNum == 0)         return (burning ? "burning" : "notBurning") + "/residentialZoneEmpty";
        if (peopleNum == capacity)  return (burning ? "burning" : "notBurning") + "/residentialZoneFull";
        return (burning ? "burning" : "notBurning") + "/residentialZone";
    }
    
    @Override
    public String asString(){
        return "rz;" + super.asString() + moveInChance;
    }
}
