package de.holube.ether.cli.commands;

import lombok.Getter;
import picocli.CommandLine;

import java.io.File;

@SuppressWarnings("FieldMayBeFinal")
@CommandLine.Command(
        name = "image",
        mixinStandardHelpOptions = true,
        versionProvider = VersionProvider.class,
        description = "Generate an image.",
        subcommands = {
                GenerateImageNoiseCommand.class
        }
)
@Getter
public class GenerateImageCommand {

    @CommandLine.Option(
            names = {"-o", "--output"},
            description = "Path to the output file.",
            defaultValue = "output.png",
            showDefaultValue = CommandLine.Help.Visibility.ALWAYS
    )
    private File outputFile = new File("output.png");

    @CommandLine.Option(
            names = {"-w", "--width"},
            description = "Width of the generated image.",
            defaultValue = "800",
            showDefaultValue = CommandLine.Help.Visibility.ALWAYS
    )
    private int width = 800;

    @CommandLine.Option(
            names = {"-h", "--height"},
            description = "Height of the generated image.",
            defaultValue = "600",
            showDefaultValue = CommandLine.Help.Visibility.ALWAYS
    )
    private int height = 600;

}
