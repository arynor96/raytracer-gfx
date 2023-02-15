package org.lab3agfx;

import org.lab3agfx.datatypes.Color;
import org.lab3agfx.datatypes.Vec3;
import org.lab3agfx.helpers.parserXML;
import org.lab3agfx.scene.objects.lights.AmbientLight;
import org.lab3agfx.scene.objects.shading.MaterialSolid;
import org.lab3agfx.scene.objects.Sphere;
import org.lab3agfx.scene.Scene;

public class Main {
    public static void main(String[] args) {


        // checks if input path is specified in the arguments.
        // if not, renders two default scenes.
        if (args.length == 0) {
            System.out.println("Please type a file path as argument.");
            System.out.println("No path found, rendering T1 and T2 tasks instead: ");

            // This creates a black scene, no object or lights present.
            Scene scene = new Scene();
            Renderer renderer = new Renderer();
            renderer.renderT1(scene);
            System.out.println();

            // This creates a scene with two green circles.
            double[] phong = new double[4];
            phong[0] = 0.8;
            phong[1] = 1.0;
            phong[2] = 1.0;
            phong[3] = 200.0;
            MaterialSolid material = new MaterialSolid(phong,0.0,0.0,0.0,new Color(0.7,0.9,0.4));
            scene.addSphere(new Sphere(0.3,new Vec3(-0.3, 0.0, -1.0), material));
            scene.addSphere(new Sphere(0.2,new Vec3(0.6, 0.0, -1.0), material));
            scene.addLight(new AmbientLight(new Color(1.0,1.0,1.0)));

            renderer.render(scene);
            System.out.println("Done.");

        } else {
            Renderer renderer = new Renderer();
            String filePath = args[0];
            System.out.println("Rendering file found in: " + filePath);
            Scene scene = new Scene(parserXML.parseXML(filePath));
            renderer.render(scene);
        }

    }


}