package cn.origin.cube.module.modules.function

import cn.origin.cube.event.events.world.Render3DEvent
import cn.origin.cube.module.Category
import cn.origin.cube.module.Module
import cn.origin.cube.module.ModuleInfo
import cn.origin.cube.module.ModuleManager
import org.lwjgl.opengl.GL11

/**
 *@author FadeRainbow
 *@date 2023/6/11
 *@time 13:15
 */
/**
 * Gl：I want to die
 * x_x
 */
@ModuleInfo(name="OpenGLBoom", descriptions ="FQ", category = Category.FUNCTION)
object OpenGLBoom: Module() {
    override fun onRender3D(event: Render3DEvent?) {
      while (this.isEnabled){
          GL11.glPushMatrix()

      }
    }
}
