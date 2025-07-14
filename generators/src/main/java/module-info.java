module ether.generators {
    requires java.desktop;
    requires static lombok;
    requires commons.math3;

    exports de.holube.ether.generators;
    exports de.holube.ether.generators.color;
    exports de.holube.ether.generators.image;
    exports de.holube.ether.generators.image.noise;
    exports de.holube.ether.generators.image.fractal;
}
