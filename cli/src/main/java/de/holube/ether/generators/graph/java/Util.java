package de.holube.ether.generators.graph.java;

import lombok.experimental.UtilityClass;

import java.lang.constant.ClassDesc;

@UtilityClass
class Util {

    static String getFullyQualifiedClassName(ClassDesc classDesc) {
        String packageName = classDesc.packageName();
        String simpleClassName = classDesc.displayName();

        return getFullyQualifiedClassName(packageName, simpleClassName);
    }

    static String getFullyQualifiedClassName(String packageName, String simpleClassName) {
        return packageName.isEmpty()
                ? simpleClassName
                : packageName + "." + simpleClassName;
    }

}
