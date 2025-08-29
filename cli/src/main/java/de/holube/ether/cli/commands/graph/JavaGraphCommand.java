package de.holube.ether.cli.commands.graph;

import de.holube.ether.cli.mixins.HelpMixin;
import de.holube.ether.generators.GeneratorResult;
import de.holube.ether.generators.graph.java.DefaultJavaNodeFactory;
import de.holube.ether.generators.graph.java.JavaGraph;
import de.holube.ether.generators.graph.java.JavaGraphGenerator;
import de.holube.ether.generators.graph.java.JavaNode;
import de.holube.ether.viz.graph.GraphVisualizer;
import de.holube.ether.viz.graph.VizGraph;
import de.holube.ether.viz.graph.VizNode;
import picocli.CommandLine;

import java.io.File;
import java.util.Collection;

@CommandLine.Command(
        name = "java",
        description = "Generate a graph visualizing Java classes.",
        sortOptions = false
)
public class JavaGraphCommand implements Runnable {

    @SuppressWarnings("unused")
    @CommandLine.Mixin
    private HelpMixin helpMixin;

    @SuppressWarnings("unused")
    @CommandLine.Parameters(
            description = "Root directories to scan for Java classes.",
            arity = "1..*",
            paramLabel = "<path/to/root/directory>"
    )
    private File[] rootDirectories;

    @Override
    public void run() {
        JavaGraphGenerator generator = new JavaGraphGenerator(rootDirectories, new DefaultJavaNodeFactory());
        GeneratorResult<JavaGraph> result = generator.generate();
        System.out.println(result);
        VizGraph vizGraph = convertToVizGraph(result.result());
        GraphVisualizer<JavaNode> visualizer = new GraphVisualizer<>(vizGraph);
        visualizer.show();
    }

    private VizGraph convertToVizGraph(JavaGraph javaGraph) {
        Collection<VizNode> vizNodes = javaGraph.nodes().values().stream()
                .map(jn -> new VizNode(jn.fullyQualifiedClassName(), jn.outgoingLinks().keySet()))
                .toList();
        return new VizGraph(vizNodes);
    }

}
