package fish_stuff;

import vectors.MyVector;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.util.Random;


public class Fish {

    public MyVector position;
    public MyVector velocity;
    public MyVector acceleration;

    protected double distance;
    protected double strenght;
    public double coeficientOfAttraction = 10;

    protected int angleOfView = 60;
    protected int distanceOfView = 200;         //100

    protected int height;
    protected int width;

    public int xSize;
    public int ySize;

    public boolean isPredator = false;
    public boolean isDead = false;
    public boolean isDying = false;
    public boolean isLocked = false;

    private boolean isFleeing = false;

    BloodParticleSystem blood = null;

    Fins sideFins;
    Fins tailFins;

    double angle = 0;

    Color bodycolor = new Color(150, 150, 150);

    Color predatorColor = new Color(20, 130, 10);

    int alpha;

    Color finColor = bodycolor;

    Random rand = new Random();

    Fish targetMover;

    protected MyVector position2 = new MyVector(0, 0);

    public Fish(int width, int height, int x, int y, float size) {

        //random start velocity
        double vx = rand.nextDouble() * 5 * (rand.nextBoolean() ? 1 : -1);
        double vy = rand.nextDouble() * 5 * (rand.nextBoolean() ? 1 : -1);

        position = new MyVector(x, y);
        velocity = new MyVector(vx, vy);
        acceleration = new MyVector(0, 0);

        sideFins = new Fins(new MyVector(position.x - xSize / 2, position.y - ySize / 2), finColor, (int) size);
        tailFins = new Fins(new MyVector(position.x - xSize / 2, position.y - ySize / 2), finColor, (int) size);
        tailFins.setTail();

        this.width = width;
        this.height = height;
        this.xSize = (int) (size);
        this.ySize = xSize / 3;
    }

    public void step() {

        acceleration.divide(xSize);

        if (isDying) {
            if (blood == null) {
                blood = new BloodParticleSystem(position);
            }
            blood.run(velocity);
            isDead = blood.isEmpty;
            acceleration.multiply(0);
        }

        velocity.add(acceleration);
        velocity.limit(isFleeing ? 7 : isDying ? 1 : 7);
        position.add(velocity);

        sideFins.step(new MyVector(position.x + xSize / 4, position.y), velocity.magnitude());
        tailFins.step(new MyVector(position.x - xSize / 2.5, position.y), velocity.magnitude());
        acceleration.multiply(0);
        isFleeing = false;

    }
    //przechodzi przz sciany
    public void checkEdges() {
        boolean needToChange = false;
        if (position.x > width) {
            position.x = 0;
            needToChange = true;
        } else if (position.x < 0) {
            position.x = width;
            needToChange = true;
        }
        if (position.y > height) {
            position.y = 0;
            needToChange = true;
        } else if (position.y < 0) {
            position.y = height;
            needToChange = true;
        }

        if (needToChange) {
            sideFins.step(new MyVector(position.x + xSize / 4, position.y), velocity.magnitude() * 1);
            tailFins.step(new MyVector(position.x - xSize / 2.5, position.y), velocity.magnitude() * 1);
        }

    }
    
    
    public void display(Graphics gg) {

        double angle = Math.atan2(velocity.y, velocity.x);

        Graphics2D g = (Graphics2D) gg;

        if (isDying && blood != null) {
            blood.display(gg);
            alpha = blood.numberOfParticles;
            if(alpha == 0)
                alpha = 1;
        }

        AffineTransform saveAT = g.getTransform();
        g.rotate(angle, position.x, position.y);

        /*
        //pole widzenia
        g.drawLine((int) position.x, (int) position.y, (int) (position.x + distanceOfView * Math.cos(Math.toRadians(angle))), (int) (position.y + distanceOfView * Math.sin(Math.toRadians(angle))));
        g.drawLine((int) position.x, (int) position.y, (int) (position.x + distanceOfView * Math.cos(Math.toRadians(angle))), (int) (position.y + distanceOfView * Math.sin(Math.toRadians(angle + angleOfView))));
        g.drawLine((int) position.x, (int) position.y, (int) (position.x + distanceOfView * Math.cos(Math.toRadians(angle))), (int) (position.y + distanceOfView * Math.sin(Math.toRadians(angle - angleOfView))));
         */
        //lewe oko
        g.setColor(isDying ? new Color(255, 255, 255, alpha) : Color.white);
        g.fillOval((int) position.x + xSize / 3, (int) ((int) position.y - ySize / 1.8), ySize / 2, ySize / 2);
        g.setColor(isDying ? new Color(0, 0, 0, alpha) : Color.black);
        g.drawOval((int) position.x + xSize / 3, (int) ((int) position.y - ySize / 1.8), ySize / 2, ySize / 2);
        //zrenica
        g.fillOval((int) ((int) position.x + xSize / 2.7), (int) ((int) position.y - ySize / 1.8), ySize / 10, ySize / 10);

        //prawe oko
        g.setColor(isDying ? new Color(255, 255, 255, alpha) : Color.white);
        g.fillOval((int) position.x + xSize / 3, (int) position.y + ySize / 10, ySize / 2, ySize / 2);
        g.setColor(isDying ? new Color(0, 0, 0, alpha) : Color.black);
        g.drawOval((int) position.x + xSize / 3, (int) position.y + ySize / 10, ySize / 2, ySize / 2);
        //zrenica
        g.fillOval((int) ((int) position.x + xSize / 2.7), (int) ((int) position.y + ySize / 2), ySize / 10, ySize / 10);

        //cialo
        g.setColor(isDying ? new Color(150, 150, 150, alpha) : isPredator ? predatorColor : bodycolor);
        g.fillOval((int) position.x - xSize / 2, (int) position.y - ySize / 2, xSize, ySize);

        g.setColor(isDying ? new Color(0, 0, 0, alpha) : Color.black);
        g.setStroke(new BasicStroke(1));
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.drawOval((int) position.x - xSize / 2, (int) position.y - ySize / 2, xSize, ySize);
        
        sideFins.display(g, alpha);
        tailFins.display(g, alpha);

        g.setTransform(saveAT);
    }

    public void setAcceleration(double x, double y) {
        acceleration.x = x;
        acceleration.y = y;
    }

    public void setAcceleration(MyVector v) {
        this.acceleration = v;
    }

    public void applyForce(MyVector force) {
        acceleration.add(force);
    }

    public double getPosX() {
        return position.x;
    }

    public double getPosY() {
        return position.y;
    }

    public void limitVelocity(double limit) {
        velocity.limit(limit);
    }

    public float getArea() {
        return (float) (Math.PI * Math.pow(xSize / 2, 2));
    }

    public MyVector calculateAttraction(Fish m) {
        MyVector force = MyVector.subtract(this.position, m.position);
        distance = force.magnitude();
        if (distance < 100 && !m.isPredator) {
            return m.velocity;
        }
        strenght = coeficientOfAttraction / (distance * 0.2);
        force = force.normalize();
        force.multiply(strenght);
        if (m.isPredator) {
            force.multiply(-500);
            isFleeing = true;
        }

        return force;
    }

    public boolean checkIfInSight(Fish m) {

        MyVector direction = MyVector.subtract(m.position, this.position);
        double distance = direction.magnitude();
        if (distance <= distanceOfView) {
            double angleOfVelocity = Math.atan(velocity.y / velocity.x);
            double angleOfDirection = Math.atan(direction.y / direction.x);
            if ((angleOfDirection <= (angleOfVelocity + angleOfView)) && (angleOfDirection >= (angleOfVelocity - angleOfView))) {
                return true;
            }
        }
        return false;

    }

}
