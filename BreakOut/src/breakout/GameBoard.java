/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package breakout;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 *
 * @author Loic
 */
public class GameBoard {
    
    private final int WIDTH, HEIGHT;
    private GraphicsContext gc;
    private int x, y;
    private Image mario;

    GameBoard(int WIDTH, int HEIGHT, GraphicsContext gc) {
        this.WIDTH = WIDTH;
        this.HEIGHT = HEIGHT;
        this.gc = gc;
    }
    
    public void reset(){
        x = 20;
        y = 20;
        mario = new Image("/images/mario.png");
    }
    
    public void refresh(){
        x += 2;
        y += 2;
        gc.clearRect(0, 0, WIDTH, HEIGHT);
        gc.drawImage(mario, x, y);
    }
    
}
