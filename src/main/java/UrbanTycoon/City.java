
package UrbanTycoon;

import java.awt.Image;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Scanner;
import javax.swing.ImageIcon;
import java.awt.Dimension;
import javax.swing.JFrame;

class City {

    private ArrayList<Resident> residents;
    private Field[][] fields;

    private final double REFUND;
    private final double CHANCEOFFIRE;
    private final int RADIUS;
    private final int POLICESTATIONSAFETY = 1;
    private final int STADIUMSATBONUS = 1;
    final int FORESTSATBONUS = 1;
    private int FIELDSIZE;
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
    private int xOffset, yOffset;
    private Dimension screenSize;

    public City(int residentsNum, int fieldSize, int fieldRowsNum, int fieldColsNum, int criticalSatisfaction,
            int moveInSatisfaction,
            int budget, int zonePrice, int roadPrice, int stadiumPrice, int policeStationPrice, int fireStationPrice,
            int forestPrice,
            double annualFeePercentage, int residentCapacity, int workplaceCapacity, double refund, double chanceOfFire,
            int radius, Dimension screenSize) {
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

        if (fieldSize > 0) {
            FIELDSIZE = fieldSize;
        } else {
            throw new IllegalArgumentException("Invalid value! Fieldsize must be greater than 0!");
        }

        if (screenSize != null) {
            this.screenSize = screenSize;
            int screenWidth = screenSize.width;
            int screenHeight = screenSize.height;
            this.xOffset = (screenWidth - (fieldSize * fieldColsNum)) / 2;
            this.yOffset = (screenHeight - (fieldSize * fieldRowsNum)) / 2;
        }

        initFields(fieldRowsNum, fieldColsNum, screenSize == null); // in JUnit test there is no screenSize
        initResidents(residentsNum);
        updateSatisfaction();
    }
    
    /**
     * set City's attributes to initial
     * @param residentsNum
     * @param fieldRowsNum
     * @param fieldColsNum
     * @param budget 
     */
    public void restart(int residentsNum, int fieldRowsNum, int fieldColsNum, int budget) {
        this.selectedField = null;
        this.satisfaction = 0;
        this.universialSatisfaction = 0;
        this.negativeBudgetNthYear = 0;
        this.tax = 100;
        this.budget = budget;

        if (residentsNum > 0) {
            this.residents = new ArrayList<>(residentsNum);
        } else {
            throw new IllegalArgumentException("Invalid value! Residents number must be greater than 0!");
        }

        initFields(fieldRowsNum, fieldColsNum, screenSize == null); // in JUnit test there is no screenSize
        initResidents(residentsNum);
        updateSatisfaction();
    }

    public int getyOffset() {
        return yOffset;
    }

    public int getxOffset() {
        return xOffset;
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

    public void addResident(Resident resident) {
        residents.add(resident);
    }

    public void removeResident(Resident resident) {
        residents.remove(resident);
    }

    public boolean isSatisfactionCritical() {
        return satisfaction <= criticalSatisfaction;
    }

    /**
     * Refresh bonuses for fields, set resident satisfactions individually,
     * remove residents if Sat is too low,
     * then calculate city average.
     */
    private void updateSatisfaction() {
        setFieldsDependentValues();
        calculateUniversialSatisfaction();
        int sumSatisfaction = 0;
        ArrayList<Resident> removeResidents = new ArrayList<>();
        for (Resident resident : residents) {
            resident.setSatisfaction(universialSatisfaction + whatSatisfactionFor(resident));
            if (resident.getSatisfaction() <= criticalSatisfaction && !resident.isRetired()) {
                resident.movesAwayFromCity();
                removeResidents.add(resident);
            } else {
            	sumSatisfaction += resident.getSatisfaction();            	
            }
        }
        
        for (Resident removeResident : removeResidents) {
            residents.remove(removeResident);
        }

        if (residents.isEmpty()) {
        	this.satisfaction = criticalSatisfaction;
        } else {
        	this.satisfaction = sumSatisfaction / residents.size();
        }
        
        updateImages();
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
        if (residents.isEmpty()) {
            universialSatisfaction = criticalSatisfaction;
        } else {
            universialSatisfaction -= (int) ((Math.abs(szolgaltatasbanDolgozok - iparbanDolgozok) / residents.size())
                    * 10);
        }
    }

    /**
     * calculate r resident satisfection
     * @param r
     * @return r satisfaction
     */
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

        // if have not retired yet
        if (workIndexX == -1)
            throw new IllegalArgumentException("Workplace not Found!");
        int d = getDistanceAlongRoad(fields[workIndexX][workIndexY], fields[homeIndexX][homeIndexY], fields);
        if (d == -1)
            throw new IllegalArgumentException("Work and home not connected! work: " + workIndexX + " " + workIndexY
                    + " Home: " + homeIndexX + " " + homeIndexY);
        sat += 5 - d;

        sat += r.getWorkplace().getSatisfactionBonus();
        sat += r.getWorkplace().getSafety();
        return sat;
    }

    public int getTax() {
        return tax;
    }

    /**
     * incresase tax by 50 and decrease satisfaction
     */
    public void increaseTax() {
        tax += 50;
        updateSatisfaction();
    }

