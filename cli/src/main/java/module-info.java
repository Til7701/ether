module ether.cli {
    requires ether.commons;

    requires static lombok;

    requires info.picocli;
    requires java.desktop;
    requires javafx.controls;
    requires javafx.graphics;

    opens de.holube.ether.cli.commands to info.picocli;
    opens de.holube.ether.cli.commands.generate.image to info.picocli;
    opens de.holube.ether.cli.commands.graph to info.picocli;
    opens de.holube.ether.cli.mixins to info.picocli;
    opens de.holube.ether.cli.type_converters to info.picocli;

    opens de.holube.ether.viz.image to javafx.graphics;
    opens de.holube.ether.viz.graph.internal to javafx.graphics;

    exports de.holube.ether.cli;
}
