/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package UrbanTycoon;

import java.awt.Image;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Scanner;
import javax.swing.ImageIcon;
import java.util.*;
import java.awt.*;
import java.util.ArrayList;
import javax.swing.JFrame;

/**
 *
 * @author Felhasználó
 */
class City {

    private ArrayList<Resident> residents;
    private Field[][] fields;

    private final double REFUND;
    private final double CHANCEOFFIRE;
    private final int RADIUS;
    private final int POLICESTATIONSAFETY = 1;
    private final int STADIUMSATBONUS = 1;
    private final int WIDTH;
    private final int HEIGHT;
    private final int HOWMANYRESIDENTSTOLOWERSAFETY = 30;
    private final int criticalSatisfaction;
    private final int moveInSatisfaction;
    private final int residentCapacity;
    private final int workplaceCapacity;
    private final int zonePrice;
    private final int roadPrice;
    private final int stadiumPrice;
    private final int policeStationPrice;
    private final int fireStationPrice;
    private int forestPrice;
    private final double annualFeePercentage; // playerBuildIt -> annualFee = price * annualFeePercentage

    Field selectedField = null;

    int satisfaction = 0;
    int universialSatisfaction = 0;
    private long budget;
    int negativeBudgetNthYear = 0;
    private int tax = 100;

    public City(int residentsNum, int fieldSize, int fieldRowsNum, int fieldColsNum, int criticalSatisfaction,
            int moveInSatisfaction,
            int budget, int zonePrice, int roadPrice, int stadiumPrice, int policeStationPrice, int fireStationPrice,
            int forestPrice,
            double annualFeePercentage, int residentCapacity, int workplaceCapacity, double refund, double chanceOfFire,
            int radius,
            int width, int height) {
        if (residentsNum > 0) {
            this.residents = new ArrayList<>(residentsNum);
        } else {
            throw new IllegalArgumentException("Invalid value! Residents number must be greater than 0!");
        }

        if (-10 <= criticalSatisfaction && criticalSatisfaction < 0) {
            this.criticalSatisfaction = criticalSatisfaction;
        } else {
            throw new IllegalArgumentException(
                    "Invalid value! Critical satisfaction must be at least -10 and lower than 0!");
        }

        if (0 < moveInSatisfaction && moveInSatisfaction <= 10) {
            this.moveInSatisfaction = moveInSatisfaction;
        } else {
            throw new IllegalArgumentException(
                    "Invalid value! Move in satisfaction must be greater than 0 and at most 10!");
        }

        this.budget = budget;

        if (zonePrice > 0) {
            this.zonePrice = zonePrice;
        } else {
            throw new IllegalArgumentException("Invalid value! Zone price must be greater than 0!");
        }

        if (roadPrice > 0) {
            this.roadPrice = roadPrice;
        } else {
            throw new IllegalArgumentException("Invalid value! Road price must be greater than 0!");
        }

        if (stadiumPrice > 0) {
            this.stadiumPrice = stadiumPrice;
        } else {
            throw new IllegalArgumentException("Invalid value! Stadium price must be greater than 0!");
        }

        if (policeStationPrice > 0) {
            this.policeStationPrice = policeStationPrice;
        } else {
            throw new IllegalArgumentException("Invalid value! Police station price must be greater than 0!");
        }

        if (fireStationPrice > 0) {
            this.fireStationPrice = fireStationPrice;
        } else {
            throw new IllegalArgumentException("Invalid value! Fire station price must be greater than 0!");
        }

        if (forestPrice > 0) {
            this.forestPrice = forestPrice;
        } else {
            throw new IllegalArgumentException("Invalid value! Forest price must be greater than 0!");
        }

        if (0.0 < annualFeePercentage) {
            this.annualFeePercentage = annualFeePercentage;
        } else {
            throw new IllegalArgumentException("Invalid value! Annual fee percentage must be greater than 0.0!");
        }

        if (residentCapacity > 0) {
            this.residentCapacity = residentCapacity;
        } else {
            throw new IllegalArgumentException("Invalid value! Residential zone capacity must be greater than 0!");
        }

        if (workplaceCapacity > 0) {
            this.workplaceCapacity = workplaceCapacity;
        } else {
            throw new IllegalArgumentException("Invalid value! Workplace zone capacity must be greater than 0!");
        }

        if (0.0 < refund && refund < 1.0) {
            REFUND = refund;
        } else {
            throw new IllegalArgumentException("Invalid value! Refund must be greater than 0.0 and lower than 1.0!");
        }

        if (0.0 <= chanceOfFire && chanceOfFire <= 1.0) {
            CHANCEOFFIRE = chanceOfFire;
        } else {
            throw new IllegalArgumentException("Invalid value! Chance of fire must be greater between 0.0 and 1.0!");
        }

        if (radius > 0) {
            RADIUS = radius;
        } else {
            throw new IllegalArgumentException("Invalid value! Radius must be greater than 0!");
        }

        if (width > 0) {
            WIDTH = width;
        } else {
            throw new IllegalArgumentException("Invalid value! Width must be greater than 0!");
        }

        if (height > 0) {
            HEIGHT = height;
        } else {
            throw new IllegalArgumentException("Invalid value! Height must be greater than 0!");
        }

        initFields(residentsNum, residentCapacity, workplaceCapacity, fieldRowsNum, fieldColsNum);
        initResidents(residentsNum);
        changeSatisfaction();
    }

    public void printFields() {
        for (int i = 0; i < fields.length; i++) {
            for (int j = 0; j < fields[0].length; j++) {
                if (fields[i][j].getBuilding() == null) {
                    System.out.print("E ");
                } else {
                    System.out.print(fields[i][j].getBuilding().getClass().getSimpleName() + " ");
                }
            }
            System.out.println();
        }
    }

    public ArrayList<Resident> getResidents() {
        return residents;
    }

    public int getResidentsNum() {
        return residents.size();
    }

    public Field[][] getFields() {
        return fields;
    }

    public int getSatisfaction() {
        return satisfaction;
    }

    public int getCriticalSatisfaction() {
        return criticalSatisfaction;
    }

    public long getBudget() {
        return budget;
    }

    public Field getSelectedField() {
        return selectedField;
    }

    public int negativeBudgetNthYear() {
        return negativeBudgetNthYear;
    }

    public void setBudget(long budget) {
        this.budget = budget;
    }

    public void decrementBudget(long fee) {
        this.budget = budget - fee;
    }

    public void setNegativeBudgetNthYear(int n) {
        negativeBudgetNthYear = n;
    }

    /**
     * add the given resident to residents
     * 
     * @param resident
     */
    public void addResident(Resident resident) {
        residents.add(resident);
    }

    /**
     * remove the given resident from residents
     * if the given resident is not in the city won't happen anything
     * 
     * @param resident
     */
    public void removeResident(Resident resident) {
        residents.remove(resident);
    }

    /**
     * 
     * @return true if the city satisfaction is critical otherwise false
     */
    public boolean isSatisfactionCritical() {
        return satisfaction <= criticalSatisfaction;
    }

    /**
     * Calculate residents avarage satisfaction and this value will be the city
     * satisfaction
     */
    private void changeSatisfaction() {
    	reevaluateAccessibility();
        calculateUniversialSatisfaction();
        int sumSatisfaction = 0;
        ArrayList<Resident> removeResidents = new ArrayList<>();
        for (Resident resident : residents) {
            resident.setSatisfaction(universialSatisfaction + whatSatisfactionFor(resident));
            if (resident.getSatisfaction() <= criticalSatisfaction && !resident.isRetired()) {
                resident.movesAwayFromCity();
                removeResidents.add(resident);
            }
            sumSatisfaction += resident.getSatisfaction();
        }
        if (residents.size() == 0) {
            this.satisfaction = criticalSatisfaction;
        } else {
            this.satisfaction = sumSatisfaction / residents.size();
        }

        for (Resident removeResident : removeResidents) {
            residents.remove(removeResident);
        }
    }

    private void calculateUniversialSatisfaction() {
        universialSatisfaction = (int) ((1000 - tax) / 100);
        universialSatisfaction -= negativeBudgetNthYear * Math.abs(Math.ceil(budget / 1000.0));
        int szolgaltatasbanDolgozok = 0, iparbanDolgozok = 0;
        for (Resident res : residents) {
            if (res.getWorkplace() instanceof IndustrialZone)
                iparbanDolgozok++;
            else if (res.getWorkplace() instanceof ServiceZone)
                szolgaltatasbanDolgozok++;
        }
        if (residents.size() == 0) {
            universialSatisfaction = criticalSatisfaction;
        } else {
            universialSatisfaction -= (int) ((Math.abs(szolgaltatasbanDolgozok - iparbanDolgozok) / residents.size())
                    * 10);
        }
    }

    private int whatSatisfactionFor(Resident r) {
        int workIndexX = -1, workIndexY = -1, homeIndexX = -1, homeIndexY = -1;
        int sat = 0;
        for (int i = 0; i < fields.length; i++)
            for (int j = 0; j < fields[0].length; j++) {
                if (!fields[i][j].isFree() && r.getWorkplace() == fields[i][j].getBuilding()) {
                    workIndexX = i;
                    workIndexY = j;
                } else if (!fields[i][j].isFree() && r.getHome() == fields[i][j].getBuilding()) {
                    homeIndexX = i;
                    homeIndexY = j;
                }
            }
        if (homeIndexX == -1)
            throw new IllegalArgumentException("Home not Found!");
        sat += r.getHome().getSatisfactionBonus();
        sat += r.getHome().getSafety();
        for (int i = Math.max(0, homeIndexX - 5); i < Math.min(fields.length, homeIndexX + 6); i++) {
            for (int j = Math.max(0, homeIndexY - 5); j < Math.min(fields[0].length, homeIndexY + 6); j++)
                if (fields[i][j].getBuilding() instanceof IndustrialZone)
                    sat += Math.max(Math.abs(i - homeIndexX), Math.abs(j - homeIndexY)) - 6;
        }
        if (r.isRetired())
            return sat;

        // Ha nem nyugdíjas

        if (workIndexX == -1)
            throw new IllegalArgumentException("Workplace not Found!");
        int d = getDistanceAlongRoad(fields[workIndexX][workIndexY], fields[homeIndexX][homeIndexY], fields);
        if (d==-1) throw new IllegalArgumentException("Work and home not connected! work: " + workIndexX + " " + workIndexY + " Home: " + homeIndexX + " " + homeIndexY);
        sat += 5 - d;

        sat += r.getWorkplace().getSatisfactionBonus();
        sat += r.getWorkplace().getSafety();
        return sat;
    }

