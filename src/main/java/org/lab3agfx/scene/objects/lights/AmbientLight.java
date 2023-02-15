package org.lab3agfx.scene.objects.lights;

import org.lab3agfx.datatypes.Color;

public class AmbientLight extends Light {

    private Color color;

    public AmbientLight(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return "AmbientLight{" +
                "color=" + color +
                '}';
    }
}
