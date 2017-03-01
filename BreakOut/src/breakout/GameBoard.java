/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package breakout;

import java.util.ArrayList;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

/**
 *
 * @author Loic
 */
public class GameBoard {
    
    private final int WIDTH, HEIGHT;
    private GraphicsContext gc;
    private final int BRICK_COLUMNS = 13;
    private final int BRICK_ROWS = 10;
    private final int BRICK_WIDTH = 38;
    private final int BRICK_HEIGHT = 16;
    private ArrayList<Brick> bricks = new ArrayList();

    GameBoard(int WIDTH, int HEIGHT, GraphicsContext gc) {
        this.WIDTH = WIDTH;
        this.HEIGHT = HEIGHT;
        this.gc = gc;
    }
    
    public void reset(){
        brickWallCreation();
    }
    
    public void refresh(){
        for (int i = 0; i < bricks.size(); i++) {
            Brick currentBrick = bricks.get(i);
            if(!currentBrick.isDestroyed()){
                currentBrick.collide();
                if(currentBrick.getDamages() == 3){
                    //gc.setFill(Color.web("rgb(90,90,140)"));
                    gc.setFill(Color.web("#216869"));
                }else if(currentBrick.getDamages() == 2){
                    //gc.setFill(Color.web("rgb(120,120,175)"));
                    gc.setFill(Color.web("#49A078"));
                }else{
                    //gc.setFill(Color.web("rgb(150,150,200)"));
                    gc.setFill(Color.web("#9CC5A1"));
                }
                // Draw Rectangle (the brick)
                // BRICK_WIDTH-2 and BRICK_HEIGHT-2 => to let little space (2px) between each brick
                gc.fillRect(currentBrick.getX(), currentBrick.getY(), BRICK_WIDTH-2, BRICK_HEIGHT-2);
            }
        }
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
    
}
