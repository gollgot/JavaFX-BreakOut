/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package breakout;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 *
 * @author Loic
 */
public class BreakOut extends Application {
    
    private final int WIDTH = 600;
    private final int HEIGHT = 500;
    private GameBoard gameBoard;
    
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("BreakOut Game");
        
        Group root = new Group();
        Scene theScene = new Scene(root, WIDTH, HEIGHT, Color.web("#7A918D"));
        primaryStage.setScene(theScene);

        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        root.getChildren().add(canvas);
        
        GraphicsContext gc = canvas.getGraphicsContext2D();

        gameBoard = new GameBoard(WIDTH, HEIGHT, gc);
        gameBoard.reset();
        
        new AnimationTimer()
        {
            public void handle(long currentNanoTime)
            {
                gameBoard.refresh();
            }
        }.start();
        
        
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
