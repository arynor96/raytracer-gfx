package org.lab3agfx.datatypes;


// used this class as suggested in Ray Tracing In One Weekend
// since Java doesn't have structs, I had to adapt

// useful to save where the ray hits the surface
// and also the color + other info of the hit point

import org.lab3agfx.scene.objects.shading.MaterialSolid;
import org.lab3agfx.scene.objects.shading.MaterialTextured;

public class HitSpot {
    private Vec3 point = new Vec3();
    private Vec3 normal = new Vec3();
    private double t = 0.0;

    private boolean frontFace = false;

    private MaterialSolid SolidMaterial = null;
    private MaterialTextured materialTextured = null;

    private Color color = new Color(0.0,0.0,0.0);

    public HitSpot(Vec3 point, Vec3 normal, double t) {
        this.point = point;
        this.normal = normal;
        this.t = t;
    }

    public HitSpot() {

    }

    public void setFaceNormal(Ray ray, Vec3 hitNormal){
        if(Vec3.dot(ray.getDirection(),hitNormal) < 0) frontFace = true;
        this.normal = hitNormal;
    }

    public Vec3 getPoint() {
        return point;
    }

    public void setPoint(Vec3 point) {
        this.point = point;
    }

    public Vec3 getNormal() {
        return normal;
    }

    public double getT() {
        return t;
    }

    public void setT(double t) {
        this.t = t;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public MaterialSolid getSolidMaterial() {
        return SolidMaterial;
    }

    public void setSolidMaterial(MaterialSolid solidMaterial) {
        SolidMaterial = solidMaterial;
    }

    public MaterialTextured getMaterialTextured() {
        return materialTextured;
    }

    public void setMaterialTextured(MaterialTextured materialTextured) {
        this.materialTextured = materialTextured;
    }

    public boolean isFrontFace() {
        return frontFace;
    }

    public void setFrontFace(boolean frontFace) {
        this.frontFace = frontFace;
    }

}
