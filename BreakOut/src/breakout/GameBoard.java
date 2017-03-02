/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package breakout;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 *
 * @author Loic
 */
public class GameBoard{
    
    private Scene scene;
    private final int WIDTH, HEIGHT;
    private GraphicsContext gc;
    
    private final int BRICK_COLUMNS = 13;
    private final int BRICK_ROWS = 10;
    private final int BRICK_WIDTH = 38;
    private final int BRICK_HEIGHT = 16;
    private ArrayList<Brick> bricks = new ArrayList();
    
    private float playerX;
    private float vPlayer = 0;
    private final int PLAYER_WIDTH = 100;
    private final int PLAYER_HEIGHT = 15;
    private boolean left = false;
    private boolean right = false;
    
    private float ballX;
    private float ballY;
    private final int BALL_SPEED = 3;
    private double vBall[] = {0,0};
    private final int BALL_RADIUS = 6;

    GameBoard(Scene scene, int WIDTH, int HEIGHT, GraphicsContext gc) {
        this.scene = scene;
        this.WIDTH = WIDTH;
        this.HEIGHT = HEIGHT;
        this.gc = gc;
        keyLiestener();
    }
    
    public void reset(){
        brickWallCreation();
        
        playerX = WIDTH / 2 - PLAYER_WIDTH / 2;
        vPlayer = 0;
        
        ballX = WIDTH / 2 - BALL_RADIUS;
        ballY = HEIGHT - 100;
        createBeginingBallAngle(45, 130);
    }

    public void refresh(){     
        // Clear all the canvas (for refresh draws etc...)
        gc.clearRect(0, 0, WIDTH, HEIGHT);
        
        displayBrickWall();
        moveAndDisplayPlayer();
        moveAndDisplayBall();
    }
    
    private void brickWallCreation(){
        for (int columnsCount = 0; columnsCount < BRICK_COLUMNS; columnsCount++) {
            for (int rowsCount = 0; rowsCount < BRICK_ROWS; rowsCount++) {
                int damages;
                
                if(rowsCount <= 2){
                    damages = 3;
                }else if(rowsCount <= 4){
                    damages = 2;
                }else{
                    damages = 1;
                }
                // Brick x : middle of the game windows - half brickwall width + current columns count * brick_width => this way the brickwall is center on the game windows 
                // Brick y : 50 (random height from the top) + current rows count * brick_height
                Brick brick = new Brick(WIDTH/2 - BRICK_COLUMNS*BRICK_WIDTH/2 + columnsCount*BRICK_WIDTH, 50+rowsCount*BRICK_HEIGHT, damages);
                bricks.add(brick);
            }
        }
    }
    
    /*
    * vBall[0] and vBall[1] is a velocity "vector" and BALL_SPEED is the norm of the "vector"
    * We would like an angle between the ball and X axis, so
    */
    private void createBeginingBallAngle(int minAngle, int maxAngle){
        double angleDegrees = ThreadLocalRandom.current().nextInt(45, 130 + 1);
        double angleRadians = Math.toRadians(angleDegrees);
        vBall[0] = Math.cos(angleRadians) * BALL_SPEED;
        vBall[1] = Math.sin(angleRadians) * BALL_SPEED;
        
        System.out.println("vBall X : " + vBall[0]);
        System.out.println("vBall Y : " + vBall[1]);
    }
    
    private void displayBrickWall(){
        for (int i = 0; i < bricks.size(); i++) {
            Brick currentBrick = bricks.get(i);
            if(!currentBrick.isDestroyed()){
                currentBrick.collide();
                if(currentBrick.getDamages() == 3){
                    gc.setFill(Color.web("#216869"));
                }else if(currentBrick.getDamages() == 2){
                    gc.setFill(Color.web("#49A078"));
                }else{
                    gc.setFill(Color.web("#9CC5A1"));
                }
                // Draw Rectangle (the brick)
                // BRICK_WIDTH-2 and BRICK_HEIGHT-2 => to let little space (2px) between each brick
                gc.fillRect(currentBrick.getX(), currentBrick.getY(), BRICK_WIDTH-2, BRICK_HEIGHT-2);
            }
        }
    }
    
    private void moveAndDisplayPlayer(){
        // Move the player
        if(left){
            vPlayer -= .6;
        }
        if(right){
            vPlayer += .6;
        }
        // If we don't move the player : We multiply the velocity by 0.9
        // This way, the velocity is reduced step by step until 0.00000xxxx so it will be stop smoothly 
        if(!left && !right){
            vPlayer *= .8;
        }
        
        // We bridle the velocity to 7 or -7 (max velocity)
        if(vPlayer < -7){
            vPlayer = -7;
        }else if(vPlayer > 7){
            vPlayer = 7;
        }
        
        playerX += vPlayer;
        // Collision with windows (left and right side)
        if(playerX < 0){
            playerX = 0;
            vPlayer = 0;
        }else if(playerX > WIDTH - PLAYER_WIDTH){
            playerX = WIDTH - PLAYER_WIDTH;
            vPlayer = 0;
        }
        
        // Display the player
        gc.setFill(Color.web("#D2D2E6"));
        gc.fillRect(playerX, HEIGHT-30, PLAYER_WIDTH, PLAYER_HEIGHT); 
    }
    
    private void moveAndDisplayBall(){
        ballX += vBall[0];
        ballY -= vBall[1];
        
        // Collision with left side
        if(ballX < 0){
            vBall[0] *= -1; // inverse the vBall X
        }
        
        // Display the ball
        gc.setFill(Color.web("#FFFFFF"));
        gc.fillOval(ballX, ballY, BALL_RADIUS * 2, BALL_RADIUS * 2);
    }
    
    private void keyLiestener(){
        scene.setOnKeyPressed(new EventHandler<javafx.scene.input.KeyEvent>() {
            public void handle(javafx.scene.input.KeyEvent event) {
                switch(event.getCode()){
                    case LEFT:
                        left = true;
                        break;
                    case RIGHT:
                        right = true;
                        break;
                }
            }
        });
        
        scene.setOnKeyReleased(new EventHandler<javafx.scene.input.KeyEvent>() {
            @Override
            public void handle(javafx.scene.input.KeyEvent event) {
                switch(event.getCode()){
                    case LEFT:
                        left = false;
                        break;
                    case RIGHT:
                        right = false;
                        break;
                }
            }
        });
    }
    
    

}
