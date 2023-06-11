package cn.origin.cube.notification

import cn.origin.cube.event.events.client.PacketEvent
import cn.origin.cube.module.AbstractModule
import cn.origin.cube.module.huds.NotificationModule
import cn.origin.cube.utils.Utils
import net.minecraft.client.Minecraft
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.network.Packet
import net.minecraft.network.play.server.SPacketEntityStatus
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

/**
 *@author FadeRainbow
 *@date 2023/6/11
 *@time 13:56
 */
    object NotificationManager {
    fun moduleToggle(module: AbstractModule, toggled: Boolean) {
        if (Minecraft.getMinecraft().world != null && toggled) NotificationManager.add(
            Notification(
                module.name + " is Enabled",
                Notification.Type.Success
            )
        )
        if (Minecraft.getMinecraft().world != null && !toggled) NotificationManager.add(
            Notification(
                module.name + " is Disabled",
                Notification.Type.Error
            )
        )
    }

    var notifications = ArrayList<Notification>()
    fun add(noti: Notification) {
        noti.y = (NotificationManager.notifications.size * 25).toFloat()
        NotificationManager.notifications.add(noti)
    }

    fun draw() {
        var i = 0
        var remove: Notification? = null
        for (notification in NotificationManager.notifications) {
//            if (i == 0) {
            if (notification.x == 0f) {
                notification.`in` = !notification.`in`
            }
            if (Math.abs(notification.x - notification.width) < 0.1 && !notification.`in`) {
                remove = notification
            }
            if (notification.`in`) {
                notification.x =
                    notification.animationUtils.animate(0f, notification.x, NotificationModule.INSTANCE.speed.value)
            } else {
                notification.x = notification.animationUtils.animate(
                    notification.width,
                    notification.x.toDouble(),
                    NotificationModule.INSTANCE.speed.value.toDouble()
                ).toFloat()
            }
            //            }
            notification.onRender()
            i++
        }
        if (remove != null) {
            NotificationManager.notifications.remove(remove)
        }
    }

    class Counter {
        val popCounter: MutableMap<String, Int> = HashMap()

        @SubscribeEvent
        fun onPacketReceive(event: PacketEvent.Receive) {
            if (event.getPacket<Packet<*>>() is SPacketEntityStatus) {
                val packet = event.getPacket<Packet<*>>() as SPacketEntityStatus
                if (packet.opCode.toInt() == 35 && packet.getEntity(Utils.mc.world) is EntityPlayer) {
                    val player = packet.getEntity(Utils.mc.world) as EntityPlayer
                    if (player.entityId == Utils.mc.player.entityId) return
                    val pop: Int = countPop(player.name)
                    if (Minecraft.getMinecraft().world != null) NotificationManager.add(
                        Notification(
                            player.name + " popped " + pop + " totems!",
                            Notification.Type.Pop
                        )
                    )
                }
            }
        }

        fun countPop(name: String): Int {
            if (!popCounter.containsKey(name)) {
                popCounter[name] = 1
                return 1
            }
            popCounter.replace(name, popCounter[name]!! + 1)
            return popCounter[name]!!
        }
    }
}
