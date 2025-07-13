package de.holube.ether.cli.commands;

import picocli.CommandLine;

@CommandLine.Command(
        name = "ether",
        mixinStandardHelpOptions = true,
        version = "ether v${ether.version}",
        description = "An image and video generation tool.",
        subcommands = {
                GenerateImageCommand.class
        }
)
public final class RootCommand {

}
