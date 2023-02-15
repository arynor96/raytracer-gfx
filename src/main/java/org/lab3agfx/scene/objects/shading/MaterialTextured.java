package org.lab3agfx.scene.objects.shading;

import org.lab3agfx.datatypes.Color;

import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.util.Arrays;

public class MaterialTextured {

    private BufferedImage image;

    private int[][] pixels;

    private double[] phong = new double[4];
    private double reflectance;
    private double transmittance;
    private double refraction;

    public MaterialTextured(BufferedImage image, double[] phong, double reflectance, double transmittance, double refraction) {
        this.image = image;
        this.phong = phong;
        this.reflectance = reflectance;
        this.transmittance = transmittance;
        this.refraction = refraction;
        this.pixels = new int[image.getWidth()][image.getHeight()];

        // saving the rgb code of each pixel of the buffered image
        for( int i = 0; i < image.getWidth(); i++ )
            for( int j = 0; j < image.getHeight(); j++ )
                pixels[i][j] = image.getRGB(i,j);

    }

    // rainbow is 400x267
    public Color getPixelColor(double x, double y){
        int xPos = (int) (x * (image.getWidth()));
        int yPos = (int) (y * (image.getHeight()));


        if (xPos<0) xPos = 0;
        if (yPos<0) yPos = 0;

        int textureColor = pixels[xPos][yPos];
        double r = ((textureColor>>16)&0xff) / 255.99;
        double g = ((textureColor>>8)&0xff) / 255.99;
        double b = (textureColor&0xff) / 255.99;

        //System.out.println(textureColor);


        return new Color(r,g,b);
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
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

    @Override
    public String toString() {
        return "MaterialTextured{" +
                "image=" + image +
                ", pixels=" + Arrays.toString(pixels) +
                ", phong=" + Arrays.toString(phong) +
                ", reflectance=" + reflectance +
                ", transmittance=" + transmittance +
                ", refraction=" + refraction +
                '}';
    }

}
