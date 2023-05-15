package UrbanTycoon;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;

public class CustomLabelButton extends JButton {

    private String filePath = "data/graphics/other/font/Inter-";
    private String type;
    private Runnable func;

    public CustomLabelButton(String text, int size, String weight, String alignment, String type) {
        super(text);
        this.type = type;
        try {
            Font font = Font.createFont(Font.TRUETYPE_FONT, new File(filePath + weight + ".ttf"));
            font = font.deriveFont(Font.PLAIN, size);
            setFont(font);
        } catch (IOException | FontFormatException e) {

        }

        switch (alignment.toLowerCase()) {
            case "left" -> setHorizontalAlignment(JButton.LEFT);
            case "center" -> setHorizontalAlignment(JButton.CENTER);
            case "right" -> setHorizontalAlignment(JButton.RIGHT);
            default -> throw new IllegalArgumentException("Invalid alignment: " + alignment);
        }
        setBorderPainted(false);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setVerticalAlignment(JButton.CENTER);
        setForeground(Color.WHITE);
        addActionListener((ActionEvent e) -> {
            if (func != null) {
                func.run();
            }
        });
    }

    public String getType() {
        return type;
    }

    public Runnable getFunc() {
        return func;
    }

    public void setFunc(Runnable func) {
        this.func = func;
    }
}
