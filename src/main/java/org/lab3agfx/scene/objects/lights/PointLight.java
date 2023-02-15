package org.lab3agfx.scene.objects.lights;

import org.lab3agfx.datatypes.Color;
import org.lab3agfx.datatypes.Vec3;

public class PointLight extends Light {

    private Color color;
    private Vec3 position;

    public PointLight(Color color, Vec3 position){
        this.color = color;
        this.position = position;
    }


    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Vec3 getPosition() {
        return position;
    }

    public void setPosition(Vec3 position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return "PointLight{" +
                "color=" + color +
                ", position=" + position +
                '}';
    }
}
