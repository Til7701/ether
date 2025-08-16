module ether.cli {
    requires ether.generators;

    requires info.picocli;
    requires static lombok;
    requires java.desktop;

    opens de.holube.ether.cli.commands to info.picocli;
    opens de.holube.ether.cli.mixins to info.picocli;
    opens de.holube.ether.cli.type_converters to info.picocli;

    exports de.holube.ether.cli;
    opens de.holube.ether.cli.commands.generate.image to info.picocli;
    opens de.holube.ether.cli.commands.graph to info.picocli;
}
