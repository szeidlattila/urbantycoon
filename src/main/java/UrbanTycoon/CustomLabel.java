package UrbanTycoon;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.io.File;
import java.io.IOException;
import javax.swing.JLabel;

public class CustomLabel extends JLabel {

    private String filePath = "data/graphics/other/font/Inter-";
    private String type;

    public CustomLabel(String text, int size, String weight, String alignment, String type) {
        super(text);
        this.type = type;
        try {
            Font font = Font.createFont(Font.TRUETYPE_FONT, new File(filePath + weight + ".ttf"));
            font = font.deriveFont(Font.PLAIN, size);
            setFont(font);
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }

        switch (alignment.toLowerCase()) {
            case "left":
                setHorizontalAlignment(JLabel.LEFT);
                break;
            case "middle":
                setHorizontalAlignment(JLabel.CENTER);
                break;
            case "right":
                setHorizontalAlignment(JLabel.RIGHT);
                break;
            default:
                throw new IllegalArgumentException("Invalid alignment: " + alignment);
        }
        
        setVerticalAlignment(JLabel.CENTER);
        setForeground(Color.WHITE);
    }
    
    public String getType() {
        return type;
    }

}
