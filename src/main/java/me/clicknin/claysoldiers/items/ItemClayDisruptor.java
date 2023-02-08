package me.clicknin.claysoldiers.items;

import me.clicknin.claysoldiers.ClaySoldiers;
import me.clicknin.claysoldiers.entities.EntityClayMan;
import me.clicknin.claysoldiers.entities.EntityDirtHorse;
import net.minecraft.client.Minecraft;
import net.minecraft.src.*;
import net.minecraft.src.helper.DamageType;

import java.util.List;

public class ItemClayDisruptor extends Item {
    public ItemClayDisruptor(int i) {
        super(i);
        this.setMaxDamage(16);
        this.setMaxStackSize(1);
    }

    @Override
    public boolean shouldRotateAroundWhenRendering() {
        return true;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer) {
        if(ClaySoldiers.waveTime <= 0) {
            itemstack.damageItem(1, entityplayer);
            ClaySoldiers.waveTime = 150;
            world.playSoundAtEntity(entityplayer, "portal.trigger", 1.5F, itemRand.nextFloat() * 0.2F + 1.4F);
            entityplayer.timeInPortal = 1.5F;
            entityplayer.prevTimeInPortal = 1.5F;
            List list1 = world.getEntitiesWithinAABBExcludingEntity(entityplayer, entityplayer.boundingBox.expand(16.0D, 16.0D, 16.0D));

            int x;
            for(x = 0; x < list1.size(); ++x) {
                Entity y = (Entity)list1.get(x);
                if(y instanceof EntityClayMan && !y.isDead && ((EntityLiving)y).health > 0) {
                    y.attackEntityFrom(entityplayer, 100, (DamageType) null);
                } else if(y instanceof EntityDirtHorse && !y.isDead && ((EntityLiving)y).health > 0) {
                    y.attackEntityFrom(entityplayer, 100, (DamageType) null);
                }
            }

            x = MathHelper.floor_double(entityplayer.posX);
            int i23 = MathHelper.floor_double(entityplayer.boundingBox.minY);
            int z = MathHelper.floor_double(entityplayer.posZ);

            int i;
            double distance;
            double d;
            double f;
            for(i = -12; i < 13; ++i) {
                for(int angle = -12; angle < 13; ++angle) {
                    for(int k = -12; k < 13; ++k) {
                        if(angle + i23 > 0 && angle + i23 < 127 && world.getBlockId(x + i, i23 + angle, z + k) == Block.blockClay.blockID) {
                            distance = (double)i;
                            d = (double)angle;
                            f = (double)k;
                            if(Math.sqrt(distance * distance + d * d + f * f) <= 12.0D) {
                                this.blockCrush(world, x + i, i23 + angle, z + k);
                            }
                        }
                    }
                }
            }

            for(i = 0; i < 128; ++i) {
                double d24 = (double)i / 3.0D;
                distance = 0.5D + (double)i / 6.0D;
                d = Math.sin(d24) * 0.25D;
                f = Math.cos(d24) * 0.25D;
                double a = entityplayer.posX + d * distance;
                double b = entityplayer.boundingBox.minY + 0.5D;
                double c = entityplayer.posZ + f * distance;
                world.spawnParticle("portal", a, b, c, d, 0.0D, f);
            }
        }

        return itemstack;
    }

    public void blockCrush(World worldObj, int x, int y, int z) {
        int a = worldObj.getBlockId(x, y, z);
        int b = worldObj.getBlockMetadata(x, y, z);
        if(a != 0) {
            Minecraft.getMinecraft().effectRenderer.addBlockDestroyEffects(x, y, z, a, b);
            Block.blocksList[a].onBlockRemoval(worldObj, x, y, z);
            Block.blocksList[a].dropBlockAsItem(worldObj, x, y, z, b);
            worldObj.setBlockWithNotify(x, y, z, 0);
        }
    }
}
