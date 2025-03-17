package all;

import java.awt.Color;
import java.awt.Graphics2D;

public class Particle {
    int type;
    double x;
    double y;
    double vx;
    double vy;
    double mass;
    double radius;
    boolean marked;

    public Particle(){
        this.type = 0;
        this.x = 0;
        this.y = 0;
        this.vx = 0;
        this.vy = 0;
        this.mass = 1;
        this.radius = Math.sqrt(mass);
        this.marked = false;
    }

    public void draw(Graphics2D g2d, int cameraPosX, int cameraPosY) {
        int drawx = (int)(x - radius * SimPanel.SCALE);
        int drawy = (int)(y - radius * SimPanel.SCALE);
        int drawrad = (int)(2*radius * SimPanel.SCALE);

        // compensate for camera
        drawx -= cameraPosX;
        drawy -= cameraPosY;

        switch (type) {
            case 0 -> g2d.setColor(Color.red);
            case 1 -> g2d.setColor(Color.cyan);
            case 2 -> g2d.setColor(Color.green);
            case 3 -> g2d.setColor(Color.orange);
            default -> System.out.println("big fuk");
        }
        g2d.fillOval(drawx, drawy, drawrad, drawrad);
    }

    public void changeType(int newType){
        type = newType;
    }

    public void changePosition(double newX, double newY) {
        x = newX;
        y = newY;
    }

    public void changeVelocity(double newVX, double newVY) {
        vx = newVX;
        vy = newVY;
    }

    public void changeMass(double newMass) {
        mass = newMass;
        radius = Math.sqrt(mass);
    }

    public void mark() {
        marked = true;
    }
}
