package de.holube.ether.viz.graph;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Collection;

@Getter
@Setter
@RequiredArgsConstructor
public class VizNode {

    private final String id;
    private final Collection<String> outgoingLinkIds;
    private final int outgoingLinkCount;
    private final int maxOutgoingLinkCount;
    private final int minOutgoingLinkCount;
    private final int incomingLinkCount;
    private final int maxIncomingLinkCount;
    private final int minIncomingLinkCount;

    private final VBox view = new VBox();

    private double x = 0;
    private double y = 0;

    public void init() {
        int size = calculateSizeOutgoingLinks();
        System.out.println("Node " + id + " has " + outgoingLinkCount + " outgoing links, size: " + size);
        Circle circle = new Circle(size);
        circle.setStyle("-fx-fill: lightblue; -fx-stroke: white; -fx-stroke-width: 1;");
        view.getChildren().add(circle);
        Label label = new Label(id);
        label.setStyle("-fx-text-fill: white; -fx-font-size: 10;");
        label.setVisible(false);
        label.setLabelFor(view);
        circle.hoverProperty().addListener((_, _, newVal) -> label.setVisible(newVal));
        view.getChildren().add(label);
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
