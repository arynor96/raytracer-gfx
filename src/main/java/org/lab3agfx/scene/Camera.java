package org.lab3agfx.scene;

import org.lab3agfx.datatypes.Vec3;

public class Camera {

    private int width = 600;
    private int height = 600;

    private int bounces = 1;

    private double view_height = 2.0;
    private double view_width = 2.0;
    private double focal_length = 1.0;
    private double halfFov = 45.0;

    private Vec3 position = new Vec3(0.0,0.0,0.0);


    private Vec3 lookat = new Vec3(0.0,0.0,-1.0);
    private Vec3 up = new Vec3(0.0,1.0,0.0);


    //w u v - got the idea from 11.2 RayTracingInOneWeekend
    private Vec3 w = new Vec3(0.0,0.0,0.0);
    private Vec3 u = new Vec3(0.0,0.0,0.0);
    private Vec3 v = new Vec3(0.0,0.0,0.0);


    private Vec3 horizontalVec = new Vec3(view_width, 0.0,0.0);
    private Vec3 verticalVec = new Vec3(0.0,view_height,0.0);
    private Vec3 bottom_left = new Vec3();


    public Camera() {
        this.w = (position.subtract(lookat)).unitVector();
        this.u = (Vec3.cross(up,w)).unitVector();
        this.v = Vec3.cross(w,u);
        this.horizontalVec = u.scale(view_width);
        this.verticalVec = v.scale(view_height);
        this.bottom_left = ((position.subtract(horizontalVec.scale(0.5))).subtract(verticalVec.scale(0.5))).subtract(w.scale(focal_length));

    }

    public Camera(int width, int height, int bounces, double halfFov, Vec3 position, Vec3 lookat, Vec3 up) {
        this.width = width;
        this.height = height;
        this.bounces = bounces;
        this.halfFov = halfFov;
        this.position = position;
        this.lookat = lookat;
        this.up = up;
        this.w = (position.subtract(lookat)).unitVector();
        this.u = (Vec3.cross(up,w)).unitVector();
        this.v = Vec3.cross(w,u);
        this.horizontalVec = u.scale(view_width);
        this.verticalVec = v.scale(view_height);
        this.bottom_left = ((position.subtract(horizontalVec.scale(0.5))).subtract(verticalVec.scale(0.5))).subtract(w.scale(focal_length));

    }


    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Vec3 getPosition() {
        return position;
    }

    public Vec3 getHorizontalVec() {
        return horizontalVec;
    }

    public Vec3 getVerticalVec() {
        return verticalVec;
    }

    public Vec3 getBottom_left() {
        return bottom_left;
    }

    public Vec3 getLookat() {
        return lookat;
    }


    public int getBounces() {
        return bounces;
    }


    // useful for debugging the parser
    @Override
    public String toString() {
        return "Camera{" +
                "width=" + width +
                ", height=" + height +
                ", bounces=" + bounces +
                ", view_height=" + view_height +
                ", view_width=" + view_width +
                ", focal_length=" + focal_length +
                ", halfFov=" + halfFov +
                ", position=" + position +
                ", lookat=" + lookat +
                ", up=" + up +
                ", w=" + w +
                ", u=" + u +
                ", v=" + v +
                ", horizontalVec=" + horizontalVec +
                ", verticalVec=" + verticalVec +
                ", bottom_left=" + bottom_left +
                '}';
    }
}
