module ether.viz {
    requires static lombok;

    requires java.base;
    requires javafx.controls;

    opens de.holube.ether.viz.graph.internal to javafx.graphics;

    exports de.holube.ether.viz.graph;
}
