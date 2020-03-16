package ghostshooterjfx;

import java.awt.Event;
import java.io.File;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.ImageCursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.AudioClip;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author Johnny Rouddro
 */
public class GhostShooterJFX extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        
        AudioClip sfx = new AudioClip(new File("src/ghostshooterjfx/pistol.mp3").toURI().toString());
        
        Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
        
        Scene scene = new Scene(root);
        
        Image crosshair_image = new Image("ghostshooterjfx/crosshair.png");
        scene.setCursor(new ImageCursor(crosshair_image, crosshair_image.getWidth()/2, crosshair_image.getHeight()/2));
        
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
        
        stage.setOnCloseRequest((WindowEvent t) -> {
            Platform.exit();
            System.exit(0);
        });
        
        stage.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                
                sfx.play();
            
            }
            
        });
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
