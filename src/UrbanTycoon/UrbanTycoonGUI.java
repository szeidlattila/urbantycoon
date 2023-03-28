/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package UrbanTycoon;

import java.awt.Dimension;
import javax.swing.JFrame;

/**
 *
 * @author Felhasználó
 */
public class UrbanTycoonGUI {
    private JFrame frame;
    private GameEngine gameArea;
    public UrbanTycoonGUI() {
        frame = new JFrame("UrbanTycoon");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        gameArea = new GameEngine();
        frame.add(gameArea);
        
        frame.setPreferredSize(new Dimension(800, 600));
        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);
    }
}
