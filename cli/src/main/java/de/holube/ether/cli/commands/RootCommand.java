package de.holube.ether.cli.commands;

import de.holube.ether.cli.commands.generate.image.GenerateImageCommand;
import de.holube.ether.cli.commands.graph.GraphCommand;
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

}
