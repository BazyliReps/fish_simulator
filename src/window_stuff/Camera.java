package window_stuff;


import java.io.Serializable;

public class Camera implements Serializable {

    public static int x = 0, y = 0;
    private int velX, velY;
 

    public Camera(int x, int y) {
        this.x = x;
        this.y = y;
        this.velX = 0;
        this.velY = 0;
        
    }

    public Camera() {
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getVelY() {
        return velY;
    }

    public void setCamX(int x) {
        this.x = x;
    }

    public void setCamY(int y) {
        this.y = y;
    }

    public void setVelY(int velY) {
        this.velY = velY;
    }

    public void setVelX(int velX) {
        this.velX = velX;
    }

    public void resetCam() {
        setCamX(0);
        setCamY(0);
    }

    public void update() {

        if(velX > 2)
            velX = 2;
        if(velY > 2)
            velY = 2;
        x += velX;
        y += velY;
    }
}
