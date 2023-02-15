package org.lab3agfx.scene;

import org.jdom2.Document;
import org.lab3agfx.datatypes.Color;
import org.lab3agfx.datatypes.HittedSpot;
import org.lab3agfx.datatypes.Ray;
import org.lab3agfx.scene.objects.Sphere;
import org.lab3agfx.scene.objects.Surface;
import org.lab3agfx.scene.objects.lights.Light;

import java.util.ArrayList;
import java.util.List;

public class Scene {

    private Camera camera = new Camera();
    private List<Surface> surfaces = new ArrayList<>();



    private List<Light> lights = new ArrayList<>();
    private Color background = new Color(0.0,0.0,0.0);


    // If no filename is specified then it has to be Task 2
    private String filename = new String("T2-Spheres.png");

    public Scene() {

    }

    public Scene(Document doc){

        SceneGenerator generator = new SceneGenerator (doc);
        Scene generatedScene = generator.generate();
        this.camera = generatedScene.getCamera();
        this.lights = generatedScene.getLights();
        this.surfaces = generatedScene.getSurfaces();
        this.background = generatedScene.getBackground();
        this.filename = generatedScene.getFilename();

    }

    public void ScenePrinter(){

        System.out.println("CAMERA--------");
        System.out.println(this.camera.toString());
        System.out.println("CAMERA--------");
        System.out.println("LIGHTS--------: " + lights.size());
        this.lights.forEach((light) -> System.out.println(light));
        System.out.println("LIGHTS--------");
        System.out.println("SURFACES--------: " + surfaces.size());
        this.surfaces.forEach((surface) -> System.out.println(surface));
        System.out.println("SURFACES--------");



    }

    public Scene(Camera camera, List<Light> lights, List<Surface> surfaces, Color background, String filename){
        this.camera = camera;
        this.lights = lights;
        this.surfaces = surfaces;
        this.background = background;
        this.filename = filename;
    }

    // source: RayTracingInOneWeekend
    public boolean checkForHit(Ray ray, double tMin, double tMax, HittedSpot hit){

        boolean hitted = false;
        double t = tMax;

        for(Surface obj : surfaces){
            if (obj.hit(ray, tMin, t, hit )){
                hitted = true;
                t = hit.getT();

            }
        }

        return hitted;

    }

    public void addSphere(Sphere e){
        surfaces.add(e);
    }

    public void addLight(Light l){
        lights.add(l);
    }

    public Camera getCamera() {
        return camera;
    }

    public Color getBackground() {
        return background;
    }

    public List<Surface> getSurfaces() {
        return surfaces;
    }

    public List<Light> getLights() {
        return lights;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }



}
