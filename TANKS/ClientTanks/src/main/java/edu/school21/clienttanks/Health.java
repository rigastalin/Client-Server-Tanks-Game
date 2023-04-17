package edu.school21.clienttanks;

import javafx.scene.Group;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.ImageView;

public class Health {
    private ImageView border;
    private ImageView healthScale;
    private int healthBar = 200;
    private final int healthBarHight = 30;
    private int playerHealth = 100;
    private static final int DAMAGE_AMOUNT = 5;
    private static final int HEALTH_BAR_DECREASE = 10;


    public void setLevelLife(int playerHealth) {
        this.playerHealth  = playerHealth;
    }

    public Health(Group root, double positionX, double positionY, GraphicsContext graphicsContext) {
        if (root == null || graphicsContext == null) {
            throw new IllegalArgumentException("Root and graphicsContext cannot be null");
        }

        this.border = new ImageView(getClass().getClassLoader().getResource("border.png").toExternalForm());
        this.healthScale = new ImageView(getClass().getClassLoader().getResource("life.png").toExternalForm());
        border.setFitHeight(healthBarHight);
        border.setFitWidth(healthBar);
        healthScale.setFitHeight(healthBarHight - 2);
        healthScale.setFitWidth(healthBar - 8);
        healthScale.setX(positionX + 4);
        healthScale.setY(positionY + 1);
        border.setX(positionX);
        border.setY(positionY);
        root.getChildren().addAll(healthScale, border);
    }

    public void takeDamage() {
        if (playerHealth > 0) {
            playerHealth -= DAMAGE_AMOUNT;
            healthBar -= HEALTH_BAR_DECREASE;
            healthScale.setFitWidth(healthBar);
            if (playerHealth <= 0) {
                healthScale.setFitWidth(1);
                healthBar = 0;
            }
        }
    }

    public int getLevelLife() {
        return playerHealth;
    }
}

