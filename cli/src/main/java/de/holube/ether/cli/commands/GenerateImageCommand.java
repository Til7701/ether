package de.holube.ether.cli.commands;

import de.holube.ether.cli.mixins.HelpMixin;
import lombok.Getter;
import picocli.CommandLine;

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

    @CommandLine.Mixin
    private HelpMixin helpMixin;

    @CommandLine.Option(
            names = {"-o", "--output"},
            description = "Path to the output file.",
            paramLabel = "<path/to/file.png>",
            showDefaultValue = CommandLine.Help.Visibility.ALWAYS,
            scope = CommandLine.ScopeType.INHERIT
    )
    private File outputFile = new File("output.png");

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

}
