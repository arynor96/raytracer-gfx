package org.lab3agfx.datatypes;

public class Color {
    private double r;
    private double g;
    private double b;

    public Color(double r, double g, double b) {
        this.r = r;
        this.g = g;
        this.b = b;

        if (r < 0 || g < 0 || b < 0) {
            if (r < 0) this.r = 0;
            if (g < 0) this.g = 0;
            if (b < 0) this.b = 0;
        }

        if (r > 1 || g > 1 || b > 1) {
            if (r > 1) this.r = 1;
            if (g > 1) this.g = 1;
            if (b > 1) this.b = 1;
        }
    }

    // https://www.tutorialspoint.com/how-to-get-pixels-rgb-values-of-an-image-using-java-opencv-library
    public Color multiply(Color other){
        return new Color(this.r * other.r, this.g * other.g, this.b * other.b);
    }

    public Color scale(double scalar){
        return new Color(this.r * scalar, this.g * scalar, this.b * scalar);
    }

    public Color add(Color other){
        return new Color(this.r + other.r, this.g + other.g, this.b + other.b);
    }

    public int getRGB(){

        int ir = (int) (255.999 * r);
        int ig = (int) (255.999 * g);
        int ib = (int) (255.999 * b);

        // I use the Color class from awt java library to get the RGB int code.
        java.awt.Color temp = new java.awt.Color(ir,ig,ib);
        return temp.getRGB();
    }



    public double getR() {
        return r;
    }

    public double getG() {
        return g;
    }

    public double getB() {
        return b;
    }

    @Override
    public String toString() {
        return "Color{" +
                "r=" + r +
                ", g=" + g +
                ", b=" + b +
                '}';
    }
}
