package de.holube.ether.cli;


import de.holube.ether.cli.commands.RootCommand;
import picocli.CommandLine;

public class Main {

    public static void main(String[] args) {
        CommandLine.Help.ColorScheme colorScheme = new CommandLine.Help.ColorScheme.Builder()
                .commands(CommandLine.Help.Ansi.Style.bold, CommandLine.Help.Ansi.Style.underline)
                .options(CommandLine.Help.Ansi.Style.fg("2;5;4"))
                .parameters(CommandLine.Help.Ansi.Style.fg("0;5;5"))
                .optionParams(CommandLine.Help.Ansi.Style.italic)
                .errors(CommandLine.Help.Ansi.Style.fg_red, CommandLine.Help.Ansi.Style.bold)
                .stackTraces(CommandLine.Help.Ansi.Style.italic)
                .applySystemProperties() // optional: allow end users to customize
                .ansi(CommandLine.Help.Ansi.ON)
                .build();
        CommandLine cli = new CommandLine(new RootCommand());
        cli.setColorScheme(colorScheme);

        int exitCode = cli.execute(args);

        System.exit(exitCode);
    }

}
