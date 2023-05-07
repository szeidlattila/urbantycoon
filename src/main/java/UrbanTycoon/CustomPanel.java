package UrbanTycoon;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class CustomPanel extends JPanel {
    private BufferedImage image;

    public void setImage(String picturePath) {
        try {
            this.image = ImageIO.read(new File(picturePath + ".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Get the scaled image with the same aspect ratio as the panel
        Image scaledImage = image.getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH);

        // Draw the scaled image on the panel
        g.drawImage(scaledImage, 0, 0, null);
    }
}
