package cn.origin.cube.utils.render

import cn.origin.cube.font.texture.MipmapTexture
import cn.origin.cube.utils.animations.Easing
import cn.origin.cube.utils.gl.GlStateUtils
import cn.origin.cube.utils.render.texture.ImageUtil
import com.sun.javafx.geom.Vec2d
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiScreen
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL11.*
import sun.plugin2.util.ColorUtil
import java.awt.Color
import java.lang.Math.cos
import java.lang.Math.sin
//ok kotlin is homo(
object KotlinRender114514D {
    var mc = Minecraft.getMinecraft()
    //3DRender
    fun  jelloRender(entity: EntityLivingBase) {
        val drawTime = (System.currentTimeMillis() % 2000).toInt()
        val drawMode = drawTime > 1000
        var drawPercent = drawTime / 1000f
        //true when goes up
        if (!drawMode) {
            drawPercent = 1 - drawPercent
        } else {
            drawPercent -= 1
        }

        val bb = entity.entityBoundingBox
        val radius = bb.maxX - bb.minX
        val height = bb.maxY - bb.minY
        val posX = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * mc.timer.renderPartialTicks
        val posY = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * mc.timer.renderPartialTicks
        val posZ = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * mc.timer.renderPartialTicks
        val yPos = Easing.IN_OUT_QUART_POW.inc0(drawPercent) * height
        val baseMove = (if (drawPercent > 0.5) {
            1.0f - drawPercent
        } else {
            drawPercent
        }) * 2.0f
        mc.entityRenderer.disableLightmap()
        GL11.glPushMatrix()
        glEnable(GL11.GL_BLEND)
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
        GL11.glShadeModel(GL_SMOOTH)
        glDisable(GL11.GL_TEXTURE_2D)
        glEnable(GL_LINE_SMOOTH)
        glDisable(GL11.GL_DEPTH_TEST)
        glDisable(GL11.GL_LIGHTING)
        GL11.glDepthMask(false)
        glHint(GL_LINE_SMOOTH_HINT, GL_NICEST)

        GL11.glTranslated(-mc.renderManager.viewerPosX, -mc.renderManager.viewerPosY, -mc.renderManager.viewerPosZ)
        GL11.glBegin(GL11.GL_QUAD_STRIP)
        for (i in 0..360) {
            var moveFace = height * 0.4f * baseMove
            if (drawMode) {
                moveFace = -moveFace
            }
            val calc = i * Math.PI / 180
            val posX2 = posX - sin(calc) * radius
            val posZ2 = posZ + cos(calc) * radius
            GL11.glColor4f(1f, 1f, 1f, 0f)
            GL11.glVertex3d(posX2, posY + yPos + moveFace + 1e-9, posZ2)
            GL11.glColor4f(1f, 1f, 1f, 0.4F)
            GL11.glVertex3d(posX2, posY + yPos, posZ2)
        }
        GL11.glEnd()

        glLineWidth(1f)
        GL11.glBegin(GL11.GL_LINE_LOOP)
        GL11.glColor4f(1f, 1f, 1f, 1f)
        var i = 0
        while (i <= 360) {
            val x = posX - sin(i * Math.PI / 180) * radius
            val z = posZ + cos(i * Math.PI / 180) * radius
            GL11.glVertex3d(x, posY + yPos, z)
            i += 1
        }
        GL11.glEnd()

        GL11.glDepthMask(true)
        glEnable(GL11.GL_DEPTH_TEST)
        glDisable(GL_LINE_SMOOTH)
        glEnable(GL11.GL_TEXTURE_2D)
        glDisable(GL11.GL_BLEND)
        GL11.glPopMatrix()
    }
//2D
fun drawArmor(player: EntityPlayer, x: Int, y: Int) {
    GlStateManager.enableTexture2D()
    var iteration = 0
    for (itemStack in player.inventory.armorInventory) {
        iteration++
        if (itemStack.isEmpty) continue
        val x2 = x - 90 + (9 - iteration) * 20 + 2
        GlStateManager.enableDepth()
        mc.renderItem.zLevel = 200f
        mc.renderItem.renderItemAndEffectIntoGUI(itemStack, x2, y)
        mc.renderItem.renderItemOverlayIntoGUI(mc.fontRenderer, itemStack, x2, y, "")
        mc.renderItem.zLevel = 0f
        GlStateManager.enableTexture2D()
        GlStateManager.disableLighting()
        GlStateManager.disableDepth()
    }
    GlStateManager.enableDepth()
    GlStateManager.disableLighting()
}
    inline fun prepareGl() {
        GlStateUtils.texture2d(false)
        GlStateUtils.blend(true)
        GlStateUtils.smooth(true)
        GlStateUtils.lineSmooth(true)
        GlStateUtils.cull(false)
    }

    inline fun releaseGl() {
        GlStateUtils.texture2d(true)
        GlStateUtils.smooth(false)
        GlStateUtils.lineSmooth(false)
        GlStateUtils.cull(true)
    }

}