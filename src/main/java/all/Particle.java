package all;

import java.awt.Color;
import java.awt.Graphics2D;
import java.lang.Math;

public class Particle {
    double x;
    double y;
    double vx;
    double vy;
    int type;
    double mass;
    double radius;
    boolean marked;

    public Particle(double x, double y, int type, double mass){
        this.x = x;
        this.y = y;
        this.vx = 0;
        this.vy = 0;
        this.type = type;
        this.mass = mass;
        this.radius = Math.sqrt(mass);
        this.marked = false;
    }

    public void draw(Graphics2D g2d) {
        int drawx = (int)(x - radius);
        int drawy = (int)(y - radius);
        int drawrad = (int)(2*radius);
        switch (type) {
            case 0: g2d.setColor(Color.red); break;
            case 1: g2d.setColor(Color.cyan); break;
            case 2: g2d.setColor(Color.green); break;
            case 3: g2d.setColor(Color.orange); break;
            default: System.out.println("big fuk"); break;
        }
        g2d.fillOval(drawx, drawy, drawrad, drawrad);
    }

    public void updateRadius() {
        radius = Math.sqrt(mass);
    }
}