    public int getTax() {
        return tax;
    }

    public void increaseTax() {
        tax += 50;
    }

    public void lowerTax() {
        tax -= 50;
        if (tax <= 0)
            tax = 0;
    }

    /**
     * 
     * @param price
     * @return price * annualFeePercentage
     */
    public int getAnnualFee(int price) {
        return (int) Math.ceil(price * annualFeePercentage);
    }

    public int getRefund(int price) {
        return (int) Math.ceil(price * REFUND);
    }

    public void fieldSelect(int x, int y) {
        if (selectedField != null && (selectedField == fields[x][y]
                || (!selectedField.isFree() && selectedField.getBuilding() == fields[x][y].getBuilding()))) {
            selectedField.unselect();
            boolean isAccessible = false;
            if (!selectedField.isFree() && !selectedField.getBuilding().isBuiltUp())
                isAccessible = isAccessibleOnRoad(selectedField);
            if (!selectedField.isFree())
                selectedField.getBuilding().unselect(isAccessible);
            selectedField = null;
        } else {
            selectedField = fields[x][y];
        }
    }

    // Itt minden a selectedField-del dolgozik.

    public String getFieldInfo() {
        if (selectedField == null) {
            throw new IllegalArgumentException("Trying to get info when selected Field is null");
        }
        return selectedField.getInfo();
    }

    public void tryDenominateOrDestroyZone() {
        boolean agreeToDelete = true;
        boolean disconnectedRoad = false;
        boolean residentProblem = false;
        if (selectedField == null) {
            return;
        }
        if (!selectedField.isFree()) {
            int refund = 0;
            if (selectedField.getBuilding() instanceof PoliceStation) {
                // setSafetyAround(selectedField, -POLICESTATIONSAFETY);
                refund = (int) (selectedField.destroyOrDenominate() * REFUND);
            } else if (selectedField.getBuilding() instanceof Stadium) {
                // setSatisfactionBonusAround(selectedField, -STADIUMSATBONUS);
                Stadium s = (Stadium) selectedField.getBuilding();
                for (int i = 0; i < 4; i++) {
                    refund = (int) (s.fields[i].destroyOrDenominate() * REFUND);
                }
            } else if (selectedField.getBuilding() instanceof Road && !canDeleteRoad(selectedField)) {
                agreeToDelete = false;
                if (GameEngine.showConfirmationDialog(
                        "Are you sure you want to delete this road? It may cause disconnections.", "Deleting Road")) {
                    agreeToDelete = true;
                    disconnectedRoad = true;
                }

            } else if (selectedField.getBuilding() instanceof Zone && selectedField.getBuilding().isBuiltUp()) {
                agreeToDelete = false;
                if (GameEngine.showConfirmationDialog(
                        "Are you sure you want to delete this zone? It may cause dissatisfaction.", "Deleting Zone")) {
                    agreeToDelete = true;
                    residentProblem = true;
                    refreshResidentData();
                }
            }
            if (agreeToDelete) {
                refund = (int) (selectedField.destroyOrDenominate() * REFUND);
                if (disconnectedRoad) {
                    refreshWorkplaces();
                }
                if (residentProblem) {
                    reassignHomesOrWorkplaces();
                }
            }
            if (refund != 0) {
                budget += refund;
                selectedField.select();
                reevaluateAccessibility();
            }
            //checkResidentData();
        }
    }

    public void checkResidentData() {
        for (int i = 0; i < residents.size(); i++) {
            Resident r = residents.get(i);
            System.out.printf("#%d Reidents's home is: ", i);
            if (r.getHome() == null) {
                System.out.print("null; ");
            } else {
                System.out.print("not null; ");
            }
            if (r.getWorkplace() == null) {
                System.out.println("workplace is null; ");
            } else {
                System.out.println("workplace is not null; ");
            }
        }
    }

    public void refreshWorkplaces() {
        for (Resident r : residents) {
            if (getDistanceAlongRoad(r.getHomeField(), r.getWorkplaceField(), fields) == -1) {
                r.setWorkplace(null);
                r.setWorkplaceField(null);
                r.decreaseSatisfaction();
                decrementBudget(500);
            }
        }
        reassignHomesOrWorkplaces();
    }

    public void refreshResidentData() {
        for (int i = 0; i < residents.size(); i++) {
            Resident r = residents.get(i);
            if (selectedField.getBuilding() == r.getHome()) {
                r.setHome(null);
                r.setWorkplace(null);
                r.decreaseSatisfaction();
                decrementBudget(500);
            }
            if (selectedField.getBuilding() == r.getWorkplace()) {
                r.setWorkplace(null);
                r.decreaseSatisfaction();
                decrementBudget(500);
            }
        }
    }

    /*
     * private void setSafetyAround(Field ps , int safetyToBeAdded) {
     * int x = -1, y = -1;
     * for (int i = 0; i < fields.length; i++) {
     * for (int j = 0; j < fields[0].length; j++) {
     * if (fields[i][j] == ps){
     * x = i;
     * y = j;
     * }
     * }
     * }
     * if (y == -1 && x == -1)
     * throw new IllegalArgumentException("Destroyed police station not found");
     * for (int i = Math.max(0, x - RADIUS); i <= Math.min(fields.length - 1,
     * x + RADIUS); i++) {
     * for (int j = Math.max(0, y - RADIUS); j <= Math.min(fields[0].length - 1,
     * y + RADIUS); j++) {
     * if (!fields[i][j].isFree() && fields[i][j].getBuilding() instanceof Zone zone
     * && getDistanceAlongRoad(ps,fields[i][j]) != -1) {
     * zone.setSafety(
     * Math.min(Math.max(-10, zone.getSafety() + safetyToBeAdded),10));
     * }
     * }
     * }
     * }
     */
    /*
     * private void setSatisfactionBonusAround(Field st, int satToBeAdded) {
     * int x = -1, y = -1;
     * for (int i = 0; i < fields.length; i++) {
     * for (int j = 0; j < fields[0].length; j++) {
     * if (fields[i][j] == st){
     * x = i;
     * y = j;
     * }
     * }
     * }
     * if (y == -1 && x == -1)
     * throw new IllegalArgumentException("Destroyed stadium not found");
     * 
     * // stadion 2x2 es, mind a 4 mezo (instanceof Stadium), itt elpozicionál a bal
     * // felső sarkába
     * if (x - 1 >= 0 && fields[x - 1][y].getBuilding() instanceof Stadium)
     * x--;
     * if (y - 1 >= 0 && fields[x][y - 1].getBuilding() instanceof Stadium)
     * y--;
     * 
     * for (int i = Math.max(0, x - RADIUS); i <= Math.min(fields.length - 1,
     * x + 1 + RADIUS); i++) {
     * for (int j = Math.max(0, y - RADIUS); j <= Math.min(fields[i].length - 1,
     * y + 1 + RADIUS); j++) {
     * if (!fields[i][j].isFree() && fields[i][j].getBuilding() instanceof Zone
     * zone) {
     * for(int k = 0; k< 4 ; k++)
     * if(getDistanceAlongRoad(((Stadium)st.getBuilding()).fields[k],fields[i][j])
     * != -1){
     * zone.setSatisfactionBonus(Math.min(Math.max(-10,
     * zone.getSatisfactionBonus() + satToBeAdded),10));
     * break;
     * }
     * }
     * }
     * }
     * }
     */
    public void yearElapsed() {
        for (Field[] row : fields) {
            for (Field field : row) {
                if (!field.isFree() && field.getBuilding().isBuiltUp()) {
                    budget -= field.getBuilding().getAnnualFee();
                    if (field.getBuilding() instanceof Forest forest) {
                        forest.increaseAgeBy1();
                    }
                }
            }
        }
        budget += countField(Stadium.class) * 3 * getAnnualFee(stadiumPrice); // because stadium size is 2x2 and decrease budget 4 times more
        for (int i = 0; i < residents.size(); i++) {
            Resident r = residents.get(i);
            if (!r.isRetired()) {
                budget += r.tax();
            } else
                budget -= r.getYearlyRetirement();
            if (r.increaseAge()) {
                residents.remove(r);
                i--;
                moveInOneResident(true);
            }
        }
        if (budget < 0)
            negativeBudgetNthYear++;
        else
            negativeBudgetNthYear = 0;

        if (satisfaction >= moveInSatisfaction) {
            for (int i = 0; i < (satisfaction - moveInSatisfaction) + 1; i++) { // example: moveInSat := 5 : sat := 5 =>
                                                                                // add 1 resident; sat := 6 => add 2
                                                                                // residents; ...
                moveInOneResident(false);
            }
        }
    }

