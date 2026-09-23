package de.holube.ether.cli.commands.generate.image;

import de.holube.ether.cli.mixins.HelpMixin;
import de.holube.ether.cli.mixins.RangeColorMixin;
import de.holube.ether.generators.image.ImageGeneratorResult;
import de.holube.ether.generators.image.landscape.LandscapeImageGenerator;
import picocli.CommandLine;

import java.util.concurrent.Callable;
import java.util.concurrent.ThreadLocalRandom;

@CommandLine.Command(
        name = "landscape",
        description = "Generate an image displaying a landscape.",
        sortOptions = false
)
public final class GenerateImageLandscapeCommand implements Callable<Integer> {

    @SuppressWarnings("unused")
    @CommandLine.Mixin
    private HelpMixin helpMixin;

    @SuppressWarnings("unused")
    @CommandLine.Mixin
    private RangeColorMixin rangeColorMixin;

    @SuppressWarnings("unused")
    @CommandLine.ParentCommand
    private GenerateImageCommand parentCommand;

    @SuppressWarnings("FieldMayBeFinal")
    @CommandLine.Option(
            names = {"--seed"},
            description = "Seed for the landscape generator."
    )
    private long seed = ThreadLocalRandom.current().nextLong();

    @SuppressWarnings({"FieldMayBeFinal", "FieldCanBeLocal"})
    @CommandLine.Option(
            names = {"--scale"},
            description = "Scale for the landscape generator.",
            showDefaultValue = CommandLine.Help.Visibility.ALWAYS
    )
    private double scale = 0.001;

    @SuppressWarnings({"FieldMayBeFinal", "FieldCanBeLocal"})
    @CommandLine.Option(
            names = {"--levels"},
            description = "Levels of noise added up.",
            showDefaultValue = CommandLine.Help.Visibility.ALWAYS
    )
    private int levels = 10;

    @SuppressWarnings({"FieldMayBeFinal", "FieldCanBeLocal"})
    @CommandLine.Option(
            names = {"--multi"},
            description = "Multiplier of levels.",
            showDefaultValue = CommandLine.Help.Visibility.ALWAYS
    )
    private double multi = 0.5;

    @Override
    public Integer call() {
        LandscapeImageGenerator generator = new LandscapeImageGenerator(
                parentCommand.width(),
                parentCommand.height(),
                seed,
                scale,
                levels,
                multi
        );

        ImageGeneratorResult imageResult = generator.generate();
        return GenerateImageCommand.handleImageResult(
                imageResult,
                parentCommand.outputFile(),
                parentCommand.parentCommand().noGUI(),
                "Landscape"
        );
    }

}
