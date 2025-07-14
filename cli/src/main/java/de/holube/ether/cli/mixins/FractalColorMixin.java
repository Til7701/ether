package de.holube.ether.cli.mixins;

import de.holube.ether.cli.type_converters.ColorTypeConverter;
import lombok.Getter;
import picocli.CommandLine;

import java.awt.*;
import java.util.List;

@CommandLine.Command
public final class FractalColorMixin {

    @SuppressWarnings({"unused"})
    @CommandLine.Option(
            names = {"--out-colors"},
            description = "The colors to use to colorize the pixels outside the fractal.",
            converter = ColorTypeConverter.class,
            split = ",",
            defaultValue = "#FFFFFF,#0000FF,#FF0000,#00FF00,#FFFF00,#FF00FF,#00FFFF,#000000,#808080,#C0C0C0,#800000,#008000,#000080,#808000,#800080,#008080"
    )
    @Getter
    private List<Color> outColors;

    @SuppressWarnings("unused")
    @CommandLine.Option(
            names = {"--in-colors"},
            description = "The colors to use to colorize the pixels inside the fractal.",
            converter = ColorTypeConverter.class,
            split = ",",
            defaultValue = "#000000,#000000"
    )
    @Getter
    private List<Color> inColors;

}
