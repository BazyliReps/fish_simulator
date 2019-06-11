package fish_stuff;


import vectors.MyVector;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author bazyli
 */
public class Fin {

    MyVector origin;
    double lenght = 25;
    double startAngle;
    double deltaAngle;
    double maxAngle = 20;
    int directionMultiplier = -1;
    boolean isLeft = false;
    
    
    
    
    Color finColor;
    
    int xSize;
    int ySize;

    public Fin(MyVector origin, double angle, Color finColor, int size) {
        this.origin = origin;
        this.startAngle = angle;
        this.finColor = finColor;
        this.xSize = (int) (size/2.5);
        this.ySize = xSize/3;
    }

    public void step(double angleVelocity) {

        deltaAngle += angleVelocity * directionMultiplier;
        if (deltaAngle > maxAngle || deltaAngle < -maxAngle) {
            if(deltaAngle > maxAngle)
                deltaAngle = maxAngle;
            if(deltaAngle < - maxAngle)
                deltaAngle = -maxAngle;
            directionMultiplier *= -1;
            
        }
    }

    public void display(Graphics2D g, int alpha) {
        
       
        AffineTransform a = g.getTransform();
        g.rotate(Math.toRadians(startAngle + deltaAngle ), (int)origin.x - (isLeft ? 0 : 0), origin.y);
        g.setColor(alpha == 0 ? finColor : new Color(finColor.getRed(),finColor.getGreen(),finColor.getBlue(), alpha));
        g.fillOval((int)origin.x - (isLeft ? xSize : 0), (int)origin.y, xSize , ySize);
        g.setTransform(a);

    }

}
