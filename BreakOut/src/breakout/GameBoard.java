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
    
    private final int BRICK_COLUMNS = 10;
    private final int BRICK_ROWS = 13;
    private final int BRICK_WIDTH = 38;
    private final int BRICK_HEIGHT = 16;
    private ArrayList<Brick> bricks = new ArrayList();
    
    private float playerX;
    private float playerY;
    private float vPlayer = 9;
    private final int PLAYER_WIDTH = 100;
    private final int PLAYER_HEIGHT = 15;
    private boolean left = false;
    private boolean right = false;
    
    private float ballX;
    private float ballY;
    private final double BALL_SPEED = 5;
    private double vBall[] = {0,0};
    private final int BALL_RADIUS = 6;
    private boolean ballLoose = false;

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
        playerY = HEIGHT - 30;
        
        ballX = WIDTH / 2 - BALL_RADIUS;
        ballY = HEIGHT - 100;
        ballLoose = false;
        generateBallAngle(45, 135);
    }

    public void refresh(){     
        // Clear all the canvas (for refresh draws etc...)
        gc.clearRect(0, 0, WIDTH, HEIGHT);
        
        displayBrickWall();
        moveAndDisplayBall();
        moveAndDisplayPlayer();
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
                System.out.println(brick.getX()+" : "+brick.getY());
                bricks.add(brick);
            }
        }
    }
    
    /*
    * vBall[0] and vBall[1] is a velocity "vector" and BALL_SPEED is the norm of the "vector"
    * We have an angle (between X axis and the ball)
    * To have the x, y composants of the velocity vector, we have to do the cosinus or sinus of the angle multiply by the norm (speed)
    * So, like that, we find the composants of te ball velocity, for the choosed angle.
    */
    private void generateBallAngle(double minAngle, double maxAngle){
        double angleDegrees = ThreadLocalRandom.current().nextDouble(minAngle, maxAngle + 1);
        double angleRadians = Math.toRadians(angleDegrees);
        vBall[0] = Math.cos(angleRadians) * BALL_SPEED;
        vBall[1] = Math.sin(angleRadians) * BALL_SPEED;
    }
    
    private void displayBrickWall(){
        for (int i = 0; i < bricks.size(); i++) {
            Brick currentBrick = bricks.get(i);
            if(!currentBrick.isDestroyed()){
                if(currentBrick.collide(ballX, ballY, BALL_RADIUS, BRICK_WIDTH, BRICK_HEIGHT)){
                    vBall[1] *= -1;
                }
                switch(currentBrick.getDamages()){
                    case 3:
                        gc.setFill(Color.web("#216869"));
                        break;
                    case 2:
                        gc.setFill(Color.web("#49A078"));
                        break;
                    case 1:
                        gc.setFill(Color.web("#9CC5A1"));
                        break;
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
            vPlayer -= .9;
        }
        if(right){
            vPlayer += .9;
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
        gc.fillRect(playerX, playerY, PLAYER_WIDTH, PLAYER_HEIGHT); 
    }
    
    private void moveAndDisplayBall(){
        // I added the X composant of the velocity ball vector to the ballX for move the ball on the X axis.
        // I substract the Y composant of the velocity ball vector to the ballY for move the ball (top direction) on the Y axis.
        ballX += vBall[0];
        ballY -= vBall[1];
        
        // Collision with left and right side
        if(ballX < 0 || ballX + BALL_RADIUS*2 > WIDTH){
            vBall[0] *= -1; // inverse the vBall X
        }
        
        // Collision with the top
        if(ballY < 0){
            vBall[1] *= -1; // inverse the vBall Y
        }
        
        
        // to prevent bug if the ball comes on the player (after the Y limit) from the side ==> it's LOOSE
        if(ballY + BALL_RADIUS*2 > playerY && ballX + BALL_RADIUS*2 < playerX || ballY + BALL_RADIUS*2 > playerY &&   ballX > playerX + PLAYER_WIDTH){
            ballLoose = true;
        }
        // Collision with the player
        if(ballY + BALL_RADIUS*2 > playerY && ballX + BALL_RADIUS*2 >= playerX && ballX <= playerX + PLAYER_WIDTH && !ballLoose){
            vBall[1] *= -1; // inverse the vBall Y
            // I search the pourcent (on 160% => return angle between 160° and 20°) where touch the ball on the player Width
            double rawAngle = 140 * ((ballX + BALL_RADIUS) - playerX) / PLAYER_WIDTH;
            if(rawAngle < 40){
                rawAngle = 40;
            }
            // The raw angle define the angle returned with where the ball touch the player. And the raw angle is beetwen 160 and 20 but 160 is right side and 20 is left side.
            // So I have to inverse the angle direction => 180 - rawAngle so an angle of 140 is in real a 40° angle from right side
            double newAngle = 180 - rawAngle;
            generateBallAngle(newAngle, newAngle);
        }
        
        // Collision with the bottom
        if(ballY + BALL_RADIUS*2 > HEIGHT){
            System.out.println("Perdu");
            // We loose a life (later) and generate new angle random beetwen 45° and 145°
            // After, call the reset method with the life and if the life is not 0 do smothing etc...
            ballX = WIDTH / 2 - BALL_RADIUS;
            ballY = HEIGHT - 100;
            ballLoose = false;
            generateBallAngle(45, 135);
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
