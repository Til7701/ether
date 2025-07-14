package de.holube.ether.cli.type_converters;

import picocli.CommandLine;

import java.awt.*;

public class ColorTypeConverter implements CommandLine.ITypeConverter<Color> {

    @Override
    public Color convert(String value) {
        return Color.decode(value);
    }

}
