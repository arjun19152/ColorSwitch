import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.control.Label;


import java.io.IOException;
import java.util.*;

public class GamePlay {
    public Label score_label;
    private Ball ball;
    private int level;
    private int score;
    private Scene scene;
    private Stage stage;
    private boolean isPaused;
    private Stag stag;
    private String user_name;
    private ArrayList<Stag> stagArrayList;
    private AnimationTimer animationTimer;
    HashMap<String, Boolean> currentlyActiveKeys = new HashMap<>();

    //Since This class has provided its own default constructor which will be called by FXML loader,
    //We need to initialize the instance variables using initialize class.

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_name() {
        return user_name;
    }

    public int getScore() {
        return score;
    }

    public void initialize(Stage stage) throws IOException {

        isPaused = false;

        final int[] index = {0};

        this.level = 1;

        this.stage = stage;
        AnchorPane root = FXMLLoader.load(getClass().getResource("GamePlay.fxml"));
        scene = new Scene(root);

        score_label = (Label) root.lookup("#score_label");


        int ballOffsetY = 50;   //Initial offset of ball from the ground
        //Creating Ball Object
        ball = new Ball(20, 15, new Cordinate(root.getPrefWidth() / 2, root.getPrefHeight() - ballOffsetY));

        stagArrayList = new ArrayList<>(0);

        this.stag = new Stag(this, ball, 1);
        stag.initialize(root, 0, index[0]++);
        stagArrayList.add(stag);
        GamePlay currentObject = this;

        animationTimer = new AnimationTimer() {
            int i = 1;
            final Random random = new Random();

            @Override
            public void handle(long now) {
                //System.out.println(stagArrayList.get(stagArrayList.size() - 1).getAvatarGroup().getTranslateY());
                if (stagArrayList.get(stagArrayList.size() - 1).getAvatarGroup().getTranslateY() > 0) {
                    try {
                        i = random.nextInt(3);
                        Stag newStag = new Stag(currentObject, ball, ++i);
//                        System.out.println(i);
                        stagArrayList.add(newStag);
                        newStag.initialize(root, (int) stagArrayList.get(stagArrayList.size() - 1).getAvatarGroup().getTranslateY() - 800, index[0]++);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        animationTimer.start();


        root.getChildren().add(ball.getBall());

        stage.setScene(scene);
        stage.show();
    }


    public Scene getScene() {
        return scene;
    }

    public Stage getStage() {
        return stage;
    }

    public ArrayList<Stag> getStagArrayList() {
        return stagArrayList;
    }

    void increaseScore() {
        score++;
        if (score % 2 == 0)
            level++;
        score_label.setText("Score: " + score);
    }

    public void setScore(int score) {
        this.score = score;
        score_label.setText("Score: " + score);
    }

    public boolean isPaused() {
        return isPaused;
    }

    public void setPaused(boolean paused) {
        isPaused = paused;
    }

    public int getLevel() {
        return level;
    }

    public Ball getBall() {
        return ball;
    }

    public void killGame() {
        animationTimer.stop();
        for (Stag stag1 : stagArrayList) {
            stag1.killStag();
        }
    }
}
