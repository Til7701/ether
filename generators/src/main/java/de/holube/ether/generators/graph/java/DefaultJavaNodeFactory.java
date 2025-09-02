package de.holube.ether.generators.graph.java;

import java.lang.classfile.ClassModel;
import java.lang.classfile.CodeModel;
import java.lang.constant.ClassDesc;
import java.lang.constant.MethodTypeDesc;
import java.util.Map;
import java.util.TreeMap;

public class DefaultJavaNodeFactory implements JavaNodeFactory {

    private Map<String, Integer> classesThisLinksTo;

    @Override
    public JavaNode create(ClassModel classModel) {
        if (classModel.isModuleInfo())
            throw new IllegalArgumentException("Module-info classes are not supported");
        classesThisLinksTo = new TreeMap<>();

        final String simpleClassName = classModel.thisClass().asSymbol().displayName();
        final String packageName = classModel.thisClass().asSymbol().packageName();

        classModel.superclass().ifPresent(classDesc -> addClassDesc(classDesc.asSymbol()));
        classModel.interfaces().forEach(classDesc -> addClassDesc(classDesc.asSymbol()));
        classModel.fields().forEach(field -> addClassDesc(field.fieldTypeSymbol()));
        classModel.methods().forEach(method -> {
            addMethodDesc(method.methodTypeSymbol());
            method.code().ifPresent(this::addCodeModel);
        });

        return new JavaNode(
                simpleClassName,
                packageName,
                classesThisLinksTo
        );
    }

    private void addMethodDesc(MethodTypeDesc methodTypeDesc) {
        addClassDesc(methodTypeDesc.returnType());
        methodTypeDesc.parameterList().forEach(this::addClassDesc);
    }

    private void addCodeModel(CodeModel codeModel) {
        //TODO
    }

    private void addClassDesc(ClassDesc classDesc) {
        classesThisLinksTo.merge(
                Util.getFullyQualifiedClassName(classDesc),
                1,
                Integer::sum
        );
        ClassDesc componentType = classDesc.componentType();
        if (componentType != null)
            addClassDesc(classDesc.componentType());
    }

}
