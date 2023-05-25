package cn.origin.cube.notification;



import cn.origin.cube.Cube;

import cn.origin.cube.event.events.client.PacketEvent;
import cn.origin.cube.module.huds.NotificationModule;
import cn.origin.cube.utils.Utils;
import cn.origin.cube.utils.animations.AnimationUtils;
import cn.origin.cube.utils.animations.Easing;
import cn.origin.cube.utils.render.KotlinRender114514D;
import cn.origin.cube.utils.render.Render2DUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class Notification extends Utils {
//Mush Hack Notification(my other hack
    public String text;
    public double width, height = 30;
    public float x;
    Type type;
    public float y;
    public boolean in = true;
    AnimationUtils animationUtils = new AnimationUtils();
    AnimationUtils yAnimationUtils = new AnimationUtils();


    public Notification(String text, Type type) {
        this.text = text;
        this.type = type;
        width = Minecraft.getMinecraft().fontRenderer.getStringWidth(text) + 30;
        x = (float) width;
    }


    public void onRender() {
        Color ribbonColor;
        if (type == Type.Success)
            ribbonColor = new Color(154, 243, 118,170);
        else if (type ==Type.Error)
            ribbonColor = new Color(255, 99, 99, 170);
        else {
            ribbonColor = new Color(255, 192, 69);
//            int i = Math.max(0, Math.min(255, (int) (Math.sin(time / 100.0) * 255.0 / 2 + 127.5)));
        }

        int i = 0;
        for (Notification notification : NotificationManager.notifications) {
            if (notification == this) {
                break;
            }
            i++;
        }
        y = yAnimationUtils.animate((float) ((float) i * (height + 4)), y, NotificationModule.INSTANCE.speed.getValue() );
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
            drawRect((sr.getScaledWidth() + x - width),
              (sr.getScaledHeight() - 50 - y - height),
                sr.getScaledWidth() + x,
                sr.getScaledHeight() - 50 - y,
                ribbonColor.getRGB());
        Cube.fontManager.CustomFont.drawString(text, ((float) (sr.getScaledWidth() + x - width + 10)), ((float) (sr.getScaledHeight() - 50f - y - 18)), new Color(204, 204, 204, 232).getRGB());
    }
    //why in it?
    private void drawRect(double x1, double y1, float x2, float y2, int color) {
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glPushMatrix();
        Render2DUtil.setColor(color);
        GL11.glBegin(7);
        GL11.glVertex2d(x2, y1);
        GL11.glVertex2d(x1, y1);
        GL11.glVertex2d(x1, y2);
        GL11.glVertex2d(x2, (double) y2);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
        GL11.glPopMatrix();
    }
    public enum Type {
        Success,
        Error,
        Pop

    }
}