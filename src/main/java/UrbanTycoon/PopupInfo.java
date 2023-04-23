/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package UrbanTycoon;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author ati
 */
public class PopupInfo {
    public PopupInfo(JFrame parentComponent, String message, String title) {
        JOptionPane.showMessageDialog(parentComponent, message, title, JOptionPane.PLAIN_MESSAGE);
    }
}
