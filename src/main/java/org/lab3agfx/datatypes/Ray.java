package org.lab3agfx.datatypes;

public class Ray {

    private Vec3 origin;
    private Vec3 direction;

    public Ray(Vec3 origin, Vec3 direction) {
        this.origin = origin;
        this.direction = direction;
    }

    // P(t) = A + tb
    // A - origin
    // b - direction vector
    // t - parameter, parts in front of A
    public Vec3 at (double t){
        return origin.add(direction.scale(t));
    }


    public Vec3 getOrigin() {
        return origin;
    }

    public Vec3 getDirection() {
        return direction;
    }
}
