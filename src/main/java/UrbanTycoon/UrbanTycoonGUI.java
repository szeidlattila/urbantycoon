/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package UrbanTycoon;

import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.*;
import java.io.*;
import java.awt.image.*;
import javax.imageio.*;
import java.awt.*;
import java.awt.event.*;

/**
 *
 * @author Felhasználó
 */
public class UrbanTycoonGUI {
    private JFrame frame;
    private GameEngine gameArea;
    private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    public UrbanTycoonGUI() {
        frame = new JFrame("UrbanTycoon");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        gameArea = new GameEngine(screenSize);

        frame.add(gameArea);

        // frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setPreferredSize(Toolkit.getDefaultToolkit().getScreenSize());
        frame.setResizable(false);
        frame.setUndecorated(true);
        frame.pack();
        frame.setVisible(true);
    }
}
