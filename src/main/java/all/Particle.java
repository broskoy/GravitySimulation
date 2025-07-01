package all;

public class Particle {
    int type;
    double x;
    double y;
    double vx;
    double vy;
    double mass;
    double radius;
    int chunkX;
    int chunkY;
    boolean marked;

    public Particle(){
        this.type = 0;
        this.x = 0;
        this.y = 0;
        this.vx = 0;
        this.vy = 0;
        this.mass = 1;
        this.radius = 0.5;
        this.chunkX = 0;
        this.chunkY = 0;
        this.marked = false;
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
        radius = Math.sqrt(Math.abs(mass)) / 2; // divided by 2 so the diameter is represantive of the mass (formula can be adjusted to preference)
    }

    public void changeChunk(int newX, int newY) {
        chunkX = newX;
        chunkY = newY;
    }

    public void mark() {
        marked = true;
    }
}
