package de.holube.ether.generators.graph.java;

import de.holube.ether.generators.GeneratorResult;
import de.holube.ether.generators.graph.GraphGenerator;
import de.holube.ether.generators.graph.GraphResult;
import lombok.RequiredArgsConstructor;

import java.io.File;
import java.io.IOException;
import java.lang.classfile.ClassFile;
import java.lang.classfile.ClassModel;
import java.util.Objects;

@RequiredArgsConstructor
public class JavaGraphGenerator implements GraphGenerator<JavaGraph> {

    private final File[] rootDirectories;
    private final JavaNodeFactory javaNodeFactory;
    private final JavaGraph graph = new JavaGraph();

    @Override
    public GeneratorResult<JavaGraph> generate() {
        for (File rootDirectory : rootDirectories) {
            if (rootDirectory.isDirectory()) {
                handleDirectory(rootDirectory);
            } else {
                handleFile(rootDirectory);
            }
        }

        for (JavaNode node : graph.nodes().values()) {
            addIncomingLinks(node);
        }

        return new GraphResult<>(graph);
    }

    private void handleDirectory(File directory) {
        if (directory.isDirectory()) {
            for (File file : Objects.requireNonNull(directory.listFiles())) {
                if (file.isDirectory()) {
                    handleDirectory(file);
                } else if (file.getName().endsWith(".class")) {
                    handleFile(file);
                }
            }
        }
    }

    private void handleFile(File file) {
        try {
            ClassModel classModel = ClassFile.of().parse(file.toPath());
            if (classModel.isModuleInfo()) {
                return; // TODO handle module-info classes
            }
            JavaNode javaNode = javaNodeFactory.create(classModel);
            graph.addNode(javaNode);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void addIncomingLinks(JavaNode node) {
        for (JavaNode n : graph.nodes().values()) {
            final String nodeId = node.fullyQualifiedClassName();
            for (String outgoingLinkId : n.outgoingLinks().keySet()) {
                if (outgoingLinkId.equals(nodeId)) {
                    node.incomingLinks().merge(n.fullyQualifiedClassName(), 1, Integer::sum);
                }
            }
        }
    }

}