    public void monthElapsed(Date currentDate) {
        Random r = new Random();
        double random = 0.0;
        for (Field[] row : fields) {
            for (Field field : row) {
                if (!field.isFree() && field.getBuilding().isBuiltUp() && !field.getBuilding().isBurning()) {
                    random = 1.0 * r.nextDouble(); // random double between 0.0 and 1.0
                    if (calculateChanceOfFire(field) > random || calculateChanceOfFire(field) == 1.0) {
                        field.getBuilding().startBurning(currentDate);
                    }
                }
            }
        }
    }

    public void dayElapsed(Date currentDate) {
        Random r = new Random();
        double random = 0.0;

        for (int i = 0; i < fields.length; i++) {
            for (int j = 0; j < fields[i].length; j++) {
                // leég a mező
                if (!fields[i][j].isFree() && fields[i][j].getBuilding().isBuiltUp()
                        && fields[i][j].getBuilding().isBurntDown(currentDate)) {
                    if (fields[i][j].getBuilding() instanceof Zone zone)
                        moveOut(zone);
                    fields[i][j].burnsDown();
                }

                // átterjedhet a tűz szomszédos mezőkre
                if (!fields[i][j].isFree() && fields[i][j].getBuilding().isBuiltUp()
                        && fields[i][j].getBuilding().isBurning()) {
                    if (j + 1 < fields[i].length && !fields[i][j + 1].isFree()
                            && fields[i][j + 1].getBuilding().isBuiltUp()
                            && !fields[i][j + 1].getBuilding().isBurning()) {
                        random = 1.0 * r.nextDouble();
                        if (calculateChanceOfFire(fields[i][j + 1]) > random) {
                            fields[i][j + 1].getBuilding().startBurning(currentDate);
                        }
                    }

                    if (i + 1 < fields.length && !fields[i + 1][j].isFree()
                            && fields[i + 1][j].getBuilding().isBuiltUp()
                            && !fields[i + 1][j].getBuilding().isBurning()) {
                        random = 1.0 * r.nextDouble();
                        if (calculateChanceOfFire(fields[i + 1][j]) > random) {
                            fields[i + 1][j].getBuilding().startBurning(currentDate);
                        }
                    }

                    if (j - 1 >= 0 && !fields[i][j - 1].isFree() && fields[i][j - 1].getBuilding().isBuiltUp()
                            && !fields[i][j - 1].getBuilding().isBurning()) {
                        random = 1.0 * r.nextDouble();
                        if (calculateChanceOfFire(fields[i][j - 1]) > random) {
                            fields[i][j - 1].getBuilding().startBurning(currentDate);
                        }
                    }

                    if (i - 1 >= 0 && !fields[i - 1][j].isFree() && fields[i - 1][j].getBuilding().isBuiltUp()
                            && !fields[i - 1][j].getBuilding().isBurning()) {
                        random = 1.0 * r.nextDouble();
                        if (calculateChanceOfFire(fields[i - 1][j]) > random) {
                            fields[i - 1][j].getBuilding().startBurning(currentDate);
                        }
                    }
                }

                // tűzoltóautó mozgatása
                if (!fields[i][j].isFree() && fields[i][j].getBuilding() instanceof FireStation fireStation) {
                    if (!fireStation.getFireEngine().isAvailable()) {
                        // visszamegy
                        if (fireStation.getFireEngine().isMovingBack()) {
                            fireStation.getFireEngine().moveBackNextRoad();
                        } else if (fireStation.getFireEngine().moveNextRoad()) { // odamegy
                            fireStation.getFireEngine().getDestination().stopBurning();
                        }

                    }
                }
            }
        }
    }

    public void performTicks(int ticks) {
        if (ticks > 0) {
            for (int i = 0; i < fields.length; i++)
                for (int j = 0; j < fields[0].length; j++) {
                    if (!fields[i][j].isFree() && fields[i][j].getBuilding() instanceof Zone
                            && isAccessibleOnRoad(fields[i][j]))
                        if (fields[i][j].getBuilding().progressBuilding(ticks)) {
                            reevaluateAccessibility();
                        }
                    if (!fields[i][j].isFree() && fields[i][j].getBuilding() instanceof Zone zone) {
                        zone.setAnnualTax(tax);
                    }
                    if (!fields[i][j].isFree() && fields[i][j].getBuilding().isBuiltUp()) {
                        fields[i][j].getBuilding()
                                .setImage(new ImageIcon(
                                        "data/graphics/field/unselected/" + fields[i][j].getBuilding().type() + ".png")
                                        .getImage());
                    }
                    if (fields[i][j].isFree() && fields[i][j].isBurntDown()) {
                        fields[i][j].setImage(
                                new ImageIcon("data/graphics/field/" + (selectedField == fields[i][j] ? "" : "un")
                                        + "selected/notBurning/burntDownField.png").getImage());
                    }
                    if (!fields[i][j].isFree() && fields[i][j].getBuilding() instanceof Road road) {
                        road.setImage(new ImageIcon("data/graphics/field/" + (selectedField == fields[i][j] ? "" : "un")
                                + "selected/" + road.type() + ".png").getImage());
                    }
                }
            if (selectedField != null && !selectedField.isFree() && selectedField.getBuilding().isBuiltUp()) {
                selectedField.getBuilding().setImage(
                        new ImageIcon("data/graphics/field/selected/" + selectedField.getBuilding().type() + ".png")
                                .getImage());
            }
            changeSatisfaction();
        }
    }

    public boolean isAccessibleOnRoad(Field field) {
        int x = -1, y = -1;
        boolean voltemar[][] = new boolean[fields.length][fields[0].length];
        for (int i = 0; i < fields.length; i++)
            for (int j = 0; j < fields[0].length; j++) {
                voltemar[i][j] = false;
                if (fields[i][j] == field) {
                    x = i;
                    y = j;
                }
            }
        if (x == -1 && y == -1)
            throw new IllegalArgumentException("First Field not Found in getDistance");
        Queue Q = new LinkedList<Coordinate>();
        voltemar[x][y] = true;
        for (int i = x - 1; i <= x + 1; i++) {
            for (int j = y - 1; j <= y + 1; j++)
                if (Math.abs(i - x) + Math.abs(j - y) == 1 && i >= 0 && i < fields.length && j >= 0
                        && j < fields[0].length)
                    if (!fields[i][j].isFree() && fields[i][j].getBuilding() instanceof Road) {
                        Q.add(new Coordinate(i, j));
                        voltemar[i][j] = true;
                    }
        }
        while (!Q.isEmpty()) {
            Coordinate o = (Coordinate) Q.remove();
            if (o.x + 1 < fields.length && ((!voltemar[o.x + 1][o.y] && !fields[o.x + 1][o.y].isFree()
                    && fields[o.x + 1][o.y].getBuilding() instanceof Road)
                    || (!fields[o.x + 1][o.y].isFree() && fields[o.x + 1][o.y].getBuilding() instanceof Zone))) {
                if (fields[o.x + 1][o.y].getBuilding() instanceof Zone zone && zone.isBuiltUp())
                    return true;
                Q.add(new Coordinate(o.x + 1, o.y));
                voltemar[o.x + 1][o.y] = true;
            }
            if (o.x - 1 >= 0 && ((!voltemar[o.x - 1][o.y] && !fields[o.x - 1][o.y].isFree()
                    && fields[o.x - 1][o.y].getBuilding() instanceof Road)
                    || (!fields[o.x - 1][o.y].isFree() && fields[o.x - 1][o.y].getBuilding() instanceof Zone))) {
                if (fields[o.x - 1][o.y].getBuilding() instanceof Zone zone && zone.isBuiltUp())
                    return true;
                Q.add(new Coordinate(o.x - 1, o.y));
                voltemar[o.x - 1][o.y] = true;
            }
            if (o.y + 1 < fields[0].length && ((!voltemar[o.x][o.y + 1] && !fields[o.x][o.y + 1].isFree()
                    && fields[o.x][o.y + 1].getBuilding() instanceof Road)
                    || (!fields[o.x][o.y + 1].isFree() && fields[o.x][o.y + 1].getBuilding() instanceof Zone))) {
                if (fields[o.x][o.y + 1].getBuilding() instanceof Zone zone && zone.isBuiltUp())
                    return true;
                Q.add(new Coordinate(o.x, o.y + 1));
                voltemar[o.x][o.y + 1] = true;
            }
            if (o.y - 1 >= 0 && ((!voltemar[o.x][o.y - 1] && !fields[o.x][o.y - 1].isFree()
                    && fields[o.x][o.y - 1].getBuilding() instanceof Road)
                    || (!fields[o.x][o.y - 1].isFree() && fields[o.x][o.y - 1].getBuilding() instanceof Zone))) {
                if (fields[o.x][o.y - 1].getBuilding() instanceof Zone zone && zone.isBuiltUp())
                    return true;
                Q.add(new Coordinate(o.x, o.y - 1));
                voltemar[o.x][o.y - 1] = true;
            }
        }
        return false;
    }

    /**
     * initialize fields
     * calculate how many Residential zone and Workplace (Service zone and
     * Industrial zone) needed depends on residentNum and zone capacities
     * fill fields with the right zones
     * 
     * @param residentsNum
     * @param homeCapacity
     * @param workplaceCapacity
     * @param fieldRowsNum
     * @param fieldColsNum
     */
    private void initFields(int residentsNum, int residentCapacity, int workplaceCapacity, int fieldRowsNum,
            int fieldColsNum) {
        int rows = 0;
        int cols = 0;
        this.fields = new Field[fieldRowsNum][fieldColsNum];

        try {
            Scanner reader = new Scanner(new File("data/persistence/initFields.txt"));
            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                processLine(line, rows);
                rows++;
                cols = line.split("\\s+").length;
            }

            reader.close();

            if (rows != fieldRowsNum) {
                throw new IllegalArgumentException("Invalid value! File lines number (" + rows
                        + ") must be FIELDROWSNUM (" + fieldRowsNum + ") !");
            }

            if (cols != fieldColsNum) {
                throw new IllegalArgumentException(
                        "Invalid value! File cols number (" + cols + ") must be FIELDCOLSNUM (" + fieldColsNum + ") !");
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
        }

        for (Field[] row : fields) {
            for (Field field : row) {
                if (!field.isFree() && field.getBuilding() instanceof Zone zone)
                    zone.setBuiltUp(true);
            }
        }
        /*
         * for (Field[] row : fields) {
         * for (Field field : row) {
         * if (!field.isFree() && field.getBuilding() instanceof Zone)
         * field.getBuilding().progressBuilding(4);
         * }
         * }
         */
    }

