package fractals;

import fractals.filter.DefaultPalette;
import fractals.filter.DynamicPalette;
import fractals.impl.JuliaSetBuilder;
import fractals.impl.MandelbrotSetBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * List of predefined objects - fractals, filters, etc.
 */

public class FractalRegistry {

    private static final FractalRegistry INSTANCE = new FractalRegistry();

    /**
     * Returns the single instance of this class.
     *
     * @return the single instance of this class
     */

    public static FractalRegistry getInstance() {
        return INSTANCE;
    }

    private List<FractalBuilder> fractals;
    private List<Filter> palettes;

    private FractalRegistry() {
        fractals = new ArrayList<>();
        fractals.add(new MandelbrotSetBuilder());
        fractals.add(new JuliaSetBuilder());
        palettes = Arrays.asList(//
                new DynamicPalette("Dynamic gray", 0xFFFFFF), //
                new DynamicPalette("Dynamic red", 0xFF0000), //
                new DynamicPalette("Dynamic green", 0x00FF00), //
                new DynamicPalette("Dynamic blue", 0x0000FF), //
                DefaultPalette.getInstance()//
        );
    }

    /**
     * Returns the list of predefined fractals.
     *
     * @return list of predefined fractals
     */

    public List<FractalBuilder> getFractals() {
        return fractals;
    }

    /**
     * Returns the list of predefined palettes.
     *
     * @return list of predefined palettes
     */

    public List<Filter> getPalettes() {
        return palettes;
    }

}
