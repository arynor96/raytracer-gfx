
# Java basic Ray Tracer
This project was done for the Foundations of Computer Graphics class at the University of Vienna.

## What can it do?

* Be able to read in geometry from the specified XML files using a parser.
* Basic Phong illumination and shading.
* Shadows.
* Parse and raycast triangles (meshes read from obj files).
* Basic reflection and refraction.
* Texture mapping.
* Uses Gradle to build.
​
## Output of the XML files

<p align="middle">
  <img src="https://github.com/arynor96/raytracer-gfx/blob/main/output-tasks/example6textured.png" width="400" />
  <img src="https://github.com/arynor96/raytracer-gfx/blob/main/output-tasks/example3.png" width="400" /> 
  <img src="https://github.com/arynor96/raytracer-gfx/blob/main/output-tasks/example6.png" width="400" />
  <img src="https://github.com/arynor96/raytracer-gfx/blob/main/output-tasks/example5.png" width="400" />
</p>


## XML File Format

The XML files can contain lights and surfaces. Each surface consists of a material. Besides that, transformations may also exist, but they are not mandatory.


Concrete examples of the XML elements:
#### Camera
```xml
<camera>
    <position x="1.0" y="-2.0E-10" z="-3"/>
    <lookat x="1" y="2" z="3"/>
    <up x="1" y="2" z="3"/>
    <horizontal_fov angle="45"/>
    <resolution horizontal="1920" vertical="1080"/>
    <max_bounces n="100"/>
</camera>
```

#### Lights
```xml
<lights>
    <ambient_light>
        <color r="0.1" g="0.2" b="0.3"/>
    </ambient_light>
    <point_light>
        <color r="0.1" g="0.2" b="0.3"/>
        <position x="1" y="2" z="3"/>
    </point_light>
    <parallel_light>
        <color r="0.1" g="0.2" b="0.3"/>
        <direction x="1" y="2" z="3"/>
    </parallel_light>
    <spot_light>
        <color r="0.1" g="0.2" b="0.3"/>
        <position x="1" y="2" z="3"/>
        <direction x="1" y="2" z="3"/>
        <falloff alpha1="1" alpha2="3"/>
    </spot_light>
</lights>
```


#### Surfaces

```xml
<surfaces>
        <sphere radius="123">
        <position x="1" y="2" z="3"/>
        <material_solid>
            <color r="0.1" g="0.2" b="0.3"/>
            <phong ka="1.0" kd="1.0" ks="1.0" exponent="1"/>
            <reflectance r="1.0"/>
            <transmittance t="1.0"/>
            <refraction iof="1.0"/>
        </material_solid>
        <transform>
            <translate x="1" y="1" z="1"/>
            <scale x="1" y="1" z="1"/>
            <rotateX theta="1"/>
            <rotateY theta="1"/>
            <rotateZ theta="1"/>
        </transform>
    </sphere>
    <mesh name="duck.dae">
        <material_textured>
            <texture name=""/>
            <phong ka="1.0" kd="1.0" ks="1.0" exponent="1"/>
            <reflectance r="1.0"/>
            <transmittance t="1.0"/>
            <refraction iof="1.0"/>
        </material_textured>
        <transform>
            <translate x="1" y="1" z="1"/>
            <scale x="1" y="1" z="1"/>
            <rotateX theta="1"/>
            <rotateY theta="1"/>
            <rotateZ theta="1"/>
            <translate x="1" y="1" z="1"/>
            <scale x="1" y="1" z="1"/>
        </transform>
    </mesh>
</surfaces>
```

More information available here: [Visualization and Data Analysis group][1] 



### References

* 052200-1 Uni Wien GFX Foundations of Computer Graphics Slides.
* [Ray Tracing in One Weekend by Peter Shirley ][2] 




[1]: http://vda.univie.ac.at/Teaching/Graphics/22w/Labs/Lab3/lab2_file_specification.html 
[2]: https://raytracing.github.io/books/RayTracingInOneWeekend.html
