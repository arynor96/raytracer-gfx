package org.lab3agfx.scene;

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.lab3agfx.datatypes.Color;
import org.lab3agfx.datatypes.Vec3;
import org.lab3agfx.scene.objects.Mesh;
import org.lab3agfx.scene.objects.Sphere;
import org.lab3agfx.scene.objects.Surface;
import org.lab3agfx.scene.objects.lights.AmbientLight;
import org.lab3agfx.scene.objects.lights.Light;
import org.lab3agfx.scene.objects.lights.ParallelLight;
import org.lab3agfx.scene.objects.lights.PointLight;
import org.lab3agfx.scene.objects.shading.MaterialSolid;
import org.lab3agfx.scene.objects.shading.MaterialTextured;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class SceneGenerator {

    private Document doc;

    private Camera camera = new Camera();
    private List<Surface> surfaces = new ArrayList<>();



    private List<Light> lights = new ArrayList<>();
    private Color background = new Color(0.0,0.0,0.0);


    // If no filename is specified then it has to be Task 2
    private String filename = new String("T2-Spheres.png");

    public SceneGenerator(Document doc){
        this.doc = doc;
    }

    // For the parser
    // all the magic from parserXML happens here
    // tried to do it without using jdom2, i gave up and found this:
    // inspired from here: https://howtodoinjava.com/java/xml/jdom2-read-parse-xml-examples/#project-folders

    public Scene generate(){
        Element root = doc.getRootElement();

        this.filename = root.getAttribute("output_file").getValue();
        System.out.println("Output file name: " + this.filename);


        //background color
        List<Attribute> backColors = root.getChild("background_color").getAttributes();
        double r = Double.parseDouble(backColors.get(0).getValue());
        double g = Double.parseDouble(backColors.get(1).getValue());
        double b = Double.parseDouble(backColors.get(2).getValue());
        Color backColor = new Color(r,g,b);

        this.background = backColor;

        this.camera = cameraParser(root.getChild("camera"));
        surfacesParser(root);
        lightsParser(root);

        return new Scene(camera,lights,surfaces,background,filename);
    }

    private void lightsParser(Element root) {

        for (Element light : root.getChild("lights").getChildren()){

            if(light.getName().compareTo("ambient_light")==0){
                double ra = Double.parseDouble(light.getChild("color").getAttributes().get(0).getValue());
                double ga = Double.parseDouble(light.getChild("color").getAttributes().get(1).getValue());
                double ba = Double.parseDouble(light.getChild("color").getAttributes().get(2).getValue());
                Color ambientColor = new Color (ra,ga,ba);
                AmbientLight ambiLight = new AmbientLight(ambientColor);
                this.lights.add(ambiLight);
            }

            if(light.getName().compareTo("parallel_light")==0){
                double ra = Double.parseDouble(light.getChild("color").getAttributes().get(0).getValue());
                double ga = Double.parseDouble(light.getChild("color").getAttributes().get(1).getValue());
                double ba = Double.parseDouble(light.getChild("color").getAttributes().get(2).getValue());

                double xd = Double.parseDouble(light.getChild("direction").getAttributes().get(0).getValue());
                double yd = Double.parseDouble(light.getChild("direction").getAttributes().get(1).getValue());
                double zd = Double.parseDouble(light.getChild("direction").getAttributes().get(2).getValue());

                Color parallelColor = new Color(ra,ga,ba);
                Vec3 direction = new Vec3(xd,yd,zd);

                ParallelLight parLight = new ParallelLight(parallelColor,direction);
                this.lights.add(parLight);
            }

            if(light.getName().compareTo("point_light")==0){
                double ra = Double.parseDouble(light.getChild("color").getAttributes().get(0).getValue());
                double ga = Double.parseDouble(light.getChild("color").getAttributes().get(1).getValue());
                double ba = Double.parseDouble(light.getChild("color").getAttributes().get(2).getValue());

                double xp = Double.parseDouble(light.getChild("position").getAttributes().get(0).getValue());
                double yp = Double.parseDouble(light.getChild("position").getAttributes().get(1).getValue());
                double zp = Double.parseDouble(light.getChild("position").getAttributes().get(2).getValue());

                Color pointColor = new Color(ra,ga,ba);
                Vec3 positionLight = new Vec3(xp,yp,zp);

                PointLight pointLt= new PointLight(pointColor,positionLight);
                this.lights.add(pointLt);

            }

        }

    }

    private void surfacesParser(Element root) {

        for (Element s : root.getChild("surfaces").getChildren()){
            if(s.getName().equals(("sphere"))){
                double radius= Double.parseDouble(s.getAttribute("radius").getValue());

                double xs = Double.parseDouble(s.getChild("position").getAttributes().get(0).getValue());
                double ys = Double.parseDouble(s.getChild("position").getAttributes().get(1).getValue());
                double zs = Double.parseDouble(s.getChild("position").getAttributes().get(2).getValue());
                Vec3 positionS = new Vec3(xs,ys,zs);

                if (s.getChild("material_solid")!=null){

                    double[] phong = new double[4];
                    phong[0] = Double.parseDouble(s.getChild("material_solid").getChild("phong").getAttributes().get(0).getValue());
                    phong[1] = Double.parseDouble(s.getChild("material_solid").getChild("phong").getAttributes().get(1).getValue());
                    phong[2] = Double.parseDouble(s.getChild("material_solid").getChild("phong").getAttributes().get(2).getValue());
                    phong[3] = Double.parseDouble(s.getChild("material_solid").getChild("phong").getAttributes().get(3).getValue());

                    double reflectance = Double.parseDouble(s.getChild("material_solid").getChild("reflectance").getAttributes().get(0).getValue());
                    double transmittance = Double.parseDouble(s.getChild("material_solid").getChild("transmittance").getAttributes().get(0).getValue());
                    double refraction = Double.parseDouble(s.getChild("material_solid").getChild("refraction").getAttributes().get(0).getValue());
                    double rs = Double.parseDouble(s.getChild("material_solid").getChild("color").getAttributes().get(0).getValue());
                    double gs = Double.parseDouble(s.getChild("material_solid").getChild("color").getAttributes().get(1).getValue());
                    double bs = Double.parseDouble(s.getChild("material_solid").getChild("color").getAttributes().get(2).getValue());

                    Color sphereColor = new Color(rs,gs,bs);
                    //System.out.println(sphereColor);
                    MaterialSolid newMaterial = new MaterialSolid(phong,reflectance,transmittance,refraction,sphereColor);
                    Sphere newSphere = new Sphere(radius,positionS,newMaterial);
                    this.surfaces.add(newSphere);
                } else{
                    try{
                        String textureName = s.getChild("material_textured").getChild("texture").getAttributes().get(0).getValue();
                        BufferedImage image = ImageIO.read(new File("input/" + textureName));

                        double[] phong = new double[4];
                        phong[0] = Double.parseDouble(s.getChild("material_textured").getChild("phong").getAttributes().get(0).getValue());
                        phong[1] = Double.parseDouble(s.getChild("material_textured").getChild("phong").getAttributes().get(1).getValue());
                        phong[2] = Double.parseDouble(s.getChild("material_textured").getChild("phong").getAttributes().get(2).getValue());
                        phong[3] = Double.parseDouble(s.getChild("material_textured").getChild("phong").getAttributes().get(3).getValue());


                        double reflectance = Double.parseDouble(s.getChild("material_textured").getChild("reflectance").getAttributes().get(0).getValue());
                        double transmittance = Double.parseDouble(s.getChild("material_textured").getChild("transmittance").getAttributes().get(0).getValue());
                        double refraction = Double.parseDouble(s.getChild("material_textured").getChild("refraction").getAttributes().get(0).getValue());

                        MaterialTextured newMaterial = new MaterialTextured(image,phong,reflectance,transmittance,refraction);


                        Sphere newSphere = new Sphere(radius,positionS,newMaterial);
                        this.surfaces.add(newSphere);

                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            } else {
                ArrayList<Vec3> vertices = new ArrayList<>();
                ArrayList<Vec3> textures = new ArrayList<>();
                ArrayList<Vec3> normals = new ArrayList<>();
                ArrayList<Vec3> indices = new ArrayList<>();

                String meshname = s.getAttribute("name").getValue();

                // https://www.journaldev.com/709/java-read-file-line-by-line
                FileReader fileReader = null;
                try {
                    fileReader = new FileReader(("input/"+ meshname));
                    BufferedReader reader = new BufferedReader(fileReader);
                    String line = reader.readLine();
                    while (line != null){
                        String[] content = line.trim().split(" ");

                        if(line.startsWith("v ")){
                            Vec3 ver = new Vec3(Double.parseDouble(content[1]), Double.parseDouble(content[2]), Double.parseDouble(content[3]));
                            vertices.add(ver);
                        }

                        if (line.startsWith("vt ")){
                            Vec3 tex = new Vec3(Double.parseDouble(content[1]), Double.parseDouble(content[2]), null);
                            textures.add(tex);
                        }

                        if (line.startsWith("vn ")){
                            Vec3 normal = new Vec3(Double.parseDouble(content[1]), Double.parseDouble(content[2]), Double.parseDouble(content[3]));
                            normals.add(normal);
                        }


                        // looks like : f 5/5/2 8/6/2 7/7/2
                        if (line.startsWith("f ")){

                            for (int i = 1; i<content.length; i++){
                                String[] part = content[i].split("/");
                                Vec3 index = new Vec3(Double.parseDouble(part[0]), Double.parseDouble(part[1]), Double.parseDouble(part[2]));
                                indices.add(index);

                            }
                        }
                        line = reader.readLine();
                    }
                    reader.close();

                } catch (Exception e){
                    e.printStackTrace();
                }


                if (s.getChild("material_solid")!=null){

                    double[] phong = new double[4];
                    phong[0] = Double.parseDouble(s.getChild("material_solid").getChild("phong").getAttributes().get(0).getValue());
                    phong[1] = Double.parseDouble(s.getChild("material_solid").getChild("phong").getAttributes().get(1).getValue());
                    phong[2] = Double.parseDouble(s.getChild("material_solid").getChild("phong").getAttributes().get(2).getValue());
                    phong[3] = Double.parseDouble(s.getChild("material_solid").getChild("phong").getAttributes().get(3).getValue());


                    double reflectance = Double.parseDouble(s.getChild("material_solid").getChild("reflectance").getAttributes().get(0).getValue());
                    double transmittance = Double.parseDouble(s.getChild("material_solid").getChild("transmittance").getAttributes().get(0).getValue());
                    double refraction = Double.parseDouble(s.getChild("material_solid").getChild("refraction").getAttributes().get(0).getValue());
                    double rs = Double.parseDouble(s.getChild("material_solid").getChild("color").getAttributes().get(0).getValue());
                    double gs = Double.parseDouble(s.getChild("material_solid").getChild("color").getAttributes().get(1).getValue());
                    double bs = Double.parseDouble(s.getChild("material_solid").getChild("color").getAttributes().get(2).getValue());

                    Color sphereColor = new Color(rs,gs,bs);
                    MaterialSolid newMaterial = new MaterialSolid(phong,reflectance,transmittance,refraction,sphereColor);


                    Mesh newMesh = new Mesh(vertices,textures,normals,indices,newMaterial);
                    this.surfaces.add(newMesh);

                } else {
                    try{
                        String textureName = s.getChild("material_textured").getChild("texture").getAttributes().get(0).getValue();
                        BufferedImage image = ImageIO.read(new File("input/" + textureName));

                        double[] phong = new double[4];
                        phong[0] = Double.parseDouble(s.getChild("material_textured").getChild("phong").getAttributes().get(0).getValue());
                        phong[1] = Double.parseDouble(s.getChild("material_textured").getChild("phong").getAttributes().get(1).getValue());
                        phong[2] = Double.parseDouble(s.getChild("material_textured").getChild("phong").getAttributes().get(2).getValue());
                        phong[3] = Double.parseDouble(s.getChild("material_textured").getChild("phong").getAttributes().get(3).getValue());


                        double reflectance = Double.parseDouble(s.getChild("material_textured").getChild("reflectance").getAttributes().get(0).getValue());
                        double transmittance = Double.parseDouble(s.getChild("material_textured").getChild("transmittance").getAttributes().get(0).getValue());
                        double refraction = Double.parseDouble(s.getChild("material_textured").getChild("refraction").getAttributes().get(0).getValue());

                        MaterialTextured newMaterial = new MaterialTextured(image,phong,reflectance,transmittance,refraction);


                        Mesh newMesh = new Mesh(vertices,textures,normals,indices,newMaterial);
                        this.surfaces.add(newMesh);



                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }

            }

        }

    }

    private Camera cameraParser(Element cameraAttributes) {

        List<Attribute> positions = cameraAttributes.getChild("position").getAttributes();
        double x = Double.parseDouble(positions.get(0).getValue());
        double y = Double.parseDouble(positions.get(1).getValue());
        double z = Double.parseDouble(positions.get(2).getValue());
        Vec3 position = new Vec3(x,y,z);


        List<Attribute> lookatValues = cameraAttributes.getChild("lookat").getAttributes();
        double x2 = Double.parseDouble(lookatValues.get(0).getValue());
        double y2 = Double.parseDouble(lookatValues.get(1).getValue());
        double z2 = Double.parseDouble(lookatValues.get(2).getValue());
        Vec3 lookat = new Vec3(x2,y2,z2);


        List<Attribute> upValues = cameraAttributes.getChild("up").getAttributes();
        double x3 = Double.parseDouble(upValues.get(0).getValue());
        double y3 = Double.parseDouble(upValues.get(1).getValue());
        double z3 = Double.parseDouble(upValues.get(2).getValue());
        Vec3 up = new Vec3(x3,y3,z3);


        double halfFOV = Double.parseDouble(cameraAttributes.getChild("horizontal_fov").getAttributes().get(0).getValue());
        int height = Integer.parseInt(cameraAttributes.getChild("resolution").getAttributes().get(1).getValue());
        int width = Integer.parseInt(cameraAttributes.getChild("resolution").getAttributes().get(0).getValue());
        int bounces = Integer.parseInt(cameraAttributes.getChild("max_bounces").getAttributes().get(0).getValue());

        return new Camera(height,width,bounces,halfFOV,position,lookat,up);

    }
}
