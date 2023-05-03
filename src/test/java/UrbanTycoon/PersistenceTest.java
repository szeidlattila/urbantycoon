/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package UrbanTycoon;

import java.util.ArrayList;
import java.util.Scanner;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Felhasználó
 */

public class PersistenceTest {
    
    public static City city;
    
    @BeforeEach
    public void setup(){
        city = new GameEngine().getCity();
    }
    
    @Test
    public void loadSameGame(){
        City city2 = new GameEngine().getCity();
        for(int i=0;i<city.getFields().length;i++)
            for(int j=0; j<city.getFields()[0].length;j++)
                assertEquals(city.getFields()[i][j],city2.getFields()[i][j]);
        String str = city.saveGame();
        Scanner sc = new Scanner(str);
        city.loadGame(sc);
        for(int i=0;i<city.getFields().length;i++)
            for(int j=0; j<city.getFields()[0].length;j++)
                assertEquals(city.getFields()[i][j],city2.getFields()[i][j]);
    }
    @Test
    public void changeThenLoad(){
        City city2 = new GameEngine().getCity();
        city.fieldSelect( 5,1);
        city.build(Stadium.class);
        city.fieldSelect(6,0);
        city.selectField(IndustrialZone.class);
        city.yearElapsed();
        city.moveInOneResident(false);
        String str = city.saveGame();
        city2.loadGame(new Scanner(str));
        for(int i=0;i<city.getFields().length;i++)
            for(int j=0; j<city.getFields()[0].length;j++)
                assertEquals(city.getFields()[i][j],city2.getFields()[i][j]);
        for(int i=0;i<city.getResidents().size();i++)
            assertEquals(city.getResidents().get(i),city2.getResidents().get(i));
    }
}
