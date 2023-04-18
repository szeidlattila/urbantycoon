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

/**
 *
 * @author Felhasználó
 */
class City {

    private ArrayList<Resident> residents;
    private Field[][] fields;

    private final double REFUND;
    private final int RADIUS;
    private final int POLICESTATIONSAFETY = 10;
    private final int STADIUMSATBONUS = 10;
    private final int WIDTH;
    private final int HEIGHT;

    private Field selectedField = null;

    private boolean showFieldInfoPopup = false;
    private int satisfaction = 0;
    private int criticalSatisfaction;
    private long budget;
    private int zonePrice;
    private int roadPrice;
    private int stadiumPrice;
    private int policeStationPrice;
    private int fireStationPrice;
    private double annualFeePercentage; // playerBuildIt -> annualFee = price * annualFeePercentage
    private int negativeBudgetNthYear = 0;
    private int tax = 0;
    private int residentCapacity;
    private int workplaceCapacity;
    private double moveInChance;
    private int residentialZoneNum;
    private int serviceZoneNum;
    private int industrialZoneNum;

    /**
     * initialize the city with the given information
     * initialize fields and residents
     * 
     * @param residentsNum
     * @param fieldSize
     * @param fieldRowsNum
     * @param fieldColsNum
     * @param criticalSatisfaction
     * @param budget
     * @param zonePrice
     * @param roadPrice
     * @param stadiumPrice
     * @param policeStationPrice
     * @param fireStationPrice
     * @param residentCapacity
     * @param workplaceCapacity
     * @param refund
     * @param radius
     */
    public City(int residentsNum, int fieldSize, int fieldRowsNum, int fieldColsNum, int criticalSatisfaction,
            int budget, int zonePrice, int roadPrice, int stadiumPrice, int policeStationPrice, int fireStationPrice,
            double annualFeePercentage, int residentCapacity, int workplaceCapacity, double refund, int radius,
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
    public void changeSatisfaction() {
        int sumSatisfaction = 0;
        for (Resident resident : residents) {
            sumSatisfaction += resident.getSatisfaction();
        }
        this.satisfaction = sumSatisfaction / residents.size();
    }

    public int getTax() {
        return tax;
    }

    public void increaseTax() {
        // TODO
    }

    public void lowerTax() {
        // TODO
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
        if (selectedField != null && selectedField == fields[y][x]) {
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
                lowerSafetyAround(selectedField);
                refund = (int) (selectedField.destroyOrDenominate() * REFUND);
            } else if (selectedField.getBuilding() instanceof Stadium) {
                lowerSatisfactionBonusAround(selectedField);
                Stadium s = (Stadium) selectedField.getBuilding();
                for (int i = 0; i < 4; i++) {
                    refund = (int) (s.fields[i].destroyOrDenominate() * REFUND);
                }
            } else {
                refund = (int) (selectedField.destroyOrDenominate() * REFUND);
            }
            if (refund != 0) {
                budget += refund;
                selectedField.select();
            }
        }
    }

    private void lowerSafetyAround(Field dps /* DestroyedPoliceStation */) {
        int x = -1, y = -1;
        for (int i = 0; i < fields.length; i++) {
            for (int j = 0; j < fields[i].length; j++) {
                if (fields[i][j] == dps){
                    x = i;
                    y = j;
                }
            }
        }
        if (y == -1 && x == -1)
            throw new IllegalArgumentException("Destroyed police station not found");
        for (int i = Math.max(0, x - (int) (RADIUS / 2)); i <= Math.min(fields.length - 1,
                x + (int) (RADIUS / 2)); i++) {
            for (int j = Math.max(0, y - (int) (RADIUS / 2)); i <= Math.min(fields[i].length - 1,
                    y + (int) (RADIUS / 2)); i++) {
                if (!fields[i][j].isFree() && fields[i][j].getBuilding() instanceof Zone) {
                    ((Zone) (fields[i][j].getBuilding())).setSafety(
                            Math.min(-10, ((Zone) fields[i][j].getBuilding()).getSafety() - POLICESTATIONSAFETY));
                }
            }
        }
    }

    private void lowerSatisfactionBonusAround(Field dst/* destroyedStadium */) {
        int x = -1, y = -1;
        for (int i = 0; i < fields.length; i++) {
            for (int j = 0; j < fields.length; j++) {
                if (fields[i][j] == dst){
                    x = i;
                    y = j;
                }
            }
        }
        if (y == -1 && x == -1)
            throw new IllegalArgumentException("Destroyed stadium not found");

        // stadion 2x2 es, mind a 4 mezo (instanceof Stadium), itt elpozicionál a bal
        // felső sarkába
        if (x - 1 >= 0 && fields[x - 1][y].getBuilding() instanceof Stadium)
            x--;
        if (y - 1 >= 0 && fields[x][y - 1].getBuilding() instanceof Stadium)
            y--;

        for (int i = Math.max(0, x - (int) (RADIUS / 2)); i <= Math.min(fields.length - 1,
                x + 1 + (int) (RADIUS / 2)); i++) {
            for (int j = Math.max(0, y - (int) (RADIUS / 2)); j <= Math.min(fields[i].length - 1,
                    y + 1 + (int) (RADIUS / 2)); j++) {
                if (!fields[i][j].isFree() && fields[i][j].getBuilding() instanceof Zone zone) {
                    zone.setSatisfactionBonus(Math.max(-10,
                            zone.getSatisfactionBonus() - STADIUMSATBONUS));
                }
            }
        }
    }

