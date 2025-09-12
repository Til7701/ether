package de.holube.ether.viz.graph.internal;

import de.holube.ether.viz.AbstractApplication;
import de.holube.ether.viz.graph.GraphVisualizer;
import de.holube.ether.viz.graph.VizNode;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class GraphFXApplication extends AbstractApplication {

    private static final int WIDTH = 1700;
    private static final int HEIGHT = 900;

    private static final double ATTRACTION_FORCE_MULTIPLIER = 0.005;
    private static final double REPULSION_FORCE_MULTIPLIER = 0.022;
    private static final int MAX_REPULSION_DISTANCE = 600;
    private static final double FORCE_TO_CENTER_MULTIPLIER = 0.01;
    private static final double MAX_ACCELERATION = 5;
    private static final double MAX_VELOCITY = 100;
    private static final double DAMPENING_FACTOR = 0.85;
    private static final int MIN_DISTANCE = 30;
    private static final double MIN_DISTANCE_CORRECTION_FACTOR = 1;

    private static GraphVisualizer<?> visualizer;
    private static Collection<VizNode> nodes;
    private static Map<String, VizNode> nodesById;
    private static Map<Pair<VizNode, VizNode>, Line> edges;
    private static double maxStrength;

    private Stage stage;

    public static <M> void setup(GraphVisualizer<M> visualizer) {
        GraphFXApplication.visualizer = visualizer;
        nodes = visualizer.graph().nodes();
        nodesById = nodes.stream()
                .collect(Collectors.toMap(VizNode::id, node -> node));
        var incomingEdges = visualizer.graph().nodes().stream()
                .map(node -> node.incomingLinkIds().stream()
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
        var outgoingEdges = visualizer.graph().nodes().stream()
                .map(node -> node.outgoingLinkIds().stream()
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
        edges = incomingEdges;
        edges.putAll(outgoingEdges);
        maxStrength = visualizer.graph().nodes().stream()
                .mapToInt(node ->
                        Math.max(node.outgoingLinkStrengths().values().stream().max(Integer::compare).orElse(0),
                                node.incomingLinkStrengths().values().stream().max(Integer::compare).orElse(0))
                )
                .max()
                .orElse(1);
    }

    public static void launchWindow() {
        Application.launch();
    }

    @Override
    public void start(Stage stage) {
        this.stage = stage;
        Pane root = new Pane();
        root.setBackground(new Background(new BackgroundFill(BACKGROUND_COLOR, null, null)));
        Scene scene = new Scene(root, WIDTH, HEIGHT);

        setAllRandomly();
        drawEdges();
        edges.forEach((pair, edge) -> {
            root.getChildren().add(edge);
            edge.setStyle("-fx-stroke-width: 1;");
            // set edge color based on source node's outgoing link count
            double hue = ((double) (pair.getKey().incomingLinkCount())) / (pair.getKey().maxIncomingLinkCount());
            edge.setStroke(Color.hsb(hue * 360, 1.0, 1.0));
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

        new Thread(() -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    Thread.sleep(30);
                    graphRelaxingStep();
                    Platform.runLater(() -> {
                        update();
                        drawEdges();
                    });
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                e.printStackTrace();
            }
        }).start();
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
            node.x(Math.random() * WIDTH);
            node.y(Math.random() * HEIGHT);
        }
    }

    private void graphRelaxingStep() {
        final double windowWidth = stage.getWidth();
        final double windowHeight = stage.getHeight();

        for (VizNode v : nodes) {
            v.accX(0);
            v.accY(0);
            for (VizNode u : nodes) {
                if (v != u) {
                    final double dx = v.x() - u.x();
                    final double dy = v.y() - u.y();
                    final double distance = Math.sqrt(dx * dx + dy * dy) + 0.001; // Avoid division by zero
                    final double repulsionForce = calculateRepulsionForce(distance);
                    double attractionForce = v.incomingLinkIds().contains(u.id())
                            ? calculateAttractionForce(distance, v.incomingLinkStrengths().get(u.id()))
                            : 0;
                    attractionForce += v.outgoingLinkIds().contains(u.id())
                            ? calculateAttractionForce(distance, v.outgoingLinkStrengths().get(u.id()))
                            : 0;

                    if (distance < MIN_DISTANCE) {
                        double overlap = MIN_DISTANCE - distance;
                        double normX = dx / distance;
                        double normY = dy / distance;

                        v.accX(v.accX() + normX * overlap * MIN_DISTANCE_CORRECTION_FACTOR);
                        v.accY(v.accY() + normY * overlap * MIN_DISTANCE_CORRECTION_FACTOR);
                    }
                    if (distance > MAX_REPULSION_DISTANCE) {
                        v.accX(v.accX() - dx * attractionForce);
                        v.accY(v.accY() - dy * attractionForce);
                    } else {
                        v.accX(v.accX() + dx * repulsionForce - dx * attractionForce);
                        v.accY(v.accY() + dy * repulsionForce - dy * attractionForce);
                    }
                }
            }
            // Apply force to center
            final double centerX = windowWidth / 2.0;
            final double centerY = windowHeight / 2.0;
            final double toCenterX = centerX - v.x();
            final double toCenterY = centerY - v.y();
            v.x(v.x() + toCenterX * FORCE_TO_CENTER_MULTIPLIER);
            v.y(v.y() + toCenterY * FORCE_TO_CENTER_MULTIPLIER);
            // Limit maximum acceleration to avoid excessive movement
            final double accelerationRate = Math.sqrt(v.accX() * v.accX() + v.accY() * v.accY());
            if (accelerationRate > MAX_ACCELERATION) {
                v.accX((v.accX() / accelerationRate) * MAX_ACCELERATION);
                v.accY((v.accY() / accelerationRate) * MAX_ACCELERATION);
            }
        }

        for (VizNode v : nodes) {
            // Apply acceleration to velocity
            v.velocityX(v.velocityX() + v.accX());
            v.velocityY(v.velocityY() + v.accY());
            // Limit maximum velocity to avoid excessive movement
            final double velocityRate = Math.sqrt(v.velocityX() * v.velocityX() + v.velocityY() * v.velocityY());
            if (velocityRate > MAX_VELOCITY) {
                v.velocityX((v.velocityX() / velocityRate) * MAX_VELOCITY);
                v.velocityY((v.velocityY() / velocityRate) * MAX_VELOCITY);
            }
            // Update position based on velocity
            v.x(v.x() + v.velocityX());
            v.y(v.y() + v.velocityY());
            // Dampen velocity to simulate friction
            v.velocityX(v.velocityX() * DAMPENING_FACTOR);
            v.velocityY(v.velocityY() * DAMPENING_FACTOR);
            // Keep nodes within bounds
            v.x(Math.clamp(v.x(), 0, Integer.MAX_VALUE));
            v.y(Math.clamp(v.y(), 0, Integer.MAX_VALUE));
        }
    }

    private double calculateAttractionForce(double distance, int strength) {
        return (ATTRACTION_FORCE_MULTIPLIER * (strength / maxStrength)) * distance;
    }

    private double calculateRepulsionForce(double distance) {
        if (distance < 1) {
            distance = 1; // Avoid extreme repulsion at very close distances
        }
        return REPULSION_FORCE_MULTIPLIER / distance;
    }

}
