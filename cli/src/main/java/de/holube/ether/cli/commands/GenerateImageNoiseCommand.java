package de.holube.ether.cli.commands;

import de.holube.ether.cli.mixins.HelpMixin;
import de.holube.ether.cli.mixins.NoiseColorMixin;
import de.holube.ether.generators.color.GrayscaleNoiseColorFactory;
import de.holube.ether.generators.color.HueNoiseColorFactory;
import de.holube.ether.generators.color.NoiseColorFactory;
import de.holube.ether.generators.image.ImageGeneratorResult;
import de.holube.ether.generators.image.noise.NoiseImageGenerator;
import picocli.CommandLine;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.concurrent.Callable;
import java.util.concurrent.ThreadLocalRandom;

@CommandLine.Command(
        name = "noise",
        description = "Generate an image containing noise.",
        sortOptions = false
)
public final class GenerateImageNoiseCommand implements Callable<Integer> {

    @SuppressWarnings("unused")
    @CommandLine.Mixin
    private HelpMixin helpMixin;

    @SuppressWarnings("unused")
    @CommandLine.Mixin
    private NoiseColorMixin noiseColorMixin;

    @SuppressWarnings("unused")
    @CommandLine.ParentCommand
    private GenerateImageCommand parentCommand;

    @SuppressWarnings("FieldMayBeFinal")
    @CommandLine.Option(
            names = {"--seed"},
            description = "Seed for the noise generator."
    )
    private long seed = ThreadLocalRandom.current().nextLong();

    @SuppressWarnings({"FieldMayBeFinal", "FieldCanBeLocal"})
    @CommandLine.Option(
            names = {"--scale"},
            description = "Scale for the noise generator.",
            showDefaultValue = CommandLine.Help.Visibility.ALWAYS
    )
    private double scale = 0.01;

    @Override
    public Integer call() {
        NoiseImageGenerator generator = new NoiseImageGenerator(
                parentCommand.width(),
                parentCommand.height(),
                seed,
                scale,
                noiseColorFactory()
        );

        ImageGeneratorResult imageResult = generator.generate();

        File outputFile = parentCommand.outputFile();
        BufferedImage image = imageResult.result();
        try {
            ImageIO.write(image, "png", outputFile);
            System.out.printf("Image generated successfully: %s%n", outputFile.getAbsolutePath());
        } catch (Exception e) {
            System.err.printf("Failed to write image to file: %s%n", e.getMessage());
            return 1;
        }

        return 0;
    }

    private NoiseColorFactory noiseColorFactory() {
        return switch (noiseColorMixin.type()) {
            case GRAYSCALE -> new GrayscaleNoiseColorFactory();
            case HUE -> new HueNoiseColorFactory();
        };
    }

}
