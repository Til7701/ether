package de.holube.ether.cli.commands.generate.image;

import de.holube.ether.cli.commands.RootCommand;
import de.holube.ether.cli.mixins.HelpMixin;
import de.holube.ether.generators.image.ImageGeneratorResult;
import de.holube.ether.viz.image.ImageAppData;
import de.holube.ether.viz.image.ImageApplication;
import lombok.Getter;
import picocli.CommandLine;

import javax.imageio.ImageIO;
import java.io.File;

@SuppressWarnings("FieldMayBeFinal")
@CommandLine.Command(
        name = "image",
        description = "Generate an image.",
        sortOptions = false,
        subcommands = {
                GenerateImageNoiseCommand.class,
                GenerateImageFractalCommand.class
        }
)
@Getter
public final class GenerateImageCommand {

    @CommandLine.ParentCommand
    private RootCommand parentCommand;

    @CommandLine.Mixin
    private HelpMixin helpMixin;

    @CommandLine.Option(
            names = {"-o", "--output"},
            description = "Path to the output file.",
            paramLabel = "<path/to/file.png>",
            scope = CommandLine.ScopeType.INHERIT
    )
    private File outputFile;

    @CommandLine.Option(
            names = {"--width"},
            description = "Width of the generated image.",
            showDefaultValue = CommandLine.Help.Visibility.ALWAYS,
            scope = CommandLine.ScopeType.INHERIT
    )
    private int width = 800;

    @CommandLine.Option(
            names = {"--height"},
            description = "Height of the generated image.",
            showDefaultValue = CommandLine.Help.Visibility.ALWAYS,
            scope = CommandLine.ScopeType.INHERIT
    )
    private int height = 600;

    public static int handleImageResult(ImageGeneratorResult result, File outputFile, boolean noGUI, String windowTitle) {
        if (outputFile != null) {
            try {
                ImageIO.write(result.result(), "png", outputFile);
                System.out.printf("Image written to %s%n", outputFile.getAbsolutePath());
            } catch (Exception e) {
                System.err.printf("Failed to write image to %s: %s%n", outputFile.getAbsolutePath(), e.getMessage());
                return 1;
            }
        }

        if (!noGUI) {
            ImageAppData data = new ImageAppData(windowTitle, result.result());
            ImageApplication.setup(data);
            ImageApplication.launchWindow();
        }
        return 0;
    }

}
