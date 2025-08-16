package de.holube.ether.generators.graph;

import de.holube.ether.generators.GeneratorResult;
import de.holube.ether.generators.graph.java.Graph;

public record GraphResult<T extends Graph>(
        T result
) implements GeneratorResult<T> {
}
