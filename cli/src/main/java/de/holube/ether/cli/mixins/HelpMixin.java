package de.holube.ether.cli.mixins;

import lombok.Getter;
import picocli.CommandLine;

@CommandLine.Command
public final class HelpMixin {

    @CommandLine.Option(
            names = {"-h", "--help"},
            usageHelp = true,
            description = "Display this help message."
    )
    @Getter
    private boolean helpRequested;

}
