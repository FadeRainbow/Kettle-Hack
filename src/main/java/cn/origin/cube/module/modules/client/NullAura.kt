package cn.origin.cube.module.modules.client

import cn.origin.cube.module.Category
import cn.origin.cube.module.Module
import cn.origin.cube.module.ModuleInfo

/**
 *@author FadeRainbow
 *@date 2023/6/11
 *@time 13:28
 */
@ModuleInfo(name="NullAura", descriptions = "java.lang.NullPointerException", category = Category.CLIENT)
object NullAura: Module() {
    override fun onEnable() {
        throw NullPointerException()
    }
}