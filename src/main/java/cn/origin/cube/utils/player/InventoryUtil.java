package cn.origin.cube.utils.player;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.item.*;
import net.minecraft.network.play.client.CPacketClickWindow;
import net.minecraft.network.play.client.CPacketHeldItemChange;

public class InventoryUtil {
    private static final Minecraft mc = Minecraft.getMinecraft();
    public static int fastestMiningTool(Block toMineBlockMaterial) {
        float fastestSpeed = 1.0f;
        int theSlot = mc.player.inventory.currentItem;

        for (int i = 0; i < 9; i++) {
            ItemStack itemStack = mc.player.inventory.getStackInSlot(i);

            if (itemStack.isEmpty() || !(itemStack.getItem() instanceof ItemTool || itemStack.getItem() instanceof ItemSword || itemStack.getItem() instanceof ItemHoe || itemStack.getItem() instanceof ItemShears))
                continue;

            float mineSpeed = BlockUtil.blockBreakSpeed(toMineBlockMaterial.getDefaultState(), itemStack);

            if (mineSpeed > fastestSpeed) {
                fastestSpeed = mineSpeed;
                theSlot = i;
            }
        }

        return theSlot;
    }

    public static int findHotbarItem(final Class<?> clazz) {
        for (int i = 0; i < 9; ++i) {
            final ItemStack stack = mc.player.inventory.getStackInSlot(i);
            if (stack != ItemStack.EMPTY) {
                if (clazz.isInstance(stack.getItem())) {
                    return i;
                }
            }
        }
        return -1;
    }

    public static void switchToHotbarSlot(final int slot, final boolean silent) {
        if (mc.player == null || mc.world == null || mc.player.inventory == null) {
            return;
        }
        if (mc.player.inventory.currentItem == slot || slot < 0) {
            return;
        }
        if (silent) {
            mc.player.connection.sendPacket(new CPacketHeldItemChange(slot));
            mc.playerController.updateController();
        } else {
            mc.player.connection.sendPacket(new CPacketHeldItemChange(slot));
            mc.player.inventory.currentItem = slot;
            mc.playerController.updateController();
        }
    }

    public static int findHotbarBlock(final Class<?> clazz) {
        for (int i = 0; i < 9; ++i) {
            final ItemStack stack = mc.player.inventory.getStackInSlot(i);
            if (stack != ItemStack.EMPTY) {
                if (clazz.isInstance(stack.getItem())) {
                    return i;
                }
                if (stack.getItem() instanceof ItemBlock) {
                    final Block block = ((ItemBlock) stack.getItem()).getBlock();
                    if (clazz.isInstance(block)) {
                        return i;
                    }
                }
            }
        }
        return -1;
    }
    public static int findHotbarBlock(Block blockIn) {
        for (int i = 0; i < 9; ++i) {
            Block block;
            ItemStack stack = mc.player.inventory.getStackInSlot(i);
            if (stack == ItemStack.EMPTY || !(stack.getItem() instanceof ItemBlock) || (block = ((ItemBlock) stack.getItem()).getBlock()) != blockIn)
                continue;
            return i;
        }
        return -1;
    }
    public static int getItemHotbar(Item input) {
        for (int i = 0; i < 9; ++i) {
            Item item = mc.player.inventory.getStackInSlot(i).getItem();
            if (Item.getIdFromItem(item) != Item.getIdFromItem(input)) continue;
            return i;
        }
        return -1;
    }

}
