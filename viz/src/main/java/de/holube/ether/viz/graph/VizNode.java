package de.holube.ether.viz.graph;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Collection;
import java.util.Map;

@Getter
@Setter
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class VizNode {

    @EqualsAndHashCode.Include
    private final String id;
    private final Collection<String> outgoingLinkIds;
    private final Collection<String> incomingLinkIds;
    private final Map<String, Integer> outgoingLinkStrengths;
    private final Map<String, Integer> incomingLinkStrengths;
    private final int outgoingLinkCount;
    private final int maxOutgoingLinkCount;
    private final int minOutgoingLinkCount;
    private final int incomingLinkCount;
    private final int maxIncomingLinkCount;
    private final int minIncomingLinkCount;

    private final Pane view = new Pane();

    private double x = 0;
    private double y = 0;

    private double velocityX = 0;
    private double velocityY = 0;

    private double accX = 0;
    private double accY = 0;

    public void init() {
        int size = calculateSizeIncomingLinks();
        System.out.println("Node " + id + " has " + outgoingLinkCount + " outgoing links, size: " + size);
        Circle circle = new Circle(size);
        circle.setStyle("-fx-fill: lightblue; -fx-stroke: white; -fx-stroke-width: 1;");
        view.getChildren().add(circle);
        Label label = new Label(id);
        label.setStyle("-fx-text-fill: white; -fx-font-size: 20;");
        label.widthProperty().addListener((obs, oldVal, newVal) -> {
            label.setLayoutX(-label.getWidth() / 2);
            label.setLayoutY(label.getHeight() / 2 + size + 5);
        });
        circle.hoverProperty().addListener((_, _, newVal) -> {
            if (newVal) {
                view.getChildren().addFirst(label);
            } else
                view.getChildren().remove(label);
        });
        // Center the circle
        circle.setCenterX(0);
        circle.setCenterY(0);

        view.setOnMouseDragged(event -> {
            x = event.getSceneX();
            y = event.getSceneY();
            velocityX = 0;
            velocityY = 0;
            event.consume();
        });
    }

    private int calculateSizeIncomingLinks() {
        double incomingLinkRatio = (double) (incomingLinkCount - minIncomingLinkCount) / (maxIncomingLinkCount - minIncomingLinkCount);
        return 5 + (int) (incomingLinkRatio * 20);
    }

    private int calculateSizeOutgoingLinks() {
        double outgoingLinkRatio = (double) (outgoingLinkCount - minOutgoingLinkCount) / (maxOutgoingLinkCount - minOutgoingLinkCount);
        return 5 + (int) (outgoingLinkRatio * 20);
    }

    public Node view() {
        return view;
    }

}
