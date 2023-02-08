package me.clicknin.claysoldiers.items;

import me.clicknin.claysoldiers.entities.EntityDirtHorse;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Block;
import net.minecraft.src.World;

public class ItemDirtHorse extends Item {
    public ItemDirtHorse(int i) {
        super(i);
        this.maxStackSize = 8;
    }

    @Override
    public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, int i, int j, int k, int l, double heightPlaced) {
        if(world.getBlockId(i, j, k) != Block.blockSnow.blockID) {
            if(l == 0) {
                --j;
            }

            if(l == 1) {
                ++j;
            }

            if(l == 2) {
                --k;
            }

            if(l == 3) {
                ++k;
            }

            if(l == 4) {
                --i;
            }

            if(l == 5) {
                ++i;
            }

            if(!world.isAirBlock(i, j, k)) {
                return false;
            }
        }

        boolean jack = false;
        int p = world.getBlockId(i, j, k);
        if(p == 0 || Block.blocksList[p].getCollisionBoundingBoxFromPool(world, i, j, k) == null) {
            while(itemstack.stackSize > 0) {
                double a = (double)i + 0.25D + (double)entityplayer.rand.nextFloat() * 0.5D;
                double b = (double)j + 0.5D;
                double c = (double)k + 0.25D + (double)entityplayer.rand.nextFloat() * 0.5D;
                EntityDirtHorse ec = new EntityDirtHorse(world, a, b, c);
                world.entityJoinedWorld(ec);
                jack = true;
                --itemstack.stackSize;
            }
        }

        return jack;
    }
}
