package de.holube.ether.cli.commands;

import de.holube.ether.cli.commands.generate.image.GenerateImageCommand;
import de.holube.ether.cli.commands.graph.GraphCommand;
import lombok.Getter;
import picocli.CommandLine;

@CommandLine.Command(
        name = "ether",
        mixinStandardHelpOptions = true,
        version = "ether v${ether.version}",
        description = "An image and video generation tool.",
        subcommands = {
                GenerateImageCommand.class,
                GraphCommand.class,
        }
)
public final class RootCommand {

    @SuppressWarnings("FieldMayBeFinal")
    @CommandLine.Option(
            names = {"--no-gui"},
            description = "Run in no-GUI mode. Does not show the generated images in a window.",
            showDefaultValue = CommandLine.Help.Visibility.ALWAYS,
            scope = CommandLine.ScopeType.INHERIT
    )
    @Getter
    private boolean noGUI = false;

}
