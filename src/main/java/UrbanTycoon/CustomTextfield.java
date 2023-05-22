package UrbanTycoon;

import javax.swing.JTextField;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

@SuppressWarnings("serial")
public class CustomTextfield extends JTextField {

    private BufferedImage backgroundImage;
    private String filePath = "data/graphics/other/font/Inter-";
    private int fontSize;
    private String fontWeight;
    private String alignment;

    public CustomTextfield(int fontSize, String fontWeight, String alignment) {
        super();
        this.fontSize = fontSize;
        this.fontWeight = fontWeight;
        this.alignment = alignment;

        try {
            Font font = Font.createFont(Font.TRUETYPE_FONT, new File(filePath + fontWeight + ".ttf"));
            font = font.deriveFont(Font.PLAIN, fontSize);
            setFont(font);
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }

        setOpaque(false); // Make the background transparent
        setBorder(null); // Remove the border
        setMargin(new Insets(0, 0, 0, 0)); // Set zero-margin

        setForeground(Color.WHITE);
        setHorizontalAlignment(getAlignmentConstant(alignment));
    }

    public void setBackgroundImage(String imagePath) {
        try {
            this.backgroundImage = ImageIO.read(new File(imagePath + ".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (backgroundImage != null) {
            // Scale the image to fit the size of the text field
            Image scaledImage = backgroundImage.getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH);
            g.drawImage(scaledImage, 0, 0, null);
        }
        super.paintComponent(g);
    }

    private int getAlignmentConstant(String alignment) {
        switch (alignment.toLowerCase()) {
            case "left" -> {
                return JTextField.LEFT;
            }
            case "center" -> {
                return JTextField.CENTER;
            }
            case "right" -> {
                return JTextField.RIGHT;
            }
            default -> throw new IllegalArgumentException("Invalid alignment: " + alignment);
        }
    }
}
