
package UrbanTycoon;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Dimension;

public class Sprite {
    /**
     * The coordinates of the top left corner of the sprite
     */
    protected int x;
    protected int y;
    protected int width;
    protected int height;
    protected Image image;

    public Sprite(int x, int y, int width, int height, Image image) {
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
        this.image = image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    /**
     * draw image
     * 
     * @param g
     * @param dimension
     */
    public void draw(Graphics g, Dimension dimension) {
        if (x > -width && y > -height && x < dimension.getWidth() + width && y < dimension.getHeight() + height) {
            g.drawImage(image, x, y, width, height, null);
        }
    }

    /**
     * 
     * @param other
     * @return true if this sprite collides with the other sprite
     */
    public boolean collides(Sprite other) {
        Rectangle rect = new Rectangle(x, y, width, height);
        Rectangle otherRect = new Rectangle(other.x, other.y, other.width, other.height);
        return rect.intersects(otherRect);
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void changeX(int x) {
        this.x += x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void changeY(int y) {
        this.y += y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * increase widht and height by size
     * 
     * @param size
     */
    public void changeSize(int size) {
        this.width += size;
        this.height += size;
    }

    public Image getImage() {
        return image;
    }
}
