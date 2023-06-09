package cn.origin.cube.utils.player;



import cn.origin.cube.utils.Utils;
import cn.origin.cube.utils.player.BlockUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockHopper;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class PlayerUtil extends Utils {


    public static boolean canSeeEntity(Entity e) {
        return mc.player.canEntityBeSeen(e);
    }

    public static double getDistance(Entity e) {
        return mc.player.getDistance(e);
    }

    public static double getDistance(BlockPos e) {
        return mc.player.getDistance(e.getX(), e.getY(), e.getZ());
    }
    
    public static double getDistance(Vec3d e) {
        return mc.player.getDistance(e.x, e.y, e.z);
    }

    public static BlockPos getPlayerPos() {
        return new BlockPos(mc.player);
    }

    public static double[] directionSpeed(final double speed) {
        float forward = mc.player.movementInput.moveForward;
        float side = mc.player.movementInput.moveStrafe;
        float yaw = mc.player.prevRotationYaw + (mc.player.rotationYaw - mc.player.prevRotationYaw) * mc.getRenderPartialTicks();
        if (forward != 0.0f) {
            if (side > 0.0f) {
                yaw += ((forward > 0.0f) ? -45 : 45);
            } else if (side < 0.0f) {
                yaw += ((forward > 0.0f) ? 45 : -45);
            }
            side = 0.0f;
            if (forward > 0.0f) {
                forward = 1.0f;
            } else if (forward < 0.0f) {
                forward = -1.0f;
            }
        }
        final double sin = Math.sin(Math.toRadians(yaw + 90.0f));
        final double cos = Math.cos(Math.toRadians(yaw + 90.0f));
        final double posX = forward * speed * cos + side * speed * sin;
        final double posZ = forward * speed * sin - side * speed * cos;
        return new double[]{posX, posZ};
    }

    public static boolean isPlayerMoving() {
        return mc.player.movementInput.moveStrafe != 0.0f || mc.player.movementInput.moveForward != 0.0f;
    }

    //from Satellite
    public static boolean isPhasing() {
        try {
            AxisAlignedBB playerBoundingBox = (mc.player).getEntityBoundingBox();
            for (int x = MathHelper.floor(playerBoundingBox.minX); x < MathHelper.floor(playerBoundingBox.maxX) + 1; x++) {
                for (int y = MathHelper.floor(playerBoundingBox.minY); y < MathHelper.floor(playerBoundingBox.maxY) + 1; y++) {
                    for (int z = MathHelper.floor(playerBoundingBox.minZ); z < MathHelper.floor(playerBoundingBox.maxZ) + 1; z++) {
                        Block block = mc.world.getBlockState(new BlockPos(x, y, z)).getBlock();
                        if (block != null && !(block instanceof BlockAir)) {
                            AxisAlignedBB boundingBox = block.getCollisionBoundingBox(mc.world.getBlockState(new BlockPos(x, y, z)), mc.world, new BlockPos(x, y, z)).offset(x, y, z);
                            if (block instanceof BlockHopper)
                                boundingBox = new AxisAlignedBB(x, y, z, x + 1, y + 1, z + 1);
                            if (boundingBox != null && playerBoundingBox.intersects(boundingBox))
                                return true;
                        }
                    }
                }
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }
}
