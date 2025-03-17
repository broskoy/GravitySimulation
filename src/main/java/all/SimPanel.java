package all;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JPanel;

public class SimPanel extends JPanel implements Runnable {

    // allocate a thread to run 
    Thread simulationThread;

    Camera camera = new Camera();
    
    // keyhandler for input
    KeyHandler keyHandler = new KeyHandler();

    // simulation features
    final static boolean MERGE = false; // if particles should merge (exclusive with collision)
    final static boolean GRAVITY = true; // if particles should have gravity
    final static boolean COLLISION = false; // if particles should collide (exclusive with merge)

    // simulatin parameters
    public final static double SCALE = MainFrame.HEIGHT * 10 / 1080; // pixels in a unit (at 1080p it is 10)
    final static int FPS = 120; // frames per second
    final static double GRAVITYSTRENGTH = 100; // strength of gravity
    final static double DECELERATOR = 0.9999; // compensates for errors
    final static double BARRIER = 50; // distance in units to the edges of the universe

    // array of particles
    public static ArrayList<Particle> particles = new ArrayList<>(); 

    Double attraction[][] = {{1.0, 1.0, 0.0, -1.0}, 
                             {1.0, 0.0, -1.0, 0.0},
                             {0.0, -1.0, 0.0, 1.0},
                             {-1.0, 0.0, 1.0, 0.0}};

    public SimPanel() {
        //panel settings
        this.setSize(MainFrame.WIDTH, MainFrame.HEIGHT);
        this.setBackground(new Color(20, 0, 20));
        this.setLocation(0, 0);
        this.addKeyListener(keyHandler);
        this.setLayout(null);
        this.setFocusable(true);
        this.setDoubleBuffered(true);
        this.setVisible(true);
    }

    public void startSimulationThread() {
        simulationThread = new Thread(this);
        simulationThread.start();
    }

