package de.holube.ether.viz.graph.internal;

import de.holube.ether.viz.graph.GraphVisualizer;
import de.holube.ether.viz.graph.VizNode;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class GraphFXApplication extends Application {

    private static GraphVisualizer<?> visualizer;
    private static Collection<VizNode> nodes;
    private static Map<String, VizNode> nodesById;
    private static Map<Pair<VizNode, VizNode>, Line> edges;

    public static <M> void setup(GraphVisualizer<M> visualizer) {
        GraphFXApplication.visualizer = visualizer;
        nodes = visualizer.graph().nodes();
        nodesById = nodes.stream()
                .collect(Collectors.toMap(VizNode::id, node -> node));
        edges = visualizer.graph().nodes().stream()
                .map(node -> node.links().stream()
                        .map(linkId -> {
                            VizNode targetNode = nodesById.get(linkId);
                            if (targetNode != null) {
                                return new Pair<>(node, targetNode);
                            } else {
                                return null;
                            }
                        })
                        .filter(Objects::nonNull)
                        .toList()
                )
                .flatMap(Collection::stream)
                .collect(Collectors.toMap(
                        pair -> pair,
                        _ -> new Line()
                ));
    }

    public static void launchWindow() {
        Application.launch();
    }

    @Override
    public void start(Stage stage) {
        Pane root = new Pane();
        Scene scene = new Scene(root, 800, 600);

        setAllRandomly();
        drawEdges();
        edges.forEach((_, edge) -> {
            root.getChildren().add(edge);
            edge.setStyle("-fx-stroke: gray; -fx-stroke-width: 1;");
        });
        for (VizNode node : nodes) {
            node.init();
            node.view().setLayoutX(node.x());
            node.view().setLayoutY(node.y());
            root.getChildren().add(node.view());
        }

        stage.setTitle("Graph Visualizer");
        stage.setScene(scene);
        stage.show();
    }

    private void update() {
        for (VizNode node : nodes) {
            node.view().setLayoutX(node.x());
            node.view().setLayoutY(node.y());
        }
    }

    private void drawEdges() {
        for (Map.Entry<Pair<VizNode, VizNode>, Line> entry : edges.entrySet()) {
            VizNode from = entry.getKey().getKey();
            VizNode to = entry.getKey().getValue();
            Line line = entry.getValue();
            line.setStartX(from.x());
            line.setStartY(from.y());
            line.setEndX(to.x());
            line.setEndY(to.y());
        }
    }

    private void setAllRandomly() {
        for (VizNode node : nodes) {
            node.x(Math.random() * 800);
            node.y(Math.random() * 600);
        }
    }

}
