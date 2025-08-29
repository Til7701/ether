package de.holube.ether.generators.graph.java;

import lombok.Getter;
import lombok.ToString;

import java.util.Map;
import java.util.TreeMap;

@Getter
@ToString
public class JavaGraph implements Graph {

    private final Map<String, JavaNode> nodes = new TreeMap<>();

    public void addNode(JavaNode javaNode) {
        nodes.put(javaNode.fullyQualifiedClassName(), javaNode);
    }

}
