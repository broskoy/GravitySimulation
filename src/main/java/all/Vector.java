package all;

// this class is used when you need a vector
public class Vector {
    double x;
    double y;

    public Vector() {
        // initialize for safety
        x = 0;
        y = 0;
    }

    public double length() {
        return (x*x + y*y);
    }
}
