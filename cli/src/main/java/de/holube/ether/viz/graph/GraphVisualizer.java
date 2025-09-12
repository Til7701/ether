package de.holube.ether.viz.graph;

import de.holube.ether.viz.graph.internal.GraphFXApplication;

public record GraphVisualizer<M>(
        VizGraph graph
) {

    public GraphVisualizer(VizGraph graph) {
        this.graph = graph;
        GraphFXApplication.setup(this);
    }

    public void show() {
        GraphFXApplication.launchWindow();
    }

}
