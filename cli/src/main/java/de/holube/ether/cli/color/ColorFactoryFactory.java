package de.holube.ether.cli.color;

import de.holube.ether.cli.mixins.RangeColorMixin;
import de.holube.ether.generators.color.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ColorFactoryFactory {

    public static RangeColorFactory create(RangeColorMixin mixin) {
        return switch (mixin.type()) {
            case GRAYSCALE -> new GrayscaleRangeColorFactory();
            case HUE -> new HueRangeColorFactory(mixin.saturation(), mixin.brightness());
            case MANY_SHARP -> new ManySharpRangeColorFactory(mixin.colors());
            case MANY -> new ManyRangeColorFactory(mixin.colors());
        };
    }

}
