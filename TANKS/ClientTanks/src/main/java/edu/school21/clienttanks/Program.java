package edu.school21.clienttanks;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.input.KeyEvent;
import java.io.*;
import java.net.Socket;
import java.util.HashMap;

public class Program extends Application {
    private HashMap<KeyCode, Boolean> pressedKeys = new HashMap<>();
    public static final double WIDTH = 1042;
    public static final double HEIGHT = 1042;
    private static final int STEP = 10;
    private static BufferedReader input;
    public static PrintWriter output;
    private String lastKeyPressed = "SPACE";
    public static Canvas canvas;
    public static Player player;
    public static Player enemy;
    public static GraphicsContext graphicsContext;
    public static AnimationTimer animationTimer;
    public static Stage mainStage;
    public static boolean canPlay = false;
    Image background;


    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        mainStage = stage;

        Image icon = new Image(getClass().getResourceAsStream("/icon/icon.png"));

        Font font = Font.loadFont(Program.class.getResourceAsStream("/fonts/PressStart2PRegular.ttf"), 31);
        Text playerText = createText("Player", font, Color.BLACK, 240, 993);
        Text enemyText = createText("Enemy", font, Color.BLACK, WIDTH - 230 - 160, 47);

        stage.setTitle("WORLD OF TANKZ!");
        Group root = new Group();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.getIcons().add(icon);


        stage.setMinWidth(WIDTH);
        stage.setMaxWidth(WIDTH);
        stage.setMinHeight(HEIGHT);
        stage.setMaxHeight(HEIGHT);
        canvas = new Canvas(WIDTH, HEIGHT);

        root.getChildren().addAll(canvas, enemyText, playerText);
        graphicsContext = canvas.getGraphicsContext2D();

        background = new Image("field.png");

        connectToServer();

