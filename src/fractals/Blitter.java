package fractals;

import java.util.Arrays;

class Blitter {
    private int width;
    private int height;
    private int[] data;

    Blitter(int width, int height, int[] data) {
        if (width <= 0) {
            throw new IllegalArgumentException("Width: " + width);
        }
        if (height <= 0) {
            throw new IllegalArgumentException("Height: " + height);
        }
        if (data == null) {
            throw new NullPointerException("Data");
        }
        if (data.length != width * height) {
            throw new IllegalArgumentException("Data length: " + data.length);
        }
        this.width = width;
        this.height = height;
        this.data = data;
    }

    int getWidth() {
        return width;
    }

    int getHeight() {
        return height;
    }

    int[] getData() {
        return data;
    }

    void move(int deltax, int deltay) {
        if (deltax == 0 && deltay == 0) {
            return;
        }
        if (Math.abs(deltax) >= width || Math.abs(deltay) >= height) {
            Arrays.fill(data, 0);
            return;
        }
        int deltaIndex = deltay * width + deltax;
        int srcIndex = Math.max(-deltaIndex, 0);
        int dstIndex = Math.max(deltaIndex, 0);
        int length = width * height - Math.abs(deltaIndex);
        System.arraycopy(data, srcIndex, data, dstIndex, length);
        // TODO zero borders
    }

}
