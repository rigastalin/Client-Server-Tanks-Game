package edu.school21.clienttanks;

import javafx.animation.AnimationTimer;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Fire extends AnimationTimer {
    private Image image;
    Player enemy;
    private double positionX;
    private double positionY;
    private double step;
    private double width;
    private double height;
    private boolean shooter;
    private GraphicsContext gc;

    private static final int FIRE_STEP_DOWN = 10;
    private static final int FIRE_STEP_UP = -10;
    private static final int MAX_Y_POSITION = 830;

    private enum State {
        OUT_OF_BOUNDS,
        HIT_ENEMY,
        MOVING
    }

    public Fire(Player character, String image, GraphicsContext gc, Player enemy, double correction) {
        this.positionX = (character.getPositionX() + character.getBoundary().getMaxX()) / 2;
        this.positionY = character.getPositionY() + correction;
        this.shooter = isShooter(correction);
        this.enemy = enemy;
        this.gc = gc;
        this.step = getFireStep(positionY);
        setImage(new Image(image));
    }

    private boolean isShooter(double correction) {
        return correction == 0;
    }

    private int getFireStep(double positionY) {
        if (positionY == MAX_Y_POSITION) {
            return FIRE_STEP_UP;
        } else {
            return FIRE_STEP_DOWN;
        }
    }

    public double getPositionY() {
        return positionY;
    }

    public void setImage(Image i) {
        image = i;
        width = i.getWidth();
        height = i.getHeight();
    }

    public void render()    {
        gc.drawImage( image, positionX, positionY );
    }

    public void move() {
        positionY += step;
    }
    public Rectangle2D getBoundary()    {
        return new Rectangle2D(positionX,positionY,width,height);
    }

    @Override
    public void handle(long l) {
        switch (getState()) {
            case OUT_OF_BOUNDS:
                this.stop();
                break;
            case HIT_ENEMY:
                enemy.takeDamage();
                if (shooter) {
                    Program.output.println("hit");
                }
                if (!enemy.checkLife()) {
                    this.stop();
                }
                this.stop();
                break;
            case MOVING:
                this.move();
                this.render();
                break;
        }
    }

    private State getState() {
        if (isOutOfBounds()) {
            return State.OUT_OF_BOUNDS;
        }
        if (hitEnemy()) {
            return State.HIT_ENEMY;
        }
        return State.MOVING;
    }


    private boolean isOutOfBounds() {
        return this.getPositionY() + 10 >= 1042 || this.getPositionY() - 10 <= 0;
    }

    private boolean hitEnemy() {
        return enemy.getBoundary().intersects(this.getBoundary());
    }

}