    /**
     * initialize residents
     * initialize resident with a random age between 18 and 60, home which is not
     * full, workplace which is not full
     * 
     * @param residentsNum
     */
    private void initResidents(int residentsNum) {
        Random r = new Random();
        for (int i = 0; i < residentsNum; i++) {
            this.residents.add(new Resident((int) r.nextInt((60 - 18) + 1) + 18, null, null));
        }

        for (int i = 0; i < residents.size(); i++) {
            Resident res = residents.get(i);
            initHome(res);
            initWorkplace(res);
            if (res.getWorkplace() == null || res.getHome() == null) {
                throw new IllegalArgumentException("Home or Workplace cannot be null at initialization.");
            }
            for (Field[] fields : this.fields) {
                for (Field field : fields) {
                    if (!field.isFree() && field.getBuilding().isBuiltUp()
                            && field.getBuilding() instanceof ResidentialZone residentialZone) {
                        residentialZone.setImage(
                                new ImageIcon("data/graphics/field/unselected/" + residentialZone.type() + ".png")
                                        .getImage());

                    }
                }
            }
        }
    }

    public Field initHome(Resident r) {
        ResidentialZone home = null;
        Field homeField = null;
        for (int j = 0; j < fields.length; j++) {
            for (int k = 0; k < fields[0].length; k++) {
                Field field = fields[j][k];
                if (!field.isFree()) {
                    if (field.getBuilding() instanceof ResidentialZone freeResidentialZone
                            && !freeResidentialZone.isFull()) {
                        home = freeResidentialZone;
                        homeField = field;
                        freeResidentialZone.incrementPeopleNum();
                    }
                }
                if (home != null) {
                    break;
                }

            }
            if (home != null) {
                break;
            }
        }
        r.setHomeField(homeField);
        r.setHome(home);
        return homeField;
    }

    public void initWorkplace(Resident r) {
        Workplace workplace = null;
        Field workplaceField = null;
        if (r.getHome() != null) {
            for (int j = 0; j < fields.length; j++) {
                for (int k = 0; k < fields[0].length; k++) {
                    Field field = fields[j][k];
                    if (!field.isFree()) {
                        if (field.getBuilding() instanceof Workplace freeWorkplace && !freeWorkplace.isFull()) {
                            workplaceField = field;
                            if (getDistanceAlongRoad(r.getHomeField(), workplaceField, fields) > -1) {
                                workplace = freeWorkplace;
                                freeWorkplace.incrementPeopleNum();
                                //System.out.printf("Workpace is (%d,%d)%n", j, k);
                            }
                        }
                    }
                    if (workplace != null) {
                        break;
                    }
                }
                if (workplace != null) {
                    break;
                }
            }
        }
        r.setWorkplaceField(workplaceField);
        r.setWorkplace(workplace);

    }

    public void reassignHomesOrWorkplaces() {
        ArrayList<Resident> removeResidents = new ArrayList<>();
        for (Resident r : residents) {
            if (r.getHome() == null) {
                initHome(r);
                initWorkplace(r);
            }
            if (r.getWorkplace() == null) {
                initWorkplace(r);
            }
            // still no home or workplace then the resident leaves the city
            if (r.getWorkplace() == null || r.getHome() == null) {
                removeResidents.add(r);
            }
        }
        for (Resident r : removeResidents) {
            r.movesAwayFromCity();
            residents.remove(r);
        }
    }

    private void processLine(String line, int rowIndex) {
        String[] fieldStrings = line.split("\\s+"); // It will split the string by single or multiple whitespace
                                                    // characters
        for (int i = 0; i < fieldStrings.length; i++) {
            String fieldType = fieldStrings[i];
            switch (fieldType) {
                case "0":
                    fields[rowIndex][i] = new Field(null, (i + 1) * WIDTH, (rowIndex + 1) * HEIGHT, WIDTH, HEIGHT,
                            new ImageIcon("data/graphics/field/unselected/notBurning/field.png").getImage());
                    break;
                case "rz":
                    fields[rowIndex][i] = new Field(
                            new ResidentialZone(1.0, residentCapacity, zonePrice, tax, 0, 0, REFUND, CHANCEOFFIRE,
                                    (i + 1) * WIDTH,
                                    (rowIndex + 1) * HEIGHT, WIDTH, HEIGHT,
                                    new ImageIcon("data/graphics/field/unselected/notBurning/residentialZoneEmpty.png")
                                            .getImage()),
                            (i + 1) * WIDTH, (rowIndex + 1) * HEIGHT, WIDTH, HEIGHT,
                            new ImageIcon("data/graphics/field/unselected/notBurning/field.png").getImage());
                    break;
                case "sz":
                    fields[rowIndex][i] = new Field(
                            new ServiceZone(workplaceCapacity, zonePrice, tax, 0, 0, REFUND, CHANCEOFFIRE,
                                    (i + 1) * WIDTH,
                                    (rowIndex + 1) * HEIGHT, WIDTH, HEIGHT,
                                    new ImageIcon("data/graphics/field/unselected/notBurning/serviceZone.png")
                                            .getImage()),
                            (i + 1) * WIDTH, (rowIndex + 1) * HEIGHT, WIDTH, HEIGHT,
                            new ImageIcon("data/graphics/field/unselected/notBurning/field.png").getImage());
                    break;
                case "iz":
                    fields[rowIndex][i] = new Field(
                            new IndustrialZone(workplaceCapacity, zonePrice, tax, 0, 0, REFUND, CHANCEOFFIRE,
                                    (i + 1) * WIDTH,
                                    (rowIndex + 1) * HEIGHT, WIDTH, HEIGHT,
                                    new ImageIcon("data/graphics/field/unselected/notBurning/industrialZone.png")
                                            .getImage()),
                            (i + 1) * WIDTH, (rowIndex + 1) * HEIGHT, WIDTH, HEIGHT,
                            new ImageIcon("data/graphics/field/unselected/notBurning/field.png").getImage());
                    break;
                case "r":
                    fields[rowIndex][i] = new Field(
                            new Road(roadPrice, (int) (roadPrice * annualFeePercentage), (i + 1) * WIDTH,
                                    (rowIndex + 1) * HEIGHT, WIDTH, HEIGHT,
                                    new ImageIcon("data/graphics/field/unselected/notBurning/road.png").getImage(),
                                    REFUND),
                            (i + 1) * WIDTH, (rowIndex + 1) * HEIGHT, WIDTH, HEIGHT,
                            new ImageIcon("data/graphics/field/unselected/notBurning/field.png").getImage());
                    break;
                case "f":
                    fields[rowIndex][i] = new Field(
                            new Forest(forestPrice, (int) (forestPrice * annualFeePercentage), (i + 1) * WIDTH,
                                    (rowIndex + 1) * HEIGHT, WIDTH, HEIGHT,
                                    new ImageIcon("data/graphics/field/unselected/notBurning/forest.png").getImage(),
                                    REFUND, CHANCEOFFIRE),
                            (i + 1) * WIDTH, (rowIndex + 1) * HEIGHT, WIDTH, HEIGHT,
                            new ImageIcon("data/graphics/field/unselected/notBurning/field.png").getImage());
                    break;
                default:
                    throw new IllegalArgumentException("Unknow field type: " + fieldType);
            }
        }
    }

