
package UrbanTycoon;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class PopupInfo {
    public PopupInfo(JFrame parentComponent, String message, String title) {
        JOptionPane.showMessageDialog(parentComponent, message, title, JOptionPane.PLAIN_MESSAGE);
    }
}
