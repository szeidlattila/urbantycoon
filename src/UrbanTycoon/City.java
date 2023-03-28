/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package UrbanTycoon;

import java.util.ArrayList;

/**
 *
 * @author Felhasználó
 */
class City {
    private ArrayList<Resident> residents;
    private Field[][] fields;
    private int satisfaction = 0;
    private int criticalSatisfaction;
    private long budget;
    private int negativeBudgetNthYear = 0;;
    
    /**
     * initialize the city with the given information
     * create new residents and fields
     * @param residentsNum
     * @param fieldSize
     * @param fieldRowsNum
     * @param fieldColsNum
     * @param criticalSatisfaction
     * @param budget 
     */
    public City(int residentsNum, int fieldSize, int fieldRowsNum, int fieldColsNum, int criticalSatisfaction, int budget, int zonePrice) {
        if (residentsNum > 0) {
            this.residents = new ArrayList<>(residentsNum);
            for (int i = 0; i < residentsNum; i++) {
                this.residents.add(new Resident(0, null, null)); // TODO: paramétereket átírni
            }
        } else {
            throw new IllegalArgumentException("Invalid value! Residents number must be greater than 0!");
        }
        
        if (fieldRowsNum > 0 || fieldColsNum > 0) {
            this.fields = new Field[fieldRowsNum][fieldColsNum];
            for (int i = 0; i < fieldRowsNum; i++) {
                for (int j = 0; j < fieldColsNum; j++) {
                    this.fields[i][j] = new Field(true, null); // TODO: null helyett más
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
        for (Resident otherResident : residents) {
            if (resident == otherResident) {
                residents.remove(resident);
                break;
            }
        }
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
}