        if (!input.ready()) {
            new AnimationTimer() {
                int i = 0;
                private long lastUpdate = 0;
                private final long frameDuration = 500_000_000; // 0.5 seconds

                @Override
                public void handle(long now) {
                    if (now - lastUpdate >= frameDuration) {
                        graphicsContext.drawImage(new Image("Wait" + i + ".png"), 0, 0);
                        if (++i > 3) {
                            i = 0;
                        }
                        try {
                            if (input.ready()) {
                                this.stop();
                            }
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        lastUpdate = now;
                    }
                }
            }.start();
        }
        stage.show();

        player = new Player("playerup.png", 480, 830, root, 30, 960, graphicsContext);
        enemy = new Player("enemydown.png", 480, 72, root, WIDTH - 230, 15, graphicsContext);


        //players moving
        scene.setOnKeyPressed(event -> pressedKeys.put(event.getCode(), true));
        scene.setOnKeyReleased(event -> pressedKeys.put(event.getCode(), false));

        //player fires
        scene.setOnKeyPressed(this::handleKeyPressed);
        scene.setOnKeyReleased(this::handleKeyReleased);

        animationTimer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                try {
                    if (input.ready()) {
                        canPlay = true;
                    }
                    if (canPlay) {
                        updateGame();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        animationTimer.start();
    }

    Text createText(String text, Font font, Color color, double x, double y) {
        Text t = new Text(text);
        t.setFont(font);
        t.setFill(color);
        t.setX(x);
        t.setY(y);
        return t;
    }

    private void handleKeyPressed(KeyEvent event) {
        String code = event.getCode().toString();
        if (!code.equals("SPACE") || !lastKeyPressed.equals("SPACE")) {
            setKeyPressed(event.getCode());
        }
        lastKeyPressed = code;
    }

    private void handleKeyReleased(KeyEvent event) {
        setKeyReleased(event.getCode());
    }

    private void setKeyPressed(KeyCode keyCode) {
        pressedKeys.put(keyCode, true);
    }

    private void setKeyReleased(KeyCode keyCode) {
        pressedKeys.put(keyCode, false);
    }


    private void updateGame() throws IOException {
        updatePlayer();
        updateEnemy();
        updateBullet();
        if (enemy.checkLeaveGame()) {
            Program.gameOver("leftgame.png");
        } else if (!enemy.checkLife()) {
            Program.gameOver("win.png");
        }
        if (!player.checkLife()) {
            Program.gameOver("lose.png");
        }
        graphicsContext.clearRect(0, 0, WIDTH, HEIGHT);
        graphicsContext.drawImage(background, 0, 0);
        player.render(graphicsContext);
        enemy.render(graphicsContext);
    }


    private void updateBullet() throws IOException {
        if (pressedKeys.containsKey(KeyCode.SPACE)) {
            outShoot("/bulletUp.png", player, enemy, 0);
            pressedKeys.remove(KeyCode.SPACE);
            output.println("outshoot");
        }
    }

    public void updatePlayer() throws IOException {
        if (isMoveRightKeyPressed()) {
            if (bordersCheck(player)) {
                player.moveRight(STEP);
            } else {
                player.setPositionX(Math.min(player.getPositionX() + STEP, WIDTH - player.getWidth()));
            }
            output.println("right");
        } else if (isMoveLeftKeyPressed()) {
            if (bordersCheck(player))
                player.moveLeft(STEP);
            else
                player.setPositionX(Math.max(player.getPositionX() - STEP, 0));
            output.println("left");
        }
    }

    public boolean bordersCheck(Player player) {
        return (player.getPositionX() >= 0 && player.getPositionX() <= (WIDTH - player.getWidth())) ? true : false;
    }

    private boolean isMoveRightKeyPressed() {
        return isPressed(KeyCode.D) || isPressed(KeyCode.RIGHT);
    }

    private boolean isMoveLeftKeyPressed() {
        return isPressed(KeyCode.A) || isPressed(KeyCode.LEFT);
    }

    public void updateEnemy() throws IOException {
        if (input.ready()) {
            String read = input.readLine();
            switch (read) {
                case "right":
                    if (enemy.canMoveDirection("left")) {
                        enemy.moveLeft(STEP);
                        if (enemy.getPositionX() + enemy.getWidth() > WIDTH) {
                            enemy.setPositionX(WIDTH - enemy.getWidth());
                        }
                    }
                    break;
                case "left":
                    if (enemy.canMoveDirection("right")) {
                        enemy.moveRight(STEP);
                        if (enemy.getPositionX() < 0) {
                            enemy.setPositionX(0);
                        }
                    }
                    break;
                case "outshoot":
                    outShoot("/bulletDown.png", enemy, player, enemy.getBoundary().getHeight());
                    break;
                case "enemyLeftGame":
                    enemy.kill();
                    break;
                default:
            }
        }
    }

    public boolean isPressed(KeyCode key) {
        return pressedKeys.getOrDefault(key, false);
    }

    private void outShoot(String img, Player character, Player enemy, double forCorrect) {
        Fire outShoot = new Fire(character, img, graphicsContext, enemy, forCorrect);
        outShoot.start();
    }

    public static void gameOver(String backgroundImage) throws IOException {
        stopAnimationTimer();

        Stage gameOverStage = createGameOverStage(backgroundImage);
        Group root = createRoot();
        Scene scene = createScene(root);

        gameOverStage.setScene(scene);

        output.println("gameOver");
        String infoStr = readGameStatsFromInput();

        String[] info = infoStr.split(":");

        GraphicsContext graphicsContextGO = getGraphicsContext2D();

        Font font = Font.loadFont(Program.class.getResourceAsStream("/fonts/PressStart2PRegular.ttf"), 40);
        graphicsContextGO.setFontSmoothingType(FontSmoothingType.LCD);
        graphicsContextGO.setFill(Color.BLACK);
        graphicsContextGO.setFont(font);

        new AnimationTimer() {
            public void handle(long currentNanoTime) {
                graphicsContextGO.drawImage(new Image(backgroundImage), 0, 0);

                graphicsContextGO.strokeText(info[1], 510, 510);
                graphicsContextGO.strokeText(info[2], 510, 627);
                graphicsContextGO.strokeText(info[3], 510, 740);

                graphicsContextGO.strokeText(info[4], 760, 510);
                graphicsContextGO.strokeText(info[5], 760, 627);
                graphicsContextGO.strokeText(info[6], 760, 740);


            }
        }.start();
        gameOverStage.show();
    }

    private static Stage createGameOverStage(String backgroundImage) {
        Stage gameOverStage = new Stage();
        gameOverStage.setTitle("WORLD OF TANKZ!");
        gameOverStage.setX(mainStage.getX());
        gameOverStage.setY(mainStage.getY());
        mainStage.close();

        return gameOverStage;
    }

    private static Group createRoot() {
        Group root = new Group();
        canvas = new Canvas(WIDTH, HEIGHT);
        root.getChildren().add(canvas);

        return root;
    }

    private static Scene createScene(Group root) {
        Scene scene = new Scene(root);
        return scene;
    }

    private static String readGameStatsFromInput() throws IOException {
        String infoStr = input.readLine();
        while (!infoStr.contains("stat")) {
            infoStr = input.readLine();
        }
        return infoStr;
    }

    private static GraphicsContext getGraphicsContext2D() {
        return canvas.getGraphicsContext2D();
    }

    private static void stopAnimationTimer() {
        animationTimer.stop();
    }


    private void connectToServer() {
        final String HOST = "localhost";
        final int PORT = 8000;

        try {
            Socket clientSocket = new Socket(HOST, PORT);
            System.out.println("Connected to server");
            input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            output = new PrintWriter(clientSocket.getOutputStream(), true);
        } catch (IOException e) {
            System.err.println("Error connecting to " + HOST + ":" + PORT);
            e.printStackTrace();
            System.exit(1);
        }
    }
}