    public void yearElapsed() {
        for (Field[] row : fields) {
            for (Field field : row) {
                if (!field.isFree()) {
                    budget += field.getBuilding().getAnnualTax();
                    budget -= field.getBuilding().getAnnualFee();
                }
            }
        }
        if (budget < 0)
            negativeBudgetNthYear++;
        else
            negativeBudgetNthYear = 0;
    }

    public void performTicks(int ticks) {
        if (ticks > 0) {
            for (int i = 0; i < fields.length; i++)
                for (int j = 0; j < fields[0].length; j++)
                    if (!fields[i][j].isFree() && fields[i][j].getBuilding() instanceof Zone
                            && isAccessibleOnRoad(fields[i][j]))
                        if (fields[i][j].getBuilding().progressBuilding(ticks)) {
                            reevaluateAccessibility();
                        }
            if (selectedField != null && !selectedField.isFree() && selectedField.getBuilding() instanceof Zone zone
                    && zone.isBuiltUp()) {
                selectedField.getBuilding().setImage(new ImageIcon("data/graphics/selected"
                        + zone.type().substring(0, 1).toUpperCase() + zone.type().substring(1) + ".png").getImage());
            }
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
        Q.add(new Coordinate(x, y));
        voltemar[x][y] = true;
        while (!Q.isEmpty()) {
            Coordinate o = (Coordinate) Q.remove();
            if (o.x + 1 < fields.length && !voltemar[o.x + 1][o.y] && !fields[o.x + 1][o.y].isFree()
                    && fields[o.x + 1][o.y].getBuilding().isBuiltUp()) {
                if (fields[o.x + 1][o.y].getBuilding() instanceof Zone zone && zone.isBuiltUp())
                    return true;
                Q.add(new Coordinate(o.x + 1, o.y));
                voltemar[o.x + 1][o.y] = true;
            }
            if (o.x - 1 >= 0 && !voltemar[o.x - 1][o.y] && !fields[o.x - 1][o.y].isFree()
                    && fields[o.x - 1][o.y].getBuilding().isBuiltUp()) {
                if (fields[o.x - 1][o.y].getBuilding() instanceof Zone zone && zone.isBuiltUp())
                    return true;
                Q.add(new Coordinate(o.x - 1, o.y));
                voltemar[o.x - 1][o.y] = true;
            }
            if (o.y + 1 < fields[0].length && !voltemar[o.x][o.y + 1] && !fields[o.x][o.y + 1].isFree()
                    && fields[o.x][o.y + 1].getBuilding().isBuiltUp()) {
                if (fields[o.x][o.y + 1].getBuilding() instanceof Zone zone && zone.isBuiltUp())
                    return true;
                Q.add(new Coordinate(o.x, o.y + 1));
                voltemar[o.x][o.y + 1] = true;
            }
            if (o.y - 1 >= 0 && !voltemar[o.x][o.y - 1] && !fields[o.x][o.y - 1].isFree()
                    && fields[o.x][o.y - 1].getBuilding().isBuiltUp()) {
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
            Scanner reader = new Scanner(new File("data/persistence/init_fields.txt"));
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
                if (!field.isFree() && field.getBuilding() instanceof Zone)
                    field.getBuilding().progressBuilding(4);
            }
        }
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
            for (Field[] row : fields) {
                for (Field field : row) {
                    if (!field.isFree()) {
                        if (field.getBuilding() instanceof ResidentialZone freeResidentialZone
                                && !freeResidentialZone.isFull()) {
                            home = freeResidentialZone;
                        } else if (field.getBuilding() instanceof Workplace freeWorkplace && !freeWorkplace.isFull()) {
                            workplace = freeWorkplace;
                        }

                        if (home != null && workplace != null) {
                            break;
                        }
                    }

                    if (home != null && workplace != null) {
                        break;
                    }
                }
            }

            if (home != null && workplace != null) {
                this.residents.add(new Resident((int) r.nextInt((60 - 18) + 1) + 18, home, workplace));
            } else {
                throw new IllegalArgumentException("Home and workplace cannot be null!");
            }
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
                            new ImageIcon("data/graphics/field.png").getImage());
                    break;
                case "rz":
                    fields[rowIndex][i] = new Field(
                            new ResidentialZone(1.0, residentCapacity, zonePrice, tax, REFUND, 0, (i + 1) * WIDTH,
                                    (rowIndex + 1) * HEIGHT, WIDTH, HEIGHT,
                                    new ImageIcon("data/graphics/residentialZone.png").getImage()),
                            (i + 1) * WIDTH, (rowIndex + 1) * HEIGHT, WIDTH, HEIGHT,
                            new ImageIcon("data/graphics/field.png").getImage());
                    break;
                case "sz":
                    fields[rowIndex][i] = new Field(
                            new ServiceZone(workplaceCapacity, zonePrice, tax, REFUND, 0, (i + 1) * WIDTH,
                                    (rowIndex + 1) * HEIGHT, WIDTH, HEIGHT,
                                    new ImageIcon("data/graphics/serviceZone.png").getImage()),
                            (i + 1) * WIDTH, (rowIndex + 1) * HEIGHT, WIDTH, HEIGHT,
                            new ImageIcon("data/graphics/field.png").getImage());
                    break;
                case "iz":
                    fields[rowIndex][i] = new Field(
                            new IndustrialZone(workplaceCapacity, zonePrice, tax, REFUND, 0, (i + 1) * WIDTH,
                                    (rowIndex + 1) * HEIGHT, WIDTH, HEIGHT,
                                    new ImageIcon("data/graphics/industrialZone.png").getImage()),
                            (i + 1) * WIDTH, (rowIndex + 1) * HEIGHT, WIDTH, HEIGHT,
                            new ImageIcon("data/graphics/field.png").getImage());
                    break;
                case "r":
                    fields[rowIndex][i] = new Field(
                            new Road(roadPrice, (int) (roadPrice * annualFeePercentage), (i + 1) * WIDTH,
                                    (rowIndex + 1) * HEIGHT, WIDTH, HEIGHT,
                                    new ImageIcon("data/graphics/road.png").getImage(), REFUND),
                            (i + 1) * WIDTH, (rowIndex + 1) * HEIGHT, WIDTH, HEIGHT,
                            new ImageIcon("data/graphics/field.png").getImage());
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
                    HEIGHT, new ImageIcon("data/graphics/selectedRoad.png").getImage(), REFUND));
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
            System.out.println(iIndex + " " + jIndex);
            if (iIndex > 0 && jIndex > 0) {
                if (fields[iIndex - 1][jIndex - 1].isFree() && fields[iIndex - 1][jIndex].isFree()
                        && fields[iIndex][jIndex - 1].isFree()) {
                    Stadium s = new Stadium(price, getAnnualFee(price), RADIUS, selectedField.getX() - WIDTH,
                            selectedField.getY() - HEIGHT,
                            WIDTH * 2, HEIGHT * 2, new ImageIcon("data/graphics/selectedStadium.png").getImage(),
                            REFUND);
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
                            WIDTH, HEIGHT, new ImageIcon("data/graphics/selectedPoliceStation.png").getImage(),
                            REFUND));
        } else if (playerBuildItClass == FireStation.class) {
            selectedField.build(
                    new FireStation(price, getAnnualFee(price), RADIUS, selectedField.getX(), selectedField.getY(),
                            WIDTH, HEIGHT, new ImageIcon("data/graphics/selectedPoliceStation.png").getImage(),
                            REFUND));
        }
        reevaluateAccessibility();

        budget -= price;
    }

    public void reevaluateAccessibility() {
        for (var row : fields)
            for (var field : row)
                if (!field.isFree() && field.getBuilding() instanceof Zone) {
                    boolean isAccessible = isAccessibleOnRoad(field);
                    field.getBuilding().select(isAccessible);
                    field.getBuilding().unselect(isAccessible);
                }
        if (selectedField != null && !selectedField.isFree())
            selectedField.getBuilding()
                    .setImage(
                            new ImageIcon(
                                    "data/graphics/selected"
                                            + (selectedField.getBuilding().isBuiltUp()
                                                    ? selectedField.getBuilding().type().substring(0, 1).toUpperCase()
                                                            + selectedField.getBuilding().type().substring(1)
                                                    : (isAccessibleOnRoad(selectedField) ? "Build" : "UnableBuild"))
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
            selectedField.setBuilding(new ResidentialZone(1.0, residentCapacity, zonePrice, tax, REFUND, 0.0,
                    selectedField.getX(), selectedField.getY(), WIDTH, HEIGHT,
                    new ImageIcon("data/graphics/" + (acc ? "selectedBuild" : "selectedUnableBuild") + ".png")
                            .getImage()));
        } else if (zoneClass == IndustrialZone.class) {
            selectedField.setBuilding(new IndustrialZone(workplaceCapacity, zonePrice, tax, REFUND, 0.0,
                    selectedField.getX(), selectedField.getY(), WIDTH, HEIGHT,
                    new ImageIcon("data/graphics/" + (acc ? "selectedBuild" : "selectedUnableBuild") + ".png")
                            .getImage()));
        } else if (zoneClass == ServiceZone.class) {
            selectedField.setBuilding(new ServiceZone(workplaceCapacity, zonePrice, tax, REFUND, 0.0,
                    selectedField.getX(), selectedField.getY(), WIDTH, HEIGHT,
                    new ImageIcon("data/graphics/" + (acc ? "selectedBuild" : "selectedUnableBuild") + ".png")
                            .getImage()));
        }

        budget -= zonePrice;
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

    private int getDistanceAlongRoad(Field one, Field other) {
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
}
