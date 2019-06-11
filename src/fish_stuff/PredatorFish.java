package fish_stuff;


import vectors.MyVector;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;

public class PredatorFish extends Fish {

    private int targetsInSight;
    private long timeOfLock;

    private final double cruiseSpeed = 8;
    private double chaseSpeed;

    private boolean isEating = false;

    public PredatorFish(int width, int height, int x, int y, float size) {
        super(width, height, x, y, size);
        this.chaseSpeed = 10;
        isPredator = true;
        distanceOfView = 300;
        this.bodycolor = Color.green;
        ySize = xSize / 6;

        sideFins.fins[0].startAngle = 140;
        sideFins.fins[1].startAngle = 40;

    }

    @Override
    public void step() {

        if (isEating) {
            acceleration.multiply(0);
            if (System.currentTimeMillis() - timeOfLock > 3000) {
                isEating = false;

            }

        } else {

            if (targetsInSight < 4 && targetsInSight > 0 && !isLocked && !targetMover.isPredator) {
                isLocked = true;
                timeOfLock = System.currentTimeMillis();
            }

            if (isLocked) {
                this.applyForce(calculateAttractionForPrey(targetMover));
                distance = MyVector.subtract(this.position, targetMover.position).magnitude();
                if (distance < 30) {
                    isLocked = false;
                    targetMover.isDying = true;
                    velocity.normalizeThis();
                    acceleration.multiply(0);
                    isEating = true;
                    timeOfLock = System.currentTimeMillis();
                    targetMover.velocity.x = this.velocity.x;
                    targetMover.velocity.y = this.velocity.y;
                }
                if (System.currentTimeMillis() - timeOfLock > 3000) {
                    isLocked = false;
                }
                if (distance > distanceOfView) {
                    isLocked = false;
                }

            }

        }

        acceleration.divide(xSize);
        velocity.add(acceleration);

        velocity.limit(isLocked ? chaseSpeed : cruiseSpeed);

        position.add(velocity);

        sideFins.step(new MyVector(position.x + xSize / 4, position.y), velocity.magnitude() * 1);
        tailFins.step(new MyVector(position.x - xSize / 2.5, position.y), velocity.magnitude() * 1);

        acceleration.multiply(0);
        targetsInSight = 0;
    }

    @Override
    public void display(Graphics gg) {
        double angle = Math.atan2(velocity.y, velocity.x);

        Graphics2D g = (Graphics2D) gg;

        AffineTransform saveAT = g.getTransform();

        if (isLocked) {
            //g.drawLine((int) targetMover.position.x, (int) targetMover.position.y, (int) position.x, (int) position.y);
        }

        g.rotate(angle, position.x, position.y);

        sideFins.display(g, 0);
        tailFins.display(g, 0);

        //lewe oko
        g.setColor(Color.white);
        g.fillOval((int) position.x + xSize / 3, (int) ((int) position.y - ySize / 1.8), ySize / 2, ySize / 2);
        g.setColor(Color.black);
        g.drawOval((int) position.x + xSize / 3, (int) ((int) position.y - ySize / 1.8), ySize / 2, ySize / 2);
        //zrenica
        g.fillOval((int) ((int) position.x + xSize / 2.7), (int) ((int) position.y - ySize / 1.8), ySize / 10, ySize / 10);

        //prawe oko
        g.setColor(Color.white);
        g.fillOval((int) position.x + xSize / 3, (int) position.y + ySize / 10, ySize / 2, ySize / 2);
        g.setColor(Color.black);
        g.drawOval((int) position.x + xSize / 3, (int) position.y + ySize / 10, ySize / 2, ySize / 2);
        //zrenica
        g.fillOval((int) ((int) position.x + xSize / 2.7), (int) ((int) position.y + ySize / 2), ySize / 10, ySize / 10);

        //cialo
        g.setColor(isDead ? Color.white : isPredator ? predatorColor : bodycolor);
        g.fillOval((int) position.x - xSize / 2, (int) position.y - ySize / 2, xSize, ySize);

        g.setColor(Color.black);
        g.setStroke(new BasicStroke(1));
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.drawOval((int) position.x - xSize / 2, (int) position.y - ySize / 2, xSize, ySize);

        g.setTransform(saveAT);
    }

    @Override
    public MyVector calculateAttraction(Fish m) {
        if (isLocked) {
            return new MyVector(0, 0);
        }

        MyVector force = MyVector.subtract(this.position, m.position);
        distance = force.magnitude();

        strenght = this.coeficientOfAttraction / (distance * 0.2);
        force = force.normalize();
        force.multiply(strenght);

        force.multiply(m.isPredator ? - 1000 : 160);
        targetsInSight++;
        targetMover = m;
        return force;
    }

    public MyVector calculateAttractionForPrey(Fish m) {
        MyVector force = MyVector.subtract(this.position, m.position);
        distance = force.magnitude();

        strenght = coeficientOfAttraction / (distance * 0.2);
        force = force.normalize();
        force.multiply(strenght);

        force.multiply(2000);
        return force;
    }

    @Override
    public boolean checkIfInSight(Fish m) {

        if (isLocked || m.isDead) {
            return false;
        } else {

            MyVector direction = MyVector.subtract(m.position, this.position);
            double distance = direction.magnitude();
            if (distance <= this.distanceOfView) {
                double angleOfVelocity = Math.atan(velocity.y / velocity.x);
                double angleOfDirection = Math.atan(direction.y / direction.x);
                if ((angleOfDirection <= (angleOfVelocity + angleOfView)) && (angleOfDirection >= (angleOfVelocity - angleOfView))) {
                    return true;
                }
            }
        }
        return false;

    }

}
