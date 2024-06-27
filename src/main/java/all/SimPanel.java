package all;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JPanel;

public class SimPanel extends JPanel implements Runnable {  

    Thread gameThread;

    final static int FPS = 60;
    final static double GRAVITY = 1000.0; // strength of gravity
    final static double DECELERATOR = 0.999; // compensates for errors
    final static boolean MERGE = false; // if particles should merge
    final static double SCALE = 100; // pixels in a meter

    public static ArrayList<Particle> particles = new ArrayList<>(); 

    Double attraction[][] = {{1.0, 1.0, 0.0, -1.0}, 
                             {1.0, 0.0, -1.0, 0.0},
                             {0.0, -1.0, 0.0, 1.0},
                             {-1.0, 0.0, 1.0, 0.0}};

    public SimPanel() {
        //panel settings
        this.setSize(Config.WIDTH, Config.HEIGHT);
        this.setBackground(new Color(40, 0, 40));
        this.setLocation(20, 20);
        this.setLayout(null);
        this.setFocusable(true);
        this.setDoubleBuffered(true);
        this.setVisible(true);
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {

        // calculations are in nanoseconds nextDrawTime is the next frame
        double drawInterval = 1e9 / FPS; 
        double nextDrawTime = System.nanoTime() + drawInterval;

        while (gameThread != null) {

            update();

            repaint();

            try {
                // the target time minus current time
                double remainingTime = nextDrawTime - System.nanoTime();
                
                // to avoid negatives and convert from nano to milisecods
                remainingTime = Math.max(remainingTime, 0) / 1e6;

                // wait until next draw
                Thread.sleep((long) remainingTime);

                nextDrawTime += drawInterval;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void update() {
        
        // we calculate the velocity through the forces particle have on eachother
        for (Particle first : particles) {
            double finalForceX = 0;
            double finalForceY = 0;

            for (Particle second : particles) {
                if (first != second) {
                    // the distance from the first particle to the other in pixels
                    Vector distance = new Vector(); 
                    distance.x = second.x - first.x;
                    distance.y = second.y - first.y;
                    double safeDist = Math.max(distance.length(), 1); // avoiding division by 0
                    //double scaleDist = safeDist / Config.SCALE; // final distance in meters

                    // calculate the force felt by the first (the formula can be changed)
                    double force;
                    if (safeDist < 10){
                        force = - GRAVITY * first.mass * second.mass;
                    }else {
                        force = GRAVITY * first.mass * second.mass; // Math.sqrt(scaleDist);
                    }
                    force *= attraction[first.type][second.type];
                    finalForceX += distance.x / safeDist * force;
                    finalForceY += distance.y / safeDist * force;

                    

                    // merge with combined mass and momentum vf = (m1*v1 + m2*v2) / (m1+m2)
                    if (MERGE && !(first.marked || second.marked) && (distance.length() < first.radius + second.radius)){
                        double resultingVX = (first.mass * first.vx + second.mass * second.vx) / (first.mass + second.mass);
                        double resultingVY = (first.mass * first.vy + second.mass * second.vy) / (first.mass + second.mass);
                        if (first.mass < second.mass){
                            second.vx += resultingVX;
                            second.vy += resultingVY;
                            second.mass += first.mass;
                            second.updateRadius();
                            first.marked = true;
                        } else {
                            first.vx += resultingVX;
                            first.vy += resultingVY;
                            first.mass += second.mass;
                            first.updateRadius();
                            second.marked = true;
                        }
                    }

                    
                }
            }

            // convert force into acceleration F = m * a
            double acccelerationx = finalForceX / first.mass;
            double acccelerationy = finalForceY / first.mass;
            
            // devide by fps because small time interval vf = vi + a * t
            first.vx += acccelerationx / FPS;
            first.vy += acccelerationy / FPS;

            // decelerate particle to compensate for errors
            first.vx *= DECELERATOR;
            first.vy *= DECELERATOR;

            // bounce off walls
            if (first.x < first.radius) {
                first.vx = Math.abs(first.vx);
            }
            if (Config.WIDTH - first.radius < first.x) {
                first.vx = -Math.abs(first.vx);
            }
            if (first.y < first.radius) {
                first.vy = Math.abs(first.vy);
            }
            if (Config.HEIGHT - first.radius < first.y) {
                first.vy = -Math.abs(first.vy);
            }
        } 

        // we calculate position after a frame xf = xi + v * t
        for (Particle particle : particles) {
            particle.x += particle.vx / FPS;
            particle.y += particle.vy / FPS;
        }

        // delete all marked
        for (int i=0; i<particles.size(); i++){
            if (particles.get(i).marked)
                particles.remove(i);
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        for (Particle p : particles){
            p.draw(g2d);
        }

        g2d.dispose();
    }

    // creates a specified amount of particles randomly
    public static void create(int number, int type, int mass){
        Random rand = new Random();

        for (int i=0; i<number; i++){
            int randomx = rand.nextInt(Config.WIDTH);
            int randomy = rand.nextInt(Config.HEIGHT);
            particles.add(new Particle(randomx, randomy, type, mass));
            System.out.println(type);
        }
    }
}