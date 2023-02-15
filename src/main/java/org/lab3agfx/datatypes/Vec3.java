package org.lab3agfx.datatypes;

public class Vec3 {

    private Double x;
    private Double y;
    private Double z;

    public Vec3(){
        this.x = 0.0;
        this.y = 0.0;
        this.z = 0.0;
    }

    public Vec3(Double x, Double y, Double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vec3 subtract(Vec3 other){
        return new Vec3(this.x - other.x, this.y - other.y, this.z - other.z);
    }

    public Vec3 add(Vec3 other){
        return new Vec3(this.x + other.x, this.y + other.y, this.z + other.z);
    }

    public Vec3 scale(double scalar){
        return new Vec3(this.x * scalar, this.y * scalar, this.z * scalar);
    }

    public Vec3 divide(double scalar){
        return new Vec3(this.x / scalar, this.y / scalar, this.z / scalar);
    }

    public Vec3 multiply(Vec3 other){
        return new Vec3(this.x * other.x, this.y * other.y, this.z * other.z);
    }

    public double length(){
        return Math.sqrt(x*x + y*y + z*z);
    }

    // formula : vector / length of vector || vector * (1 / length)
    public Vec3 unitVector(){
        return this.scale(1/this.length());
    }

    public Vec3 invertVertically(){
        return new Vec3(this.x,-this.y,this.z);
    }

    public static Vec3 cross (Vec3 left, Vec3 right){
        // x: Y * Z  -  Z * Y
        // y: Z * X  -  X * Z
        // z: X * Y  -  Y * X
        return new Vec3(left.y * right.z - left.z * right.y,
                        left.z * right.x - left.x * right.z,
                        left.x * right.y - left.y * right.x);
    }

    public static double dot (Vec3 left, Vec3 right){
        return left.x * right.x + left.y * right.y + left.z * right.z;
    }

    public static Vec3 negate(Vec3 toNegate){
        return new Vec3(-toNegate.x, -toNegate.y, -toNegate.z);
    }


    public Double getX() {
        return x;
    }

    public Double getY() {
        return y;
    }

    public Double getZ() {
        return z;
    }

    @Override
    public String toString() {
        return "Vec3{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }


}
