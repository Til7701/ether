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
        final int maxOutgoing = javaGraph.nodes().values().stream()
                .mapToInt(n -> n.outgoingLinks().size())
                .max()
                .orElse(0);
        final int minOutgoing = javaGraph.nodes().values().stream()
                .mapToInt(n -> n.outgoingLinks().size())
                .min()
                .orElse(0);
        final int maxIncoming = javaGraph.nodes().values().stream()
                .mapToInt(n -> n.incomingLinks().size())
                .max()
                .orElse(0);
        final int minIncoming = javaGraph.nodes().values().stream()
                .mapToInt(n -> n.incomingLinks().size())
                .min()
                .orElse(0);

        System.out.println("Max outgoing links: " + maxOutgoing);
        System.out.println("Min outgoing links: " + minOutgoing);
        System.out.println("Max incoming links: " + maxIncoming);
        System.out.println("Min incoming links: " + minIncoming);

        Collection<VizNode> vizNodes = javaGraph.nodes().values().stream()
                .map(jn -> new VizNode(
                        jn.fullyQualifiedClassName(),
                        jn.outgoingLinks().keySet(),
                        jn.incomingLinks().keySet(),
                        jn.outgoingLinks(),
                        jn.incomingLinks(),
                        jn.outgoingLinks().size(),
                        maxOutgoing,
                        minOutgoing,
                        jn.incomingLinks().size(),
                        maxIncoming,
                        minIncoming
                ))
                .toList();
        return new VizGraph(vizNodes);
    }

}
