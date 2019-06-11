package fish_stuff;


import vectors.MyVector;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;


public class BloodParticle {

    public MyVector position;
    public MyVector velocity;
    public MyVector acceleration;

    private int height;
    private int width;
    private final float mass;

    public int size;

    private int timeToLive;
    private boolean isDead = false;

    public BloodParticle(MyVector position, MyVector velocity) {
        this.position = new MyVector(position.x, position.y);
        this.velocity = new MyVector(velocity.x, velocity.y);
        acceleration = new MyVector(0, 0);

        this.size = 6;
        this.mass = size;
        this.timeToLive = 255;
    }

    public void step() {

        acceleration.divide(mass);
        velocity.add(acceleration);
        position.add(velocity);
        acceleration.multiply(0);
        timeToLive -= 8;
        if (timeToLive < 8) {
            isDead = true;
        }

    }

    public void checkEdges() {
        if (position.x > width - size) {
            position.x = width - size;
            velocity.x *= (-1);
        } else if (position.x < 0) {
            position.x = 0;
            velocity.x *= (-1);
        }
        if (position.y > height - size) {
            position.y = height - size;
            velocity.y *= (-1);
        } else if (position.y < 0 + size) {
            position.y = size;
            velocity.y *= (-1);
        }

    }

    public void display(Graphics gg) {

        Graphics2D g = (Graphics2D) gg;

        g.setColor(new Color(255, 255, 255, timeToLive));
        g.setColor(new Color(150, 30, 20, timeToLive));
        g.fillOval((int) position.x, (int) position.y, size, size);
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

    public float getMass() {
        return mass;
    }

    public float getArea() {
        return (float) (Math.PI * Math.pow(size / 2, 2));
    }

    public boolean isDead() {
        return isDead;
    }

}
