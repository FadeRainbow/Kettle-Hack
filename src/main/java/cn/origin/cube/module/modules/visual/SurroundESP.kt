package cn.origin.cube.module.modules.visual


import cn.origin.cube.Cube
import cn.origin.cube.event.events.world.Render3DEvent
import cn.origin.cube.module.ModuleInfo
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import java.awt.Color
import cn.origin.cube.module.Category

import cn.origin.cube.module.Module
import cn.origin.cube.module.modules.client.ClickGui
import cn.origin.cube.utils.render.Render3DUtil


@ModuleInfo(name = "SurroundESP", descriptions = "RenderTargetUnsafeHole", category = Category.VISUAL)
object SurroundESP : Module() {
    private val outline = registerSetting("Outline", false)
    private val lineWidth = registerSetting("LineWidth", 1.0f, 0.1f, 5.0f)
    private val burrow = registerSetting("Burrow", true)
    private val burName = registerSetting("BurrowName", true).booleanVisible(burrow)
    private val friend = registerSetting("RenderFriend", false)

    private val surroundPos = arrayListOf(
        POS(BlockPos(1, 0, 0), BlockPos(2, 0, 0)),
        POS(BlockPos(-1, 0, 0), BlockPos(-2, 0, 0)),
        POS(BlockPos(0, 0, 1), BlockPos(0, 0, 2)),
        POS(BlockPos(0, 0, -1), BlockPos(0, 0, -2)),
    )

    override fun onRender3D(event: Render3DEvent?) {
        mc.world.playerEntities.forEach {
            if (Cube.friendManager.isFriend(it) && friend.value) {
                return@forEach
            }
            val pos = BlockPos(it)
            if (it.isBurrowed()) {
                Render3DUtil.drawBlockBox(pos, Color(55, 55, 55, 75), outline.value, lineWidth.value)
                if (burName.value) {
                    Render3DUtil.drawText(
                        Vec3d(pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble()).add(
                            0.0,
                            0.15,
                            0.0
                        ),
                        it.name,
                        ClickGui.getCurrentColor()
                    )
                    Render3DUtil.drawText(
                        Vec3d(pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble()).add(
                            0.0,
                            -0.15,
                            0.0
                        ),
                        "Burrowed",
                        ClickGui.getCurrentColor()
                    )
                }
            }
            if (it.isSurround()) {
                surroundPos.forEach { sur ->
                    var color = Color(15, 15, 175, 75)
                    if (mc.world.getBlockState(pos.add(sur.pos2)).block != Blocks.AIR) {
                        color = Color(172, 15, 15, 75)
                    }
                    Render3DUtil.drawBlockBox(pos.add(sur.pos1), color, outline.value, lineWidth.value)
                    if (mc.world.getBlockState(pos.add(sur.pos2)).block != Blocks.OBSIDIAN) {
                        Render3DUtil.drawBlockBox(
                            pos.add(sur.pos2),
                            Color(172, 15, 15, 75),
                            outline.value,
                            lineWidth.value
                        )
                    }
                }
            }
        }
    }

    private fun EntityPlayer.isBurrowed(): Boolean {
        return mc.world.getBlockState(BlockPos(this)).block != Blocks.AIR
    }

    private fun EntityPlayer.isSurround(): Boolean {
        return mc.world.getBlockState(BlockPos(this).north()).block == Blocks.OBSIDIAN &&
                mc.world.getBlockState(BlockPos(this).west()).block == Blocks.OBSIDIAN &&
                mc.world.getBlockState(BlockPos(this).south()).block == Blocks.OBSIDIAN &&
                mc.world.getBlockState(BlockPos(this).east()).block == Blocks.OBSIDIAN
    }

    internal class POS(val pos1: BlockPos, val pos2: BlockPos)
}