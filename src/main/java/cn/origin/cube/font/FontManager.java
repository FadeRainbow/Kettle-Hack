package cn.origin.cube.font;

import cn.origin.cube.Cube;
import net.minecraft.client.Minecraft;

import java.awt.*;
import java.io.IOException;
import java.util.Objects;

public class FontManager {
    public MinecraftFontRenderer CustomFont;

    public MinecraftFontRenderer IconFont;

    public FontManager() throws IOException, FontFormatException {
        CustomFont = new MinecraftFontRenderer(Font.createFont(Font.PLAIN, Objects.requireNonNull(Cube.class.getResourceAsStream("/assets/fonts/CustomFont.ttf"))).deriveFont(38f));
        IconFont = new MinecraftFontRenderer(Font.createFont(Font.PLAIN, Objects.requireNonNull(Cube.class.getResourceAsStream("/assets/fonts/CubeBaseIcon.ttf"))).deriveFont(38f));
    }

    public MinecraftFontRenderer getCustomFont(float size) {
        try {
            return new MinecraftFontRenderer(Font.createFont(Font.PLAIN,Objects.requireNonNull(Cube.class.getResourceAsStream("/assets/fonts/CustomFont.ttf"))).deriveFont(size));
        } catch (FontFormatException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public MinecraftFontRenderer getIconFont(float size) {
        try {
            return new MinecraftFontRenderer(Font.createFont(Font.PLAIN,Objects.requireNonNull(Cube.class.getResourceAsStream("/assets/fonts/CubeBaseIcon.ttf"))).deriveFont(size));
        } catch (FontFormatException | IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void drawFadeFont(final String string, float x, float y,Color firstColor,Color secondColor) {
        float updateX = x;
        for (int i = 0; i < string.length(); i++) {
            String str = String.valueOf(string.charAt(i));

            double colorOffset = (Math.abs(((System.currentTimeMillis()) / 20D)) / 50) + (50 / (Cube.fontManager.CustomFont.getFontHeight() + i * 14f + 50D));
            Color color = FontHelper.getGradientOffset(new Color(firstColor.getRGB()), new Color(secondColor.getRGB()), colorOffset, 190);

            Cube.fontManager.CustomFont.drawString(str, (int) updateX, (int) y, color.hashCode());
            updateX +=Cube.fontManager.CustomFont.getStringWidth(str);
        }
    }
}
