package cn.origin.cube.module.huds

import cn.origin.cube.Cube
import cn.origin.cube.module.Category
import cn.origin.cube.module.HudModule
import cn.origin.cube.module.HudModuleInfo
import cn.origin.cube.module.modules.client.ClickGui
import cn.origin.cube.settings.FloatSetting
import org.lwjgl.opengl.GL11

/**
 * @author LovelyRainbow
 * @date 01/05/2023
 * @time 08:41
 */
@HudModuleInfo(name = "WaterMark",x= 114F,y= 114F, descriptions = "Show hack name", category = Category.HUD)

class WaterMark: HudModule() {
    private var scala: FloatSetting =registerSetting("Scala",1.0f,0.0f,0.4f)
    override fun onRender2D() {
        GL11.glPushMatrix()
        GL11.glTranslated(this.x.toDouble(),this.y.toDouble(), 0.0)
        GL11.glScaled(scala.value.toDouble(), scala.getValue().toDouble(), 0.0)
        Cube.fontManager.CustomFont.drawString("Cube Base", 0, 0, ClickGui.getCurrentColor().rgb)

        GL11.glPopMatrix()
        width = (Cube.fontManager.CustomFont.getStringWidth("Cube Base").toFloat() * scala.getValue()).toInt()
            .toFloat()
        height = (Cube.fontManager.CustomFont.height.toFloat() * scala.getValue()).toInt().toFloat()
    }
    }
