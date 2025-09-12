package de.holube.ether.generators.graph.java;

import java.util.Map;
import java.util.TreeMap;

public record JavaNode(
        String simpleClassName,
        String packageName,
        Map<String, Integer> outgoingLinks,
        Map<String, Integer> incomingLinks
) {

    /**
     * Constructs a JavaNode with the given class name, package name, and outgoing links.
     * The incoming links are initialized as an empty map.
     *
     * @param simpleClassName the simple class name (without package)
     * @param packageName     the package name (can be empty)
     * @param outgoingLinks   a map of class names this class links to, with the number of links as values
     */
    public JavaNode(
            String simpleClassName,
            String packageName,
            Map<String, Integer> outgoingLinks
    ) {
        this(simpleClassName, packageName, outgoingLinks, new TreeMap<>());
    }

    /**
     * Returns the fully qualified class name of this Java node.
     * The format is "packageName.simpleClassName".
     * If the package name is empty, it returns only the simple class name.
     *
     * @return the fully qualified class name
     */
    public String fullyQualifiedClassName() {
        return Util.getFullyQualifiedClassName(packageName, simpleClassName);
    }

}
