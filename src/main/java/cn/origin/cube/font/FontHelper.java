package cn.origin.cube.font;

import java.awt.*;

public class FontHelper {
    public static Color getGradientOffset(final Color color1, final Color color2, double offset, final int alpha) {
        if (offset > 1) {
            double left = offset % 1;
            int off = (int) offset;
            offset = off % 2 == 0 ? left : 1 - left;

        }
        final double inverse_percent = 1 - offset;
        final int redPart = (int) (color1.getRed() * inverse_percent + color2.getRed() * offset);
        final int greenPart = (int) (color1.getGreen() * inverse_percent + color2.getGreen() * offset);
        final int bluePart = (int) (color1.getBlue() * inverse_percent + color2.getBlue() * offset);
        return new Color(redPart, greenPart, bluePart, alpha);
    }
}
