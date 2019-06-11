package window_stuff;


import vectors.MyVector;
import fish_stuff.PredatorFish;
import fish_stuff.Fish;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;


public class Loop extends JPanel implements Runnable {

    private int width, height;

    private List<Fish> movers = new ArrayList<>();

    int numberOfMovers = 350;
    int numberOfPredators = 4;
    double base_fish_mass = 40;
    int distanceOfView = 200;

    private boolean running = false;

    Color color = new Color(30, 90, 197);

    Random randomizer = new Random();

    private Camera cam;
    private int camSpeed = 10;

    public Loop(int width, int height) {
        this.cam = new Camera(0, 0);
        this.width = width;
        this.height = height - 30;
        this.setPreferredSize(new Dimension(width, height));
        this.setBackground(color);
        for (int i = 0; i < numberOfMovers; i++) {
            if (numberOfPredators-- > 0) {
                movers.add(new PredatorFish(width, height, randomizer.nextInt(width), randomizer.nextInt(height), (float) ((float) 2 * base_fish_mass)));
            } else {
                movers.add(new Fish(width, height, randomizer.nextInt(width), randomizer.nextInt(height), (float) base_fish_mass));
            }
        }
        KeyAdapter keyboard = new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                if (key == KeyEvent.VK_D) {
                    cam.setVelX(-camSpeed);
                }

                if (key == KeyEvent.VK_A) {
                    cam.setVelX(camSpeed);
                }

                if (key == KeyEvent.VK_W) {
                    cam.setVelY(camSpeed);
                }

                if (key == KeyEvent.VK_S) {
                    cam.setVelY(-camSpeed);
                }

                if (key == KeyEvent.VK_ESCAPE) {
                    System.exit(0);
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                int key = e.getKeyCode();
                if (key == KeyEvent.VK_D) {
                    cam.setVelX(0);
                }

                if (key == KeyEvent.VK_A) {
                    cam.setVelX(0);
                }

                if (key == KeyEvent.VK_W) {
                    cam.setVelY(0);
                }

                if (key == KeyEvent.VK_S) {
                    cam.setVelY(0);
                }

            }

        };
        this.addKeyListener(keyboard);
        this.requestFocus();

    }

    @Override
    public void run() {

        while (running) {

            try {
                TimeUnit.MILLISECONDS.sleep(15);
            } catch (InterruptedException ex) {
                Logger.getLogger(Loop.class.getName()).log(Level.SEVERE, null, ex);
            }
            for (int i = 0; i < movers.size(); i++) {
                Fish m1 = movers.get(i);
                for (int j = 0; j < movers.size(); j++) {
                    if (i != j && m1.checkIfInSight(movers.get(j)) ) {
                        MyVector force = m1.calculateAttraction(movers.get(j));
                        m1.applyForce(force);
                    }
                }

                m1.step();
                m1.limitVelocity(movers.get(i).isPredator ? 8 : 2);
                m1.checkEdges();
                if (m1.isDead) {
                    movers.remove(m1);
                }

            }

        }
    }

    public void start() {
        if (running) {
            return;
        }
        running = true;
        new Thread(this).start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.GREEN);
        for (int i = 0; i < movers.size(); i++) {
            movers.get(i).display(g);
        }
        //drawGrid(g);
    }
    
    private void drawGrid(Graphics g) {
        for (int i = 0; i < width; i += distanceOfView) {
            g.drawLine(i, 0, i, height);
        }
        for (int i = 0; i < width; i += distanceOfView) {
            g.drawLine(0, i, width, i);
        }

    }
}
