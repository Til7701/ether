package de.holube.ether.cli.mixins;

import de.holube.ether.cli.type_converters.ColorTypeConverter;
import lombok.Getter;
import picocli.CommandLine;

import java.awt.*;
import java.util.List;

@CommandLine.Command
public final class RangeColorMixin {

    @SuppressWarnings("FieldMayBeFinal")
    @CommandLine.Option(
            names = {"-t", "--type"},
            description = "The type of colorization to use for the noise image. Valid values: ${COMPLETION-CANDIDATES}"
    )
    @Getter
    private RangeColorType type = RangeColorType.GRAYSCALE;

    @SuppressWarnings("FieldMayBeFinal")
    @CommandLine.Option(
            names = {"--saturation"},
            description = "The saturation to use for the noise image. Only used for 'hue' colorization."
    )
    @Getter
    private double saturation = 1.0f;

    @SuppressWarnings("FieldMayBeFinal")
    @CommandLine.Option(
            names = {"--brightness"},
            description = "The brightness to use for the noise image. Only used for 'hue' colorization."
    )
    @Getter
    private double brightness = 1.0f;

    @SuppressWarnings("FieldMayBeFinal")
    @CommandLine.Option(
            names = {"--colors"},
            description = "The colors to use for the image. Only used for 'many' colorization.",
            converter = ColorTypeConverter.class,
            split = ",",
            defaultValue = "#000000,#0000FF"
    )
    @Getter
    private List<Color> colors = List.of(Color.BLACK, Color.BLUE);

    public enum RangeColorType {
        GRAYSCALE,
        HUE,
        MANY_SHARP,
        MANY,
    }

}
