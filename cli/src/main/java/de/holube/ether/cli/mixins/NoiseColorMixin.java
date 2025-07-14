package de.holube.ether.cli.mixins;

import lombok.Getter;
import picocli.CommandLine;

@CommandLine.Command
public final class NoiseColorMixin {

    @SuppressWarnings("FieldMayBeFinal")
    @CommandLine.Option(
            names = {"-t", "--type"},
            description = "The type of colorization to use for the noise image. Valid values: ${COMPLETION-CANDIDATES}"
    )
    @Getter
    private NoiseColorType type = NoiseColorType.GRAYSCALE;

    public enum NoiseColorType {
        GRAYSCALE,
        HUE
    }

}
