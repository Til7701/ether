package de.holube.ether.generators.graph.java;

import java.lang.classfile.ClassModel;

public interface JavaNodeFactory {

    JavaNode create(ClassModel classModel);

}
