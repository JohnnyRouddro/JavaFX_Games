package javafxsnake;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import javafx.scene.control.Label;

/**
 *
 * @author Johnny Rouddro
 */
public class JavaFXSnake extends Application implements Initializable {
    
    @FXML
    private Label label, scoreLabel;
    
    @FXML
    private Rectangle snakeHead, food;
    
    @FXML
    private Pane snakeRoot;
    
    @FXML
    private Button button;
    
    public static enum Direction {
        UP, RIGHT, DOWN, LEFT
    };
    
    public static final int BLOCK_SIZE = 30;
    public static final int GRID_MAX_X = 20;
    public static final int GRID_MAX_Y = 20;
    public static Direction facing = Direction.RIGHT;
    public static Direction moving = Direction.RIGHT;

    private int score = 0;
    private double foodX, foodY;
    private double facePosX, facePosY;
    private boolean isAlive = true, gameOver = false, restarted = false;
    
    private List<List> snakePos = new ArrayList();
    
    Random random = new Random();
    Stage primaryStage = new Stage();
    
    
    public void growTail(double x, double y){
        Rectangle newTail = new Rectangle(snakeHead.getWidth(), snakeHead.getHeight(), snakeHead.getFill());
        newTail.setStroke(snakeHead.getStroke());
        newTail.setStrokeWidth(snakeHead.getStrokeWidth());
        newTail.setStrokeType(snakeHead.getStrokeType());
        newTail.setEffect(snakeHead.getEffect());
        newTail.setArcHeight(20);
        newTail.setArcWidth(20);
        newTail.setLayoutX(0);
        newTail.setLayoutY(0);
        newTail.setTranslateX(x);
        newTail.setTranslateY(y);
        snakeRoot.getChildren().add(newTail);
        snakePos.add(Arrays.asList(newTail.getTranslateX(), newTail.getTranslateY()));
    }
    
    public void foodReposition(){
        
        do{
            foodX = (random.nextInt(GRID_MAX_X - 7) + 3) * BLOCK_SIZE;
            foodY = (random.nextInt(GRID_MAX_Y - 7) + 3) * BLOCK_SIZE;
        }
        while(snakePos.contains(Arrays.asList(foodX, foodY))); // So that the food doesn't spawn on the snake's body
        food.setTranslateX(foodX);
        food.setTranslateY(foodY);
    }

    
    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        primaryStage.setTitle("JavaFX Snake Game");
        
        Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
        
        Scene scene = new Scene(root);
        
        stage.setOnCloseRequest(new EventHandler<WindowEvent> (){
            @Override
            public void handle(WindowEvent event) {
                Platform.exit();
                System.exit(0);
            }
            
        });
        