    /**
     * decrease tax by 50 and increase satisfaction
     */
    public void lowerTax() {
        tax -= 50;
        if (tax <= 0) {
            tax = 0;
            updateSatisfaction();
        }
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
    
    /**
     * called on-click
     * if player clicked on the selected field again, then it gets unselected,
     * otherwise the new field becomes selected
     * @param x
     * @param y
     */
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

    // here everything work with selectedField
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
                refund = (int) (selectedField.getDestroyMoney() * REFUND);
                selectedField.destroyOrDenominate();
            } else if (selectedField.getBuilding() instanceof Stadium) {
                Stadium s = (Stadium) selectedField.getBuilding();
                for (int i = 0; i < 4; i++) {
                    refund = (int) (s.fields[i].getDestroyMoney() * REFUND);
                    s.fields[i].destroyOrDenominate();
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
            if (agreeToDelete && refund == 0) {
                refund = (int) (selectedField.getDestroyMoney() * REFUND);
                selectedField.destroyOrDenominate();
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
                updateSatisfaction();
            }
        }
    }

    /**
     * refresh workplace whene resident cannot reach it and decrease budget by 500
     */
    public void refreshWorkplaces() {
        for (Resident r : residents) {
            if (getDistanceAlongRoad(r.getHomeField(), r.getWorkplaceField(), fields) == -1) {
                r.setWorkplace(null);
                r.setWorkplaceField(null);
                decrementBudget(500);
            }
        }
        reassignHomesOrWorkplaces();
    }

    /**
     * refresh residents home and workplace and decrease budget if residents workplaced or home changed by 500-500
     */
    public void refreshResidentData() {
        for (int i = 0; i < residents.size(); i++) {
            Resident r = residents.get(i);
            if (selectedField.getBuilding() == r.getHome()) {
                r.setHome(null);
                r.setWorkplace(null);
                decrementBudget(500);
            }
            if (selectedField.getBuilding() == r.getWorkplace()) {
                r.setWorkplace(null);
                decrementBudget(500);
            }
        }
    }

    /**
     * increase residents and forest ages
     * collect tax from residents
     * pay annual taxes
     * residents can move up if city's satisfaction is more than 
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
        budget += countField(Stadium.class) * 3 * getAnnualFee(stadiumPrice); // because stadium size is 2x2 and
                                                                              // decrease budget 4 times more
        Random random = new Random();
        ArrayList<Resident> removeResidents = new ArrayList<>();
        for (Resident r : residents) {
        	
            if (!r.isRetired()) {
                budget += r.tax();
            } else
                budget -= r.getYearlyRetirement();
            
            r.increaseAge();
            
            if (random.nextDouble() <= r.getChanceOfDeath()) {
            	r.die();
            	removeResidents.add(r);
            }
        }
        
        for(Resident removeResident : removeResidents) {
        	residents.remove(removeResident);
        }
        
        for(int i = 0 ; i < removeResidents.size(); i++) {
        	moveInOneResident(true);
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
        setForestSatisfaction();
        updateSatisfaction();
    }

    /**
     * calculate chance of fire and fields can lights up
     * @param currentDate 
     */
    public void monthElapsed(Date currentDate) {
        Random r = new Random();
        for (Field[] row : fields) {
            for (Field field : row) {
                if (!field.isFree() && field.getBuilding().isBuiltUp() && !field.getBuilding().isBurning()) {
                    double random = 1.0 * r.nextDouble(); // random double between 0.0 and 1.0
                    if (calculateChanceOfFire(field) > random || calculateChanceOfFire(field) == 1.0) {
                        field.getBuilding().startBurning(currentDate);
                    }
                }
            }
        }
    }

    /**
     * calculate chance of fire and fire can spread to adjacent fields
     * burning fields can burn down
     * move fire track if it is moving
     * @param currentDate 
     */
    public void dayElapsed(Date currentDate) {
        Random r = new Random();

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
                        double random = 1.0 * r.nextDouble();
                        if (calculateChanceOfFire(fields[i][j + 1]) > random) {
                            fields[i][j + 1].getBuilding().startBurning(currentDate);
                        }
                    }

                    if (i + 1 < fields.length && !fields[i + 1][j].isFree()
                            && fields[i + 1][j].getBuilding().isBuiltUp()
                            && !fields[i + 1][j].getBuilding().isBurning()) {
                        double random = 1.0 * r.nextDouble();
                        if (calculateChanceOfFire(fields[i + 1][j]) > random) {
                            fields[i + 1][j].getBuilding().startBurning(currentDate);
                        }
                    }

                    if (j - 1 >= 0 && !fields[i][j - 1].isFree() && fields[i][j - 1].getBuilding().isBuiltUp()
                            && !fields[i][j - 1].getBuilding().isBurning()) {
                        double random = 1.0 * r.nextDouble();
                        if (calculateChanceOfFire(fields[i][j - 1]) > random) {
                            fields[i][j - 1].getBuilding().startBurning(currentDate);
                        }
                    }

