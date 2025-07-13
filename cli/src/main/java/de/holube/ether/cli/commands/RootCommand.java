package de.holube.ether.cli.commands;

import picocli.CommandLine;

@CommandLine.Command(
        name = "ether",
        mixinStandardHelpOptions = true,
        versionProvider = VersionProvider.class,
        description = "An image and video generation tool.",
        subcommands = {
                GenerateImageCommand.class
        }
)
public final class RootCommand {

}