        scene.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>(){
            @Override
            public void handle(KeyEvent event) {
                
                /**
                    This makes sure that the snake cant move against the direction it's facing.
                    Also, 2 different variables 'moving' and 'facing', so that
                    we can calculate if the snake collides with itself before moving.
                **/
                if (event.getCode() == KeyCode.UP && moving != Direction.DOWN) {
                    facing = Direction.UP;
                }
                else if (event.getCode() == KeyCode.RIGHT && moving != Direction.LEFT) {
                    facing = Direction.RIGHT;
                }
                else if (event.getCode() == KeyCode.DOWN && moving != Direction.UP) {
                    facing = Direction.DOWN;
                }
                else if (event.getCode() == KeyCode.LEFT && moving != Direction.RIGHT) {
                    facing = Direction.LEFT;
                }
            }
        });
        
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }
    
    @FXML
    public void startGame(){
        
        score = 0;
        scoreLabel.setText("Score: 0");
        
        isAlive = true;
        label.setVisible(false);
        button.setVisible(false);
        
        facing = Direction.RIGHT;
        moving = Direction.RIGHT;
        
        snakeHead.setLayoutX(0);
        snakeHead.setLayoutY(0);
        
        Rectangle root = snakeHead;
        snakeRoot.getChildren().clear();
        snakePos.clear();
        snakeRoot.getChildren().add(root);
        
        snakeHead.setTranslateX((random.nextInt(GRID_MAX_X - 7) + 3) * BLOCK_SIZE);
        snakeHead.setTranslateY((random.nextInt(GRID_MAX_Y - 7) + 3) * BLOCK_SIZE);
        
        growTail(snakeHead.getTranslateX() - BLOCK_SIZE, snakeHead.getTranslateY());
        snakeRoot.getChildren().get(1).setTranslateX(snakeHead.getTranslateX() - BLOCK_SIZE);
        
        facePosX = snakeHead.getTranslateX() + BLOCK_SIZE;
        facePosY = snakeHead.getTranslateY();
        
        snakePos.add(Arrays.asList(snakeHead.getTranslateX(), snakeHead.getTranslateY()));
        
        foodReposition();
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
        startGame();

        Timeline gameLoop = new Timeline(new KeyFrame(Duration.seconds(0.10), new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                
                if (isAlive) {
                    
                    // POSITION OF A SNAKE BLOCK IS SET TO BE THE POSITION OF THE PREVIOUS SNAKE BLOCK
                    
                    if (snakeRoot.getChildren().size() > 1) {
                        for (int i = snakeRoot.getChildren().size() - 1; i > 0; i--) {
                            snakeRoot.getChildren().get(i).setTranslateX(snakeRoot.getChildren().get(i-1).getTranslateX());
                            snakeRoot.getChildren().get(i).setTranslateY(snakeRoot.getChildren().get(i-1).getTranslateY());
                        }
                    }
                    
                    // UPDATING THE (X, Y) POSITION LIST
                    
                    for (int i = 0; i < snakePos.size(); i++) {
                        snakePos.set(i, Arrays.asList(snakeRoot.getChildren().get(i).getTranslateX(), snakeRoot.getChildren().get(i).getTranslateY()));
                    }
                    
                    // POSITION THE FACEPOS AT THE NEXT BLOCK
                    
                    if(facing == Direction.UP){
                        facePosY = snakeHead.getTranslateY() - BLOCK_SIZE;
                        moving = facing;
                    }
                    else if(facing == Direction.RIGHT){
                        facePosX = snakeHead.getTranslateX() + BLOCK_SIZE;
                        moving = facing;
                    }
                    else if(facing == Direction.DOWN){
                        facePosY = snakeHead.getTranslateY() + BLOCK_SIZE;
                        moving = facing;
                    }
                    else if(facing == Direction.LEFT){
                        facePosX = snakeHead.getTranslateX() - BLOCK_SIZE;
                        moving = facing;
                    }
                    
                    // CHECK IF THE FACEPOS IS COLLIDING WITH SNAKE BODY
                    
                    if (snakePos.contains(Arrays.asList(facePosX, facePosY))) {
                        isAlive = false;
                        label.setVisible(true);
                        button.setVisible(true);
                        button.requestFocus();
                    }
                    else{   
                        
                        // MOVE THE SNAKE HEAD
                        
                        snakeHead.setTranslateX(facePosX);
                        snakeHead.setTranslateY(facePosY);
                    }

                    // FOOD HAS BEEN EATEN
                    
                    if ((int)snakeHead.getTranslateX() == foodX && (int)snakeHead.getTranslateY() == foodY) {

                        growTail(snakeHead.getTranslateX(), snakeHead.getTranslateY());
                        foodReposition();
                        score ++;
                        scoreLabel.setText("Score: " + String.valueOf(score));

                    }

                    // SNAKE ROTATES FROM ONE WALL TO THE OPPOSITE WALL
                    
                    if (snakeHead.getTranslateX() < 0) {
                        snakeHead.setTranslateX((GRID_MAX_X - 1) * BLOCK_SIZE);
                    }
                    if (snakeHead.getTranslateX() > (GRID_MAX_X - 1) * BLOCK_SIZE) {
                        snakeHead.setTranslateX(0);
                    }
                    if (snakeHead.getTranslateY() < 0) {
                        snakeHead.setTranslateY((GRID_MAX_Y - 1) * BLOCK_SIZE);
                    }
                    if (snakeHead.getTranslateY() > (GRID_MAX_Y - 1) * BLOCK_SIZE) {
                        snakeHead.setTranslateY(0);
                    }
                }
                else{
                }
                
            }
        }));
        gameLoop.setCycleCount(Timeline.INDEFINITE);
        gameLoop.play();
    } 
    

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