                    if (i - 1 >= 0 && !fields[i - 1][j].isFree() && fields[i - 1][j].getBuilding().isBuiltUp()
                            && !fields[i - 1][j].getBuilding().isBurning()) {
                        double random = 1.0 * r.nextDouble();
                        if (calculateChanceOfFire(fields[i - 1][j]) > random) {
                            fields[i - 1][j].getBuilding().startBurning(currentDate);
                        }
                    }
                }

                // move fire track
                if (!fields[i][j].isFree() && fields[i][j].getBuilding() instanceof FireStation fireStation) {
                    if (!fireStation.getFireEngine().isAvailable()) {
                        // moving back
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

    /**
     * set attributes of the fields, then update view
     * @param ticks
     */
    public void performTicks(int ticks) {
        if (ticks > 0) {
            for (Field[] field : fields) {
                for (int j = 0; j < fields[0].length; j++) {
                    if (!field[j].isFree() && field[j].getBuilding() instanceof Zone && isAccessibleOnRoad(field[j])) {
                        field[j].getBuilding().progressBuilding(ticks);
                    }
                    if (!field[j].isFree() && field[j].getBuilding() instanceof Zone zone) {
                        zone.setAnnualTax(tax);
                    }
                }
            }
            updateSatisfaction();
        }
    }

    /**
     * 
     * @param field
     * @return true if field is accessible on road
     */
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
        Queue<Coordinate> Q = new LinkedList<>();
        voltemar[x][y] = true;
        
        // adds the 4 fields next to it, to the queue, if they are a road.
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
     * fill fields with the right zones
     * 
     * @param fieldRowsNum
     * @param fieldColsNum
     * @param test only for JUnit test
     */
    private void initFields(int fieldRowsNum, int fieldColsNum, boolean test) {
        int rows = 0;
        int cols = 0;
        this.fields = new Field[fieldRowsNum][fieldColsNum];

        try {
            try (Scanner reader = new Scanner(new File("data/persistence/initFields.txt"))) {
                while (reader.hasNextLine()) {
                    String line = reader.nextLine();
                    processLine(line, rows);
                    rows++;
                    cols = line.split("\\s+").length;
                }
            }

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
    }
    
    private void initFields(int fieldRowsNum, int fieldColsNum) {
        initFields(fieldRowsNum, fieldColsNum, false);
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
            for (Field[] allFields : this.fields) {
                for (Field field : allFields) {
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

    /**
     * r move in a residential zone
     * @param r
     * @return chosen residential zone
     */
    public Field initHome(Resident r) {
        ResidentialZone home = null;
        Field homeField = null;
        for (Field[] allFields : fields) {
            for (int k = 0; k < fields[0].length; k++) {
                Field field = allFields[k];
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

    /**
     * r move in a workplace zone
     * @param r 
     */
    public void initWorkplace(Resident r) {
        Workplace workplace = null;
        Field workplaceField = null;
        if (r.getHome() != null) {
            for (Field[] allFields : fields) {
                for (int k = 0; k < fields[0].length; k++) {
                    Field field = allFields[k];
                    if (!field.isFree()) {
                        if (field.getBuilding() instanceof Workplace freeWorkplace && !freeWorkplace.isFull()) {
                            workplaceField = field;
                            if (getDistanceAlongRoad(r.getHomeField(), workplaceField, fields) > -1) {
                                workplace = freeWorkplace;
                                freeWorkplace.incrementPeopleNum();
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

    /**
     * remove resinents if they have no home or workplace and cannot find new available home or workplace
     */
    public void reassignHomesOrWorkplaces() {
        ArrayList<Resident> removeResidents = new ArrayList<>();
        for (Resident r : residents) {
            if (r.getWorkplace() == null || r.getHome() == null) {
                // apply satisfaction penalty per capita if they are included in a conflicted
                // building/road removal
            }
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

    /**
     * process line from txt
     * @param line
     * @param rowIndex 
     */
    private void processLine(String line, int rowIndex) {
        String[] fieldStrings = line.split("\\s+"); // It will split the string by single or multiple whitespace characteres
                                                    
        for (int i = 0; i < fieldStrings.length; i++) {
            int offsetX = (i + 1) * FIELDSIZE;
            int offsetY = (rowIndex + 1) * FIELDSIZE;
            if (screenSize != null) {
                offsetX = xOffset + i * FIELDSIZE;
                offsetY = yOffset + rowIndex * FIELDSIZE;
            }
            String fieldType = fieldStrings[i];
            switch (fieldType) {
                case "0":
                    fields[rowIndex][i] = new Field(null, offsetX, offsetY, FIELDSIZE,
                            FIELDSIZE,
                            new ImageIcon("data/graphics/field/unselected/notBurning/field.png").getImage());
                    break;
                case "rz":
                    fields[rowIndex][i] = new Field(
                            new ResidentialZone(1.0, residentCapacity, zonePrice, tax, 0, 0, REFUND, CHANCEOFFIRE,
                                    offsetX,
                                    offsetY, FIELDSIZE, FIELDSIZE,
                                    new ImageIcon("data/graphics/field/unselected/notBurning/residentialZoneEmpty.png")
                                            .getImage()),
                            offsetX, offsetY, FIELDSIZE, FIELDSIZE,
                            new ImageIcon("data/graphics/field/unselected/notBurning/field.png").getImage());
                    break;
                case "sz":
                    fields[rowIndex][i] = new Field(
                            new ServiceZone(workplaceCapacity, zonePrice, tax, 0, 0, REFUND, CHANCEOFFIRE,
                                    offsetX,
                                    offsetY, FIELDSIZE, FIELDSIZE,
                                    new ImageIcon("data/graphics/field/unselected/notBurning/serviceZone.png")
                                            .getImage()),
                            offsetX, offsetY, FIELDSIZE, FIELDSIZE,
                            new ImageIcon("data/graphics/field/unselected/notBurning/field.png").getImage());
                    break;
                case "iz":
                    fields[rowIndex][i] = new Field(
                            new IndustrialZone(workplaceCapacity, zonePrice, tax, 0, 0, REFUND, CHANCEOFFIRE,
                                    offsetX,
                                    offsetY, FIELDSIZE, FIELDSIZE,
                                    new ImageIcon("data/graphics/field/unselected/notBurning/industrialZone.png")
                                            .getImage()),
                            offsetX, offsetY, FIELDSIZE, FIELDSIZE,
                            new ImageIcon("data/graphics/field/unselected/notBurning/field.png").getImage());
                    break;
                case "r":
                    fields[rowIndex][i] = new Field(
                            new Road(roadPrice, (int) (roadPrice * annualFeePercentage), offsetX,
                                    offsetY, FIELDSIZE, FIELDSIZE,
                                    new ImageIcon("data/graphics/field/unselected/notBurning/road.png").getImage(),
                                    REFUND),
                            offsetX, offsetY, FIELDSIZE, FIELDSIZE,
                            new ImageIcon("data/graphics/field/unselected/notBurning/field.png").getImage());
                    break;
                case "f":
                    fields[rowIndex][i] = new Field(
                            new Forest(forestPrice, (int) (forestPrice * annualFeePercentage), offsetX,
                                    offsetY, FIELDSIZE, FIELDSIZE,
                                    new ImageIcon("data/graphics/field/unselected/notBurning/forest.png").getImage(),
                                    REFUND, CHANCEOFFIRE),
                            offsetX, offsetY, FIELDSIZE, FIELDSIZE,
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
    public void build(Class<?> playerBuildItClass) {
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
        FIELDSIZE = fields[0][0].getWidth();
        // The field is free, have enough money -> build it:
        if (playerBuildItClass == Road.class) {
            selectedField.build(new Road(price, getAnnualFee(price), selectedField.getX(), selectedField.getY(),
                    FIELDSIZE,
                    FIELDSIZE, new ImageIcon("data/graphics/field/selected/notBurning/road.png").getImage(), REFUND));
        } else if (playerBuildItClass == Stadium.class) {
            int iIndex = 0, jIndex = 0;
            for (int i = 0; i < fields.length; i++) {
                for (int j = 0; j < fields[0].length; j++) {
                    if (selectedField == fields[i][j]) {
                        iIndex = i;
                        jIndex = j;
                    }
                }
            }
            if (iIndex > 0 && jIndex > 0) {
                if (fields[iIndex - 1][jIndex - 1].isFree() && fields[iIndex - 1][jIndex].isFree()
                        && fields[iIndex][jIndex - 1].isFree()) {
                    Stadium s = new Stadium(price, getAnnualFee(price), RADIUS, selectedField.getX() - FIELDSIZE,
                            selectedField.getY() - FIELDSIZE,
                            FIELDSIZE * 2, FIELDSIZE * 2,
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
                    return;

                }
            } else {
                return;
            }
        } else if (playerBuildItClass == PoliceStation.class) {
            selectedField.build(
                    new PoliceStation(price, getAnnualFee(price), RADIUS, selectedField.getX(), selectedField.getY(),
                            FIELDSIZE, FIELDSIZE,
                            new ImageIcon("data/graphics/field/selected/notBurning/policeStation.png").getImage(),
                            REFUND, CHANCEOFFIRE));
        } else if (playerBuildItClass == FireStation.class) {
            selectedField.build(
                    new FireStation(price, getAnnualFee(price), RADIUS, selectedField.getX(), selectedField.getY(),
                            FIELDSIZE, FIELDSIZE,
                            new ImageIcon("data/graphics/field/selected/notBurning/fireStation.png").getImage(),
                            REFUND));
        } else if (playerBuildItClass == Forest.class) {
            selectedField.build(
                    new Forest(price, getAnnualFee(price), selectedField.getX(), selectedField.getY(),
                            FIELDSIZE, FIELDSIZE, new ImageIcon("data/graphics/field/selected/forest.png").getImage(),
                            REFUND, CHANCEOFFIRE));
            calculateForestBonusResZone();
        }
        updateSatisfaction();

        budget -= price;
    }

    /**
     * preparation for save (persistence)
     * @return 
     */
    public String gameStateAsString() {
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

    /**
     * load one of the saved game (persistence)
     * @param s
     * @param onScreen 
     */
    public void loadGame(Scanner s, boolean onScreen) {
        residents.clear();
        selectedField = null;
        tax = Integer.parseInt(s.nextLine());
        budget = Integer.parseInt(s.nextLine());
        negativeBudgetNthYear = Integer.parseInt(s.nextLine());
        satisfaction = Integer.parseInt(s.nextLine());
        universialSatisfaction = Integer.parseInt(s.nextLine());
        
        boolean[][] alreadySet = new boolean[fields.length][fields[0].length];
        
        for (int i = 0; i < fields.length; i++) {
            for (int j = 0; j < fields[0].length; j++) {
                String[] str = s.nextLine().split(";");
                if (str.length > 2 && str[1].equals("st") && !alreadySet[i][j]) {
                	
                    Stadium stad = (Stadium)loadBuildable(j,i,str,onScreen);
                    stad.fields[0] = fields[i + 1][j + 1];
                    stad.fields[0].setBuilding(stad);
                    stad.fields[1] = fields[i][j];
                    stad.fields[1].setBuilding(stad);
                    stad.fields[2] = fields[i][j + 1];
                    stad.fields[2].setBuilding(stad);
                    stad.fields[3] = fields[i + 1][j];
                    stad.fields[3].setBuilding(stad);
                    alreadySet[i][j] = true;
                    alreadySet[i + 1][j + 1] = true;
                    alreadySet[i + 1][j] = true;
                    alreadySet[i][j + 1] = true;
                } else if (str.length > 2 && str[1].equals("st")) {

                } else {
                    fields[i][j].setBuilding(loadBuildable(j, i, str, onScreen));
                }
                if (fields[i][j].isFree())
                    fields[i][j].setBurntDown(Boolean.parseBoolean(str[0]));
            }
        }
        while (s.hasNextLine()) {
            residents.add(residentByString(s.nextLine()));
        }
        updateSatisfaction();
    }

    /**
     * load buildings for persistence
     * @param x
     * @param y
     * @param s
     * @param onScreen
     * @return 
     */
    private Buildable loadBuildable(int x, int y, String[] s, boolean onScreen) {
        Buildable b;
        if (s[1].equals("empty"))
            return null;
        double refund = Double.parseDouble(s[2]);
        double chanceOfFire = Double.parseDouble(s[3]);
        boolean burning = Boolean.parseBoolean(s[4]);
        Date date = Date.parseDate(s[5]);
        int offsetX = (x + 1) * FIELDSIZE;
        int offsetY = (y + 1) * FIELDSIZE;
        if (onScreen) {
            offsetX = xOffset + x * FIELDSIZE;
            offsetY = yOffset + y * FIELDSIZE;
        }
        switch (s[1]) {
            case "rz" -> {
                b = new ResidentialZone(Double.parseDouble(s[14]), Integer.parseInt(s[9]), Integer.parseInt(s[13]),
                        Integer.parseInt(s[6]), Integer.parseInt(s[11]), Integer.parseInt(s[12]), refund, chanceOfFire,
                        offsetX, offsetY, FIELDSIZE, FIELDSIZE,
                        whatImageFor(ResidentialZone.class, s));
                Zone z = (Zone) b;
                z.setPeopleNum(Integer.parseInt(s[10]));
                z.setBuildProgress(Integer.parseInt(s[7]));
                z.setBuiltUp(Boolean.parseBoolean(s[8]));
            }
            case "iz" -> {
                b = new IndustrialZone(Integer.parseInt(s[9]), Integer.parseInt(s[13]), Integer.parseInt(s[6]),
                        Integer.parseInt(s[11]), Integer.parseInt(s[12]), refund, chanceOfFire / 2, offsetX,
                        offsetY, FIELDSIZE, FIELDSIZE, whatImageFor(IndustrialZone.class, s));
                Zone z = (Zone) b;
                z.setPeopleNum(Integer.parseInt(s[10]));
                z.setBuildProgress(Integer.parseInt(s[7]));
                z.setBuiltUp(Boolean.parseBoolean(s[8]));
            }
            case "sz" -> {
                b = new ServiceZone(Integer.parseInt(s[9]), Integer.parseInt(s[13]), Integer.parseInt(s[6]),
                        Integer.parseInt(s[11]), Integer.parseInt(s[12]), refund, chanceOfFire, offsetX,
                        offsetY, FIELDSIZE, FIELDSIZE, whatImageFor(ServiceZone.class, s));
                Zone z = (Zone) b;
                z.setPeopleNum(Integer.parseInt(s[10]));
                z.setBuildProgress(Integer.parseInt(s[7]));
                z.setBuiltUp(Boolean.parseBoolean(s[8]));
            }
            case "ps" -> {
                int buildPrice = Integer.parseInt(s[6]);
                int annualFee = Integer.parseInt(s[7]);
                int radius = Integer.parseInt(s[8]);
                b = new PoliceStation(buildPrice, annualFee, radius, offsetX, offsetY,
                        FIELDSIZE, FIELDSIZE,
                        whatImageFor(PoliceStation.class, s), refund, chanceOfFire);
            }
            case "fs" -> {
                int buildPrice = Integer.parseInt(s[6]);
                int annualFee = Integer.parseInt(s[7]);
                int radius = Integer.parseInt(s[8]);
                b = new FireStation(buildPrice, annualFee, radius, offsetX, offsetY, FIELDSIZE,
                        FIELDSIZE,
                        whatImageFor(FireStation.class, s), refund);
            }
            case "for" -> {
                int buildPrice = Integer.parseInt(s[6]);
                int annualFee = Integer.parseInt(s[7]);
                int age = Integer.parseInt(s[8]);
                b = new Forest(buildPrice, annualFee, offsetX, offsetY, FIELDSIZE, FIELDSIZE,
                        whatImageFor(Forest.class, s), refund, chanceOfFire);
                ((Forest) b).setAge(age);
            }
            case "st" -> {
                int buildPrice = Integer.parseInt(s[6]);
                int annualFee = Integer.parseInt(s[7]);
                int radius = Integer.parseInt(s[8]);
                b = new Stadium(buildPrice, annualFee, radius, offsetX, offsetY, FIELDSIZE * 2,
                        FIELDSIZE * 2,
                        whatImageFor(Stadium.class, s), refund, chanceOfFire);
            }
            default -> {
                int buildPrice = Integer.parseInt(s[6]);
                int annualFee = Integer.parseInt(s[7]);
                b = new Road(buildPrice, annualFee, offsetX, offsetY, FIELDSIZE, FIELDSIZE,
                        whatImageFor(Road.class, s), refund);
            }
        }
        b.setBurning(burning);
        b.setBurningStartDate(date);
        return b;
    }

    /**
     * search image when loading saved game (persistence)
     * @param buildableClass
     * @param s
     * @return 
     */
    private Image whatImageFor(Class<?> buildableClass, String[] s) {
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

    /**
     * prepare residents when loading saved game (persistence)
     * @param tmp
     * @return 
     */
    private Resident residentByString(String tmp) {
        String[] s = tmp.split(";");
        Resident r = new Resident(42, null, null);
        r.setAge(Integer.parseInt(s[0]));
        r.setRetired(Boolean.parseBoolean(s[1]));
        r.setChanceOfDeath(Double.parseDouble(s[2]));
        r.setHome((ResidentialZone) fields[Integer.parseInt(s[3])][Integer.parseInt(s[4])].getBuilding());
        r.setWorkplace((Workplace) fields[Integer.parseInt(s[5])][Integer.parseInt(s[6])].getBuilding());
        r.setHomeField(fields[Integer.parseInt(s[3])][Integer.parseInt(s[4])]);
        r.setWorkplaceField(fields[Integer.parseInt(s[5])][Integer.parseInt(s[6])]);
        r.setSatisfaction(Integer.parseInt(s[7]));
        r.setWorkedYearsBeforeRetired(Integer.parseInt(s[8]));
        r.setPaidTaxesBeforeRetired(Integer.parseInt(s[9]));
        return r;
    }
    
    /**
     * update images of Zones and selectedField
     */
    private void updateImages() {
        for (Field[] field : fields) {
            for (int j = 0; j<fields[0].length; j++) {
                if (!field[j].isFree() && field[j].getBuilding() instanceof Zone) {
                    boolean isAccessible = isAccessibleOnRoad(field[j]);
                    field[j].getBuilding().select(isAccessible);
                    field[j].getBuilding().unselect(isAccessible);
                }
                if (!field[j].isFree() && field[j].getBuilding().isBuiltUp()) {
                    field[j].getBuilding().setImage(new ImageIcon("data/graphics/field/unselected/" + field[j].getBuilding().type() + ".png").getImage());
                }
                if (field[j].isFree() && field[j].isBurntDown()) {
                    field[j].setImage(new ImageIcon("data/graphics/field/" + (selectedField == field[j] ? "" : "un") + "selected/notBurning/burntDownField.png").getImage());
                }
                if (!field[j].isFree() && field[j].getBuilding() instanceof Road road) {
                    road.setImage(new ImageIcon("data/graphics/field/" + (selectedField == field[j] ? "" : "un") + "selected/" + road.type() + ".png").getImage());
                }
            }
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
     * set the values of fields that are dependent on the surrounding fields.
     */
    private void setFieldsDependentValues() {
    	for (var row : fields)
            for (var field : row)
                if (!field.isFree() && field.getBuilding() instanceof Zone zone) {
                    zone.setSafety(Math.min(Math.max(calculateSafety(field), -10), 10));
        			zone.setSatisfactionBonus(Math.min(Math.max(calculateSatBonus(field), -10), 10));
                }
    }

    /**
     * player select free field (residential-, industrial-, service zone) and
     * residents can build on this automatically
     * budget decrease by the price of zone select
     * 
     * @param zoneClass
     */
    public void selectField(Class zoneClass) {
        FIELDSIZE = fields[0][0].getWidth();
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
                    selectedField.getX(), selectedField.getY(), FIELDSIZE, FIELDSIZE,
                    new ImageIcon("data/graphics/field/selected/notBurning/" + (acc ? "build" : "unableBuild") + ".png")
                            .getImage()));
        } else if (zoneClass == IndustrialZone.class) {
            selectedField.setBuilding(new IndustrialZone(workplaceCapacity, zonePrice, tax,
                    calculateSafety(selectedField), calculateSatBonus(selectedField), REFUND, CHANCEOFFIRE,
                    selectedField.getX(), selectedField.getY(), FIELDSIZE, FIELDSIZE,
                    new ImageIcon("data/graphics/field/selected/notBurning/" + (acc ? "build" : "unableBuild") + ".png")
                            .getImage()));
        } else if (zoneClass == ServiceZone.class) {
            selectedField.setBuilding(new ServiceZone(workplaceCapacity, zonePrice, tax, calculateSafety(selectedField),
                    calculateSatBonus(selectedField), REFUND, CHANCEOFFIRE,
                    selectedField.getX(), selectedField.getY(), FIELDSIZE, FIELDSIZE,
                    new ImageIcon("data/graphics/field/selected/notBurning/" + (acc ? "build" : "unableBuild") + ".png")
                            .getImage()));
        }
        updateSatisfaction();
        budget -= zonePrice;
    }

    /**
     * calculate field's safety
     * @param field
     * @return 
     */
    private int calculateSafety(Field field) {
        int safety = 0;
        int x = -1, y = -1;
        for (int i = 0; i < fields.length; i++)
            for (int j = 0; j < fields[0].length; j++) {
                if (fields[i][j] == field) {
                    x = i;
                    y = j;
                }
            }
        // if there are PoliceStation close by, and accessible on road,
        // then increase the bonus for each
        for (int i = Math.max(0, x - RADIUS); i <= Math.min(fields.length - 1, x + RADIUS); i++)
            for (int j = Math.max(0, y - RADIUS); j <= Math.min(fields[0].length - 1, y + RADIUS); j++)
                if (!fields[i][j].isFree() && fields[i][j].getBuilding() instanceof PoliceStation
                        && getDistanceAlongRoad(field, fields[i][j], fields) != -1) {
                    safety += POLICESTATIONSAFETY;
                }
        safety -= (int) (residents.size() / HOWMANYRESIDENTSTOLOWERSAFETY);
        return safety;
    }

    /**
     * calculate field's satisfaction bonus
     * @param field
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
                        if (!usedStadiums.contains(stadium) && getDistanceAlongRoad(stadium.fields[k], field, fields) != -1) {
                            bonus += STADIUMSATBONUS;
                            usedStadiums.add(stadium);
                        }
                    }
                }
        return bonus;
    }

    /**
     * calculate field's chance of fire
     * @param field
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

    /**
     * calculate forest bonuses for residential zones
     * @return 
     */
    public int[][] calculateForestBonusResZone() {
        int defaultBonus = FORESTSATBONUS;
        int[][] bonusPerHouse = new int[fields.length][fields[0].length];
        ArrayList<Field> houseFields = new ArrayList<>();
        ArrayList<Field> forestFields = new ArrayList<>();
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
                                            // calculate distance between point and the line
                                            // pass point into e(x)
                                            double pR = eX * fiX - eY * fiY - eR;
                                            // normal vector length
                                            double nL = Math.sqrt(Math.pow(eX, 2) + Math.pow(eY, 2));
                                            // distance result
                                            double distance = Math.abs(pR / nL);
                                            // if the distance is less or equal than the diagonal/2 then its blocking
                                            // the line of sight, so we mark the house as blocked
                                            if (distance <= halfCellDiagonal) {
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

    // !!IMPORTANT!! only check cells for forests between the res and ind zones
    // if you check the field even if its not between the zones the math will be
    // still correct
    public boolean decreaseIndustrialPenalty(Field residental, Field industrial, Field field) {
        // cell diagonal divided by 2
        double halfCellDiagonal = (Math
                .sqrt(Math.pow(residental.getHeight(), 2) + Math.pow(residental.getWidth(), 2))) / 2;
        // residental cell middlePoint
        double resX = residental.getX() + (residental.getWidth() / 2);
        double resY = residental.getY() + (residental.getHeight() / 2);

        // industrial cell middlePoint
        double indX = industrial.getX() + (industrial.getWidth() / 2);
        double indY = industrial.getY() + (industrial.getHeight() / 2);

        // field cell middlePoint
        double fX = field.getX() + (field.getWidth() / 2);
        double fY = field.getY() + (field.getHeight() / 2);

        // calculate equation of the line between them, e(x)
        double eX, eY, eR;
        // normal vectors
        eX = resY - indY;
        eY = resX - indX;
        // result
        eR = (eX * indX) - (eY * indY);

        // pass point into e(x)
        double pR = eX * fX - eY * fY - eR;
        // normal vector length
        double nL = Math.sqrt(Math.pow(eX, 2) + Math.pow(eY, 2));
        // distance result
        double distance = Math.abs(pR / nL);
        if (distance <= halfCellDiagonal && field.getBuilding() instanceof Forest) {
            return true;
        }

        return false;
    }

    public void setForestSatisfaction() {
        int[][] bonusPerHouse = calculateForestBonusResZone();
        for (int i = 0; i < fields.length; i++) {
            for (int j = 0; j < fields[0].length; j++) {
                if (fields[i][j].getBuilding() instanceof ResidentialZone) {
                    // applying bonus to the house
                }
            }
        }
    }

    /**
     * 
     * @param field
     * @return if deleting road will not destroy home-workplace connection true, otherwise false
     */
    public boolean canDeleteRoad(Field field) {
        if (field == null)
            return false;

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

    
    private class Coordinate {
        public int x, y;

        public Coordinate(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
    /**
     * @param field
     * @param fields
     * @return shortest distances from field along road, or -1 if unaccessible
     */
    private int[][] getMatrixDistanceAlongRoad(Field field, Field[][] fields) {
        int x = -1, y = -1;
        int[][] distances = new int[fields.length][fields[0].length];
        for (int i = 0; i < fields.length; i++)
            for (int j = 0; j < fields[0].length; j++) {
                distances[i][j] = -1;
                if (fields[i][j] == field) {
                    x = i;
                    y = j;
                    distances[i][j] = 0;
                }
            }
        if (x == -1 && y == -1)
            throw new IllegalArgumentException("Field not Found in getMatrixDistance");
        
        Queue<Coordinate> Q = new LinkedList<>();
        
        // adds the 4 fields next to it, to the queue, if they are a road.
        for (int i = x - 1; i <= x + 1; i++) {
            for (int j = y - 1; j <= y + 1; j++)
                if (Math.abs(i - x) + Math.abs(j - y) == 1 && i >= 0 && i < fields.length && j >= 0
                        && j < fields[0].length)
                    if (!fields[i][j].isFree() && fields[i][j].getBuilding() instanceof Road) {
                        Q.add(new Coordinate(i, j));
                        distances[i][j] = 1;
                    }
        }
        
        // if stadium then stadium 4 fields distance is 0 and every adjacent road distance is 1
        if(field.getBuilding() instanceof Stadium s) {
        	int currentLastFieldNumber = -1;
        	for (int k=0; k<4; k++) {
        		if (field == s.fields[k])   currentLastFieldNumber = k;
        	}
        	ArrayList<Coordinate> fieldsToAddToQueue = new ArrayList<>();
        	switch(currentLastFieldNumber) {
        		// right down
        		case 0 -> {
        			fieldsToAddToQueue.addAll(Arrays.asList(new Coordinate(x - 1,y + 1), new Coordinate(x - 2, y), new Coordinate(x - 2, y - 1), new Coordinate(x - 1, y - 2), new Coordinate(x, y - 2), new Coordinate(x + 1, y - 1)));
        			distances[x - 1][y - 1] = 0;
        			distances[x - 1][y] = 0;
        			distances[x][y - 1] = 0;
        		}
        		// left up
        		case 1 -> {
        			fieldsToAddToQueue.addAll(Arrays.asList(new Coordinate(x - 1, y + 1), new Coordinate(x, y + 2), new Coordinate(x + 1, y + 2), new Coordinate(x + 2, y + 1), new Coordinate(x + 2, y), new Coordinate(x + 1, y - 1)));
        			distances[x][y + 1] = 0;
        			distances[x + 1][y] = 0;
        			distances[x + 1][y + 1] = 0;
        		}
        		// right up
        		case 2 -> {
        			fieldsToAddToQueue.addAll(Arrays.asList(new Coordinate(x + 1, y + 1), new Coordinate(x + 2, y), new Coordinate(x + 2, y - 1), new Coordinate(x + 1, y - 2), new Coordinate(x, y - 2), new Coordinate(x - 1, y - 1)));
        			distances[x][y - 1] = 0;
        			distances[x + 1][y - 1] = 0;
        			distances[x + 1][y] = 0;
        		}
        		// right down
        		case 3 -> {
        			fieldsToAddToQueue.addAll(Arrays.asList(new Coordinate(x - 1, y - 1), new Coordinate(x - 2, y), new Coordinate(x - 2, y + 1), new Coordinate(x - 1,y + 2), new Coordinate(x, y + 2), new Coordinate(x + 1, y + 1)));
        			distances[x - 1][y] = 0;
        			distances[x - 1][y + 1] = 0;
        			distances[x][y + 1] = 0;
        		}
        		default -> throw new IllegalArgumentException("MatrixDistance stadium: field not in stadium.fields");
        	}
        	for(Coordinate c : fieldsToAddToQueue)
        		if(c.x >= 0 && c.y >= 0 && c.x < fields.length && c.y < fields[0].length && fields[c.x][c.y].getBuilding() instanceof Road) {
        			Q.add(c);
        			distances[c.x][c.y] = 1;
        		}
        }
        
        while (!Q.isEmpty()) {
            Coordinate o = (Coordinate) Q.remove();
            if (o.x + 1 < distances.length && distances[o.x + 1][o.y] == -1 && !fields[o.x + 1][o.y].isFree()) {
                distances[o.x + 1][o.y] = distances[o.x][o.y] + 1;
                if(fields[o.x + 1][o.y].getBuilding() instanceof Road)
                	Q.add(new Coordinate(o.x + 1, o.y));
            }
            if (o.x - 1 >= 0 && distances[o.x - 1][o.y] == -1 && !fields[o.x - 1][o.y].isFree()) {
                distances[o.x - 1][o.y] = distances[o.x][o.y] + 1;
                if(fields[o.x - 1][o.y].getBuilding() instanceof Road)
                	Q.add(new Coordinate(o.x - 1, o.y));
            }
            if (o.y + 1 < distances[0].length && distances[o.x][o.y + 1] == -1 && !fields[o.x][o.y + 1].isFree()) {
                distances[o.x][o.y + 1] = distances[o.x][o.y] + 1;
                if(fields[o.x][o.y + 1].getBuilding() instanceof Road)
                	Q.add(new Coordinate(o.x, o.y + 1));
            }
            if (o.y - 1 >= 0 && distances[o.x][o.y - 1] == -1 && !fields[o.x][o.y - 1].isFree()) {
                distances[o.x][o.y - 1] = distances[o.x][o.y] + 1;
                if(fields[o.x][o.y - 1].getBuilding() instanceof Road)
                	Q.add(new Coordinate(o.x, o.y - 1));
            }
        }
        return distances;
    }
    
    /**
     * BFS
     * 
     * @param one
     * @param other
     * @return distance between the fields or -1 if they are not connected
     */
    
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

        return getMatrixDistanceAlongRoad(one, fields)[x2][y2];
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
        for (Field[] allFields : this.fields) {
            for (Field field : allFields) {
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
    public int countField(Class<?> buildableClass) {
        int count = 0;
        for (Field[] allFields : this.fields) {
            for (Field field : allFields) {
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

        for (Field[] allfFields : this.fields) {
            for (Field field : allfFields) {
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
            updateSatisfaction();
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

        for (Field[] allFields : this.fields) {
            for (Field field : allFields) {
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

    /**
     * if there is available fire station then set up route for fire truck
     */
    public void fireFighting() {
        if (selectedField == null || selectedField.isFree() || !selectedField.getBuilding().isBurning()) {
            return;
        }

        Field fireStationField = findClosestFireStation();
        if (fireStationField == null) {
            new PopupInfo(new JFrame(), "There is no available fire station!", "Warning");
            return;
        }

        int[][] distanceMatrix = getMatrixDistanceAlongRoad(selectedField, fields);

        ArrayList<Road> route = new ArrayList<>();
        int x = -1;
        int y = -1;
        for (int i = 0; i < fields.length; i++) {
            for (int j = 0; j < fields[i].length; j++) {
                if (fields[i][j].equals(fireStationField)) {
                    x = i;
                    y = j;
                }
            }
        }
        
        int roadsLeft = distanceMatrix[x][y];
        while (roadsLeft > 1) {
            if (y + 1 < fields[0].length && fields[x][y + 1].getBuilding() instanceof Road && distanceMatrix[x][y + 1] == distanceMatrix[x][y] - 1) {
                route.add((Road) fields[x][++y].getBuilding());
                roadsLeft--;
                // rigth
            } else if (x + 1 < fields.length && fields[x + 1][y].getBuilding() instanceof Road && distanceMatrix[x + 1][y] == distanceMatrix[x][y] - 1) {
                route.add((Road) fields[++x][y].getBuilding());
                roadsLeft--;
                // down
            } else if (y - 1 >= 0 && fields[x][y - 1].getBuilding() instanceof Road && distanceMatrix[x][y - 1] == distanceMatrix[x][y] - 1) {
                route.add((Road) fields[x][--y].getBuilding());
                roadsLeft--;
                //left
            } else if (x - 1 >= 0 && fields[x - 1][y].getBuilding() instanceof Road && distanceMatrix[x - 1][y] == distanceMatrix[x][y] - 1) {
                route.add((Road) fields[--x][y].getBuilding());
                roadsLeft--;
                //up
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
        }

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
        for (Field[] allFields : this.fields) {
            for (Field field : allFields) {
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

    /**
     * move out all residents from zone
     * @param zone 
     */
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
