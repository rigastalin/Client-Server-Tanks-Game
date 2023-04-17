package edu.school21.clienttanks;

import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Player {
    private Image image;
    private Health healthBar;
    private double positionX;
    private double positionY;
    private double width;
    private double height;


    public Player(String img, double positionX, double positionY, Group root, double posXHealthBar,
                  double posYHealthBar, GraphicsContext graphicsContext) {
        this.positionX = positionX;
        this.positionY = positionY;
        healthBar = new Health(root, posXHealthBar, posYHealthBar, graphicsContext);
        setImage(new Image(img));
    }

    public void setImage(Image img) {
        image = img;
        width = img.getWidth();
        height = img.getHeight();
    }

    public void moveLeft(int step) {
        positionX -= step;
    }

    public void moveRight(int step) {
        positionX += step;
    }

    public boolean canMoveDirection(String move) {
        int leftBoundary = -20;
        int rightBoundary = 980;

        return move.equals("right") ? positionX + 10 < rightBoundary : positionX - 10 > leftBoundary;
    }

    public double getPositionX() {
        return positionX;
    }

    public double getPositionY() {
        return positionY;
    }

    public double getWidth() {
        return width;
    }


    public void setPositionX(double positionX) {
        this.positionX = positionX;
    }

    public void render(GraphicsContext gc)    {
        gc.drawImage( image, positionX, positionY );
    }

    public Rectangle2D getBoundary()    {
        return new Rectangle2D(positionX,positionY,width,height);
    }

    public void takeDamage() {
        healthBar.takeDamage();
    }

    public void kill() {
        healthBar.setLevelLife(-10);
    }

    public boolean checkLeaveGame() {
        return healthBar.getLevelLife() == -10;
    }

    public boolean checkLife() {
        return healthBar.getLevelLife() > 0;
    }
}