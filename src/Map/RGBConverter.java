package Map;

public class RGBConverter {
    
    /**
     * Converts an RGB integer to an array of [r, g, b] values
     * @param rgbInt The RGB value as a single integer
     * @return int array with [r, g, b] values (0-255)
     */
    public static int[] rgbIntToArray(int rgbInt) {
        int[] rgb = new int[3];
        rgb[0] = (rgbInt >> 16) & 0xFF;  // Red
        rgb[1] = (rgbInt >> 8) & 0xFF;   // Green
        rgb[2] = rgbInt & 0xFF;          // Blue
        return rgb;
    }
    
    /**
     * Converts RGB array to a single integer
     * @param rgb Array with [r, g, b] values (0-255)
     * @return RGB as a single integer
     */
    public static int rgbArrayToInt(int[] rgb) {
        return (rgb[0] << 16) | (rgb[1] << 8) | rgb[2];
    }
} 