    /**
     * Build a Buildable
     * budget decrease by the price and build the chosen building
     * 
     * @param selectedField
     * @param playerBuildItClass
     * @return error message (field is not free / do not have enough money)
     *         otherwise null
     */
    public void build(Class playerBuildItClass) {
        if (selectedField == null || !selectedField.isFree())
            return;
        if (!PlayerBuildIt.class.isAssignableFrom(playerBuildItClass))
            throw new IllegalArgumentException("Building must be playerBuiltIt subclass!");
        int price = -1;
        if (playerBuildItClass == Road.class)
            price = roadPrice;
        else if (playerBuildItClass == Stadium.class)
            price = stadiumPrice;
        else if (playerBuildItClass == PoliceStation.class)
            price = policeStationPrice;
        else if (playerBuildItClass == FireStation.class)
            price = fireStationPrice;
        else if (playerBuildItClass == Forest.class)
            price = forestPrice;
        if (price < 0)
            throw new IllegalArgumentException("Invalid value! Price must be greater than 0!");

        // The field is free, have enough money -> build it:
        if (playerBuildItClass == Road.class) {
            selectedField.build(new Road(price, getAnnualFee(price), selectedField.getX(), selectedField.getY(), WIDTH,
                    HEIGHT, new ImageIcon("data/graphics/field/selected/notBurning/road.png").getImage(), REFUND));
        } else if (playerBuildItClass == Stadium.class) {
            int iIndex = 0, jIndex = 0;
            for (int i = 0; i < fields.length; i++) {
                for (int j = 0; j < fields[0].length; j++) {
                    if (selectedField == fields[i][j]) {
                        iIndex = i;
                        jIndex = j;
                        break;
                    }
                }
                if (jIndex != 0) {
                    break;
                }
            }
            if (iIndex > 0 && jIndex > 0) {
                if (fields[iIndex - 1][jIndex - 1].isFree() && fields[iIndex - 1][jIndex].isFree()
                        && fields[iIndex][jIndex - 1].isFree()) {
                    Stadium s = new Stadium(price, getAnnualFee(price), RADIUS, selectedField.getX() - WIDTH,
                            selectedField.getY() - HEIGHT,
                            WIDTH * 2, HEIGHT * 2,
                            new ImageIcon("data/graphics/field/selected/notBurning/stadium.png").getImage(),
                            REFUND, CHANCEOFFIRE);
                    selectedField.build(s);
                    s.fields[0] = selectedField;
                    s.fields[1] = fields[iIndex - 1][jIndex - 1];
                    s.fields[2] = fields[iIndex - 1][jIndex];
                    s.fields[3] = fields[iIndex][jIndex - 1];
                    fields[iIndex - 1][jIndex - 1].build(s);
                    fields[iIndex - 1][jIndex].build(s);
                    fields[iIndex][jIndex - 1].build(s);
                } else {
                    throw new IllegalArgumentException("Invalid value! Nearby fields are occupied.");

                }
            } else {
                throw new IllegalArgumentException("Invalid value! Can't build on selected field.");
            }
        } else if (playerBuildItClass == PoliceStation.class) {
            selectedField.build(
                    new PoliceStation(price, getAnnualFee(price), RADIUS, selectedField.getX(), selectedField.getY(),
                            WIDTH, HEIGHT,
                            new ImageIcon("data/graphics/field/selected/notBurning/policeStation.png").getImage(),
                            REFUND, CHANCEOFFIRE));
        } else if (playerBuildItClass == FireStation.class) {
            selectedField.build(
                    new FireStation(price, getAnnualFee(price), RADIUS, selectedField.getX(), selectedField.getY(),
                            WIDTH, HEIGHT,
                            new ImageIcon("data/graphics/field/selected/notBurning/fireStation.png").getImage(),
                            REFUND));
        } else if (playerBuildItClass == Forest.class) {
            selectedField.build(
                    new Forest(price, getAnnualFee(price), selectedField.getX(), selectedField.getY(),
                            WIDTH, HEIGHT, new ImageIcon("data/graphics/field/selected/forest.png").getImage(),
                            REFUND, CHANCEOFFIRE));
            calculateForestBonusResZone();
        }
        reevaluateAccessibility();

        budget -= price;
    }

    public String saveGame() {
        StringBuilder b = new StringBuilder();
        b.append(tax);
        b.append("\n");
        b.append(budget);
        b.append("\n");
        b.append(negativeBudgetNthYear);
        b.append("\n");
        b.append(satisfaction);
        b.append("\n");
        b.append(universialSatisfaction);
        b.append("\n");
        for (var row : fields) {
            for (Field field : row) {
                b.append(field.asString());
                b.append("\n");
            }
        }
        int homeX = -1, homeY = -1, workX = -1, workY = -1;
        for (Resident r : residents) {
            for (int i = 0; i < fields.length; i++)
                for (int j = 0; j < fields[0].length; j++)
                    if (fields[i][j].getBuilding() == r.getHome()) {
                        homeX = i;
                        homeY = j;
                    } else if (fields[i][j].getBuilding() == r.getWorkplace()) {
                        workX = i;
                        workY = j;
                    }
            b.append(r.asString(homeX, homeY, workX, workY));
            b.append('\n');
        }
        return b.toString();
    }

    public void loadGame(Scanner s) {
        residents.clear();
        tax = Integer.parseInt(s.nextLine());
        budget = Integer.parseInt(s.nextLine());
        negativeBudgetNthYear = Integer.parseInt(s.nextLine());
        satisfaction = Integer.parseInt(s.nextLine());
        universialSatisfaction = Integer.parseInt(s.nextLine());
        boolean[][] gud = new boolean[fields.length][fields[0].length];
        for (int i = 0; i < fields.length; i++) {
            for (int j = 0; j < fields[0].length; j++) {
                String[] str = s.nextLine().split(";");
                if (str.length > 3 && str[1].equals("st") && !gud[i][j]) {
                    double refund = Double.parseDouble(str[2]);
                    double chanceOfFire = Double.parseDouble(str[3]);
                    int buildPrice = Integer.parseInt(str[6]);
                    int annualFee = Integer.parseInt(str[7]);
                    int radius = Integer.parseInt(str[8]);
                    Stadium stad = new Stadium(buildPrice, annualFee, radius, (j + 1) * WIDTH, (i + 1) * HEIGHT,
                            WIDTH * 2, HEIGHT * 2, whatImageFor(Stadium.class, str), refund, chanceOfFire);
                    stad.fields[0] = fields[i + 1][j + 1];
                    stad.fields[0].setBuilding(stad);
                    stad.fields[1] = fields[i][j];
                    stad.fields[1].setBuilding(stad);
                    stad.fields[2] = fields[i][j + 1];
                    stad.fields[2].setBuilding(stad);
                    stad.fields[3] = fields[i + 1][j];
                    stad.fields[3].setBuilding(stad);
                    gud[i][j] = true;
                    gud[i + 1][j + 1] = true;
                    gud[i + 1][j] = true;
                    gud[i][j + 1] = true;
                } else if (str.length > 2 && str[1].equals("st")) {

                } else {
                    fields[i][j].setBuilding(unpack(j, i, str));
                }
                if (fields[i][j].isFree())
                    fields[i][j].setBurntDown(Boolean.parseBoolean(str[0]));
            }
        }
        reevaluateAccessibility();
        while (s.hasNextLine()) {
            residents.add(residentByString(s.nextLine()));
        }
    }

    private Buildable unpack(int x, int y, String[] s) {
        Buildable b;
        if (s[1].equals("empty"))
            return null;
        double refund = Double.parseDouble(s[2]);
        double chanceOfFire = Double.parseDouble(s[3]);
        boolean burning = Boolean.parseBoolean(s[4]);
        Date date = Date.parseDate(s[5]);
        switch (s[1]) {
            case "rz" -> {
                b = new ResidentialZone(Double.parseDouble(s[14]), Integer.parseInt(s[9]), Integer.parseInt(s[13]),
                        Integer.parseInt(s[6]), Integer.parseInt(s[11]), Integer.parseInt(s[12]), refund, chanceOfFire,
                        (x + 1) * WIDTH, (y + 1) * HEIGHT, WIDTH, HEIGHT, whatImageFor(ResidentialZone.class, s));
                Zone z = (Zone) b;
                z.setPeopleNum(Integer.parseInt(s[10]));
                z.setBuildProgress(Integer.parseInt(s[7]));
                z.setBuiltUp(Boolean.parseBoolean(s[8]));
            }
            case "iz" -> {
                b = new IndustrialZone(Integer.parseInt(s[9]), Integer.parseInt(s[13]), Integer.parseInt(s[6]),
                        Integer.parseInt(s[11]), Integer.parseInt(s[12]), refund, chanceOfFire / 2, (x + 1) * WIDTH,
                        (y + 1) * HEIGHT, WIDTH, HEIGHT, whatImageFor(IndustrialZone.class, s));
                Zone z = (Zone) b;
                z.setPeopleNum(Integer.parseInt(s[10]));
                z.setBuildProgress(Integer.parseInt(s[7]));
                z.setBuiltUp(Boolean.parseBoolean(s[8]));
            }
            case "sz" -> {
                b = new ServiceZone(Integer.parseInt(s[9]), Integer.parseInt(s[13]), Integer.parseInt(s[6]),
                        Integer.parseInt(s[11]), Integer.parseInt(s[12]), refund, chanceOfFire, (x + 1) * WIDTH,
                        (y + 1) * HEIGHT, WIDTH, HEIGHT, whatImageFor(ServiceZone.class, s));
                Zone z = (Zone) b;
                z.setPeopleNum(Integer.parseInt(s[10]));
                z.setBuildProgress(Integer.parseInt(s[7]));
                z.setBuiltUp(Boolean.parseBoolean(s[8]));
            }
            case "ps" -> {
                int buildPrice = Integer.parseInt(s[6]);
                int annualFee = Integer.parseInt(s[7]);
                int radius = Integer.parseInt(s[8]);
                b = new PoliceStation(buildPrice, annualFee, radius, (x + 1) * WIDTH, (y + 1) * HEIGHT, WIDTH, HEIGHT,
                        whatImageFor(PoliceStation.class, s), refund, chanceOfFire);
            }
            case "fs" -> {
                int buildPrice = Integer.parseInt(s[6]);
                int annualFee = Integer.parseInt(s[7]);
                int radius = Integer.parseInt(s[8]);
                b = new FireStation(buildPrice, annualFee, radius, (x + 1) * WIDTH, (y + 1) * HEIGHT, WIDTH, HEIGHT,
                        whatImageFor(FireStation.class, s), refund);
            }
            case "for" -> {
                int buildPrice = Integer.parseInt(s[6]);
                int annualFee = Integer.parseInt(s[7]);
                int age = Integer.parseInt(s[8]);
                b = new Forest(buildPrice, annualFee, (x + 1) * WIDTH, (y + 1) * HEIGHT, WIDTH, HEIGHT,
                        whatImageFor(Forest.class, s), refund, chanceOfFire);
                ((Forest) b).setAge(age);
            }
            case "st" -> {
                int buildPrice = Integer.parseInt(s[6]);
                int annualFee = Integer.parseInt(s[7]);
                int radius = Integer.parseInt(s[8]);
                b = new Stadium(buildPrice, annualFee, radius, (x + 1) * WIDTH, (y + 1) * HEIGHT, WIDTH * 2, HEIGHT * 2,
                        whatImageFor(Stadium.class, s), refund, chanceOfFire);
            }
            default -> {
                int buildPrice = Integer.parseInt(s[6]);
                int annualFee = Integer.parseInt(s[7]);
                b = new Road(buildPrice, annualFee, (x + 1) * WIDTH, (y + 1) * HEIGHT, WIDTH, HEIGHT,
                        whatImageFor(Road.class, s), refund);
            }
        }
        b.setBurning(burning);
        b.setBurningStartDate(date);
        return b;
    }

