package cn.origin.cube.module.modules.combat

import cn.origin.cube.event.events.world.Render3DEvent
import cn.origin.cube.module.ModuleInfo
import cn.origin.cube.utils.player.InventoryUtil

import net.minecraft.block.BlockObsidian
import net.minecraft.init.Blocks
import net.minecraft.network.play.client.CPacketPlayer
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock
import net.minecraft.util.EnumHand
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import kotlin.math.floor
import cn.origin.cube.module.Category

import cn.origin.cube.module.Module
import cn.origin.cube.settings.BooleanSetting
import cn.origin.cube.utils.client.ChatUtil
import cn.origin.cube.utils.player.BlockUtil
import cn.origin.cube.utils.player.RotationUtil

@ModuleInfo(name = "Surround", descriptions = "Auto place block with surround", category = Category.COMBAT)
class Surround : Module(){
    private var slot = 0
    private var oldslot = 0
    private var startPos: BlockPos? = null
    var packet: BooleanSetting = registerSetting("Packet", true)
    private var rot: BooleanSetting = registerSetting("Rotate", false)
    private var center: BooleanSetting = registerSetting("AutoCenter", true)
    private var extra: BooleanSetting = registerSetting("SuperSpace", false)
    private var head: BooleanSetting = registerSetting("HeadProtect", false).booleanVisible(extra)
    override fun onEnable() {
        if (fullNullCheck()) return
        startPos = BlockPos(floor(mc.player.posX), floor(mc.player.posY), floor(mc.player.posZ))
        val centerPos = mc.player.position
        val y = centerPos.y.toDouble()
        var x = centerPos.x.toDouble()
        var z = centerPos.z.toDouble()
        val plusPlus = Vec3d(x + 0.5, y, z + 0.5)
        val plusMinus = Vec3d(x + 0.5, y, z - 0.5)
        val minusMinus = Vec3d(x - 0.5, y, z - 0.5)
        val minusPlus = Vec3d(x - 0.5, y, z + 0.5)
        if (center.value) {
            if (getDst(plusPlus) < getDst(plusMinus) && getDst(plusPlus) < getDst(minusMinus) && getDst(plusPlus) < getDst(
                    minusPlus
                )
            ) {
                x = centerPos.x + 0.5
                z = centerPos.z + 0.5
                centerPlayer(x, y, z)
            }
            if (getDst(plusMinus) < getDst(plusPlus) && getDst(plusMinus) < getDst(minusMinus) && getDst(plusMinus) < getDst(
                    minusPlus
                )
            ) {
                x = centerPos.x + 0.5
                z = centerPos.z - 0.5
                centerPlayer(x, y, z)
            }
            if (getDst(minusMinus) < getDst(plusPlus) && getDst(minusMinus) < getDst(plusMinus) && getDst(minusMinus) < getDst(
                    minusPlus
                )
            ) {
                x = centerPos.x - 0.5
                z = centerPos.z - 0.5
                centerPlayer(x, y, z)
            }
            if (getDst(minusPlus) < getDst(plusPlus) && getDst(minusPlus) < getDst(plusMinus) && getDst(minusPlus) < getDst(
                    minusMinus
                )
            ) {
                x = centerPos.x - 0.5
                z = centerPos.z + 0.5
                centerPlayer(x, y, z)
            }
        }
    }

    override fun onUpdate() {
        if (fullNullCheck()) {
            toggle()
            return
        }
        slot = InventoryUtil.findHotbarBlock(BlockObsidian::class.java)
        oldslot = mc.player.inventory.currentItem
        if (startPos != null) {
            if (startPos != BlockPos(
                    floor(mc.player.posX),
                    floor(mc.player.posY),
                    floor(mc.player.posZ)
                )
            ) {
                toggle()
                return
            }
        }
        if (slot == -1) {
            toggle()
            ChatUtil.sendMessage("Couldn't find enough material in hotbar!")
            return
        }
        for (pos in surroundPos) {
            pos.placeGhostHandFewerPacket()
        }
        if (extra.value) {
            for (blockPos in more) {
                blockPos.placeGhostHandFewerPacket()
            }
            if (head.value) {
                for (blockPos in top) {
                    blockPos.placeGhostHandFewerPacket()
                }
            }
        }
    }


    private fun BlockPos.placeGhostHandFewerPacket() {
        val obsidian = InventoryUtil.findHotbarBlock(BlockObsidian::class.java)
        if (mc.world.getBlockState(this).block != Blocks.AIR || obsidian == -1) return
        val side = BlockUtil.getFirstFacing(this) ?: return
        val neighbour = this.offset(side)
        val opposite = side.opposite
        val hitVec = Vec3d(neighbour).add(0.5, 0.5, 0.5).add(Vec3d(opposite.directionVec).scale(0.5))
        if (rot.value) {
            RotationUtil.faceVector(hitVec, true)
        }
        val currentItem = mc.player.inventory.currentItem
        InventoryUtil.switchToHotbarSlot(obsidian, false)
        if (packet.value) {
            val f: Float = (hitVec.x - this.x.toDouble()).toFloat()
            val f1: Float = (hitVec.y - this.y.toDouble()).toFloat()
            val f2: Float = (hitVec.z - this.z.toDouble()).toFloat()
            mc.player.connection.sendPacket(CPacketPlayerTryUseItemOnBlock(this, side, EnumHand.MAIN_HAND, f, f1, f2))
        } else {
            mc.playerController.processRightClickBlock(mc.player, mc.world, this, side, hitVec, EnumHand.MAIN_HAND)
        }
        InventoryUtil.switchToHotbarSlot(currentItem, false)
        mc.rightClickDelayTimer = 4
    }

    private fun getDst(vec: Vec3d): Double {
        return mc.player.positionVector.distanceTo(vec)
    }

    private fun centerPlayer(x: Double, y: Double, z: Double) {
        mc.player.connection.sendPacket(CPacketPlayer.Position(x, y, z, true))
        mc.player.setPosition(x, y, z)
    }

    private var surroundPos = arrayOf(
        BlockPos(0, -1, 0),
        BlockPos(1, -1, 0),
        BlockPos(-1, -1, 0),
        BlockPos(0, -1, 1),
        BlockPos(0, -1, -1),
        BlockPos(1, 0, 0),
        BlockPos(-1, 0, 0),
        BlockPos(0, 0, 1),
        BlockPos(0, 0, -1)
    )
    private var more = arrayOf(
        BlockPos(1, 0, 0),
        BlockPos(-1, 0, 0),
        BlockPos(0, 0, -1),
        BlockPos(0, 0, 1),
        BlockPos(1, 0, 1),
        BlockPos(-1, 0, 1),
        BlockPos(1, 0, -1),
        BlockPos(-1, 0, -1),
        BlockPos(2, 0, 0),
        BlockPos(-2, 0, 0),
        BlockPos(0, 0, -2),
        BlockPos(0, 0, 2)
    )
    private var top = arrayOf(
        BlockPos(0, 3, 0),
        BlockPos(1, 0, 0),
        BlockPos(1, 1, 0),
        BlockPos(1, 2, 0),
        BlockPos(1, 3, 0),
    )
}