    @Override
    public void run() {

        // calculations are in nanoseconds nextDrawTime is the next frame
        double drawInterval = 1e9 / FPS; 
        double nextDrawTime = System.nanoTime() + drawInterval;

        while (simulationThread != null) {

            // check for paused game
            if (!keyHandler.spacePressed) 
                updatePhysics();

            updateCamera();
            repaint();

            try {
                // the target time minus current time
                double remainingTime = nextDrawTime - System.nanoTime();
                
                // avoiding negatives and convert from nano to milisecods
                remainingTime = Math.max(remainingTime, 0) / 1e6;

                // wait until next draw
                Thread.sleep((long) remainingTime);

                nextDrawTime += drawInterval;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // moves the camera position
    private void updateCamera() {
        
        if (keyHandler.upPressed)
            camera.y -= camera.speed;
        
        if (keyHandler.downPressed)
            camera.y += camera.speed;
        
        if (keyHandler.leftPressed)
            camera.x -= camera.speed;

        if (keyHandler.rightPressed)
            camera.x += camera.speed;

        // System.out.println(keyHandler.upPressed + " " + keyHandler.downPressed+ " " + keyHandler.leftPressed+ " " + keyHandler.rightPressed);
    }

    // this method controls the mechanics of each frame
    public void updatePhysics() {
        
        if (MERGE) updateMerge();

        if (COLLISION) updateCollisions();

        if (GRAVITY) updateGravity();

        updateDeceleration();

        updateBarrierCollisons();

        updatePositions();
    }

    // this method calculates the resulting collision velocities between all particles
    // it must be one way since a call of collideParticles() computes for both
    private void updateCollisions() {
        for (int i = 0; i < particles.size() - 1; i++) {
            Particle first = particles.get(i);
            for (int j = i + 1; j < particles.size(); j++) {
                Particle second = particles.get(j);

                if (touching(first, second))
                    collideParticles(first, second);
            }
        }
    }

    private void collideParticles(Particle first, Particle second) {

    }

    // this method calculates the attraction between all particles
    // it must be two way scince a pulls be while b also pulls a
    // we calculate the velocity through the forces particle have on eachother
    private void updateGravity() {
        for (Particle first : particles) {
            double totalForceX = 0;
            double totalForceY = 0;

            for (Particle second : particles) {
                if (first != second) {
                    // the distance from the first particle to the other
                    Vector distance = new Vector(second.x - first.x, second.y - first.y);
                    double safeDistance = Math.max(distance.mag, 0.1);
                    
                    // calculate the force felt by the first (the formula can be changed)
                    double force = GRAVITYSTRENGTH * first.mass * second.mass / safeDistance;
                    // force *= attraction[first.type][second.type];
                    totalForceX += distance.x / safeDistance * force;
                    totalForceY += distance.y / safeDistance * force;
                }
            }

            // convert force into acceleration F = m * a
            double accelerationx = totalForceX / first.mass;
            double accelerationy = totalForceY / first.mass;
            
            // devide by fps because small time interval vf = vi + a * t
            first.vx += accelerationx / FPS;
            first.vy += accelerationy / FPS;   
        } 
    }

    // decelerate particle to compensate for approximation errors
    private void updateDeceleration() {
        for (Particle particle : particles){
            particle.vx *= DECELERATOR;
            particle.vy *= DECELERATOR; 
        }
    }

    // returns the distance between two particles
    private double calculateDistance(Particle first, Particle second) {
        double distanceX = second.x - first.x;
        double distanceY = second.y - first.y;
        double distance = Math.sqrt(distanceX * distanceX + distanceY * distanceY);

        return distance;
    }

    // merge particles with combined mass and momentum vf = (m1*v1 + m2*v2) / (m1+m2)
    private void updateMerge() {
        ArrayList<Particle> particlesToAdd = new ArrayList<>();

        for (Particle first : particles) {
            for (Particle second : particles) {
                
                double distance = calculateDistance(first, second);

                if (!(first.marked || second.marked) && (distance < first.radius + second.radius)){
                    particlesToAdd.add(mergeParticles(first, second));
                }
            }
        }

        // move them from one to the other
        for (Particle particle : particlesToAdd)
            particles.add(particle);

        deleteMarked();
    }

    private Particle mergeParticles(Particle first, Particle second) {
        Particle result = new Particle();

        result.vx = (first.mass * first.vx + second.mass * second.vx) / (first.mass + second.mass);
        result.vy = (first.mass * first.vy + second.mass * second.vy) / (first.mass + second.mass);
        result.changeMass(first.mass + second.mass);
        result.x = (first.x + second.x) / 2;
        result.y = (first.y + second.y) / 2;
        result.type = first.type; // TODO: think abot this

        first.marked = true;
        second.marked = true;

        return result;
    }

    // we calculate position after a frame xf = xi + v * t
    private void updatePositions() {
        for (Particle particle : particles) {
            particle.x += particle.vx / FPS;
            particle.y += particle.vy / FPS;
        }
    }

    // change the velocities to bounce off barrier
    private void updateBarrierCollisons() {
        for (Particle particle : particles){
            // left side
            if (particle.x - particle.radius < - BARRIER )
                particle.vx = Math.abs(particle.vx);
            
            // right side
            if (BARRIER  < particle.x + particle.radius)
                particle.vx = -Math.abs(particle.vx);

            // bottom side
            if (particle.y - particle.radius < - BARRIER )
                particle.vy = Math.abs(particle.vy);

            // top side
            if (BARRIER < particle.y + particle.radius)
                particle.vy = -Math.abs(particle.vy);
        }
    }

    private boolean touching(Particle first, Particle second) {
        double distance = calculateDistance(first, second);

        return distance < first.radius + second.radius;
    }

    // delete all marked
    private void deleteMarked() {
        for (int i=0; i<particles.size(); i++){
            if (particles.get(i).marked)
                particles.remove(i);
        }
    }

    // draw the border of the universe
    private void drawBorder(Graphics2D g2d) {
        // the coordinates of the the bounding box
        double drawx = -BARRIER;
        double drawy = -BARRIER;
        double drawlen = 2 * BARRIER;

        // compensate for scale
        drawx *= SCALE;
        drawy *= SCALE;
        drawlen *= SCALE;

        // compensate for screen center
        drawx += MainFrame.WIDTH / 2;
        drawy += MainFrame.HEIGHT / 2;

        // compensate for camera
        drawx -= camera.x * SCALE;
        drawy -= camera.y * SCALE;

        g2d.setColor(Color.white);
        g2d.drawRect((int)drawx, (int)drawy, (int)drawlen, (int)drawlen);
    }

    // draw all of the particles
    private void drawParticles(Graphics2D g2d) {
        for (Particle particle : particles) {
            // the coordinates of the the bounding box
            double drawx = particle.x - particle.radius;
            double drawy = particle.y - particle.radius;
            double drawrad = 2 * particle.radius;

            // compensate for scale
            drawx *= SCALE;
            drawy *= SCALE;
            drawrad *= SCALE;

            // compensate for screen center
            drawx += MainFrame.WIDTH / 2;
            drawy += MainFrame.HEIGHT / 2;

            // compensate for camera
            drawx -= camera.x * SCALE;
            drawy -= camera.y * SCALE;

            switch (particle.type) {
                case 0 -> g2d.setColor(Color.red);
                case 1 -> g2d.setColor(Color.cyan);
                case 2 -> g2d.setColor(Color.green);
                case 3 -> g2d.setColor(Color.orange);
                default -> System.out.println("big fuk");
            }

            g2d.fillOval((int)drawx, (int)drawy, (int)drawrad, (int)drawrad);
        }
    }

    // draws the white refrence of a unit in the corner
    private void drawReference(Graphics2D g2d) {
        // coordinates relative to frame
        for (int i=0; i<10; i++) {

            int bufferX = MainFrame.WIDTH / 96;
            int bufferY = MainFrame.HEIGHT / 20;
            int fromX = bufferX+ i * (int)SCALE;
            int fromY = MainFrame.HEIGHT - bufferY;
            int toX = fromX + (int)SCALE;
            int toY = fromY;

            // draw the lines
            if (i % 2 == 0) g2d.setColor(new Color(255, 255, 255, 255));
            else g2d.setColor(new Color(255, 255, 255, 128));

            g2d.drawLine(fromX, fromY, toX, toY);
        }
    }

    // the primary paint method called each frame
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        drawParticles(g2d);
        drawBorder(g2d);
        drawReference(g2d);

        g2d.dispose();
    }

    // creates a specified amount of particles with random positions
    public static void create(int number, int type, int mass){
        Random rand = new Random();

        for (int i=0; i<number; i++){
            Particle particle = new Particle();

            particle.x = rand.nextDouble(-BARRIER / 2, BARRIER / 2);
            particle.y = rand.nextDouble(-BARRIER / 2, BARRIER / 2);
            particle.vx = rand.nextDouble(-10.0, 10.0);
            particle.vy = rand.nextDouble(-10.0, 10.0);
            particle.type = type;
            particle.changeMass(mass);

            particles.add(particle);
        }
    }
}