    private Image whatImageFor(Class buildableClass, String[] s) {
        boolean burning = Boolean.parseBoolean(s[4]);
        StringBuilder pathName = new StringBuilder("data/graphics/field/unselected/");
        pathName.append(burning ? "burning/" : "notBurning/");
        if (PlayerBuildIt.class.isAssignableFrom(buildableClass)) {
            if (buildableClass == PoliceStation.class) {
                pathName.append("policeStation");
            } else if (buildableClass == Road.class)
                pathName.append("road");
            else if (buildableClass == FireStation.class)
                pathName.append("fireStation");
            else if (buildableClass == Forest.class)
                pathName.append("forest");
            else
                pathName.append("stadium");

        } else
            return null;
        pathName.append(".png");
        return new ImageIcon(pathName.toString()).getImage();
    }

    private Resident residentByString(String tmp) {
        String[] s = tmp.split(";");
        Resident r = new Resident(42, null, null);
        r.setAge(Integer.parseInt(s[0]));
        r.setRetired(Boolean.parseBoolean(s[1]));
        r.setChanceOfDeath(Double.parseDouble(s[2]));
        r.setHome((ResidentialZone) fields[Integer.parseInt(s[3])][Integer.parseInt(s[4])].getBuilding());
        r.setWorkplace((Workplace) fields[Integer.parseInt(s[5])][Integer.parseInt(s[6])].getBuilding());
        r.setSatisfaction(Integer.parseInt(s[7]));
        r.setWorkedYearsBeforeRetired(Integer.parseInt(s[8]));
        r.setPaidTaxesBeforeRetired(Integer.parseInt(s[9]));
        return r;
    }

    public void reevaluateAccessibility() {
        for (var row : fields)
            for (var field : row)
                if (!field.isFree() && field.getBuilding() instanceof Zone zone) {
                    boolean isAccessible = isAccessibleOnRoad(field);
                    field.getBuilding().select(isAccessible);
                    field.getBuilding().unselect(isAccessible);
                    zone.setSafety(Math.min(Math.max(calculateSafety(field), -10), 10));
                    zone.setSatisfactionBonus(Math.min(Math.max(calculateSatBonus(field), -10), 10));
                }
        if (selectedField != null && !selectedField.isFree())
            selectedField.getBuilding()
                    .setImage(
                            new ImageIcon(
                                    "data/graphics/field/selected/"
                                            + (selectedField.getBuilding().isBuiltUp()
                                                    ? selectedField.getBuilding().type()
                                                    : (isAccessibleOnRoad(selectedField) ? "notBurning/build"
                                                            : "notBurning/unableBuild"))
                                            + ".png")
                                    .getImage());
    }

    /**
     * player select free field (residential-, industrial-, service zone) and
     * residents can build on this automatically
     * budget decrease by the price of zone select
     * 
     * @param selectedField
     * @param zoneClass
     */
    public void selectField(Class zoneClass) {
        if (selectedField == null || !selectedField.isFree())
            return;
        if (!Zone.class.isAssignableFrom(zoneClass))
            throw new IllegalArgumentException("Selected zone class must be Zone subclass!");
        boolean acc = false;
        if (isAccessibleOnRoad(selectedField))
            acc = true;
        if (zoneClass == ResidentialZone.class) {
            selectedField.setBuilding(new ResidentialZone(1.0, residentCapacity, zonePrice, tax,
                    calculateSafety(selectedField), calculateSatBonus(selectedField), REFUND, CHANCEOFFIRE,
                    selectedField.getX(), selectedField.getY(), WIDTH, HEIGHT,
                    new ImageIcon("data/graphics/field/selected/notBurning/" + (acc ? "build" : "unableBuild") + ".png")
                            .getImage()));
        } else if (zoneClass == IndustrialZone.class) {
            selectedField.setBuilding(new IndustrialZone(workplaceCapacity, zonePrice, tax,
                    calculateSafety(selectedField), calculateSatBonus(selectedField), REFUND, CHANCEOFFIRE,
                    selectedField.getX(), selectedField.getY(), WIDTH, HEIGHT,
                    new ImageIcon("data/graphics/field/selected/notBurning/" + (acc ? "build" : "unableBuild") + ".png")
                            .getImage()));
        } else if (zoneClass == ServiceZone.class) {
            selectedField.setBuilding(new ServiceZone(workplaceCapacity, zonePrice, tax, calculateSafety(selectedField),
                    calculateSatBonus(selectedField), REFUND, CHANCEOFFIRE,
                    selectedField.getX(), selectedField.getY(), WIDTH, HEIGHT,
                    new ImageIcon("data/graphics/field/selected/notBurning/" + (acc ? "build" : "unableBuild") + ".png")
                            .getImage()));
        }

        budget -= zonePrice;
    }

    /**
     * A selectedField-re dolgozik
     * 
     * @return
     */
    private int calculateSafety(Field field) {
        int bonus = 0;
        int x = -1, y = -1;
        for (int i = 0; i < fields.length; i++)
            for (int j = 0; j < fields[0].length; j++) {
                if (fields[i][j] == field) {
                    x = i;
                    y = j;
                }
            }
        for (int i = Math.max(0, x - RADIUS); i <= Math.min(fields.length - 1, x + RADIUS); i++)
            for (int j = Math.max(0, y - RADIUS); j <= Math.min(fields[0].length - 1, y + RADIUS); j++)
                if (!fields[i][j].isFree() && fields[i][j].getBuilding() instanceof PoliceStation
                        && getDistanceAlongRoad(field, fields[i][j], fields) != -1) {
                    bonus += POLICESTATIONSAFETY;
                }
        bonus -= (int) (residents.size() / HOWMANYRESIDENTSTOLOWERSAFETY);
        return bonus;
    }

    /**
     * A selectedField-re dolgozik
     * 
     * @return
     */
    private int calculateSatBonus(Field field) {
        int bonus = 0;
        int x = -1, y = -1;
        for (int i = 0; i < fields.length; i++)
            for (int j = 0; j < fields[0].length; j++) {
                if (fields[i][j] == field) {
                    x = i;
                    y = j;
                }
            }
        ArrayList<Stadium> usedStadiums = new ArrayList<>();
        for (int i = Math.max(0, x - RADIUS); i <= Math.min(fields.length - 1, x + RADIUS); i++)
            for (int j = Math.max(0, y - RADIUS); j <= Math.min(fields[0].length - 1, y + RADIUS); j++)
                if (!fields[i][j].isFree() && fields[i][j].getBuilding() instanceof Stadium stadium
                        && !usedStadiums.contains(stadium)) {
                    for (int k = 0; k < 4; k++) {
                        if (getDistanceAlongRoad(stadium.fields[k], field, fields) != -1) {
                            bonus += STADIUMSATBONUS;
                            usedStadiums.add(stadium);
                            break;
                        }
                    }
                }
        return bonus;
    }

    /**
     * A selectedField-re dolgozik
     * 
     * @return
     */
    private double calculateChanceOfFire(Field field) {
        int x = -1, y = -1;
        for (int i = 0; i < fields.length; i++)
            for (int j = 0; j < fields[0].length; j++) {
                if (fields[i][j] == field) {
                    x = i;
                    y = j;
                }
            }

        for (int i = Math.max(0, x - RADIUS); i <= Math.min(fields.length - 1, x + RADIUS); i++)
            for (int j = Math.max(0, y - RADIUS); j <= Math.min(fields[0].length - 1, y + RADIUS); j++)
                if (!fields[i][j].isFree() && fields[i][j].getBuilding() instanceof FireStation
                        && getDistanceAlongRoad(field, fields[i][j], fields) != -1) {
                    return fields[x][y].getBuilding().getChanceOfFire() * 0.5;
                }
        return fields[x][y].getBuilding().getChanceOfFire();
    }

