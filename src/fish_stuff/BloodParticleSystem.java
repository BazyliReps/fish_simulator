package fish_stuff;


import vectors.MyVector;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;


public class BloodParticleSystem {

    private final MyVector origin;
    private final List<BloodParticle> particles;

    private int counter;

    private Random rand = new Random();

    int numberOfParticles = 150;

    boolean isEmpty = false;

    public BloodParticleSystem(MyVector origin) {
        this.origin = origin;
        particles = new ArrayList<>();
    }

    public void addParticle(MyVector v) {
        MyVector velocity = new MyVector(v.x - 1 + 2 * rand.nextDouble(), v.y - 1 + 2 * rand.nextDouble());
        particles.add(new BloodParticle(origin, velocity));
    }

    public void run(MyVector velocity) {

        counter++;
        if (counter == 1 && numberOfParticles != 0) {
            addParticle(velocity);
            numberOfParticles--;
            counter = 0;
        }
        Iterator<BloodParticle> iterator = particles.iterator();

        while (iterator.hasNext()) {
            BloodParticle p = iterator.next();
            p.step();
            if (p.isDead()) {
                iterator.remove();
            }
        }
        if (particles.isEmpty() && numberOfParticles == 0) {
            isEmpty = true;
        }
    }

    public void display(Graphics g) {
        for (int i = 0; i < particles.size(); i++) {
            BloodParticle m = particles.get(i);
            if (m != null) {
                m.display(g);
            }
        }
    }

}
