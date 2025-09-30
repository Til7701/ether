package de.holube.ether.cli.mixins;

import de.holube.ether.cli.type_converters.ColorTypeConverter;
import lombok.Getter;
import picocli.CommandLine;

import java.awt.*;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

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
    private List<Color> colors = List.of(Color.BLACK, Color.BLUE);

    @SuppressWarnings("FieldMayBeFinal")
    @CommandLine.Option(
            names = {"--random-colors"},
            description = "Use random colors instead of the specified ones."
    )
    @Getter
    private boolean randomColors = false;

    @SuppressWarnings("FieldMayBeFinal")
    @CommandLine.Option(
            names = {"--color-count"},
            description = "The number of random colors to use if --random-colors is set."
    )
    @Getter
    private int colorCount = 2;

    @SuppressWarnings("FieldMayBeFinal")
    @CommandLine.Option(
            names = {"--repeat-colors"},
            description = "The number of times to repeat random colors in the gradient."
    )
    @Getter
    private int repeatColors = 1;

    public enum RangeColorType {
        GRAYSCALE,
        HUE,
        MANY_SHARP,
        MANY,
    }

    public List<Color> colors() {
        if (randomColors) {
            return randomColors(colorCount);
        } else {
            return colors;
        }
    }

    private List<Color> randomColors(int count) {
        final Random random = ThreadLocalRandom.current();
        List<Color> cs = IntStream.range(0, count)
                .mapToObj(i -> new Color(
                        (int) (random.nextInt(256) * brightness),
                        (int) (random.nextInt(256) * brightness),
                        (int) (random.nextInt(256) * brightness)
                ))
                .toList();
        return IntStream.range(0, repeatColors)
                .boxed()
                .flatMap(_ -> cs.stream())
                .toList();
    }

}
