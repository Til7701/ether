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
    private final Collection<String> links;

    private final VBox view = new VBox();

    private double x = 0;
    private double y = 0;

    public void init() {
        Circle circle = new Circle(10);
        circle.setStyle("-fx-fill: lightblue; -fx-stroke: black; -fx-stroke-width: 1;");
        view.getChildren().add(circle);
        Label label = new Label(id);
        view.getChildren().add(label);
    }

    public Node view() {
        return view;
    }

}
