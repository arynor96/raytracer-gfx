package org.lab3agfx.scene.objects.lights;

import org.lab3agfx.datatypes.Color;
import org.lab3agfx.datatypes.Vec3;

public class ParallelLight extends Light {

    private Color color;
    private Vec3 direction;

    public ParallelLight(Color color, Vec3 direction) {
        this.color = color;
        this.direction = direction;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Vec3 getDirection() {
        return direction;
    }

    public void setDirection(Vec3 direction) {
        this.direction = direction;
    }
}
