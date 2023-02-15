package org.lab3agfx;

import org.lab3agfx.datatypes.Color;
import org.lab3agfx.datatypes.HitSpot;
import org.lab3agfx.datatypes.Ray;
import org.lab3agfx.datatypes.Vec3;
import org.lab3agfx.scene.Scene;
import org.lab3agfx.scene.objects.lights.AmbientLight;
import org.lab3agfx.scene.objects.lights.Light;
import org.lab3agfx.scene.objects.lights.ParallelLight;
import org.lab3agfx.scene.objects.lights.PointLight;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Renderer {

    private double acne = 0.00001;
    public Renderer() {
    }


    // 4.2 Sending Rays Into the Scene
    // source of information: https://raytracing.github.io/books/RayTracingInOneWeekend.html
    // this renders a black image
    public void renderT1(Scene scene) {
        int width = scene.getCamera().getWidth();
        int height = scene.getCamera().getHeight();

        System.out.println("Exporting: " + "T1-BlackImage" + " to /output-tasks folder");
        File image = new File("output-tasks", "T1-BlackImage.png");
        BufferedImage buffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);


        for (int j = height - 1; j >= 0; --j) {
            for (int i = 0; i < width; ++i) {
                buffer.setRGB(i, j, scene.getBackground().getRGB());
            }
        }

        try {
            ImageIO.write(buffer, "PNG", image);
            System.out.println("Exported " + image.getName() + " successfully");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    // 4.2 Sending Rays Into the Scene
    // source of information: https://raytracing.github.io/books/RayTracingInOneWeekend.html
    public void render(Scene scene){
        int width = scene.getCamera().getWidth();
        int height = scene.getCamera().getHeight();

        Vec3 origin = scene.getCamera().getPosition();
        Vec3 horizontalVec = scene.getCamera().getHorizontalVec();
        Vec3 verticalVec = scene.getCamera().getVerticalVec();
        Vec3 bottom_left = scene.getCamera().getBottom_left();

        System.out.println("Exporting: " + scene.getFilename() + " to /output-tasks folder");

        File image = new File("output-tasks",scene.getFilename());
        BufferedImage buffer = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);

        Color finalColor = new Color(0.0,0.0,0.0);

        for(int j = height-1 ; j >= 0; --j){
            for(int i = 0; i < width; ++i){

                double u = (double) i / (width - 1) ;
                double v = (double) j / (height - 1) ;

                Vec3 horizontalVecN = horizontalVec.scale(u);
                Vec3 verticalVecN = verticalVec.scale(v);

                Vec3 direction2 = ((bottom_left.add(horizontalVecN)).add(verticalVecN)).subtract(origin);

                // invert the image vertically
                direction2 = direction2.invertVertically();
                Ray ray = new Ray(origin, direction2);
                finalColor = rayColor(ray,scene,scene.getCamera().getBounces());
                buffer.setRGB(i,j,finalColor.getRGB());

            }
        }

        try {
            ImageIO.write(buffer, "PNG", image);
            System.out.println("Exported " + image.getName() + " successfully");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }



    }


    public Color rayColor(Ray r, Scene scene, int bounces){

        HitSpot hit = new HitSpot();
        if(scene.checkForHit(r,acne,Double.POSITIVE_INFINITY,hit)){
            Color colorHit = new Color(hit.getColor().getR(),hit.getColor().getG(),hit.getColor().getB());
            double[] phong;
            if(hit.getSolidMaterial()!=null){
               phong = hit.getSolidMaterial().getPhong();
            } else  phong = hit.getMaterialTextured().getPhong();
            double ka = phong[0];
            double kd = phong[1];
            double ks = phong[2];
            double exponent = phong[3];


            Color ambientColor = new Color(0.0,0.0,0.0);
            Color diffuseColor = new Color(0.0,0.0,0.0);
            Color specularColor = new Color(0.0,0.0,0.0);


            Vec3 L = new Vec3();


            // if there are more lights, I go through all lights from the scene.
            for(Light light : scene.getLights()){
                Color lightColor = new Color(0.0,0.0,0.0);
                boolean parallelExists = false;
                boolean pointExists = false;
                double lightDistance = Double.POSITIVE_INFINITY;


                if(light instanceof AmbientLight){
                    lightColor = ((AmbientLight) light).getColor();
                    ambientColor = lightColor.multiply(colorHit);
                    ambientColor = ambientColor.scale(ka);

                }

                if(light instanceof ParallelLight){
                    lightColor = ((ParallelLight) light).getColor();
                    Vec3 direction = ((ParallelLight) light).getDirection();
                    L = direction.unitVector();
                    parallelExists = true;

                }

                // L = (S-P) / | S - P |
                if(light instanceof PointLight){
                    lightColor = ((PointLight) light).getColor();
                    Vec3 lightPosition = ((PointLight) light).getPosition();
                    Vec3 distance = lightPosition.subtract(hit.getPoint());
                    L = distance.unitVector();
                    L = Vec3.negate(L);

                    lightDistance = distance.length();

                    pointExists = true;
                }

                // if only ambient light exists, then there is no diffuse or specular
                // and also no shadow

                if(parallelExists || pointExists){

                    Ray shadowRay = new Ray(hit.getPoint(), Vec3.negate(L));
                    boolean pointInShadow = scene.checkForHit(shadowRay,acne,lightDistance, new HitSpot(hit.getPoint(),hit.getNormal(),hit.getT()));
                    if(pointInShadow) continue;

                    // all vectors are normalised
                    Vec3 N = (hit.getNormal()).unitVector();
                    Vec3 R = new Vec3();
                    Vec3 to_Camera = ((Vec3.negate(r.getDirection()))).unitVector();

                    double diffuseColorCos = Vec3.dot(N,Vec3.negate(L));
                    if (diffuseColorCos>1.0) diffuseColorCos = 1.0;
                    if (diffuseColorCos<0.0) diffuseColorCos = 0.0;

                    diffuseColor = diffuseColor.add((lightColor.scale(diffuseColorCos)).multiply(colorHit));
                    diffuseColor.scale(kd);


                    // R formula:  2 * (N prod L ) - L
                    Vec3 tempForR = N.scale(2 * Vec3.dot(Vec3.negate(L),N)); // 2 * (N prod L )
                    R = tempForR.subtract(Vec3.negate(L));
                    R = R.unitVector();

                    // Used information from here to calculate the specular lights: http://learnwebgl.brown37.net/09_lights/lights_specular.html
                    double specularColorCos = Vec3.dot(R,to_Camera);
                    if (specularColorCos>1.0) specularColorCos = 1.0;
                    if (specularColorCos<0.0) specularColorCos = 0.0;
                    specularColorCos = Math.pow(specularColorCos,exponent);

                    specularColor = ((specularColor.add((lightColor.scale(specularColorCos)))).scale(ks));

                }


            }


            // REFLECTION
            // tutorial slides + https://www.scratchapixel.com/lessons/3d-basic-rendering/introduction-to-shading/reflection-refraction-fresnel
            Color reflection = new Color(0.0,0.0,0.0);
            double reflectance;
            if(hit.getSolidMaterial()!=null) reflectance = hit.getSolidMaterial().getReflectance();
            else reflectance = hit.getMaterialTextured().getReflectance();
            if(reflectance>0 && bounces > 0) reflection = reflection(reflectance,bounces,hit,r,scene).scale(reflectance);

            // REFRACTION
            // tutorial slides + https://www.scratchapixel.com/lessons/3d-basic-rendering/introduction-to-shading/reflection-refraction-fresnel
            Color refraction = new Color(0.0,0.0,0.0);
            double transmittance;
            if(hit.getSolidMaterial()!=null) transmittance = hit.getSolidMaterial().getTransmittance();
            else transmittance = hit.getMaterialTextured().getTransmittance();

            double refractionIndices;
            if(hit.getSolidMaterial()!=null) refractionIndices = hit.getSolidMaterial().getRefraction();
            else refractionIndices = hit.getMaterialTextured().getRefraction();

            if(transmittance>0 && bounces > 0) refraction = refraction(refractionIndices,bounces,hit,r,scene, transmittance).scale(transmittance);


            // Final color

            Color finalColor = ((ambientColor.add(diffuseColor)).add(specularColor));
            finalColor = finalColor.scale(1-reflectance-transmittance);
            return (finalColor.add(reflection)).add(refraction);


        }

        return scene.getBackground();

       /*  for debugging purpose
        Vec3 unitDirection = r.getDirection().unitVector();
        double t = 0.5 * (unitDirection.getY() + 1.0);

        Color white = new Color(1.0,1.0,1.0);
        white = white.scale(1.0-t);

        Color grad = new Color(0.5,0.7,1.0);
        grad = grad.scale(t);
        return white.add(grad);  */
    }



    // tutorial slides + https://www.scratchapixel.com/lessons/3d-basic-rendering/introduction-to-shading/reflection-refraction-fresnel
    private Color reflection(double reflectance, int bounces, HitSpot hit, Ray r, Scene scene){

        Color reflection = new Color(0.0,0.0,0.0);
        Vec3 N = (hit.getNormal()).unitVector();
        Vec3 rayDirection = (r.getDirection()).unitVector();

        // r = 2 * ( -d * n) * n + d
        double reflectionDirectionHelper = Vec3.dot(Vec3.negate(rayDirection),N);
        reflectionDirectionHelper = reflectionDirectionHelper * 2;
        Vec3 reflectionDirection = rayDirection.add(N.scale(reflectionDirectionHelper));

        // The origin of the reflected ray is the intersection point
        Ray reflRay = new Ray(hit.getPoint(),reflectionDirection);
        reflection = rayColor(reflRay,scene,bounces-1);

        return reflection;
    }



    // tutorial slides + https://www.scratchapixel.com/lessons/3d-basic-rendering/introduction-to-shading/reflection-refraction-fresnel
    private Color refraction(double refractionIndices, int bounces, HitSpot hit, Ray r, Scene scene, double transmittance){

        Color refraction = new Color(0.0,0.0,0.0);
        Ray refractionRay = null;
        Vec3 N = hit.getNormal().unitVector();
        Vec3 V = r.getDirection().unitVector();


        double cos = Vec3.dot(V,N);
        if (cos>1.0) cos = 1.0;
        if (cos<-1.0) cos = -1.0;


        double snell;

        if(cos<0.0) {
            snell = 1.0 / refractionIndices;
            cos = -cos;

        }
        else {
            snell = refractionIndices;
            N = Vec3.negate(N);
        }

        double underSqrtPart = 1.0 - snell*snell * (1.0 - (cos*cos));

        if(underSqrtPart < 0.0 ){
             double reflectionDirectionHelper = Vec3.dot(Vec3.negate(V),N);
             reflectionDirectionHelper = reflectionDirectionHelper * 2;
             Vec3 reflectionDirection = V.add(N.scale(reflectionDirectionHelper));
             refractionRay = new Ray(hit.getPoint(),reflectionDirection);

        } else {
            Vec3 leftPart = V.add(N.scale(cos));
            leftPart = leftPart.scale(snell);
            Vec3 rightPart = N.scale(Math.sqrt(underSqrtPart));
            Vec3 refractionDirection = (leftPart.subtract(rightPart));
            refractionRay = new Ray(hit.getPoint(), refractionDirection);
        }

        refraction = rayColor(refractionRay,scene,bounces-1);

        return refraction;

    }




}
