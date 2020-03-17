package ghostshooterjfx;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.util.Duration;

/**
 *
 * @author Johnny Rouddro
 */
public class FXMLDocumentController implements Initializable {
    
    public static boolean gameStarted = false;
    
    private boolean gameOver = false;
    private int score = 0;
    private double time = 0;
    
    Timeline timeline = new Timeline();
    
    @FXML
    private ImageView ghost;
    @FXML
    private Label scoreLabel, gameStartLabel, gameOverLabel;
    @FXML
    private ProgressBar timeLeft;
    
    
    @FXML
    public void ghostShot(){
        ghost.setLayoutX(Math.random() * 600 + 100);
        ghost.setLayoutY(Math.random() * 400 + 100);
        score++;
        scoreLabel.setText("Score: " + String.valueOf(score));
        time = 1;
        gameOver = false;
    }
    
    
    
    @FXML
    public void startGame(){
        time = 1;
        score = 0;
        scoreLabel.setText("Score: " + String.valueOf(score));
        if(gameStarted){
            gameOverLabel.setLayoutX(-3000);
        }
        else{
            gameStarted = true;
            gameStartLabel.setLayoutX(-3000);
        }
        
        gameOver = false;

        ghost.setLayoutX(Math.random() * 600 + 100);
        ghost.setLayoutY(Math.random() * 400 + 100);
    }
    
    public void gameOver(){
        
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        gameOverLabel.setLayoutX(-3000);

        timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(0.01), new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                if(time > 0){
                    timeLeft.setProgress(time);
                    time -= 0.01;
                }
                else{
                    if(gameStarted && !gameOver){
                        gameOverLabel.setLayoutX(0);
                        ghost.setLayoutX(-300);
                        gameOver = true;
                    }
                }
            }
            
        }));
        
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
        
//        Timer timer = new Timer();
//        timer.scheduleAtFixedRate(new TimerTask(){
//            @Override
//            public void run() {
//                
//                if(time > 0){
//                    timeLeft.setProgress(time);
//                    time -= 0.01;
//                }
//                else{
//                    if(gameStarted && !gameOver){
//                        gameOverLabel.setLayoutX(0);
//                        ghost.setLayoutX(-300);
//                        gameOver = true;
//                    }
//                }
//            }
//            
//        }, 0, 10);
        
    
        
//        AnimationTimer process = new AnimationTimer() {
//        @Override
//        public void handle(long now) {
//            double t = (now - startNanoTime) / 1000000000.0; 
// 
//            double x = 232 + 128 * Math.cos(t);
//            double y = 232 + 128 * Math.sin(t);
//            
//            ghost.setLayoutX(x);
//            ghost.setLayoutY(y);
//        }
//        };
//    
//        process.start();
    }    
    
}
