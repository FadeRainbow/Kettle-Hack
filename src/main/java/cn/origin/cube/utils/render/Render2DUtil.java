package cn.origin.cube.utils.render;

import cn.origin.cube.utils.Utils;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;


import java.awt.*;
import java.util.Arrays;

import static org.lwjgl.opengl.GL11.*;

public class Render2DUtil extends Utils {

    public static void setColor(Color color) {
        GL11.glColor4d((double) color.getRed() / 255.0, (double) color.getGreen() / 255.0, (double) color.getBlue() / 255.0, (double) color.getAlpha() / 255.0);
    }

    public static void setColor(int color) {
        float f = (float) (color >> 24 & 255) / 255.0f;
        float f1 = (float) (color >> 16 & 255) / 255.0f;
        float f2 = (float) (color >> 8 & 255) / 255.0f;
        float f3 = (float) (color & 255) / 255.0f;
        GL11.glColor4f(f1, f2, f3, f);
    }

    public static void drawRect(float x, float y, float w, float h, int color) {
        float alpha = (float) (color >> 24 & 0xFF) / 255.0f;
        float red = (float) (color >> 16 & 0xFF) / 255.0f;
        float green = (float) (color >> 8 & 0xFF) / 255.0f;
        float blue = (float) (color & 0xFF) / 255.0f;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(x, y + h, 0.0).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(x + w, y + h, 0.0).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(x + w, y, 0.0).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(x, y, 0.0).color(red, green, blue, alpha).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawOutlineRect(double x, double y, double w, double h, float lineWidth, Color color) {
        drawLine(x, y, x + w, y, lineWidth, color);
        drawLine(x, y, x, y + h, lineWidth, color);
        drawLine(x, y + h, x + w, y + h, lineWidth, color);
        drawLine(x + w, y, x + w, y + h, lineWidth, color);
    }

    public static void drawGradientRect(double x, double y, double width, double height, GradientDirection direction, Color startColor, Color endColor) {
        glDisable(3553);
        glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glShadeModel(7425);
        Color[] result = checkColorDirection(direction, startColor, endColor);
        GL11.glBegin(7);
        setColor(result[0]);
        GL11.glVertex2d(x + width, y);
        setColor(result[1]);
        GL11.glVertex2d(x, y);
        setColor(result[2]);
        GL11.glVertex2d(x, y + height);
        setColor(result[3]);
        GL11.glVertex2d(x + width, y + height);
        GL11.glEnd();
        glDisable(3042);
        glEnable(3553);
    }

    private static void enableGL2D() {
        glDisable(2929);
        glEnable(3042);
        glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glDepthMask(true);
        glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glHint(3155, 4354);
    }

    private static void disableGL2D() {
        glEnable(3553);
        glDisable(3042);
        glDisable(2848);
        GL11.glHint(3154, 4352);
        GL11.glHint(3155, 4352);
    }

    public static void drawGradientBorderedRect(float x, float y, float x1, float y1, int insideC) {
        enableGL2D();
        x *= 2;
        x1 *= 2;
        y *= 2;
        y1 *= 2;
        glScalef(0.5F, 0.5F, 0.5F);
        drawVLine(x, y, y1 - 1, new Color(ColorUtils.getRainbow(5000, 0, 1)).getRGB());
        drawVLine(x1 - 1, y, y1, new Color(ColorUtils.getRainbow(5000, 1000, 1)).getRGB());
        drawGradientHLine(x, x1 - 1, y, new Color(ColorUtils.getRainbow(5000, 0, 1)).getRGB(), new Color(ColorUtils.getRainbow(5000, 1000, 1)).getRGB());
        drawGradientHLine(x, x1 - 2, y1 - 1, new Color(ColorUtils.getRainbow(5000, 0, 1)).getRGB(), new Color(ColorUtils.getRainbow(5000, 1000, 1)).getRGB());
        drawRect1(x + 1, y + 1, x1 - 1, y1 - 1, insideC);
        glScalef(2.0F, 2.0F, 2.0F);
        disableGL2D();
    }

    private static void fakeGuiRect(double left, double top, double right, double bottom, final int color) {
        if (left < right) {
            final double i = left;
            left = right;
            right = i;
        }
        if (top < bottom) {
            final double j = top;
            top = bottom;
            bottom = j;
        }
        final float f3 = (color >> 24 & 0xFF) / 255.0f;
        final float f4 = (color >> 16 & 0xFF) / 255.0f;
        final float f5 = (color >> 8 & 0xFF) / 255.0f;
        final float f6 = (color & 0xFF) / 255.0f;
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder bufferbuilder = tessellator.getBuffer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.color(f4, f5, f6, f3);
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION);
        bufferbuilder.pos(left, bottom, 0.0).endVertex();
        bufferbuilder.pos(right, bottom, 0.0).endVertex();
        bufferbuilder.pos(right, top, 0.0).endVertex();
        bufferbuilder.pos(left, top, 0.0).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawPolygonOutline(double startDegree, double endDegree, int corners, int x, int y, int radius, float width, int color) {
        double increment = 360 / (double) corners;
        x += radius;
        y += radius;
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
        GlStateManager.disableTexture2D();
        GlStateManager.enableAlpha();
        GlStateManager.depthMask(false);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glHint(GL_LINE_SMOOTH_HINT, GL_NICEST);
        GL11.glLineWidth(width);

        float a = (float) (color >> 24 & 255) / 255.0F;
        float r = (float) (color >> 16 & 255) / 255.0F;
        float g = (float) (color >> 8 & 255) / 255.0F;
        float b = (float) (color & 255) / 255.0F;

        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(GL_LINE_STRIP, DefaultVertexFormats.POSITION_COLOR);
        for (double i = startDegree; i <= endDegree; i += increment) {
            bufferbuilder.pos(x - Math.cos(Math.toRadians(i)) * radius, y - Math.sin(Math.toRadians(i)) * radius, 0.0D).color(r, g, b, a).endVertex();
        }
        bufferbuilder.pos(x - Math.cos(Math.toRadians(endDegree)) * radius, y - Math.sin(Math.toRadians(endDegree)) * radius, 0.0D).color(r, g, b, a).endVertex();
        tessellator.draw();
        GL11.glDisable(GL_LINE_SMOOTH);
        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    public static void drawBorderedRect(final double x, final double y, final double x1, final double y1, final double width, final int internalColor, final int borderColor) {
        enableGL2D();
        fakeGuiRect(x + width, y + width, x1 - width, y1 - width, internalColor);
        fakeGuiRect(x + width, y, x1 - width, y + width, borderColor);
        fakeGuiRect(x, y, x + width, y1, borderColor);
        fakeGuiRect(x1 - width, y, x1, y1, borderColor);
        fakeGuiRect(x + width, y1 - width, x1 - width, y1, borderColor);
        disableGL2D();
    }

    private static void drawRect1(float x, float y, float x1, float y1, int color) {
        float alpha = (color >> 24 & 255) / 255.0F;
        float red = (color >> 16 & 255) / 255.0F;
        float green = (color >> 8 & 255) / 255.0F;
        float blue = (color & 255) / 255.0F;
        Tessellator tessellator = Tessellator.getInstance();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        final BufferBuilder builder = tessellator.getBuffer();
        builder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        builder.pos(x, y1, 0.0D).color(red, green, blue, alpha).endVertex();
        builder.pos(x1, y1, 0.0D).color(red, green, blue, alpha).endVertex();
        builder.pos(x1, y, 0.0D).color(red, green, blue, alpha).endVertex();
        builder.pos(x, y, 0.0D).color(red, green, blue, alpha).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawGradientHLine(float x, float y, float x1, int color1, int color2) {
        if (y < x) {
            float var5 = x;
            x = y;
            y = var5;
        }

        drawGradientHRect(x, x1, y + 1, x1 + 1, color1, color2);
    }

    public static void drawGradientHRect(float x, float y, float x1, float y1, int topColor, int bottomColor) {
        float alpha = (topColor >> 24 & 255) / 255.0F;
        float red = (topColor >> 16 & 255) / 255.0F;
        float green = (topColor >> 8 & 255) / 255.0F;
        float blue = (topColor & 255) / 255.0F;

        float alpha2 = (bottomColor >> 24 & 255) / 255.0F;
        float red2 = (bottomColor >> 16 & 255) / 255.0F;
        float green2 = (bottomColor >> 8 & 255) / 255.0F;
        float blue2 = (bottomColor & 255) / 255.0F;
        Tessellator tessellator = Tessellator.getInstance();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.shadeModel(GL_SMOOTH);
        final BufferBuilder builder = tessellator.getBuffer();

        builder.begin(GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
        builder.pos(x, y, 0.0D).color(red, green, blue, alpha).endVertex();
        builder.pos(x, y1, 0.0D).color(red, green, blue, alpha).endVertex();

        builder.pos(x1, y1, 0.0D).color(red2, green2, blue2, alpha2).endVertex();
        builder.pos(x1, y, 0.0D).color(red2, green2, blue2, alpha2).endVertex();
        tessellator.draw();

        GlStateManager.shadeModel(GL_FLAT);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawVLine(float x, float y, float x1, int y1) {
        if (x1 < y) {
            float var5 = y;
            y = x1;
            x1 = var5;
        }

        drawRect1(x, y + 1, x + 1, x1, y1);
    }

    public static void drawRoundedRectangle(double x, double y, double width, double height, double radius, GradientDirection direction, Color startColor, Color endColor) {
        if (width < radius * 2.0 || height < radius * 2.0) {
            return;
        }
        GL11.glShadeModel(7425);
        Color[] result = checkColorDirection(direction, startColor, endColor);
        setColor(result[0]);
        drawArc(x + width - radius, y + height - radius, radius, 0.0, 90.0, 16);
        setColor(result[1]);
        drawArc(x + radius, y + height - radius, radius, 90.0, 180.0, 16);
        setColor(result[2]);
        drawArc(x + radius, y + radius, radius, 180.0, 270.0, 16);
        setColor(result[3]);
        drawArc(x + width - radius, y + radius, radius, 270.0, 360.0, 16);
        drawGradientRect(x + radius, y, width - radius * 2.0, radius, GradientDirection.LeftToRight, result[2], result[3]);
        drawGradientRect(x + radius, y + height - radius, width - radius * 2.0, radius, GradientDirection.LeftToRight, result[1], result[0]);
        drawGradientRect(x, y + radius, radius, height - radius * 2.0, GradientDirection.DownToUp, result[1], result[2]);
        drawGradientRect(x + width - radius, y + radius, radius, height - radius * 2.0, GradientDirection.DownToUp, result[0], result[3]);
        drawGradientRect(x + radius, y + radius, width - radius * 2.0, height - radius * 2.0, direction, startColor, endColor);
        GL11.glShadeModel(7424);
    }

    public static void drawArc(double cx, double cy, double r, double start_angle, double end_angle, int num_segments) {
        glDisable(3553);
        glEnable(3042);
        GL11.glBegin(4);
        int i = (int) ((double) num_segments / (360.0 / start_angle)) + 1;
        while ((double) i <= (double) num_segments / (360.0 / end_angle)) {
            double previousangle = Math.PI * 2 * (double) (i - 1) / (double) num_segments;
            double angle = Math.PI * 2 * (double) i / (double) num_segments;
            GL11.glVertex2d(cx, cy);
            GL11.glVertex2d(cx + Math.cos(angle) * r, cy + Math.sin(angle) * r);
            GL11.glVertex2d(cx + Math.cos(previousangle) * r, cy + Math.sin(previousangle) * r);
            ++i;
        }
        if (end_angle == 360) {
            GL11.glVertex2d(cx, cy);
            GL11.glVertex2d(cx + Math.cos(360) * r, cy + Math.sin(360) * r);
        }
        GL11.glEnd();
        glDisable(3042);
        glEnable(3553);
    }

    public static void drawRoundedRectangle(double x, double y, double width, double height, double radius, Color color) {
        drawRoundedRectangle(x, y, width, height, radius, GradientDirection.Normal, color, color);
    }

    public static void drawLine(Double x1, Double y1, Double x2, Double y2, Float lineWidth) {
        glDisable(3553);
        glEnable(3042);
        GL11.glLineWidth(lineWidth);
        GL11.glShadeModel(7425);
        GL11.glBegin(2);
        GL11.glVertex2d(x1, y1);
        GL11.glVertex2d(x2, y2);
        GL11.glEnd();
        GL11.glShadeModel(7424);
        glDisable(3042);
        glEnable(3553);
    }

    public static void drawLine(double x1, double y1, double x2, double y2, float lineWidth, Color ColorStart) {
        glDisable(3553);
        glEnable(3042);
        GL11.glLineWidth(lineWidth);
        GL11.glShadeModel(7425);
        GL11.glBegin(2);
        setColor(ColorStart);
        GL11.glVertex2d(x1, y1);
        GL11.glVertex2d(x2, y2);
        GL11.glEnd();
        GL11.glShadeModel(7424);
        glDisable(3042);
        glEnable(3553);
    }

    private static Color[] checkColorDirection(GradientDirection direction, Color start, Color end) {
        Color[] dir = new Color[4];
        if (direction == GradientDirection.Normal) {
            Arrays.fill(dir, start);
        } else if (direction == GradientDirection.DownToUp) {
            dir[0] = start;
            dir[1] = start;
            dir[2] = end;
            dir[3] = end;
        } else if (direction == GradientDirection.UpToDown) {
            dir[0] = end;
            dir[1] = end;
            dir[2] = start;
            dir[3] = start;
        } else if (direction == GradientDirection.RightToLeft) {
            dir[0] = start;
            dir[1] = end;
            dir[2] = end;
            dir[3] = start;
        } else if (direction == GradientDirection.LeftToRight) {
            dir[0] = end;
            dir[1] = start;
            dir[2] = start;
            dir[3] = end;
        } else {
            Arrays.fill(dir, Color.WHITE);
        }
        return dir;
    }

    public enum GradientDirection {
        LeftToRight,
        RightToLeft,
        UpToDown,
        DownToUp,
        Normal
    }

    public static void drawGlow(double x, double y, double x1, double y1, int color) {
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.shadeModel(7425);
        drawVGradientRect((int) x, (int) y, (int) x1, (int) (y + (y1 - y) / 2f), ColorUtils.toRGBA(new Color(color).getRed(), new Color(color).getGreen(), new Color(color).getBlue(), 0), color);
        drawVGradientRect((int) x, (int) (y + (y1 - y) / 2f), (int) x1, (int) y1, color, ColorUtils.toRGBA(new Color(color).getRed(), new Color(color).getGreen(), new Color(color).getBlue(), 0));
        int radius = (int) ((y1 - y) / 2f);
        drawPolygonPart(x, (y + (y1 - y) / 2f), radius, 0, color, ColorUtils.toRGBA(new Color(color).getRed(), new Color(color).getGreen(), new Color(color).getBlue(), 0));
        drawPolygonPart(x, (y + (y1 - y) / 2f), radius, 1, color, ColorUtils.toRGBA(new Color(color).getRed(), new Color(color).getGreen(), new Color(color).getBlue(), 0));
        drawPolygonPart(x1, (y + (y1 - y) / 2f), radius, 2, color, ColorUtils.toRGBA(new Color(color).getRed(), new Color(color).getGreen(), new Color(color).getBlue(), 0));
        drawPolygonPart(x1, (y + (y1 - y) / 2f), radius, 3, color, ColorUtils.toRGBA(new Color(color).getRed(), new Color(color).getGreen(), new Color(color).getBlue(), 0));
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

    public static void drawVGradientRect(float left, float top, float right, float bottom, int startColor, int endColor) {
        float f = (float) (startColor >> 24 & 255) / 255.0F;
        float f1 = (float) (startColor >> 16 & 255) / 255.0F;
        float f2 = (float) (startColor >> 8 & 255) / 255.0F;
        float f3 = (float) (startColor & 255) / 255.0F;
        float f4 = (float) (endColor >> 24 & 255) / 255.0F;
        float f5 = (float) (endColor >> 16 & 255) / 255.0F;
        float f6 = (float) (endColor >> 8 & 255) / 255.0F;
        float f7 = (float) (endColor & 255) / 255.0F;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.shadeModel(7425);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(right, top, 0).color(f1, f2, f3, f).endVertex();
        bufferbuilder.pos(left, top, 0).color(f1, f2, f3, f).endVertex();
        bufferbuilder.pos(left, bottom, 0).color(f5, f6, f7, f4).endVertex();
        bufferbuilder.pos(right, bottom, 0).color(f5, f6, f7, f4).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

    public static void drawPolygonPart(double x, double y, int radius, int part, int color, int endcolor) {
        float alpha = (float) (color >> 24 & 255) / 255.0F;
        float red = (float) (color >> 16 & 255) / 255.0F;
        float green = (float) (color >> 8 & 255) / 255.0F;
        float blue = (float) (color & 255) / 255.0F;
        float alpha1 = (float) (endcolor >> 24 & 255) / 255.0F;
        float red1 = (float) (endcolor >> 16 & 255) / 255.0F;
        float green1 = (float) (endcolor >> 8 & 255) / 255.0F;
        float blue1 = (float) (endcolor & 255) / 255.0F;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.shadeModel(7425);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(GL_TRIANGLE_FAN, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(x, y, 0).color(red, green, blue, alpha).endVertex();
        final double TWICE_PI = Math.PI * 2;
        for (int i = part * 90; i <= part * 90 + 90; i++) {
            double angle = (TWICE_PI * i / 360) + Math.toRadians(180);
            bufferbuilder.pos(x + Math.sin(angle) * radius, y + Math.cos(angle) * radius, 0).color(red1, green1, blue1, alpha1).endVertex();
        }
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

    public static void renderPlayer2d(AbstractClientPlayer abstractClientPlayer, int x, int y, int width, int height) {
        preRender();
        GlStateManager.color(1f, 1f, 1f, 1f);
        mc.getTextureManager().bindTexture(abstractClientPlayer.getLocationSkin());
        GuiScreen.drawScaledCustomSizeModalRect(
                x, y,
                32f,
                32f,
                31,
                31,
                width,
                height,
                255f,
                255f
        );
        postRender();
    }
    private static void preRender() {
        GL11.glPushMatrix();
        GlStateManager.pushMatrix();
        GlStateManager.disableAlpha();
        GlStateManager.clear(256);
        GlStateManager.enableBlend();
    }

    private static void postRender() {
        GlStateManager.disableBlend();
        GlStateManager.disableDepth();
        GlStateManager.disableLighting();
        GlStateManager.enableDepth();
        GlStateManager.enableAlpha();
        GlStateManager.popMatrix();
        GL11.glPopMatrix();
    }


}