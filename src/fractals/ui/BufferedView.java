package fractals.ui;

import fractals.*;
import fractals.filter.AntialiasingFilter;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;
import java.util.List;

public class BufferedView extends View {
    private Filter paletteFilter;
    private int width;
    private int height;
    private BufferedImage backImage;

    public BufferedView(Filter paletteFilter, int width, int height) {
        this.paletteFilter = paletteFilter;
        this.width = width;
        this.height = height;
    }

    @Override
    protected RenderingContext getFinalContext(RenderingHints hints) {
        int width = getWidth();
        int height = getHeight();
        if (backImage == null || backImage.getWidth() != width || backImage.getHeight() != height) {
            backImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        }
        return new SimpleRenderingContext(width, height, ((DataBufferInt) backImage.getRaster().getDataBuffer()).getData());
    }

    @Override
    protected List<Filter> getFilters(RenderingHints hints) {
        List<Filter> result = new ArrayList<>(2);
        result.add(AntialiasingFilter.ANTIALIASING_2);
        result.add(paletteFilter);
        return result;
    }

    public BufferedImage getImage() {
        return backImage;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

}
