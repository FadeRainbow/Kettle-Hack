package cn.origin.cube.module

import cn.origin.cube.event.events.world.Render3DEvent
import cn.origin.cube.module.huds.ModuleArrayList
import cn.origin.cube.module.huds.NotificationModule
import cn.origin.cube.module.huds.TargetHUD
import cn.origin.cube.module.huds.WaterMark
import cn.origin.cube.module.modules.client.ClickGui
import cn.origin.cube.module.modules.client.HudEditor
import cn.origin.cube.module.modules.client.NullAura
import cn.origin.cube.module.modules.combat.KillAura
import cn.origin.cube.module.modules.combat.Replenish
import cn.origin.cube.module.modules.combat.Surround
import cn.origin.cube.module.modules.function.*
import cn.origin.cube.module.modules.movement.AutoWalk
import cn.origin.cube.module.modules.movement.Sprint
import cn.origin.cube.module.modules.visual.*
import cn.origin.cube.module.modules.world.AutoRespawn
import cn.origin.cube.module.modules.world.FakePlayer

class ModuleManager {
    var allModuleList = ArrayList<AbstractModule>()
    var moduleList = ArrayList<Module>()
    var hudList = ArrayList<HudModule>()

    init {
        //Client
        registerModule(ClickGui())
        registerModule(HudEditor())
        registerModule(NullAura)

        //Combat
        registerModule(Surround())
        registerModule(KillAura())
        registerModule(Replenish())

        //Function
        registerModule(MiddleClick())
        registerModule(FakeKick())
        registerModule(NoRotate())
        registerModule(AntiFps())
        registerModule(OpenGLBoom)

        //Movement
        registerModule(Sprint())
        registerModule(AutoWalk())

        //Visual
        registerModule(FullBright())
        registerModule(BlockHighlight())
        registerModule(HoleESP())
        registerModule(ESP())
        registerModule(NameTags())
        registerModule(ShaderCharms())
        registerModule(SurroundESP)
        registerModule(BreakESP())
        registerModule(TargetHUD())

        //World
        registerModule(FakePlayer())
        registerModule(AutoRespawn())

        //Hud
        registerModule(WaterMark())
        registerModule(ModuleArrayList())
        registerModule(NotificationModule())

    }

    private fun registerModule(module: AbstractModule) {
        if (!allModuleList.contains(module)) allModuleList.add(module)
        if (module.isHud) {
            if (!hudList.contains(module)) hudList.add(module as HudModule)
        } else if (!moduleList.contains(module)) {
            moduleList.add(module as Module)
        }
    }

    fun getModulesByCategory(category: Category): List<AbstractModule> {
        return allModuleList.filter { it.category == category }
    }

    fun getModuleByClass(clazz: Class<*>): AbstractModule? {
        for (abstractModule in allModuleList) {
            if (abstractModule::class.java == clazz) return abstractModule
        }
        return null
    }


    fun getModuleByName(name: String): AbstractModule? {
        for (abstractModule in allModuleList) {
            if (abstractModule.name.lowercase() == name.lowercase()) return abstractModule
        }
        return null
    }

    fun onUpdate() {
        allModuleList.filter { it.isEnabled }.forEach { it.onUpdate() }
    }

    fun onLogin() {
        allModuleList.filter { it.isEnabled }.forEach { it.onLogin() }
    }

    fun onLogout() {
        allModuleList.filter { it.isEnabled }.forEach { it.onLogout() }
    }

    fun onRender3D(event: Render3DEvent) {
        allModuleList.filter { it.isEnabled }.forEach { it.onRender3D(event) }
    }

    fun onRender2D() {
        allModuleList.filter { it.isEnabled }.forEach { it.onRender2D() }
    }
}