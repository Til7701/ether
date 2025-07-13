package de.holube.ether.cli.commands;

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
        mixinStandardHelpOptions = true,
        versionProvider = VersionProvider.class,
        description = "Generate an image containing noise."
)
public class GenerateImageNoiseCommand implements Callable<Integer> {

    @SuppressWarnings("unused")
    @CommandLine.ParentCommand
    private GenerateImageCommand parentCommand;

    @SuppressWarnings("FieldMayBeFinal")
    @CommandLine.Option(
            names = {"-s", "--seed"},
            description = "Seed for the noise generator."
    )
    private long seed = ThreadLocalRandom.current().nextLong();

    @Override
    public Integer call() {
        NoiseImageGenerator generator = new NoiseImageGenerator(
                seed,
                parentCommand.width(),
                parentCommand.height()
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
