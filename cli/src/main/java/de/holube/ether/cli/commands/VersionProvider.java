package de.holube.ether.cli.commands;

import picocli.CommandLine;

public final class VersionProvider implements CommandLine.IVersionProvider {

    @Override
    public String[] getVersion() {
        return new String[]{
                "ether v" + System.getProperty("ether.version")
        };
    }

}
