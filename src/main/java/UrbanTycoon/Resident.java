/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package UrbanTycoon;

import java.util.Objects;
import java.util.Random;

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
    private long paidTaxesBeforeRetired = 0;
    private int workedYearsBeforeRetired = 0;
    private Field homeField;
    private Field workplaceField;    

    public Resident(int age, ResidentialZone home, Workplace workplace) {
        if (age >= 18 && age <= 60) {
            this.age = age;
        } else {
            throw new IllegalArgumentException("Invalid value! Age must be at least 18!");
        }

        this.satisfaction = 0;
        this.home = home;
        this.workplace = workplace;
    }

    public Field getHomeField() {
        return homeField;
    }

    public void setHomeField(Field homeField) {
        this.homeField = homeField;
    }

    public Field getWorkplaceField() {
        return workplaceField;
    }

    public void setWorkplaceField(Field workplaceField) {
        this.workplaceField = workplaceField;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setRetired(boolean retired) {
        this.retired = retired;
    }

    public void setChanceOfDeath(double chanceOfDeath) {
        this.chanceOfDeath = chanceOfDeath;
    }

    public long getPaidTaxesBeforeRetired() {
        return paidTaxesBeforeRetired;
    }

    public void setPaidTaxesBeforeRetired(long paidTaxesBeforeRetired) {
        this.paidTaxesBeforeRetired = paidTaxesBeforeRetired;
    }

    public int getWorkedYearsBeforeRetired() {
        return workedYearsBeforeRetired;
    }

    public void setWorkedYearsBeforeRetired(int workedYearsBeforeRetired) {
        this.workedYearsBeforeRetired = workedYearsBeforeRetired;
    }

    public void setSatisfaction(int satisfaction) {
        this.satisfaction = satisfaction;
    }

    public int getAge() {
        return age;
    }

    public boolean isRetired() {
        return retired;
    }

    public double getChanceOfDeath() {
        return chanceOfDeath;
    }

    public int getSatisfaction() {
        return satisfaction;
    }

    public ResidentialZone getHome() {
        return home;
    }

    public Workplace getWorkplace() {
        return workplace;
    }

    /**
     * returns true if resident died
     * 
     * @return
     */
    public boolean increaseAge() {
        ++age;
        if (retired) {
            Random r = new Random();
            if (r.nextDouble() <= chanceOfDeath) {
                die();
                return true;
            } else
                chanceOfDeath += 0.1;
        }

        if (age >= 65 && !retired) {
            retire();
        }
        return false;
        /* RÉSZFELADAT: Nyugdíj */
    }

    public void paidTaxes(int amount) {
        if (retired)
            throw new IllegalArgumentException("Nyugdíjas, de adózott");
        if (age >= 45)
            paidTaxesBeforeRetired += amount;
    }

    public void workedAYear() {
        if (retired)
            throw new IllegalArgumentException("Nyugdíjas, de dolgozott");
        if (age >= 45)
            workedYearsBeforeRetired++;
    }

    public int getYearlyRetirement() {
        if (!retired)
            throw new IllegalArgumentException("Nem nyugdíjas!");
        return (int) (paidTaxesBeforeRetired / workedYearsBeforeRetired / 2);
    }

    public void increaseSatisfaction() {
        if (satisfaction < 10) {
            satisfaction++;
        }
    }

    public void decreaseSatisfaction() {
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

    public void movesAwayFromCity() {
        if (home != null) {
            home.setPeopleNum(home.getPeopleNum() - 1);
            home = null;
        }
        if (workplace != null) {
            workplace.setPeopleNum(workplace.getPeopleNum() - 1);
            workplace = null;
        }
    }

    public void retire() {
        retired = true;
        System.out.println("Retired! " + age + " Paid taxes: " + paidTaxesBeforeRetired + " worked years: "
                + workedYearsBeforeRetired);
        workplace.setPeopleNum(workplace.getPeopleNum() - 1);
        workplace = null;
    }

    public void die() {
        home.setPeopleNum(home.getPeopleNum() - 1);
        home = null;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + this.age;
        hash = 79 * hash + (this.retired ? 1 : 0);
        hash = 79 * hash + (int) (Double.doubleToLongBits(this.chanceOfDeath)
                ^ (Double.doubleToLongBits(this.chanceOfDeath) >>> 32));
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

    @Override
    public String toString() {
        return "age: " + age + ", satisfaction: " + satisfaction + ", workplace: "
                + (retired ? "retired" : (workplace.getClass() == IndustrialZone.class ? "Industrial" : "Service"));
    }

    public String asString(int homeX, int homeY, int workX, int workY) {
        StringBuilder b = new StringBuilder();
        b.append(age);
        b.append(';');
        b.append(retired);
        b.append(';');
        b.append(chanceOfDeath);
        b.append(';');
        b.append(homeX);
        b.append(';');
        b.append(homeY);
        b.append(';');
        b.append(workX);
        b.append(';');
        b.append(workY);
        b.append(';');
        b.append(satisfaction);
        b.append(';');
        b.append(workedYearsBeforeRetired);
        b.append(';');
        b.append(paidTaxesBeforeRetired);
        return b.toString();
    }
}