    public int[][] calculateForestBonusResZone() {
        int defaultBonus = 10;
        int[][] bonusPerHouse = new int[fields.length][fields[0].length];
        ArrayList<Field> houseFields = new ArrayList<Field>();
        ArrayList<Field> forestFields = new ArrayList<Field>();
        // populate arrayLists
        for (Field row[] : fields) {
            for (Field f : row) {
                if (f.getBuilding() instanceof ResidentialZone) {
                    houseFields.add(f);
                }
                if (f.getBuilding() instanceof Forest) {
                    forestFields.add(f);
                }
            }
        }
        // calculate eyesight
        for (int i = 0; i < fields.length; i++) {
            for (int j = 0; j < fields[0].length; j++) {
                // iterate through fields[][]
                if (fields[i][j].getBuilding() instanceof Forest) {
                    Field forest = fields[i][j];
                    // cell diagonal divided by 2
                    double halfCellDiagonal = (Math
                            .sqrt(Math.pow(forest.getHeight(), 2) + Math.pow(forest.getWidth(), 2))) / 2;
                    // forest cell middlePoint
                    double fX = forest.getX() + (forest.getWidth() / 2);
                    double fY = forest.getY() + (forest.getHeight() / 2);
                    // System.out.printf("Checking Forest (%d,%d), Middle point: (%f,%f)%n", i, j,
                    // fX, fY);
                    // checking a 7x7 are where the current Forest is in the middle
                    int k, l, m, n;
                    if (i - 3 < 0) {
                        k = 0;
                    } else {
                        k = i - 3;
                    }
                    if (j - 3 < 0) {
                        l = 0;
                    } else {
                        l = j - 3;
                    }
                    if (i + 3 > fields.length - 1) {
                        m = fields.length - 1;
                    } else {
                        m = i + 3;
                    }
                    if (j + 3 > fields[0].length - 1) {
                        n = fields[0].length - 1;
                    } else {
                        n = j + 3;
                    }
                    for (int a = k; a <= m; a++) {
                        for (int b = l; b <= n; b++) {
                            boolean blockedSight = false;
                            if (fields[a][b].getBuilding() instanceof ResidentialZone) {
                                Field house = fields[a][b];
                                // house cell middlePoint
                                double hX = house.getX() + (house.getWidth() / 2);
                                double hY = house.getY() + (house.getHeight() / 2);
                                // System.out.printf("Checking House (%d,%d), Middle point: (%f,%f)%n", a, b,
                                // hX, hY);
                                // calculate equation of the line between them, e(x)
                                double eX, eY, eR;
                                // normal vectors
                                eX = hY - fY;
                                eY = hX - fX;
                                // result
                                eR = (eX * fX) - (eY * fY);
                                // (hY - fY)*x - (hX - fX)*y = (hY - fY)*fX - (hX - fX)*fY = eE
                                int ca, cb, cm, cn;
                                // check in which corner we are
                                // upper left corner
                                ca = a;
                                cb = b;
                                cm = i;
                                cn = j;
                                if (a >= i && b >= j) {
                                    // bottom right corner
                                    ca = i;
                                    cb = j;
                                    cm = a;
                                    cn = b;
                                } else if (a <= i && b >= j) {
                                    // upper right corner
                                    ca = a;
                                    cb = j;
                                    cm = i;
                                    cn = b;
                                } else if (a >= i && b <= j) {
                                    // bottom left corner
                                    ca = i;
                                    cb = b;
                                    cm = a;
                                    cn = j;
                                }
                                // System.out.printf("Checking between corner: (%d,%d) (%d,%d)%n", ca, cb, cm,
                                // cn);
                                // iterate thru thre remaining cells in the 7x7 matrix and check if the line of
                                // sight is blocked
                                for (int o = ca; o <= cm; o++) {
                                    for (int p = cb; p <= cn; p++) {
                                        Field f = fields[o][p];
                                        // check only for non empty cells
                                        if (f.getBuilding() != null && f != house && f != forest) {
                                            // middlepoint of iterated cell
                                            double fiX = f.getX() + (f.getWidth() / 2);
                                            double fiY = f.getY() + (f.getHeight() / 2);
                                            // System.out.printf("Checking Blockade (%d,%d), Middle point: (%f,%f)%n",
                                            // o,p, fiX, fiY);
                                            // calculate distance between point and the line
                                            // pass point into e(x)
                                            double pR = eX * fiX - eY * fiY - eR;
                                            // normal vector length
                                            double nL = Math.sqrt(Math.pow(eX, 2) + Math.pow(eY, 2));
                                            // distance result
                                            double distance = Math.abs(pR / nL);
                                            // System.out.printf("Distance: %f; halfCellDiagonal: %f%n",
                                            // distance,halfCellDiagonal);
                                            // if the distance is less or equal than the diagonal/2 then its blocking
                                            // the line of sight, so we mark the house as blocked
                                            if (distance <= halfCellDiagonal) {
                                                /*
                                                 * System.out.printf(
                                                 * "Line of sight between Forest(%d,%d) and House(%d,%d) blocked by obstacle(%d,%d)%n"
                                                 * ,
                                                 * i, j, a, b, o, p);
                                                 */
                                                blockedSight = true;
                                                break;
                                            }
                                        }
                                    }
                                    if (blockedSight) {
                                        break;
                                    }
                                }
                                // if its not blocked then lets change the bonus to the biggest possible
                                if (!blockedSight) {
                                    /*
                                     * System.out.printf(
                                     * "There is a line of sight between Forest(%d,%d) and House(%d,%d)%n",
                                     * i,
                                     * j, a, b);
                                     */
                                }
                                if (!blockedSight && (bonusPerHouse[a][b] < (defaultBonus
                                        * ((Forest) forest.getBuilding()).getBonusMultiplier()))) {
                                    bonusPerHouse[a][b] = defaultBonus
                                            * ((Forest) forest.getBuilding()).getBonusMultiplier();
                                }
                            }
                        }
                    }
                }
            }
        }
        return bonusPerHouse;
    }

    public boolean canDeleteRoad(Field field) {
        // copy fields matrix to simulate road deleting
        Field[][] copyFields = new Field[fields.length][fields[0].length];

        // populating copyFields
        for (int i = 0; i < fields.length; i++) {
            for (int j = 0; j < fields[0].length; j++) {
                copyFields[i][j] = new Field(fields[i][j]);
                if (fields[i][j] == field) {
                    // deleting road from field given in the prompt
                    copyFields[i][j].setBuilding(null);
                }

            }
        }
        // check houses-workplaces connections
        for (int i = 0; i < fields.length; i++) {
            for (int j = 0; j < fields[0].length; j++) {
                if (fields[i][j].getBuilding() instanceof ResidentialZone && fields[i][j].getBuilding().isBuiltUp()) {
                    boolean isConnected = false;
                    for (int l = 0; l < fields.length; l++) {
                        for (int k = 0; k < fields[0].length; k++) {
                            if (fields[l][k].getBuilding() instanceof Workplace
                                    && fields[l][k].getBuilding().isBuiltUp()) {
                                if (getDistanceAlongRoad(copyFields[i][j], copyFields[l][k], copyFields) > -1) {
                                    isConnected = true;
                                }
                            }
                        }
                    }
                    if (!isConnected) {
                        return false;
                    }
                }
            }
        }
        for (int i = 0; i < fields.length; i++) {
            for (int j = 0; j < fields[0].length; j++) {
                if (fields[i][j].getBuilding() instanceof Workplace && fields[i][j].getBuilding().isBuiltUp()) {
                    boolean isConnected = false;
                    for (int l = 0; l < fields.length; l++) {
                        for (int k = 0; k < fields[0].length; k++) {
                            if (fields[l][k].getBuilding() instanceof ResidentialZone
                                    && fields[l][k].getBuilding().isBuiltUp()) {
                                if (getDistanceAlongRoad(copyFields[i][j], copyFields[l][k], copyFields) > -1) {
                                    isConnected = true;
                                }
                            }
                        }
                    }
                    if (!isConnected) {
                        return false;
                    }
                }
            }
        }

        return true;

    }

    /**
     * BFS
     * 
     * @param one
     * @param other
     * @return distance between fields or -1
     */
    private class Coordinate {
        public int x, y;

