package de.holube.ether.cli.commands;

import lombok.Getter;
import picocli.CommandLine;

@CommandLine.Command
public class HelpMixin {

    @CommandLine.Option(
            names = {"-h", "--help"},
            usageHelp = true,
            description = "Display this help message."
    )
    @Getter
    private boolean helpRequested;

}
