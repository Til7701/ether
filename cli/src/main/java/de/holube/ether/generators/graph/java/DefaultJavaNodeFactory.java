package de.holube.ether.generators.graph.java;

import java.lang.classfile.*;
import java.lang.classfile.attribute.RuntimeInvisibleTypeAnnotationsAttribute;
import java.lang.classfile.attribute.RuntimeVisibleTypeAnnotationsAttribute;
import java.lang.classfile.attribute.StackMapTableAttribute;
import java.lang.classfile.instruction.*;
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
        for (CodeElement codeElement : codeModel.elementList()) {
            addCodeElement(codeElement);
        }
    }

    private void addCodeElement(CodeElement codeElement) {
        switch (codeElement) {
            case Instruction instruction -> addInstruction(instruction);
            case PseudoInstruction pseudoInstruction -> addPseudoInstruction(pseudoInstruction);
            case RuntimeInvisibleTypeAnnotationsAttribute runtimeInvisibleTypeAnnotationsAttribute -> {
            }
            case RuntimeVisibleTypeAnnotationsAttribute runtimeVisibleTypeAnnotationsAttribute -> {
            }
            case StackMapTableAttribute stackMapTableAttribute -> {
            }
            default -> {
                // Other code elements that do not reference types
            }
        }
    }

    private void addInstruction(Instruction instruction) {
        switch (instruction) {
            case ArrayLoadInstruction arrayLoadInstruction ->
                    addClassDesc(arrayLoadInstruction.typeKind().upperBound());
            case ArrayStoreInstruction arrayStoreInstruction -> {
                addClassDesc(arrayStoreInstruction.typeKind().upperBound());
            }
            case ConstantInstruction constantInstruction -> constantInstruction.typeKind().upperBound();
            case ConvertInstruction convertInstruction -> {
                addClassDesc(convertInstruction.fromType().upperBound());
                addClassDesc(convertInstruction.toType().upperBound());
            }
            case FieldInstruction fieldInstruction -> addClassDesc(fieldInstruction.typeSymbol());
            case LoadInstruction loadInstruction -> addClassDesc(loadInstruction.typeKind().upperBound());
            case NewMultiArrayInstruction newMultiArrayInstruction ->
                    addClassDesc(newMultiArrayInstruction.arrayType().asSymbol());
            case NewObjectInstruction newObjectInstruction -> addClassDesc(newObjectInstruction.className().asSymbol());

            case NewPrimitiveArrayInstruction newPrimitiveArrayInstruction ->
                    addClassDesc(newPrimitiveArrayInstruction.typeKind().upperBound());
            case NewReferenceArrayInstruction newReferenceArrayInstruction ->
                    addClassDesc(newReferenceArrayInstruction.componentType().asSymbol());
            case OperatorInstruction operatorInstruction -> addClassDesc(operatorInstruction.typeKind().upperBound());
            case ReturnInstruction returnInstruction -> addClassDesc(returnInstruction.typeKind().upperBound());
            case StoreInstruction storeInstruction -> addClassDesc(storeInstruction.typeKind().upperBound());
            case TypeCheckInstruction typeCheckInstruction -> addClassDesc(typeCheckInstruction.type().asSymbol());
            default -> {
                // Instructions that do not reference types
            }
        }
    }

    private void addPseudoInstruction(PseudoInstruction pseudoInstruction) {
        switch (pseudoInstruction) {
            case CharacterRange characterRange -> {
            }
            case ExceptionCatch exceptionCatch -> {
            }
            case LabelTarget labelTarget -> {
            }
            case LineNumber lineNumber -> {
            }
            case LocalVariable localVariable -> {
            }
            case LocalVariableType localVariableType -> addSignature(localVariableType.signatureSymbol());
        }
    }

    private void addSignature(Signature signature) {
        switch (signature) {
            case Signature.BaseTypeSig baseTypeSig -> {
            }
            case Signature.RefTypeSig refTypeSig -> {
                switch (refTypeSig) {
                    case Signature.ArrayTypeSig arrayTypeSig -> {
                        ClassDesc classDesc = ClassDesc.ofDescriptor(arrayTypeSig.componentSignature().signatureString());
                        addClassDesc(classDesc);
                    }
                    case Signature.ClassTypeSig classTypeSig -> {
                        addClassDesc(classTypeSig.classDesc());
                        classTypeSig.typeArgs().forEach(this::addTypeArg);
                    }
                    case Signature.TypeVarSig typeVarSig -> {
                    }
                }
            }
            case Signature.ThrowableSig throwableSig -> {
            }
        }
    }

    private void addTypeArg(Signature.TypeArg typeArg) {
        switch (typeArg) {
            case Signature.TypeArg.Bounded bounded -> addSignature(bounded.boundType());
            case Signature.TypeArg.Unbounded _ -> {
                // nothing to do
            }
        }
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
