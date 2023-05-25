package cn.origin.cube.notification;

import cn.origin.cube.event.events.client.PacketEvent;
import cn.origin.cube.module.AbstractModule;
import cn.origin.cube.module.huds.NotificationModule;
import cn.origin.cube.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NotificationManager {
    public static void moduleToggle(AbstractModule module, boolean toggled) {

        if(Minecraft.getMinecraft().world!=null&& toggled==true) add(new Notification(module.name +" is Enabled", Notification.Type.Success));
        if(Minecraft.getMinecraft().world!=null&&toggled==false) add(new Notification(module.name+ " is Disabled", Notification.Type.Error));

    }

    public static ArrayList<Notification> notifications = new ArrayList<Notification>();

    public static void add(Notification noti) {
        noti.y = notifications.size() * 25;
        notifications.add(noti);
    }

    public static void draw() {
            int i = 0;
            Notification remove = null;
            for (Notification notification : notifications) {
//            if (i == 0) {
                if (notification.x == 0) {
                    notification.in = !notification.in;
                }
                if (Math.abs(notification.x - notification.width) < 0.1 && !notification.in) {
                    remove = notification;
                }
                if (notification.in) {
                    notification.x = (float) notification.animationUtils.animate(0, notification.x, NotificationModule.INSTANCE.speed.getValue());
                } else {
                    notification.x = (float) notification.animationUtils.animate(notification.width, notification.x, NotificationModule.INSTANCE.speed.getValue());
                }
//            }
                notification.onRender();
                i++;
            }

            if (remove != null) {
                notifications.remove(remove);
            }
        }
    private Map<String , Integer> popCounter = new HashMap<>();
    @SubscribeEvent
    public void onPacketReceive(PacketEvent.Receive event)
    {
        if (event.getPacket() instanceof SPacketEntityStatus) {
            SPacketEntityStatus packet = (SPacketEntityStatus) event.getPacket();
            if (packet.getOpCode() == 35 && packet.getEntity(Utils.mc.world) instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) packet.getEntity(Utils.mc.world);
                if(player.entityId == Utils.mc.player.entityId) return;
                    int pop = countPop(player.getName());
                        if(Minecraft.getMinecraft().world!=null) add(new Notification(player.getName() + " popped " + pop + " totems!", Notification.Type.Pop));
            }
        }
    }

    public int countPop(String name)
    {
        if(!popCounter.containsKey(name))
        {
            popCounter.put(name , 1);
            return 1;
        }

        popCounter.replace(name , popCounter.get(name) + 1);
        return popCounter.get(name);
    }

    public int getPop(String name)
    {
        if(!popCounter.containsKey(name)) return 0;

        return popCounter.get(name);
    }


}
