package org.lab3agfx.scene.objects;

import org.lab3agfx.datatypes.Color;
import org.lab3agfx.datatypes.HittedSpot;
import org.lab3agfx.datatypes.Ray;
import org.lab3agfx.datatypes.Vec3;
import org.lab3agfx.scene.objects.shading.MaterialSolid;
import org.lab3agfx.scene.objects.shading.MaterialTextured;

public class Sphere extends Surface{
    private double radius;
    private Vec3 position;
    private Color color;

    private MaterialSolid materialSolid = null;
    private MaterialTextured materialTextured = null;



    public Sphere(double radius, Vec3 position, Color color) {
        this.radius = radius;
        this.position = position;
        this.color = color;
        this.materialSolid = new MaterialSolid();
        this.materialSolid.setColor(this.color);
    }

    public Sphere(double radius, Vec3 position, MaterialSolid materialSolid) {
        this.radius = radius;
        this.position = position;
        this.materialSolid = materialSolid;
        this.color = materialSolid.getColor();
    }

    public Sphere(double radius, Vec3 position, MaterialTextured materialTextured) {
        this.radius = radius;
        this.position = position;
        this.materialTextured = materialTextured;
    }

    public double getRadius() {
        return radius;
    }

    public Vec3 getPosition() {
        return position;
    }

    public Color getColor() {
        return color;
    }

    public MaterialSolid getMaterialSolid() {
        return materialSolid;
    }

    public void setMaterialSolid(MaterialSolid materialSolid) {
        this.materialSolid = materialSolid;
    }




    // method inspired by 5.2 from RayTracingInOneWeekend + Tutorial Slides
    // https://raytracing.github.io/books/RayTracingInOneWeekend.html

    // P(t) = A + tb - ray that needs to hit the sphere
    // C = CENTER.
    // (P(t) - C) * (P - C) = r^2 =>
    // (A + tb - C) * (A + tb - C) = r^2 =>
    // (A + tb - C)^2 - R^2 = 0
    // t1,2 = (-b *(A-C) +- sqrt((b*(A-C)^2) - b^2 * ((A-C)^2-R^2)) / A^2

    public boolean hit(Ray ray, double tMin, double tMax, HittedSpot hit) {

        Vec3 oc = ray.getOrigin().subtract(this.position);
        // double A = Vec3.dot(ray.getDirection(),ray.getDirection());
        double A = ray.getDirection().length() * ray.getDirection().length();
        // double B = 2.0 * Vec3.dot(oc,ray.getDirection());
        double half_B = Vec3.dot(oc,ray.getDirection());
        double C = (oc.length() * oc.length()) - this.radius * this.radius;
        double discriminant = half_B * half_B - A*C;

        if(discriminant < 0){
            return false;
        }

        double sqrtDiscriminant = Math.sqrt(discriminant);

        // nearest root in range
        // 6.3 https://raytracing.github.io/books/RayTracingInOneWeekend.html
        double root = (-half_B - sqrtDiscriminant) / A;
        if (root < tMin || root > tMax){
            root = (-half_B + sqrtDiscriminant) / A;
            if (root < tMin || root > tMax) return false;

        }

        // all relevant information about the hitted spot
        hit.setT(root);
        hit.setPoint(ray.at(hit.getT()));
        hit.setSolidMaterial(this.materialSolid);
        hit.setMaterialTextured(this.materialTextured);

        if(this.getMaterialSolid()!= null) hit.setColor(this.getMaterialSolid().getColor());
        else{
            // tutorial slides.
            Vec3 d = hit.getPoint().subtract(this.getPosition());
            double u = 0.5 + (Math.atan2(d.getX(),d.getZ()) / (2 * Math.PI));
            double v = 0.5 - (Math.asin(d.getY())/ Math.PI);
            hit.setColor(this.materialTextured.getPixelColor(u,v));
        }

        Vec3 normal = ((hit.getPoint().subtract(this.position)).scale(1.0/this.radius)).unitVector();
        hit.setFaceNormal(ray,normal);

        return true;


    }


    @Override
    public String toString() {
        return "Sphere{" +
                "radius=" + radius +
                ", position=" + position +
                ", color=" + color +
                ", materialSolid=" + materialSolid +
                ", materialTextured=" + materialTextured +
                '}';
    }
}
