package de.holube.ether.cli.commands.graph;

import de.holube.ether.cli.mixins.HelpMixin;
import picocli.CommandLine;

@CommandLine.Command(
        name = "graph",
        description = "Generate a graph.",
        sortOptions = false,
        subcommands = {
                JavaGraphCommand.class,
        }
)
public class GraphCommand {

    @SuppressWarnings("unused")
    @CommandLine.Mixin
    private HelpMixin helpMixin;

}
