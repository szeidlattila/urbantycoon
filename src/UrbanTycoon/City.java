/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package UrbanTycoon;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Felhasználó
 */
class City {
    private ArrayList<Resident> residents;
    private Field[][] fields;
    private Field selectedField = null;
    private boolean showFieldInfoPopup = false;
    private int satisfaction = 0;
    private int criticalSatisfaction;
    private long budget;
    private int negativeBudgetNthYear = 0;
    private int tax = 0;
    
    /**
     * initialize the city with the given information
     * create new residents and fields
     * calculate how many Residential zone and Workplace (Service zone and Industrial zone) needed depends on residentNum and zone capacities
     * fill fields with the right zones
     * @param residentsNum
     * @param fieldSize
     * @param fieldRowsNum
     * @param fieldColsNum
     * @param criticalSatisfaction
     * @param budget
     * @param zonePrice
     * @param residentCapacity
     * @param workplaceCapacity
     * @param refund
     * @param radius
     */
    public City(int residentsNum, int fieldSize, int fieldRowsNum, int fieldColsNum, int criticalSatisfaction, int budget, int zonePrice, int residentCapacity, int workplaceCapacity, double refund, int radius) {
        if (residentsNum > 0) {
            this.residents = new ArrayList<>(residentsNum);
            for (int i = 0; i < residentsNum; i++) {
                this.residents.add(new Resident(18, null, null)); // TODO: paramétereket átírni
            }
        } else {
            throw new IllegalArgumentException("Invalid value! Residents number must be greater than 0!");
        }
        
        if (fieldRowsNum > 0 && fieldColsNum > 0) {
            this.fields = new Field[fieldRowsNum][fieldColsNum];
            for (int i = 0; i < fieldRowsNum; i++) {
                for (int j = 0; j < fieldColsNum; j++) {
                    this.fields[i][j] = new Field(null); // TODO: null helyett más
                }
            }
        } else {
            throw new IllegalArgumentException("Invalid value! Rows and coloumns number must be greater than 0!");
        } 
        
        if (-10 <= criticalSatisfaction && criticalSatisfaction < 0) {
            this.criticalSatisfaction = criticalSatisfaction;
        } else {
            throw new IllegalArgumentException("Invalid value! Critical satisfaction must be at least -10 and lower than 0!");
        }
        
        this.budget = budget;
        
        if (zonePrice <= 0) {
            throw new IllegalArgumentException("Invalid value! Zone price must be greater than 0!");
        }
        
        if (residentCapacity <= 0) {
            throw new IllegalArgumentException("Invalid value! Residential zone capacity must be greater than 0!");
        }
        
        if (workplaceCapacity <= 0) {
            throw new IllegalArgumentException("Invalid value! Workplace zone capacity must be greater than 0!");
        }
        
        if (refund <= 0.0 || refund >= 1.0) {
            throw new IllegalArgumentException("Invalid value! Refund must be greater than 0.0 and lower than 1.0!");
        }
        
        if (radius <= 0) {
            throw new IllegalArgumentException("Invalid value! Radius must be greater than 0!");
        }
        
        // initialize fields:
        // calculate how many Residential zone and Workplace (Service zone and Industrial zone) needed depends on residentNum and zone capacities
        int residentialZoneNum = (int)Math.ceil(residentsNum / residentCapacity);
        int workplaceNum = (int)Math.ceil(residentsNum / workplaceCapacity);
        int serviceZoneNum = (int)Math.ceil(workplaceNum / 2.0);
        int industrialZoneNum = (int)Math.floor(workplaceNum / 2.0); 
        
        if (residentialZoneNum + workplaceNum > fieldRowsNum * fieldColsNum) {
            throw new IllegalArgumentException("There are more zones than fields!");
        }
        
        // fill fields with empty fields
        for (int row = 0; row < fieldRowsNum; row++) {
            for (int col = 0; col < fieldColsNum; col++) {
                fields[row][col] = new Field(null);
            }
        }
        
        // change empty fields with residential zone and workplace (service zone and industrial zone)
        Random r = new Random();    // r.nextInt((max - min) + 1) + min; => random number [min, max]
        int freeRow = r.nextInt((fieldRowsNum-1 - 0) + 1) + 0;
        int freeCol = r.nextInt((fieldColsNum-1 - 0) + 1) + 0;
        
        while (residentialZoneNum-- > 0) {
            while (!fields[freeRow][freeCol].isFree()) {    // while the chosen field is not free
                freeRow = r.nextInt((fieldRowsNum-1 - 0) + 1) + 0;
                freeCol = r.nextInt((fieldColsNum-1 - 0) + 1) + 0;
            }
            fields[freeRow][freeCol].setBuilding(new ResidentialZone(0.5, residentCapacity, tax, refund, 0, 10, 10, 10, 10, null)); // TODO: Minden spriteos cucc beállítása
        }
        
        while (serviceZoneNum-- > 0) {
            while (!fields[freeRow][freeCol].isFree()) {    // while the chosen field is not free
                freeRow = r.nextInt((fieldRowsNum-1 - 0) + 1) + 0;
                freeCol = r.nextInt((fieldColsNum-1 - 0) + 1) + 0;
            }
            fields[freeRow][freeCol].setBuilding(new ServiceZone(workplaceCapacity, tax, refund, 0, 10, 10, 10, 10, null)); // TODO: Minden spriteos cucc beállítása
        }
        
        while (industrialZoneNum-- > 0) {
            while (!fields[freeRow][freeCol].isFree()) {    // while the chosen field is not free
                freeRow = r.nextInt((fieldRowsNum-1 - 0) + 1) + 0;
                freeCol = r.nextInt((fieldColsNum-1 - 0) + 1) + 0;
            }
            fields[freeRow][freeCol].setBuilding(new IndustrialZone(workplaceCapacity, tax, refund, 0, 10, 10, 10, 10, null)); // TODO: Minden spriteos cucc beállítása
        }
    }
    
