package cn.origin.cube.utils.player;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.*;

import static net.minecraft.init.Enchantments.EFFICIENCY;

public class BlockUtil {
    private static final Minecraft mc = Minecraft.getMinecraft();
    public static List<Block> blackList;
    public static List<Block> shulkerList;

    static {
        blackList = Arrays.asList(Blocks.ENDER_CHEST, Blocks.CHEST, Blocks.TRAPPED_CHEST, Blocks.CRAFTING_TABLE, Blocks.ANVIL, Blocks.BREWING_STAND, Blocks.HOPPER, Blocks.DROPPER, Blocks.DISPENSER);
        shulkerList = Arrays.asList(Blocks.WHITE_SHULKER_BOX, Blocks.ORANGE_SHULKER_BOX, Blocks.MAGENTA_SHULKER_BOX, Blocks.LIGHT_BLUE_SHULKER_BOX, Blocks.YELLOW_SHULKER_BOX, Blocks.LIME_SHULKER_BOX, Blocks.PINK_SHULKER_BOX, Blocks.GRAY_SHULKER_BOX, Blocks.SILVER_SHULKER_BOX, Blocks.CYAN_SHULKER_BOX, Blocks.PURPLE_SHULKER_BOX, Blocks.BLUE_SHULKER_BOX, Blocks.BROWN_SHULKER_BOX, Blocks.GREEN_SHULKER_BOX, Blocks.RED_SHULKER_BOX, Blocks.BLACK_SHULKER_BOX);
    }
    public static void placeBlock(BlockPos pos, EnumHand hand, boolean rotate, boolean packet) {
        EnumFacing side = getFirstFacing(pos);
        if (side == null) {
            return;
        }
        BlockPos neighbour = pos.offset(side);
        EnumFacing opposite = side.getOpposite();
        Vec3d hitVec = new Vec3d(neighbour).add(0.5, 0.5, 0.5).add(new Vec3d(opposite.getDirectionVec()).scale(0.5));
        Block neighbourBlock = mc.world.getBlockState(neighbour).getBlock();
        if (!mc.player.isSneaking() && (blackList.contains(neighbourBlock) || shulkerList.contains(neighbourBlock))) {
            mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SNEAKING));
            mc.player.setSneaking(true);
        }
        if (rotate) {
            RotationUtil.faceVector(hitVec, true);
        }
        rightClickBlock(neighbour, hitVec, hand, opposite, packet);
        mc.rightClickDelayTimer = 4;
    }
    public enum AirMode {
        NoAir,
        AirOnly,
        Ignored
    }
    public static boolean canPlaceCrystal(BlockPos blockPos) {
        BlockPos boost = blockPos.add(0, 1, 0);
        BlockPos boost2 = blockPos.add(0, 2, 0);
        try {
            return ((mc.world.getBlockState(blockPos).getBlock() == Blocks.BEDROCK || mc.world
                    .getBlockState(blockPos).getBlock() == Blocks.OBSIDIAN) && mc.world
                    .getBlockState(boost).getBlock() == Blocks.AIR && mc.world
                    .getBlockState(boost2).getBlock() == Blocks.AIR && mc.world
                    .getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(boost)).isEmpty() && mc.world
                    .getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(boost2)).isEmpty());
        } catch (Exception e) {
            return false;
        }
    }
    public static float blockBreakSpeed(IBlockState blockMaterial, ItemStack tool) {
        float mineSpeed = tool.getDestroySpeed(blockMaterial);
        int efficiencyFactor = EnchantmentHelper.getEnchantmentLevel(EFFICIENCY, tool);

        mineSpeed = (float) (mineSpeed > 1.0 && efficiencyFactor > 0 ? (efficiencyFactor * efficiencyFactor + mineSpeed + 1.0) : mineSpeed);

        if (mc.player.isPotionActive(MobEffects.HASTE)) {
            mineSpeed *= 1.0f + Objects.requireNonNull(mc.player.getActivePotionEffect(MobEffects.HASTE)).getAmplifier() * 0.2f;
        }

        if (mc.player.isPotionActive(MobEffects.MINING_FATIGUE)) {
            switch (Objects.requireNonNull(mc.player.getActivePotionEffect(MobEffects.MINING_FATIGUE)).getAmplifier()) {
                case 0 : {
                    mineSpeed *= 0.3f;
                    break;
                }

                case 1: {
                    mineSpeed *= 0.09f;
                    break;
                }

                case 2: {
                    mineSpeed *= 0.0027f;
                    break;
                }

                default: {
                    mineSpeed *= 0.00081f;
                }
            }
            if (!mc.player.onGround || (mc.player.isInWater() && EnchantmentHelper.getEnchantmentLevel(Enchantments.AQUA_AFFINITY, mc.player.inventory.armorItemInSlot(0)) == 0)) {
                mineSpeed /= 5.0f;
            }
        } else {
            if (!mc.player.onGround || (mc.player.isInWater() && EnchantmentHelper.getEnchantmentLevel(Enchantments.AQUA_AFFINITY, mc.player.inventory.armorItemInSlot(0)) == 0)) {
                mineSpeed /= 5.0f;
            }
        }

        return mineSpeed;
    }




    public static List<BlockPos> getBlocksInRadius(double radius, AirMode airMode) {
        ArrayList<BlockPos> posList = new ArrayList<>();
        BlockPos pos = new BlockPos(Math.floor(mc.player.posX), Math.floor(mc.player.posY), Math.floor(mc.player.posZ));
        for (int x = pos.getX() - (int) radius; x <= pos.getX() + radius; ++x) {
            for (int y = pos.getY() - (int) radius; y < pos.getY() + radius; ++y) {
                for (int z = pos.getZ() - (int) radius; z <= pos.getZ() + radius; ++z) {
                    double distance = (pos.getX() - x) * (pos.getX() - x) + (pos.getZ() - z) * (pos.getZ() - z) + (pos.getY() - y) * (pos.getY() - y);
                    BlockPos position = new BlockPos(x, y, z);
                    if (distance < radius * radius) {
                        if (airMode.equals(AirMode.NoAir) && mc.world.getBlockState(position).getBlock().equals(Blocks.AIR))
                            continue;
                        if (airMode.equals(AirMode.AirOnly) && !mc.world.getBlockState(position).getBlock().equals(Blocks.AIR))
                            continue;
                        posList.add(position);
                    }
                }
            }
        }
        return posList;
    }
    public static void rightClickBlock(BlockPos pos, Vec3d vec, EnumHand hand, EnumFacing direction, boolean packet) {
        if (packet) {
            mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(pos, direction, hand, 0.5f, 1.0f, 0.5f));
        } else {
            mc.playerController.processRightClickBlock(mc.player, mc.world, pos, direction, vec, hand);
        }
        mc.player.connection.sendPacket(new CPacketAnimation(EnumHand.MAIN_HAND));
        mc.rightClickDelayTimer = 4;
    }


    public static EnumFacing getFirstFacing(BlockPos pos) {
        Iterator<EnumFacing> iterator = getPossibleSides(pos).iterator();
        if (iterator.hasNext()) {
            return iterator.next();
        }
        return null;
    }

    public static List<EnumFacing> getPossibleSides(BlockPos pos) {
        List<EnumFacing> facings = new ArrayList<EnumFacing>();
        if (mc.world == null || pos == null) {
            return facings;
        }
        for (EnumFacing side : EnumFacing.values()) {
            BlockPos neighbour = pos.offset(side);
            IBlockState blockState = mc.world.getBlockState(neighbour);
            if (blockState.getBlock().canCollideCheck(blockState, false) && !blockState.getMaterial().isReplaceable()) {
                facings.add(side);
            }
        }
        return facings;
    }
}
