/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package UrbanTycoon;

/**
 *
 * @author Felhasználó
 */
class Resident {
    private int age;
    private boolean retired = false;
    private double chanceOfDeath = 0.0;
    private int satisfaction;
    private ResidentialZone home;
    private Workplace workplace;
    private long paidTaxesInLast20YearBeforeRetired = 0;    /* RÉSZFELADAT: Nyugdíj */
    
    public Resident(int age, ResidentialZone home, Workplace workplace){
        if (age >= 18) {
            this.age = age;
        } else {
            throw new IllegalArgumentException("Invalid value! Age must be at least 18!");
        }
        
        this.satisfaction = calculateSatisfaction();
        this.home = null;      // TODO: ideális Residentalzone
        this.workplace = null; // TODO: ideális Workplace
    }
    
    public int getAge(){
        return age;
    }
    
    public boolean isRetired(){
        return retired;
    }
    
    public double getChanceOfDeath(){
        return chanceOfDeath;
    }
    
    public int getSatisfaction(){
        return satisfaction;
    }
    
    public ResidentialZone getHome(){
        return home;
    }
    
    public Workplace getWorkplace(){
        return workplace;
    }
    
    public void increaseAge(){
        /* RÉSZFELADAT: Nyugdíj */
    }
    
    public void increaseSatisfaction(){
        if (satisfaction < 10) {
            satisfaction++;
        }
    }
    
    public void decreaseSatisfaction(){
        if (satisfaction > -10) {
            satisfaction--;
        }
    }
    
    public void setHome(ResidentialZone home) {
        this.home = home;
    }
    
    public void setWorkplace(Workplace workplace) {
        this.workplace = workplace;
    }
    
    private void movesAwayFromCity() {
        
    }
    
    public int calculateSatisfaction() {
        /* TODO: Tényezők szerint kiszámítja az elégedettséget */
        return 0;
    }
    
    public void retire() {
        /* RÉSZFELADAT: Nyugdíj */
    }
    
    public void die() {
        /* RÉSZFELADAT: Nyugdíj */
    }
}
