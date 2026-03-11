package de.holube.ether.generators.graph.java;

import java.lang.classfile.*;
import java.lang.classfile.attribute.*;
import java.lang.classfile.constantpool.*;
import java.lang.classfile.instruction.*;
import java.lang.constant.*;
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

        classModel.attributes().forEach(this::addAttribute);
        classModel.superclass().ifPresent(classDesc -> addClassDesc(classDesc.asSymbol()));
        classModel.interfaces().forEach(classDesc -> addClassDesc(classDesc.asSymbol()));
        classModel.fields().forEach(field -> {
            addClassDesc(field.fieldTypeSymbol());
            field.attributes().forEach(this::addAttribute);
        });
        classModel.methods().forEach(method -> {
            addMethodDesc(method.methodTypeSymbol());
            method.code().ifPresent(this::addCodeModel);
            method.attributes().forEach(this::addAttribute);
        });
        addConstantPool(classModel.constantPool());

        return new JavaNode(
                simpleClassName,
                packageName,
                classesThisLinksTo
        );
    }

    private void addConstantPool(ConstantPool constantPool) {
        constantPool.forEach(entry -> {
                    switch (entry) {
                        case AnnotationConstantValueEntry annotationConstantValueEntry -> {
                            addConstantDesc(annotationConstantValueEntry.constantValue());
                        }
                        case DynamicConstantPoolEntry dynamicConstantPoolEntry -> {
                        }
                        case LoadableConstantEntry loadableConstantEntry -> {
                        }
                        case MemberRefEntry memberRefEntry -> {
                        }
                        case ModuleEntry moduleEntry -> {
                        }
                        case NameAndTypeEntry nameAndTypeEntry -> {
                        }
                        case PackageEntry packageEntry -> {
                        }
                    }
                }
        );
    }

    private void addMethodDesc(MethodTypeDesc methodTypeDesc) {
        addClassDesc(methodTypeDesc.returnType());
        methodTypeDesc.parameterList().forEach(this::addClassDesc);
    }

    private void addCodeModel(CodeModel codeModel) {
        codeModel.attributes().forEach(this::addAttribute);
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

            case InvokeDynamicInstruction invokeDynamicInstruction -> {
                addMethodDesc(invokeDynamicInstruction.typeSymbol());
                invokeDynamicInstruction.bootstrapArgs().forEach(this::addConstantDesc
                );
            }
            case InvokeInstruction invokeInstruction -> {
                addMethodDesc(invokeInstruction.typeSymbol());
                addClassDesc(invokeInstruction.owner().asSymbol());
            }
            case LookupSwitchInstruction lookupSwitchInstruction -> {
            }
            case MonitorInstruction monitorInstruction -> {
            }
            case NopInstruction nopInstruction -> {
            }
            case StackInstruction stackInstruction -> {
            }
            case TableSwitchInstruction tableSwitchInstruction -> {
            }
            case ThrowInstruction throwInstruction -> {
            }
            default -> {
                // Other instructions that do not reference types
            }
        }
    }

    private void addConstantDesc(ConstantDesc constantDesc) {
        switch (constantDesc) {
            case Double aDouble -> {
            }
            case Float aFloat -> {
            }
            case Integer integer -> {
            }
            case Long aLong -> {
            }
            case String string -> {
            }
            case ClassDesc classDesc -> {
                addClassDesc(classDesc);
            }
            case DynamicConstantDesc dynamicConstantDesc -> {
            }
            case MethodHandleDesc methodHandleDesc -> {
            }
            case MethodTypeDesc methodTypeDesc -> {
                addMethodDesc(methodTypeDesc);
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
                addClassDesc(localVariable.typeSymbol());
            }
            case LocalVariableType localVariableType -> addSignature(localVariableType.signatureSymbol());
        }
    }

    private void addAttribute(Attribute<?> attribute) {
        switch (attribute) {
            case CustomAttribute customAttribute -> {
            }
            case AnnotationDefaultAttribute annotationDefaultAttribute -> {
            }
            case BootstrapMethodsAttribute bootstrapMethodsAttribute -> {
            }
            case CharacterRangeTableAttribute characterRangeTableAttribute -> {
            }
            case CodeAttribute codeAttribute -> {
            }
            case CompilationIDAttribute compilationIDAttribute -> {
            }
            case ConstantValueAttribute constantValueAttribute -> {
                addConstantDesc(constantValueAttribute.constant().constantValue());
            }
            case DeprecatedAttribute deprecatedAttribute -> {
            }
            case EnclosingMethodAttribute enclosingMethodAttribute -> {
                enclosingMethodAttribute.enclosingMethodTypeSymbol().ifPresent(this::addMethodDesc);
            }
            case ExceptionsAttribute exceptionsAttribute -> {
                exceptionsAttribute.exceptions().forEach(classDesc -> addClassDesc(classDesc.asSymbol()));
            }
            case InnerClassesAttribute innerClassesAttribute -> {
                innerClassesAttribute.classes().forEach(innerClass -> {
                    addClassDesc(innerClass.innerClass().asSymbol());
                });
            }
            case LineNumberTableAttribute lineNumberTableAttribute -> {
            }
            case LocalVariableTableAttribute localVariableTableAttribute -> {
                localVariableTableAttribute.localVariables().forEach(localVariable -> {
                    addClassDesc(localVariable.typeSymbol());
                });
            }
            case LocalVariableTypeTableAttribute localVariableTypeTableAttribute -> {
            }
            case MethodParametersAttribute methodParametersAttribute -> {
            }
            case ModuleAttribute moduleAttribute -> {
            }
            case ModuleHashesAttribute moduleHashesAttribute -> {
            }
            case ModuleMainClassAttribute moduleMainClassAttribute -> {
            }
            case ModulePackagesAttribute modulePackagesAttribute -> {
            }
            case ModuleResolutionAttribute moduleResolutionAttribute -> {
            }
            case ModuleTargetAttribute moduleTargetAttribute -> {
            }
            case NestHostAttribute nestHostAttribute -> {
            }
            case NestMembersAttribute nestMembersAttribute -> {
            }
            case PermittedSubclassesAttribute permittedSubclassesAttribute -> {
            }
            case RecordAttribute recordAttribute -> {
            }
            case RuntimeInvisibleAnnotationsAttribute runtimeInvisibleAnnotationsAttribute -> {
                runtimeInvisibleAnnotationsAttribute.annotations().forEach(this::addAnnotation);
            }
            case RuntimeInvisibleParameterAnnotationsAttribute runtimeInvisibleParameterAnnotationsAttribute -> {
                runtimeInvisibleParameterAnnotationsAttribute.parameterAnnotations().forEach(parameterAnnotation -> parameterAnnotation.forEach(this::addAnnotation));
            }
            case RuntimeInvisibleTypeAnnotationsAttribute runtimeInvisibleTypeAnnotationsAttribute -> {
                runtimeInvisibleTypeAnnotationsAttribute.annotations().forEach(this::addTypeAnnotation);
            }
            case RuntimeVisibleAnnotationsAttribute runtimeVisibleAnnotationsAttribute -> {
                runtimeVisibleAnnotationsAttribute.annotations().forEach(this::addAnnotation);
            }
            case RuntimeVisibleParameterAnnotationsAttribute runtimeVisibleParameterAnnotationsAttribute -> {
                runtimeVisibleParameterAnnotationsAttribute.parameterAnnotations().forEach(parameterAnnotation -> parameterAnnotation.forEach(this::addAnnotation));
            }
            case RuntimeVisibleTypeAnnotationsAttribute runtimeVisibleTypeAnnotationsAttribute -> {
                runtimeVisibleTypeAnnotationsAttribute.annotations().forEach(this::addTypeAnnotation);
            }
            case SignatureAttribute signatureAttribute -> {
            }
            case SourceDebugExtensionAttribute sourceDebugExtensionAttribute -> {
            }
            case SourceFileAttribute sourceFileAttribute -> {
            }
            case SourceIDAttribute sourceIDAttribute -> {
            }
            case StackMapTableAttribute stackMapTableAttribute -> {
            }
            case SyntheticAttribute syntheticAttribute -> {
            }
            case UnknownAttribute unknownAttribute -> {
            }
            default -> throw new IllegalStateException("Unexpected value: " + attribute);
        }
    }

    private void addTypeAnnotation(TypeAnnotation annotation) {
        addAnnotation(annotation.annotation());
    }

    private void addAnnotation(Annotation annotation) {
        addClassDesc(annotation.classSymbol());
        annotation.elements().forEach(this::addAnnotationElement);
    }

    private void addAnnotationElement(AnnotationElement annotationElement) {
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
