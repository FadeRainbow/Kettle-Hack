package cn.origin.cube.module.huds;


import cn.origin.cube.Cube;
import cn.origin.cube.module.Category;
import cn.origin.cube.module.HudModule;
import cn.origin.cube.module.HudModuleInfo;
import cn.origin.cube.module.modules.visual.NameTags;
import cn.origin.cube.notification.Notification;
import cn.origin.cube.settings.FloatSetting;
import cn.origin.cube.utils.player.EntityUtil;
import cn.origin.cube.utils.player.PlayerUtil;
import cn.origin.cube.utils.render.ColorUtils;
import cn.origin.cube.utils.render.KotlinRender114514D;
import cn.origin.cube.utils.render.Render2DUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;


import java.awt.*;
import java.util.Comparator;

import static cn.origin.cube.utils.player.EntityUtil.getHealth;
import static org.lwjgl.opengl.GL11.GL_LINE_SMOOTH;

@HudModuleInfo(name = "TargetHud", x = 114, y = 114, descriptions = "show target", category = Category.HUD)
public class TargetHUD extends HudModule {
    public  static AbstractClientPlayer target;

    @Override
    public void onRender2D() {

        try {
            if (fullNullCheck()) return;
            target = (AbstractClientPlayer) getNearestPlayer(30.0);
            if (target == null) return;
            Render2DUtil.drawRect(this.x, this.y, this.width, this.height,new Color(10,10,10,200).getRGB());
            Render2DUtil.renderPlayer2d(target, (int) (this.x+5), (int) (this.y + 5),36,36);
            Cube.fontManager.CustomFont.drawString( "Name "+target.getName(), (int) (this.x + 43), (int) (this.y+ 10),Color.WHITE.getRGB());
            String distance = "Distance " + String.format("%.1f", mc.player.getDistance(target));
            String health = "Health " + String.format("%.1f",  target.getAbsorptionAmount()+target.getHealth());
            Cube.fontManager.CustomFont.drawString(distance,this.x+43,this.y+24,Color.WHITE.getRGB());
            Cube.fontManager.CustomFont.drawString(health,this.x+105,this.y+24,Color.WHITE.getRGB());
            KotlinRender114514D.INSTANCE.drawArmor( target, (int) (this.x+31), (int) (this.y+32));
            Render2DUtil.drawRect(this.x+4.3F, this.y+48.1F,this.width+((getHealth(target) / target.getMaxHealth()*100)) -201.3f,
                    this.height-54,new Color(154, 243, 118,170).getRGB());
            } catch (Exception e) {
            }
    }
    public static EntityPlayer getNearestPlayer(double range)
    {
        //search nearest player
        return mc.world.playerEntities.stream().filter(p -> mc.player.getDistance(p) <= range)
                .filter(p -> mc.player.entityId != p.entityId)
                .min(Comparator.comparing(p -> mc.player.getDistance(p)))
                .orElse(null);
    }

}