module ether.cli {
    requires ether.generators;

    requires info.picocli;
    requires static lombok;
    requires java.desktop;

    opens de.holube.ether.cli.commands to info.picocli;

    exports de.holube.ether.cli;
}
