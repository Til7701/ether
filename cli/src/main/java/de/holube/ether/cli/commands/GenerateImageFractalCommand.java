package de.holube.ether.cli.commands;

import de.holube.ether.cli.mixins.FractalColorMixin;
import de.holube.ether.cli.mixins.HelpMixin;
import de.holube.ether.generators.color.ManyRangeColorFactory;
import de.holube.ether.generators.image.ImageGeneratorResult;
import de.holube.ether.generators.image.fractal.MandelbrotsetImageGenerator;
import picocli.CommandLine;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.concurrent.Callable;

@CommandLine.Command(
        name = "fractal",
        description = "Generate an image of the Mandelbrot set fractal.",
        sortOptions = false
)
public final class GenerateImageFractalCommand implements Callable<Integer> {

    @SuppressWarnings("unused")
    @CommandLine.Mixin
    private HelpMixin helpMixin;

    @SuppressWarnings("unused")
    @CommandLine.Mixin
    private FractalColorMixin fractalColorMixin;

    @SuppressWarnings("unused")
    @CommandLine.ParentCommand
    private GenerateImageCommand parentCommand;

    @SuppressWarnings({"FieldMayBeFinal", "FieldCanBeLocal"})
    @CommandLine.Option(
            names = {"--max-iterations"},
            description = "Maximum number of iterations for the fractal calculation."
    )
    private int maxIterations = 1000;

    @SuppressWarnings({"FieldMayBeFinal", "FieldCanBeLocal"})
    @CommandLine.Option(
            names = {"--x-min"},
            description = "Minimum x-coordinate for the fractal calculation.",
            showDefaultValue = CommandLine.Help.Visibility.ALWAYS
    )
    private double xMin = -2;

    @SuppressWarnings({"FieldMayBeFinal", "FieldCanBeLocal"})
    @CommandLine.Option(
            names = {"--x-max"},
            description = "Maximum x-coordinate for the fractal calculation.",
            showDefaultValue = CommandLine.Help.Visibility.ALWAYS
    )
    private double xMax = 1;

    @SuppressWarnings({"FieldMayBeFinal", "FieldCanBeLocal"})
    @CommandLine.Option(
            names = {"--y-min"},
            description = "Minimum y-coordinate for the fractal calculation.",
            showDefaultValue = CommandLine.Help.Visibility.ALWAYS
    )
    private double yMin = -1.5;

    @SuppressWarnings({"FieldMayBeFinal", "FieldCanBeLocal"})
    @CommandLine.Option(
            names = {"--y-max"},
            description = "Maximum y-coordinate for the fractal calculation.",
            showDefaultValue = CommandLine.Help.Visibility.ALWAYS
    )
    private double yMax = 1.5;

    @Override
    public Integer call() {
        MandelbrotsetImageGenerator generator = new MandelbrotsetImageGenerator(
                parentCommand.width(),
                parentCommand.height(),
                xMin,
                xMax,
                yMin,
                yMax,
                maxIterations,
                new ManyRangeColorFactory(fractalColorMixin.inColors()),
                new ManyRangeColorFactory(fractalColorMixin.outColors())
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

}
