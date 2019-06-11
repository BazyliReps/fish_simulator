package fish_stuff;


import vectors.MyVector;
import java.awt.Color;
import java.awt.Graphics2D;

public class Fins {

    MyVector position;

    Fin[] fins;

    public Fins(MyVector position, Color finColor, int size) {
        this.position = position;
        fins = new Fin[2];
        fins[0] = new Fin(position, 140, finColor, size);
        fins[1] = new Fin(position, 40, finColor, size);
        fins[1].directionMultiplier *= -1;
        fins[1].isLeft = true;

    }

    public void step(MyVector position, double angleVelocity) {

        for (int i = 0; i < 2; i++) {
            fins[i].origin = position;
            fins[i].step(angleVelocity);
        }
    }

    public void display(Graphics2D g, int alpha) {
        for (int i = 0; i < 2; i++) {
            fins[i].display(g, alpha);
        }
    }
    
    void setTail() {
        fins[0].startAngle = 160;
        fins[1].startAngle = 20;
        fins[0].maxAngle = 10;
        fins[1].maxAngle = 10;
        
    }

}
