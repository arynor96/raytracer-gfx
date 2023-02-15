package org.lab3agfx.scene.objects;

import org.lab3agfx.datatypes.HitSpot;
import org.lab3agfx.datatypes.Ray;
import org.lab3agfx.datatypes.Vec3;
import org.lab3agfx.scene.objects.shading.MaterialSolid;
import org.lab3agfx.scene.objects.shading.MaterialTextured;

import java.util.ArrayList;

public class Mesh extends Surface{


    private ArrayList<Vec3> vertices = new ArrayList<>();
    private ArrayList<Vec3> textures = new ArrayList<>();
    private ArrayList<Vec3> normals = new ArrayList<>();
    private ArrayList<Vec3> indices = new ArrayList<>();

    private MaterialSolid materialSolid = null;


    private MaterialTextured materialTexture = null;

    public Mesh(ArrayList<Vec3> vertices, ArrayList<Vec3> textures, ArrayList<Vec3> normals,ArrayList<Vec3> indices ){
        this.vertices = vertices;
        this.textures = textures;
        this.normals = normals;
        this.indices = indices;
    }

    public Mesh(ArrayList<Vec3> vertices, ArrayList<Vec3> textures, ArrayList<Vec3> normals,ArrayList<Vec3> indices, MaterialSolid materialSolid ){
        this.vertices = vertices;
        this.textures = textures;
        this.normals = normals;
        this.indices = indices;
        this.materialSolid = materialSolid;

        //System.out.println(vertices.size());
    }

    public Mesh(ArrayList<Vec3> vertices, ArrayList<Vec3> textures, ArrayList<Vec3> normals,ArrayList<Vec3> indices, MaterialTextured materialTexture){
        this.vertices = vertices;
        this.textures = textures;
        this.normals = normals;
        this.indices = indices;
        this.materialTexture = materialTexture;

        //System.out.println(vertices.size());
    }

    public MaterialTextured getMaterialTexture() {
        return materialTexture;
    }

    public void setMaterialTexture(MaterialTextured materialTexture) {
        this.materialTexture = materialTexture;
    }


    // Möller-Trumbore
    // source: tutorial slides + https://www.scratchapixel.com/lessons/3d-basic-rendering/ray-tracing-rendering-a-triangle/moller-trumbore-ray-triangle-intersection
    @Override
    public boolean hit(Ray ray, double tMin, double tMax, HitSpot hit) {
        Vec3 direction = ray.getDirection();
        Vec3 origin = ray.getOrigin();
        Boolean isHit = false;
        Vec3 normal = null;

        Vec3 texture0 = null;
        Vec3 texture1 = null;
        Vec3 texture2 = null;

        // for barycentric: https://www.scratchapixel.com/lessons/3d-basic-rendering/ray-tracing-rendering-a-triangle/barycentric-coordinates
        double xCoord = 0;
        double yCoord = 0;


        // general idea: https://www.scratchapixel.com/lessons/3d-basic-rendering/ray-tracing-rendering-a-triangle/moller-trumbore-ray-triangle-intersection
        // the idea of using a for loop so that I can check every polygon: https://www.scratchapixel.com/lessons/3d-basic-rendering/ray-tracing-polygon-mesh/Ray-Tracing%20a%20Polygon%20Mesh-part-1

        for (int i = 0; i < indices.size(); i += 3) {
            Vec3 vec0 = vertices.get(indices.get(i).getX().intValue() - 1);
            Vec3 vec1 = vertices.get(indices.get(i + 1).getX().intValue() - 1);
            Vec3 vec2 = vertices.get(indices.get(i + 2).getX().intValue() - 1);

            texture0 = textures.get(indices.get(i).getY().intValue() - 1);
            texture1 = textures.get(indices.get(i + 1).getY().intValue() - 1);
            texture2 = textures.get(indices.get(i + 2).getY().intValue() - 1);

            //System.out.println(vec1);

            Vec3 v0v1 = vec1.subtract(vec0);
            Vec3 v0v2 = vec2.subtract(vec0);

            Vec3 pvec = Vec3.cross(direction, v0v2);
            double det = Vec3.dot(v0v1, pvec);

            if (Math.abs(det) < tMin) continue; // det close to 0 -> ray and triangle are parallel

            double invDet = 1.0 / det;
            Vec3 tvec = origin.subtract(vec0);
            double u = (Vec3.dot(tvec, pvec)) * invDet;

            if (u < 0.0 || u > 1.0) continue;

            Vec3 qvec = Vec3.cross(tvec, v0v1);
            double v = (Vec3.dot(direction, qvec)) * invDet;

            if (v < 0.0 || u + v > 1) continue;

            double t = (Vec3.dot(v0v2, qvec)) * invDet;

            // // nearest root in range
            // in my case, u and v are a and b from tutorial slides
            if (t > tMin && t < tMax) {
                tMax = t;
                normal = normals.get(indices.get(i).getZ().intValue() - 1);
                isHit = true;
                xCoord = (1 - u - v) * texture0.getX() + u * texture1.getX() + v * texture2.getX();
                yCoord = (1 - u - v) * texture0.getY() + u * texture1.getY() + v * texture2.getY();
            }
        }
        if (isHit) {
            updateHit(hit, this, ray, tMax, normal, xCoord, yCoord);
            return true;
        } else return false;

    }

    private MaterialSolid getMaterialSolid() {
        return this.materialSolid;
    }


    private void updateHit(HitSpot hit, Mesh mesh, Ray ray, double tMax, Vec3 normal, double xCoord, double yCoord){
        hit.setT(tMax);
        hit.setPoint(ray.at(hit.getT()));
        hit.setSolidMaterial(mesh.getMaterialSolid());
        hit.setMaterialTextured(mesh.getMaterialTexture());
        hit.setFaceNormal(ray,normal);

        if(mesh.getMaterialSolid()!= null) hit.setColor(mesh.getMaterialSolid().getColor());
        else{
            hit.setColor(mesh.materialTexture.getPixelColor(xCoord, yCoord));
        }

    }

    @Override
    public String toString() {
        return "Mesh{" +
                "vertices=" + vertices +
                ", textures=" + textures +
                ", normals=" + normals +
                ", indices=" + indices +
                ", materialSolid=" + materialSolid +
                '}';
    }
}