        public Coordinate(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    private int[][] getMatrixDistanceAlongRoad(Field one, Field other, Field[][] fields) {
        if (one == other)
            return null;
        int x1 = -1, x2 = -1, y1 = -1, y2 = -1;
        int[][] distances = new int[fields.length][fields[0].length];
        for (int i = 0; i < fields.length; i++)
            for (int j = 0; j < fields[0].length; j++) {
                distances[i][j] = -1;
                if (fields[i][j] == one) {
                    x1 = i;
                    y1 = j;
                    distances[i][j] = 0;
                }
                if (fields[i][j] == other) {
                    x2 = i;
                    y2 = j;
                }
            }
        if (x1 == -1 && y1 == -1)
            throw new IllegalArgumentException("First Field not Found in getDistance");
        if (x2 == -1 && y2 == -1)
            throw new IllegalArgumentException("Second Field not Found in getDistance");
        Queue Q = new LinkedList<Coordinate>();
        Q.add(new Coordinate(x1, y1));
        while (!Q.isEmpty()) {
            Coordinate o = (Coordinate) Q.remove();
            if ((o.x + 1 < distances.length && distances[o.x + 1][o.y] == -1 && !fields[o.x + 1][o.y].isFree()
                    && fields[o.x + 1][o.y].getBuilding() instanceof Road) || (o.x + 1 == x2 && o.y == y2)) {
                distances[o.x + 1][o.y] = distances[o.x][o.y] + 1;
                if (o.x + 1 == x2 && o.y == y2)
                    break;
                Q.add(new Coordinate(o.x + 1, o.y));
            }
            if ((o.x - 1 >= 0 && distances[o.x - 1][o.y] == -1 && !fields[o.x - 1][o.y].isFree()
                    && fields[o.x - 1][o.y].getBuilding() instanceof Road) || (o.x - 1 == x2 && o.y == y2)) {
                distances[o.x - 1][o.y] = distances[o.x][o.y] + 1;
                if (o.x - 1 == x2 && o.y == y2)
                    break;
                Q.add(new Coordinate(o.x - 1, o.y));
            }
            if ((o.y + 1 < distances[0].length && distances[o.x][o.y + 1] == -1 && !fields[o.x][o.y + 1].isFree()
                    && fields[o.x][o.y + 1].getBuilding() instanceof Road) || (o.x == x2 && o.y + 1 == y2)) {
                distances[o.x][o.y + 1] = distances[o.x][o.y] + 1;
                if (o.x == x2 && o.y + 1 == y2)
                    break;
                Q.add(new Coordinate(o.x, o.y + 1));
            }
            if ((o.y - 1 >= 0 && distances[o.x][o.y - 1] == -1 && !fields[o.x][o.y - 1].isFree()
                    && fields[o.x][o.y - 1].getBuilding() instanceof Road) || (o.x == x2 && o.y - 1 == y2)) {
                distances[o.x][o.y - 1] = distances[o.x][o.y] + 1;
                if (o.x == x2 && o.y - 1 == y2)
                    break;
                Q.add(new Coordinate(o.x, o.y - 1));
            }
        }
        return distances;
    }

    private int getDistanceAlongRoad(Field one, Field other, Field[][] fields) {
        if (one == other)
            return 0;
        int x2 = -1;
        int y2 = -1;
        for (int i = 0; i < fields.length; i++)
            for (int j = 0; j < fields[0].length; j++) {
                if (fields[i][j] == other) {
                    x2 = i;
                    y2 = j;
                }
            }

        return getMatrixDistanceAlongRoad(one, other, fields)[x2][y2];
    }

    /**
     * find a resident who lives or works at the given zone and return it
     * the satisfaction of all residents is the same in the same zones so finding 1
     * resident who lives or works there is enough
     * if the zone is empty (home or workplace without any resident) return null
     * 
     * @param zone
     * @return if there is at least 1 resident return zone satisfaction otherwise
     *         null
     */
    public Integer getZoneSatisfaction(Zone zone) {
        for (Resident resident : residents) {
            if (resident.getHome().equals(zone) || resident.getWorkplace().equals(zone)) {
                return resident.getSatisfaction();
            }
        }

        return null;
    }

    /**
     * get residents who are living or working at the given zone
     * 
     * @param zone
     * @return residents who are living or working at the given zone
     */
    public ArrayList<Resident> getZonePeople(Zone zone) {
        ArrayList<Resident> peopleInZone = new ArrayList<>();
        for (Resident resident : residents) {
            if (resident.getHome().equals(zone) || resident.getWorkplace().equals(zone)) {
                peopleInZone.add(resident);
            }
        }

        return peopleInZone;
    }

    /**
     * 
     * @return sum of annual fees
     */
    public long calculateAnnualFee() {
        long sum = 0;
        for (Field[] fields : this.fields) {
            for (Field field : fields) {
                if (field.getBuilding() instanceof Road)
                    sum += getAnnualFee(roadPrice);
                else if (field.getBuilding() instanceof Stadium)
                    sum += getAnnualFee(stadiumPrice);
                else if (field.getBuilding() instanceof PoliceStation)
                    sum += getAnnualFee(policeStationPrice);
                else if (field.getBuilding() instanceof FireStation)
                    sum += getAnnualFee(fireStationPrice);
            }
        }
        return sum - countField(Stadium.class) * 3 * getAnnualFee(stadiumPrice); // because stadium size is 2x2 and
                                                                                 // decrease budget 4 times more
    }

    /**
     * 
     * @param buildableClass
     * @return count of given field
     *         if given field is stadium: divide count by 4 (because stadium is 2x2)
     */
    public int countField(Class buildableClass) {
        int count = 0;
        for (Field[] fields : this.fields) {
            for (Field field : fields) {
                if (!field.isFree() && field.getBuilding().getClass() == buildableClass)
                    count++;
            }
        }
        return buildableClass == Stadium.class ? (int) count / 4 : count;
    }

    /**
     * find a ResidentialZone (not full, already built up) with the highest
     * movInChance which is connected to at least 1 Workplace (not full, already
     * build up) by road
     * find the closest Workplace to this ResidentialZone connected by Road
     */
    public void moveInOneResident(boolean isBecauseDeath) {
        ResidentialZone bestResidentialZone = null;
        Workplace nearestWorkplace = null;
        double highestMoveInChance = Double.NEGATIVE_INFINITY;

        for (Field[] fields : this.fields) {
            for (Field field : fields) {
                if (!field.isFree() && (field.getBuilding() instanceof ResidentialZone)
                        && field.getBuilding().isBuiltUp()) {
                    if (((ResidentialZone) field.getBuilding()).getCapacity()
                            - ((ResidentialZone) field.getBuilding()).getPeopleNum() > 0) {
                        if (((ResidentialZone) field.getBuilding()).getMoveInChance() > highestMoveInChance) {
                            Workplace tempWorkplace = findNearestWorkplace(field);
                            if (tempWorkplace != null) {
                                bestResidentialZone = (ResidentialZone) field.getBuilding();
                                nearestWorkplace = tempWorkplace;
                                highestMoveInChance = bestResidentialZone.getMoveInChance();
                            }
                        }
                    }
                }
            }
        }

        Random r = new Random();
        if (bestResidentialZone != null && nearestWorkplace != null) {
            this.residents.add(new Resident(isBecauseDeath ? 18 : (int) r.nextInt((60 - 18) + 1) + 18,
                    bestResidentialZone, nearestWorkplace));
            bestResidentialZone.incrementPeopleNum();
            nearestWorkplace.incrementPeopleNum();
        }
    }

    /**
     * Find the nearest Workplace which is not full and already built up from the
     * given field
     * 
     * @param field1
     * @return the nearest workplace if there is no workplace null
     */
    private Workplace findNearestWorkplace(Field field1) {
        Workplace nearestWorkplace = null;
        int minDistance = Integer.MAX_VALUE;

        for (Field[] fields : this.fields) {
            for (Field field : fields) {
                if (!field.isFree() && field.getBuilding().isBuiltUp() && (field.getBuilding() instanceof Workplace)) {
                    if (((Workplace) field.getBuilding()).getCapacity()
                            - ((Workplace) field.getBuilding()).getPeopleNum() > 0) {
                        int distance = getDistanceAlongRoad(field1, field, this.fields);
                        if (distance >= 0 && distance < minDistance) {
                            nearestWorkplace = (Workplace) field.getBuilding();
                            minDistance = distance;
                        }
                    }
                }
            }
        }
        return nearestWorkplace;
    }

    public void fireFighting() {
        if (selectedField == null || selectedField.isFree() || !selectedField.getBuilding().isBurning()) {
            return;
        }

        Field fireStationField = findClosestFireStation();
        if (fireStationField == null) {
            new PopupInfo(new JFrame(), "There is no available fire station!", "Warning");
            return;
        }

        int[][] distanceMatrix = getMatrixDistanceAlongRoad(selectedField, fireStationField, fields);

        /*
         * for (int i = 0; i < distanceMatrix.length; i++) {
         * for (int j = 0; j < distanceMatrix[i].length; j++) {
         * System.out.print((0 <= distanceMatrix[i][j] && distanceMatrix[i][j] < 10 ?
         * "+" : "") + distanceMatrix[i][j] + " ");
         * }
         * System.out.println("");
         * }
         * System.out.println("-------------------------------------------------");
         */

        ArrayList<Road> route = new ArrayList<>();
        int x = -1;
        int y = -1;
        for (int i = 0; i < fields.length; i++) {
            for (int j = 0; j < fields[i].length; j++) {
                if (fields[i][j].equals(fireStationField)) {
                    x = i;
                    y = j;
                    break;
                }
            }
            if (x != -1 && y != 1) {
                break;
            }
        }

        int roadsLeft = distanceMatrix[x][y];
        while (roadsLeft > 1) {
            if (y + 1 < fields[0].length && distanceMatrix[x][y + 1] == distanceMatrix[x][y] - 1) {
                route.add((Road) fields[x][++y].getBuilding());
                roadsLeft--;
                // System.out.println("Jobbra");
            } else if (x + 1 < fields.length && distanceMatrix[x + 1][y] == distanceMatrix[x][y] - 1) {
                route.add((Road) fields[++x][y].getBuilding());
                roadsLeft--;
                // System.out.println("Lefele");
            } else if (y - 1 >= 0 && distanceMatrix[x][y - 1] == distanceMatrix[x][y] - 1) {
                route.add((Road) fields[x][--y].getBuilding());
                roadsLeft--;
                // System.out.println("Balra");
            } else if (x - 1 >= 0 && distanceMatrix[x - 1][y] == distanceMatrix[x][y] - 1) {
                route.add((Road) fields[--x][y].getBuilding());
                roadsLeft--;
                // System.out.println("Felfele");
            } else {
                // -0 would be fire truck next position
                for (int i = 0; i < distanceMatrix.length; i++) {
                    for (int j = 0; j < distanceMatrix[i].length; j++) {
                        if (x == i && y == j) {
                            System.out.print("-0 ");
                        } else {
                            System.out.print((0 <= distanceMatrix[i][j] && distanceMatrix[i][j] < 10 ? "+" : "")
                                    + distanceMatrix[i][j] + " ");
                        }
                    }
                    System.out.println("");
                }

                throw new IllegalArgumentException("Fire truck cannot move to destination!");
            }

            // System.out.println("hátralevő: " + roadsLeft);
        }

        // System.out.println(route.size());

        // set up route for fire engine
        ((FireStation) fireStationField.getBuilding()).getFireEngine().setRouteAndDestination(route,
                selectedField.getBuilding());
    }

    /**
     * 
     * @return closest fireStation if there is no fireStation null
     */
    private Field findClosestFireStation() {
        Field closestFireStation = null;
        int minDistance = Integer.MAX_VALUE;
        for (Field[] fields : this.fields) {
            for (Field field : fields) {
                if (!field.isFree() && field.getBuilding() instanceof FireStation fireStation) {
                    if (fireStation.getFireEngine().isAvailable()) {
                        int distance = getDistanceAlongRoad(selectedField, field, this.fields);
                        if (distance != -1 && distance < minDistance) {
                            closestFireStation = field;
                            minDistance = distance;
                        }
                    }
                }
            }
        }
        return closestFireStation;
    }

    public void moveOut(Zone zone) {
        ArrayList<Resident> removeResidents = new ArrayList<>();
        for (Resident resident : residents) {
            if (resident.getHome().equals(zone) || zone.equals(resident.getWorkplace())) {
                resident.movesAwayFromCity();
                removeResidents.add(resident);
            }
        }

        for (Resident removeResident : removeResidents) {
            residents.remove(removeResident);
        }
    }
}
