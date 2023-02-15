package org.lab3agfx.scene.objects.shading;

import org.lab3agfx.datatypes.Color;

import java.util.Arrays;

public class MaterialSolid {

    //ka, kd, ks, expo
    private double[] phong = new double[4];
    private double reflectance;
    private double transmittance;
    private double refraction;

    private Color color;


    public MaterialSolid(double[] phong, double reflectance, double transmittance, double refraction, Color color) {
        this.phong = phong;
        this.reflectance = reflectance;
        this.transmittance = transmittance;
        this.refraction = refraction;
        this.color = color;
    }

    public MaterialSolid(){
        this.color = new Color(0.5,0.5,0.5);
    }

    public double[] getPhong() {
        return phong;
    }

    public void setPhong(double[] phong) {
        this.phong = phong;
    }

    public double getReflectance() {
        return reflectance;
    }

    public void setReflectance(double reflectance) {
        this.reflectance = reflectance;
    }

    public double getTransmittance() {
        return transmittance;
    }

    public void setTransmittance(double transmittance) {
        this.transmittance = transmittance;
    }

    public double getRefraction() {
        return refraction;
    }

    public void setRefraction(double refraction) {
        this.refraction = refraction;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return "MaterialSolid{" +
                "phong=" + Arrays.toString(phong) +
                ", reflectance=" + reflectance +
                ", transmittance=" + transmittance +
                ", refraction=" + refraction +
                ", color=" + color +
                '}';
    }
}