    public ArrayList<Resident> getResidents(){
        return residents;
    }
    
    public int getResidentsNum(){
        return residents.size();
    }
    
    public Field[][] getFields(){
        return fields;
    }
    
    public int getSatisfaction(){
        return satisfaction;
    }
    
    public int getCriticalSatisfaction(){
        return criticalSatisfaction;
    }
    
    public long getBudget(){
        return budget;
    }
    
    public int negativeBudgetNthYear(){
        return negativeBudgetNthYear;
    }
    
    
    public void setBudget(long budget){
        this.budget = budget;
    }
    
    public void setNegativeBudgetNthYear(int n){
        negativeBudgetNthYear = n;
    }
    
    /**
     * add the given resident to residents
     * @param resident 
     */
    public void addResident(Resident resident){
        residents.add(resident);
    }
    
    /**
     * remove the given resident from residents
     * if the given resident is not in the city won't happen anything
     * @param resident 
     */
    public void removeResident(Resident resident){
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
     * Calculate residents avarage satisfaction and this value will be the city satisfaction
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
    
    public void increaseTax(){
        //TODO
    }
    
    public void lowerTax(){
        //TODO
    }
    
    public void fieldSelect(int x,int y){
        if(selectedField != null && selectedField == fields[x][y]){
            selectedField = null;
            showFieldInfoPopup = false;
        } else {
            selectedField = fields[x][y];
            showFieldInfoPopup = true;
        }
    }
    
    //Itt minden a selectedField-del dolgozik.
    
    public String getFieldInfo(){
        if(selectedField == null){
            throw new IllegalArgumentException("Trying to get info when selectedField is null");
        }
        return selectedField.getInfo();
    }
    
    public void nominateAsIndustrialZone(){
        if(selectedField == null){
            throw new IllegalArgumentException("Trying to get info when selectedField is null");
        }
        //TODO
    }
    
    public void nominateAsServiceZone(){
        if(selectedField == null){
            throw new IllegalArgumentException("Trying to get info when selectedField is null");
        }
        //TODO
    }
    
    public void nominateAsResidentialZone(){
        if(selectedField == null){
            throw new IllegalArgumentException("Trying to get info when selectedField is null");
        }
        //TODO
    }
    
    public void buildRoad(){
        if(selectedField == null){
            throw new IllegalArgumentException("Trying to get info when selectedField is null");
        }
        //TODO
    }
    
    public void buildPoliceStation(){
        if(selectedField == null){
            throw new IllegalArgumentException("Trying to get info when selectedField is null");
        }
        //TODO
    }
    
    public void buildFireStation(){
        if(selectedField == null){
            throw new IllegalArgumentException("Trying to get info when selectedField is null");
        }
        //TODO
    }
    
    public void buildStadium(){
        if(selectedField == null){
            throw new IllegalArgumentException("Trying to get info when selectedField is null");
        }
        //TODO
    }
    
    public void tryDenominateOrDestroyZone(){
        if(selectedField == null){
            throw new IllegalArgumentException("Trying to get info when selectedField is null");
        }
        //TODO
    }
    
    public void yearElapsed(){
        for(Field[] row:fields){
            for(Field field:row){
                if(!field.isFree()){
                    budget += field.getBuilding().getAnnualTax();
                    budget -= field.getBuilding().getAnnualFee();
                }
            }
        }
        if(budget < 0) negativeBudgetNthYear++;
        else negativeBudgetNthYear = 0;
    }
    public void performTicks(int ticks){
        for(Field[] row:fields){
            for(Field field:row){
                if(!field.isFree()) field.getBuilding().progressBuilding(ticks);
            }
        }
    }
}
