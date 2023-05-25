package cn.origin.cube.module.modules.visual;

import cn.origin.cube.event.events.world.Render3DEvent;
import cn.origin.cube.inject.client.IRenderGlobal;
import cn.origin.cube.module.Category;
import cn.origin.cube.module.Module;
import cn.origin.cube.module.ModuleInfo;
import cn.origin.cube.module.modules.client.ClickGui;
import cn.origin.cube.settings.FloatSetting;
import cn.origin.cube.utils.client.MathUtil;
import cn.origin.cube.utils.player.BlockUtil;
import cn.origin.cube.utils.render.Render3DUtil;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

import java.awt.*;
import java.util.Objects;

//ToDo make more to this
@ModuleInfo(name = "BreakEsp",
        descriptions = "BreakEsp",
        category = Category.VISUAL)
public class BreakESP extends Module {

    FloatSetting range = registerSetting("Range", 5F, 5F, 100F);

    @Override
    public void onRender3D(Render3DEvent event) {
        ((IRenderGlobal) mc.renderGlobal).getDamagedBlocks().forEach((pos, progress) -> {
            if (progress != null) {
                BlockPos blockPos = progress.getPosition();

                if (mc.world.getBlockState(blockPos).getBlock().equals(Blocks.AIR)) {
                    return;
                }

                if (blockPos.getDistance((int) mc.player.posX, (int) mc.player.posY, (int) mc.player.posZ) <= range.getValue()) {
                    int damage = MathHelper.clamp(progress.getPartialBlockDamage(), 0, 8);

                    AxisAlignedBB bb = mc.world.getBlockState(blockPos).getSelectedBoundingBox(mc.world, blockPos);

                    double x = bb.minX + (bb.maxX - bb.minX) / 2;
                    double y = bb.minY + (bb.maxY - bb.minY) / 2;
                    double z = bb.minZ + (bb.maxZ - bb.minZ) / 2;

                    double sizeX = damage * ((bb.maxX - x) / 8);
                    double sizeY = damage * ((bb.maxY - y) / 8);
                    double sizeZ = damage * ((bb.maxZ - z) / 8);

                    int colourFactor = (damage * 255) / 8;
                    float renderScale = 1.0f;
                    drawBBFill(bb,ClickGui.getCurrentColor(),40);

                }
            }
        });
    }
        public static void drawBBFill(AxisAlignedBB BB, Color color, int alpha) {
            AxisAlignedBB bb = new AxisAlignedBB(BB.minX - mc.getRenderManager().viewerPosX, BB.minY - mc.getRenderManager().viewerPosY, BB.minZ - mc.getRenderManager().viewerPosZ, BB.maxX - mc.getRenderManager().viewerPosX, BB.maxY - mc.getRenderManager().viewerPosY, BB.maxZ - mc.getRenderManager().viewerPosZ);
            Render3DUtil.camera.setPosition(Objects.requireNonNull(mc.getRenderViewEntity()).posX, mc.getRenderViewEntity().posY,mc.getRenderViewEntity().posZ);
            if (Render3DUtil.camera.isBoundingBoxInFrustum(new AxisAlignedBB(bb.minX + mc.getRenderManager().viewerPosX, bb.minY +mc.getRenderManager().viewerPosY, bb.minZ + mc.getRenderManager().viewerPosZ, bb.maxX + mc.getRenderManager().viewerPosX, bb.maxY + mc.getRenderManager().viewerPosY, bb.maxZ + mc.getRenderManager().viewerPosZ))) {
                Render3DUtil.prepareGL();
                RenderGlobal.renderFilledBox((AxisAlignedBB)bb, (float)((float)color.getRed() / 255.0f), (float)((float)color.getGreen() / 255.0f), (float)((float)color.getBlue() / 255.0f), (float)((float)alpha / 255.0f));
                Render3DUtil.releaseGL();
            }
        }
    }

