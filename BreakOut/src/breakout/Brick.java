/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package breakout;

/**
 *
 * @author Loic
 */
public class Brick {
    
    private int x;
    private int y;
    private int damages;
    private boolean destroyed = false;

    public Brick(int x, int y, int damages) {
        this.x = x;
        this.y = y;
        this.damages = damages;
    }
    
    public void collide(float ballX, float ballY) {
        
    }
    
    // GETTERS
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
    
    public int getDamages() {
        return damages;
    }
    
    public boolean isDestroyed() {
        return destroyed;
    }
    
    
    // SETTERS
    public void setX(int x) {
        this.x = x;
    }
    
    public void setY(int y) {
        this.y = y;
    }

    public void setDamages(int damages) {
        this.damages = damages;
    }

    public void setDestroyed(boolean destroyed) {
        this.destroyed = destroyed;
    }

    
}
