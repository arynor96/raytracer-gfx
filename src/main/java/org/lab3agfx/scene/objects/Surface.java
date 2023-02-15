package org.lab3agfx.scene.objects;

import org.lab3agfx.datatypes.HitSpot;
import org.lab3agfx.datatypes.Ray;

public abstract class Surface {

    public abstract boolean hit(Ray ray, double tMin, double tMax, HitSpot hit);
}
