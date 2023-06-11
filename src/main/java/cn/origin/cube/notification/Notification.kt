package cn.origin.cube.notification

import cn.origin.cube.module.huds.NotificationModule
import cn.origin.cube.utils.Utils
import cn.origin.cube.utils.animations.AnimationUtils
import cn.origin.cube.utils.render.Render2DUtil
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.ScaledResolution
import org.lwjgl.opengl.GL11
import java.awt.Color

/**
 *@author FadeRainbow
 *@date 2023/6/11
 *@time 13:49
 */

class Notification(//Mush Hack Notification(my other hack
    var text: String, var type: Type
) : Utils() {
    var width: Double
    var height = 30.0
    var x: Float
    var y = 0f
    public var `in` = true
    var animationUtils = AnimationUtils()
    var yAnimationUtils = AnimationUtils()

    init {
        width = (Minecraft.getMinecraft().fontRenderer.getStringWidth(text) + 30).toDouble()
        x = width.toFloat()
    }

    fun onRender() {
        val ribbonColor: Color
        ribbonColor =
            if (type == Type.Success) Color(154, 243, 118, 170) else if (type == Type.Error) Color(
                255,
                99,
                99,
                170
            ) else {
                Color(255, 192, 69)
                //            int i = Math.max(0, Math.min(255, (int) (Math.sin(time / 100.0) * 255.0 / 2 + 127.5)));
            }
        var i = 0
        for (notification in NotificationManager.notifications) {
            if (notification === this) {
                break
            }
            i++
        }
        y = yAnimationUtils.animate((i.toFloat() * (height + 4)).toFloat(), y, NotificationModule.INSTANCE.speed.value)
        val sr = ScaledResolution(Minecraft.getMinecraft())
        drawRect(
            sr.scaledWidth + x - width,
            sr.scaledHeight - 50 - y - height,
            sr.scaledWidth + x,
            sr.scaledHeight - 50 - y,
            ribbonColor.rgb
        )
        Cube.fontManager.CustomFont.drawString(
            text,
            (sr.scaledWidth + x - width + 10).toFloat(),
            (sr.scaledHeight - 50f - y - 18), Color(204, 204, 204, 232).rgb
        )
    }

    //why in it?
    private fun drawRect(x1: Double, y1: Double, x2: Float, y2: Float, color: Int) {
        GL11.glPushMatrix()
        GL11.glEnable(3042)
        GL11.glDisable(3553)
        GL11.glBlendFunc(770, 771)
        GL11.glEnable(2848)
        GL11.glPushMatrix()
        Render2DUtil.setColor(color)
        GL11.glBegin(7)
        GL11.glVertex2d(x2.toDouble(), y1)
        GL11.glVertex2d(x1, y1)
        GL11.glVertex2d(x1, y2.toDouble())
        GL11.glVertex2d(x2.toDouble(), y2.toDouble())
        GL11.glEnd()
        GL11.glPopMatrix()
        GL11.glEnable(3553)
        GL11.glDisable(3042)
        GL11.glDisable(2848)
        GL11.glPopMatrix()
    }

    enum class Type {
        Success,
        Error,
        Pop
    }
}