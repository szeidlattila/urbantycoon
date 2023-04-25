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
    private final double SCREENHEIGHT = Toolkit.getDefaultToolkit().getScreenSize().getHeight();
    private final double SCREENWIDTH = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
    private final int HOWMANYRESIDENTSTOLOWERSAFETY = 30;
    
    private Field selectedField = null;

    private boolean showFieldInfoPopup = false;
    private int satisfaction = 0;
    private int universialSatisfaction = 0;
    private int criticalSatisfaction;
    private int moveInSatisfaction;
    private long budget;
    private int zonePrice;
    private int roadPrice;
    private int stadiumPrice;
    private int policeStationPrice;
    private int fireStationPrice;
    private double annualFeePercentage; // playerBuildIt -> annualFee = price * annualFeePercentage
    private int negativeBudgetNthYear = 0;
    private int tax = 100;
    private int residentCapacity;
    private int workplaceCapacity;
    private double moveInChance;
    private int residentialZoneNum;
    private int serviceZoneNum;
    private int industrialZoneNum;

    
    public City(int residentsNum, int fieldSize, int fieldRowsNum, int fieldColsNum, int criticalSatisfaction, int moveInSatisfaction,
            int budget, int zonePrice, int roadPrice, int stadiumPrice, int policeStationPrice, int fireStationPrice,
            double annualFeePercentage, int residentCapacity, int workplaceCapacity, double refund, double chanceOfFire, int radius,
            int width, int height) {
        if (residentsNum > 0) {
            this.residents = new ArrayList<>(residentsNum);
        } else {
            throw new IllegalArgumentException("Invalid value! Residents number must be greater than 0!");
        }

        if (-10 <= criticalSatisfaction && criticalSatisfaction < 0) {
            this.criticalSatisfaction = criticalSatisfaction;
        } else {
            throw new IllegalArgumentException("Invalid value! Critical satisfaction must be at least -10 and lower than 0!");
        }
        
        if (0 < moveInSatisfaction && moveInSatisfaction <= 10) {
            this.moveInChance = moveInSatisfaction;
        } else {
            throw new IllegalArgumentException("Invalid value! Move in satisfaction must be greater than 0 and at most 10!");
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
        calculateUniversialSatisfaction();
        int sumSatisfaction = 0;
        ArrayList<Resident> removeResidents = new ArrayList<>();
        for (Resident resident : residents) {
            resident.setSatisfaction(universialSatisfaction + whatSatisfactionFor(resident));
            if (resident.getSatisfaction() <= criticalSatisfaction) {
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
        universialSatisfaction -= negativeBudgetNthYear * (int) (Math.abs(budget) / 1000);
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
            universialSatisfaction -= (int) ((Math.abs(szolgaltatasbanDolgozok - iparbanDolgozok) / residents.size()) * 10);
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
        if (homeIndexX == -1 || workIndexX == -1)
            throw new IllegalArgumentException("Workplace/Home not Found!");

        sat += 5 - getDistanceAlongRoad(fields[workIndexX][workIndexY], fields[homeIndexX][homeIndexY], fields);

        for (int i = Math.max(0, homeIndexX - 5); i < Math.min(fields.length, homeIndexX + 6); i++) {
            for (int j = Math.max(0, homeIndexY - 5); j < Math.min(fields[0].length, homeIndexY + 6); j++)
                if (fields[i][j].getBuilding() instanceof IndustrialZone)
                    sat += Math.max(Math.abs(i - homeIndexX), Math.abs(j - homeIndexY)) - 6;
        }
        sat += (int) ((r.getHome().getSatisfactionBonus() + r.getWorkplace().getSatisfactionBonus()) / 2);
        sat += (int) ((r.getHome().getSafety() + r.getWorkplace().getSafety())/2);
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
        if (tax <= 0)   tax = 0;
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
        if (selectedField != null && (selectedField == fields[y][x] || (!selectedField.isFree() && selectedField.getBuilding() == fields[y][x].getBuilding()))) {
            selectedField.unselect();
            boolean isAccessible = false;
            if(!selectedField.isFree() && !selectedField.getBuilding().isBuiltUp())
                isAccessible = isAccessibleOnRoad(selectedField);
            if(!selectedField.isFree())
                selectedField.getBuilding().unselect(isAccessible);
            selectedField = null;
            showFieldInfoPopup = false;
        } else {
            selectedField = fields[y][x];
            showFieldInfoPopup = true;
        }
    }

    // Itt minden a selectedField-del dolgozik.

    public String getFieldInfo() {
        if (selectedField == null) {
            throw new IllegalArgumentException("Trying to get info when selected Field is null");
        }
        return selectedField.getInfo();
    }

    public void nominateAsIndustrialZone() {
        if (selectedField == null) {
            throw new IllegalArgumentException("Trying to get info when selected Field is null");
        }
        // TODO
    }

    public void nominateAsServiceZone() {
        if (selectedField == null) {
            throw new IllegalArgumentException("Trying to get info when selected Field is null");
        }
        // TODO
    }

    public void nominateAsResidentialZone() {
        if (selectedField == null) {
            throw new IllegalArgumentException("Trying to get info when selected Field is null");
        }
        // TODO
    }

    public void buildRoad() {
        if (selectedField == null) {
            throw new IllegalArgumentException("Trying to get info when selected Field is null");
        }
        // TODO
    }

    public void buildPoliceStation() {
        if (selectedField == null) {
            throw new IllegalArgumentException("Trying to get info when selected Field is null");
        }
        // TODO
    }

    public void buildFireStation() {
        if (selectedField == null) {
            throw new IllegalArgumentException("Trying to get info when selected Field is null");
        }
        // TODO
    }

    public void buildStadium() {
        if (selectedField == null) {
            throw new IllegalArgumentException("Trying to get info when selected Field is null");
        }
        // TODO
    }

    public void tryDenominateOrDestroyZone() {
        if (selectedField == null) {
            throw new IllegalArgumentException("Trying to get info when selected Field is null");
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
                throw new IllegalArgumentException("Cannot delete the road!");
            } else {
                refund = (int) (selectedField.destroyOrDenominate() * REFUND);
            }
            if (refund != 0) {
                budget += refund;
                selectedField.select();
                reevaluateAccessibility();
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
                if (!field.isFree()) {
                    budget += field.getBuilding().getAnnualTax();
                    budget -= field.getBuilding().getAnnualFee();
                    budget += countField(Stadium.class) * 3 * getAnnualFee(stadiumPrice); // because stadium size is 2x2 and decrease budget 4 times more
                }
            }
        }
        if (budget < 0)
            negativeBudgetNthYear++;
        else
            negativeBudgetNthYear = 0;
        
        if (satisfaction >= moveInSatisfaction) {
            for (int i = 0; i < (satisfaction - moveInSatisfaction) + 1; i++) { // example: moveInSat := 5 : sat := 5 => add 1 resident; sat := 6 => add 2 residents; ... 
                moveInOneResident();
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
                    if (field.getBuilding().getChanceOfFire() > random || field.getBuilding().getChanceOfFire() == 1.0) {
                        field.getBuilding().startBurning(currentDate);
                    }
                }
            }
        }
    }
    
    public void dayElapsed(Date currentDate) {
        for (Field[] row : fields) {
            for (Field field : row) {
                if (!field.isFree() && field.getBuilding().isBuiltUp() && field.getBuilding().isBurntDown(currentDate)) {
                    if (field.getBuilding() instanceof Zone zone)   moveOut(zone);
                    field.burnsDown();
                }
            }
        }
    }

    public void performTicks(int ticks) {
        if (ticks > 0) {
            for (int i = 0; i < fields.length; i++)
                for (int j = 0; j < fields[0].length; j++){
                    if (!fields[i][j].isFree() && fields[i][j].getBuilding() instanceof Zone
                            && isAccessibleOnRoad(fields[i][j]))
                        if (fields[i][j].getBuilding().progressBuilding(ticks)) {
                            reevaluateAccessibility();
                        }
                    if (!fields[i][j].isFree() && fields[i][j].getBuilding() instanceof Zone zone) {
                        zone.setAnnualTax(tax);
                    }
                    if (!fields[i][j].isFree() && fields[i][j].getBuilding().isBuiltUp() && fields[i][j].getBuilding() instanceof Zone zone) {
                        zone.setImage(new ImageIcon("data/graphics/field/unselected/" + zone.type() + ".png").getImage()); 
                    }
                    if (fields[i][j].isFree() && fields[i][j].isBurntDown()) {
                        fields[i][j].setImage(new ImageIcon("data/graphics/field/" + (selectedField == fields[i][j] ? "" : "un") + "selected/notBurning/burntDownField.png").getImage());
                    }
                }
            if (selectedField != null && !selectedField.isFree() && selectedField.getBuilding() instanceof Zone zone && zone.isBuiltUp()) {
                selectedField.getBuilding().setImage(new ImageIcon("data/graphics/field/selected/" + zone.type() + ".png").getImage());
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
        for(int i = x-1; i<=x+1;i++){
            for(int j=y-1;j<=y+1;j++)
                if(Math.abs(i-x) + Math.abs(j-y) == 1 && i>=0 && i<fields.length && j>=0 && j<fields[0].length)
                    if(!fields[i][j].isFree() && fields[i][j].getBuilding() instanceof Road){
                        Q.add(new Coordinate(i,j));
                        voltemar[i][j] = true;
                    }
        }
        while (!Q.isEmpty()) {
            Coordinate o = (Coordinate) Q.remove();
            if (o.x + 1 < fields.length && ((!voltemar[o.x + 1][o.y] && !fields[o.x + 1][o.y].isFree()
                    && fields[o.x + 1][o.y].getBuilding() instanceof Road) || (!fields[o.x + 1][o.y].isFree() && fields[o.x + 1][o.y].getBuilding() instanceof Zone))) {
                if (fields[o.x + 1][o.y].getBuilding() instanceof Zone zone && zone.isBuiltUp())
                    return true;
                Q.add(new Coordinate(o.x + 1, o.y));
                voltemar[o.x + 1][o.y] = true;
            }
            if (o.x - 1 >= 0 && ((!voltemar[o.x - 1][o.y] && !fields[o.x - 1][o.y].isFree()
                    && fields[o.x - 1][o.y].getBuilding() instanceof Road) || (!fields[o.x - 1][o.y].isFree() && fields[o.x - 1][o.y].getBuilding() instanceof Zone))) {
                if (fields[o.x - 1][o.y].getBuilding() instanceof Zone zone && zone.isBuiltUp())
                    return true;
                Q.add(new Coordinate(o.x - 1, o.y));
                voltemar[o.x - 1][o.y] = true;
            }
            if (o.y + 1 < fields[0].length && ((!voltemar[o.x][o.y + 1] && !fields[o.x][o.y + 1].isFree()
                    && fields[o.x][o.y + 1].getBuilding() instanceof Road) || (!fields[o.x][o.y + 1].isFree() && fields[o.x][o.y + 1].getBuilding() instanceof Zone))) {
                if (fields[o.x][o.y + 1].getBuilding() instanceof Zone zone && zone.isBuiltUp())
                    return true;
                Q.add(new Coordinate(o.x, o.y + 1));
                voltemar[o.x][o.y + 1] = true;
            }
            if (o.y - 1 >= 0 && ((!voltemar[o.x][o.y - 1] && !fields[o.x][o.y - 1].isFree()
                    && fields[o.x][o.y - 1].getBuilding() instanceof Road) || (!fields[o.x][o.y - 1].isFree() && fields[o.x][o.y - 1].getBuilding() instanceof Zone))) {
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
        this.residentialZoneNum = 0;
        this.serviceZoneNum = 0;
        this.industrialZoneNum = 0;
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
        for (Field[] row : fields) {
            for (Field field : row) {
                if (!field.isFree() && field.getBuilding() instanceof Zone)
                    field.getBuilding().progressBuilding(4);
            }
        }
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
            ResidentialZone home = null;
            Workplace workplace = null;
            Field homeField = null;
            Field workplaceField = null;
            for (int j = 0; j < fields.length; j++) {
                for (int k = 0; k < fields[0].length; k++) {
                    Field field = fields[j][k];
                    if (!field.isFree()) {
                        if (field.getBuilding() instanceof ResidentialZone freeResidentialZone
                                && !freeResidentialZone.isFull()) {
                            home = freeResidentialZone;
                            homeField = field;
                            freeResidentialZone.incrementPeopleNum();
                            /* System.out.printf("#%d Residents's home(%d/%d) is (%d,%d)%n", i,
                                    tmp.getPeopleNum(), tmp.getCapacity(), j, k); */
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
            for (int j = 0; j < fields.length; j++) {
                for (int k = 0; k < fields[0].length; k++) {
                    Field field = fields[j][k];
                    if (!field.isFree()) {
                        if (field.getBuilding() instanceof Workplace freeWorkplace && !freeWorkplace.isFull()) {
                            workplaceField = field;
                            if (getDistanceAlongRoad(homeField, workplaceField, fields) > -1) {
                                workplace = freeWorkplace;
                                freeWorkplace.incrementPeopleNum();
                                /* System.out.printf("#%d Residents's workplace(%d/%d) is (%d,%d)%n%n", i,
                                        tmp.getPeopleNum(), tmp.getCapacity(), j, k); */
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

            if (home != null && workplace != null) {
                this.residents.add(new Resident((int) r.nextInt((60 - 18) + 1) + 18, home, workplace));
            } else {
                throw new IllegalArgumentException("Home and workplace cannot be null!");
            }

            // update residential zones graphics after residents move in
            for (Field[] fields : this.fields) {
                for (Field field : fields) {
                    if (!field.isFree() && field.getBuilding().isBuiltUp() && field.getBuilding() instanceof ResidentialZone residentialZone) {
                        residentialZone.setImage(new ImageIcon("data/graphics/field/unselected/" + residentialZone.type() + ".png").getImage());
                        
                    }
                }
            }
            /*
             * for (Field[] row : fields) {
             * for (Field field : row) {
             * if (!field.isFree()) {
             * if (field.getBuilding() instanceof ResidentialZone freeResidentialZone
             * && !freeResidentialZone.isFull()) {
             * home = freeResidentialZone;
             * homeField = field;
             * 
             * } else if (field.getBuilding() instanceof Workplace freeWorkplace &&
             * !freeWorkplace.isFull()) {
             * workplaceField = field;
             * if (getDistanceAlongRoad(homeField, workplaceField, fields) > -1) {
             * workplace = freeWorkplace;
             * }
             * }
             * 
             * if (home != null && workplace != null) {
             * break;
             * }
             * }
             * 
             * if (home != null && workplace != null) {
             * break;
             * }
             * }
             * }
             * 
             * if (home != null && workplace != null) {
             * this.residents.add(new Resident((int) r.nextInt((60 - 18) + 1) + 18, home,
             * workplace));
             * } else {
             * throw new IllegalArgumentException("Home and workplace cannot be null!");
             * }
             */
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
                            new ResidentialZone(1.0, residentCapacity, zonePrice, tax, 0, 0, REFUND, CHANCEOFFIRE, (i + 1) * WIDTH,
                                    (rowIndex + 1) * HEIGHT, WIDTH, HEIGHT,
                                    new ImageIcon("data/graphics/field/unselected/notBurning/residentialZoneEmpty.png").getImage()),
                            (i + 1) * WIDTH, (rowIndex + 1) * HEIGHT, WIDTH, HEIGHT,
                            new ImageIcon("data/graphics/field/unselected/notBurning/field.png").getImage());
                    break;
                case "sz":
                    fields[rowIndex][i] = new Field(
                            new ServiceZone(workplaceCapacity, zonePrice, tax, 0, 0, REFUND, CHANCEOFFIRE, (i + 1) * WIDTH,
                                    (rowIndex + 1) * HEIGHT, WIDTH, HEIGHT,
                                    new ImageIcon("data/graphics/field/unselected/notBurning/serviceZone.png").getImage()),
                            (i + 1) * WIDTH, (rowIndex + 1) * HEIGHT, WIDTH, HEIGHT,
                            new ImageIcon("data/graphics/field/unselected/notBurning/field.png").getImage());
                    break;
                case "iz":
                    fields[rowIndex][i] = new Field(
                            new IndustrialZone(workplaceCapacity, zonePrice, tax, 0, 0, REFUND, CHANCEOFFIRE, (i + 1) * WIDTH,
                                    (rowIndex + 1) * HEIGHT, WIDTH, HEIGHT,
                                    new ImageIcon("data/graphics/field/unselected/notBurning/industrialZone.png").getImage()),
                            (i + 1) * WIDTH, (rowIndex + 1) * HEIGHT, WIDTH, HEIGHT,
                            new ImageIcon("data/graphics/field/unselected/notBurning/field.png").getImage());
                    break;
                case "r":
                    fields[rowIndex][i] = new Field(
                            new Road(roadPrice, (int) (roadPrice * annualFeePercentage), (i + 1) * WIDTH,
                                    (rowIndex + 1) * HEIGHT, WIDTH, HEIGHT,
                                    new ImageIcon("data/graphics/field/unselected/notBurning/road.png").getImage(), REFUND),
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
                            WIDTH * 2, HEIGHT * 2, new ImageIcon("data/graphics/field/selected/notBurning/stadium.png").getImage(),
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
                            WIDTH, HEIGHT, new ImageIcon("data/graphics/field/selected/notBurning/policeStation.png").getImage(),
                            REFUND, CHANCEOFFIRE));
        } else if (playerBuildItClass == FireStation.class) {
            selectedField.build(
                    new FireStation(price, getAnnualFee(price), RADIUS, selectedField.getX(), selectedField.getY(),
                            WIDTH, HEIGHT, new ImageIcon("data/graphics/field/selected/notBurning/fireStation.png").getImage(),
                            REFUND));
        }
        reevaluateAccessibility();

        budget -= price;
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
                                                    : (isAccessibleOnRoad(selectedField) ? "notBurning/build" : "notBurning/unableBuild"))
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
        bonus -= (int)(residents.size()/HOWMANYRESIDENTSTOLOWERSAFETY);
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
                    for (int k = 0; k < 4; k++)
                        if (getDistanceAlongRoad(stadium.fields[k], field, fields) != -1) {
                            bonus += STADIUMSATBONUS;
                            usedStadiums.add(stadium);
                            break;
                        }
                }
        return bonus;
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
                            if (fields[l][k].getBuilding() instanceof Workplace && fields[l][k].getBuilding().isBuiltUp()) {
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
                            if (fields[l][k].getBuilding() instanceof ResidentialZone && fields[l][k].getBuilding().isBuiltUp()) {
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
     * @return
     */
    private class Coordinate {
        public int x, y;

        public Coordinate(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    private int getDistanceAlongRoad(Field one, Field other, Field[][] fields) {
        if (one == other)
            return 0;
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
        return distances[x2][y2];
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
                if (field.getBuilding() instanceof Road)                  sum += getAnnualFee(roadPrice);
                else if (field.getBuilding() instanceof Stadium)          sum += getAnnualFee(stadiumPrice);
                else if (field.getBuilding() instanceof PoliceStation)    sum += getAnnualFee(policeStationPrice);
                else if (field.getBuilding() instanceof FireStation)      sum += getAnnualFee(fireStationPrice);
            }
        }
        return sum - countField(Stadium.class) * 3 * getAnnualFee(stadiumPrice); // because stadium size is 2x2 and decrease budget 4 times more
    }
    
    /**
     * 
     * @param buildableClass
     * @return count of given field
     * if given field is stadium: divide count by 4 (because stadium is 2x2)
     */
    public int countField(Class buildableClass) {
        int count = 0;
        for (Field[] fields : this.fields) {
            for (Field field : fields) {
                if (!field.isFree() && field.getBuilding().getClass() == buildableClass)  count++;
            }
        }
        return buildableClass == Stadium.class ? (int)count/4 : count;
    }
    
    /**
     * find a ResidentialZone (not full, already built up) with the highest movInChance which is connected to at least 1 Workplace (not full, already build up) by road
     * find the closest Workplace to this ResidentialZone connected by Road
     */
    public void moveInOneResident() {
        ResidentialZone bestResidentialZone = null;
        Workplace nearestWorkplace = null;
        double highestMoveInChance = Double.NEGATIVE_INFINITY;
        
        for (Field[] fields : this.fields) {
            for (Field field : fields) {
                if (!field.isFree() && (field.getBuilding() instanceof ResidentialZone) && field.getBuilding().isBuiltUp()) {
                    if (((ResidentialZone)field.getBuilding()).getCapacity() - ((ResidentialZone)field.getBuilding()).getPeopleNum() > 0) {
                        if (((ResidentialZone)field.getBuilding()).getMoveInChance() > highestMoveInChance) {
                            Workplace tempWorkplace = findNearestWorkplace(field);
                            if (tempWorkplace != null) {
                                bestResidentialZone = (ResidentialZone)field.getBuilding();
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
            this.residents.add(new Resident((int) r.nextInt((60 - 18) + 1) + 18, bestResidentialZone, nearestWorkplace));
            bestResidentialZone.incrementPeopleNum();
            nearestWorkplace.incrementPeopleNum();
        }
    }
    
    /**
     * Find the nearest Workplace which is not full and already built up from the given field
     * @param field1
     * @return the nearest workplace if there is no workplace null
     */
    private Workplace findNearestWorkplace(Field field1) {
        Workplace nearestWorkplace = null;
        int minDistance = Integer.MAX_VALUE;
        
        for (Field[] fields : this.fields) {
            for (Field field : fields) {
                if (!field.isFree() && field.getBuilding().isBuiltUp() && (field.getBuilding() instanceof Workplace)) {
                    if (((Workplace)field.getBuilding()).getCapacity() - ((Workplace)field.getBuilding()).getPeopleNum() > 0) {
                        int distance = getDistanceAlongRoad(field1, field, this.fields);
                        if (distance >= 0 && distance < minDistance) {
                            nearestWorkplace = (Workplace)field.getBuilding();
                            minDistance = distance;
                        }
                    }
                }
            }
        }
        return  nearestWorkplace;
    }
    
    public void fireFighting() {
        if (selectedField == null || selectedField.isFree() || !selectedField.getBuilding().isBurning()) {
            return;
        }
        
        if (true) { // TODO
            selectedField.getBuilding().stopBurning();
        }
    }
    
    public void moveOut(Zone zone) {
        ArrayList<Resident> removeResidents = new ArrayList<>();
        for (Resident resident : residents) {
            if (resident.getHome().equals(zone) || resident.getWorkplace().equals(zone)) {
                resident.movesAwayFromCity();
                removeResidents.add(resident);
            }
        }
        
        for (Resident removeResident : removeResidents) {
            residents.remove(removeResident);
        }
    }
}
