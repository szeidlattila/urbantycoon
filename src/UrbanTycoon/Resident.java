/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package UrbanTycoon;

import java.util.Objects;

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

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + this.age;
        hash = 79 * hash + (this.retired ? 1 : 0);
        hash = 79 * hash + (int) (Double.doubleToLongBits(this.chanceOfDeath) ^ (Double.doubleToLongBits(this.chanceOfDeath) >>> 32));
        hash = 79 * hash + this.satisfaction;
        hash = 79 * hash + Objects.hashCode(this.home);
        hash = 79 * hash + Objects.hashCode(this.workplace);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Resident other = (Resident) obj;
        if (this.age != other.age) {
            return false;
        }
        if (this.retired != other.retired) {
            return false;
        }
        if (Double.doubleToLongBits(this.chanceOfDeath) != Double.doubleToLongBits(other.chanceOfDeath)) {
            return false;
        }
        if (this.satisfaction != other.satisfaction) {
            return false;
        }
        if (!Objects.equals(this.home, other.home)) {
            return false;
        }
        return Objects.equals(this.workplace, other.workplace);
    }
    
    
}
