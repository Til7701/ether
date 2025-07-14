package de.holube.ether.cli.mixins;

import de.holube.ether.cli.type_converters.ColorTypeConverter;
import lombok.Getter;
import picocli.CommandLine;

import java.awt.*;

@CommandLine.Command
public final class NoiseColorMixin {

    @SuppressWarnings("FieldMayBeFinal")
    @CommandLine.Option(
            names = {"-t", "--type"},
            description = "The type of colorization to use for the noise image. Valid values: ${COMPLETION-CANDIDATES}"
    )
    @Getter
    private NoiseColorType type = NoiseColorType.GRAYSCALE;

    @SuppressWarnings("FieldMayBeFinal")
    @CommandLine.Option(
            names = {"--color1"},
            description = "The first color to use for the noise image. Only used for dual colorization.",
            converter = ColorTypeConverter.class
    )
    @Getter
    private Color color1 = Color.BLACK;

    @SuppressWarnings("FieldMayBeFinal")
    @CommandLine.Option(
            names = {"--color2"},
            description = "The second color to use for the noise image. Only used for dual colorization.",
            converter = ColorTypeConverter.class
    )
    @Getter
    private Color color2 = Color.BLUE;

    public enum NoiseColorType {
        GRAYSCALE,
        HUE,
        DUAL,
    }

}
