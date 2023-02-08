package me.clicknin.claysoldiers.entities;

import me.clicknin.claysoldiers.ClaySoldiers;
import net.minecraft.client.Minecraft;
import net.minecraft.src.*;
import net.minecraft.src.helper.DamageType;

import java.util.List;

public class EntityDirtHorse extends EntityAnimal {
    public boolean gotRider;

    public EntityDirtHorse(World world) {
        super(world);
        this.health = 30;
        this.yOffset = 0.0F;
        this.stepHeight = 0.1F;
        this.moveSpeed = 0.6F;
        this.setSize(0.25F, 0.4F);
        this.setPosition(this.posX, this.posY, this.posZ);
        this.texture = "/assets/claysoldiers/entity/dirt_horse.png";
        this.renderDistanceWeight = 5.0D;
    }

    public EntityDirtHorse(World world, double x, double y, double z) {
        super(world);
        this.health = 30;
        this.yOffset = 0.0F;
        this.stepHeight = 0.1F;
        this.moveSpeed = 0.6F;
        this.setSize(0.25F, 0.4F);
        this.setPosition(x, y, z);
        this.texture = "assets/claysoldiers/entity/dirt_horse.png";
        this.renderDistanceWeight = 5.0D;
        this.worldObj.playSoundAtEntity(this, "step.gravel", 0.8F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F) * 0.9F);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if(this.gotRider) {
            if(this.riddenByEntity != null) {
                this.gotRider = false;
                return;
            }

            List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand(0.1D, 0.1D, 0.1D));

            for(int i = 0; i < list.size(); ++i) {
                Entity entity = (Entity)list.get(i);
                if(entity instanceof EntityClayMan) {
                    EntityLiving entityliving = (EntityLiving)entity;
                    if(entityliving.ridingEntity == null && entityliving.riddenByEntity != this) {
                        entity.mountEntity(this);
                        break;
                    }
                }
            }

            this.gotRider = false;
        }

    }

    @Override
    public void updatePlayerActionState() {
        if(this.riddenByEntity != null && this.riddenByEntity instanceof EntityClayMan) {
            EntityClayMan rider = (EntityClayMan)this.riddenByEntity;
            this.isJumping = rider.isJumping || this.handleWaterMovement();
            this.moveForward = rider.moveForward * (rider.sugarTime > 0 ? 1.0F : 2.0F);
            this.moveStrafing = rider.moveStrafing * (rider.sugarTime > 0 ? 1.0F : 2.0F);
            this.rotationYaw = this.prevRotationYaw = rider.rotationYaw;
            this.rotationPitch = this.prevRotationPitch = rider.rotationPitch;
            rider.renderYawOffset = this.renderYawOffset;
            this.riddenByEntity.fallDistance = 0.0F;
            if(rider.isDead || rider.health <= 0) {
                rider.mountEntity((Entity)null);
            }
        } else {
            super.updatePlayerActionState();
        }

    }

    @Override
    public void moveEntityWithHeading(float f, float f1) {
        super.moveEntityWithHeading(f, f1);
        double d2 = (this.posX - this.prevPosX) * 2.0D;
        double d3 = (this.posZ - this.prevPosZ) * 2.0D;
        float f5 = MathHelper.sqrt_double(d2 * d2 + d3 * d3) * 4.0F;
        if(f5 > 1.0F) {
            f5 = 1.0F;
        }

        this.field_704_R += (f5 - this.field_704_R) * 0.4F;
        this.field_703_S += this.field_704_R;
    }

    @Override
    public void writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        this.gotRider = this.riddenByEntity == null;
        nbttagcompound.setBoolean("GotRider", this.gotRider);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.gotRider = nbttagcompound.getBoolean("GotRider");
    }

    @Override
    protected String getHurtSound() {
        this.worldObj.playSoundAtEntity(this, "step.gravel", 0.6F, 1.0F / (this.rand.nextFloat() * 0.2F + 0.9F));
        return "";
    }

    @Override
    protected String getDeathSound() {
        return "step.gravel";
    }

    @Override
    protected void jump() {
        this.motionY = 0.4D;
    }

    @Override
    protected boolean canDespawn() {
        return false;
    }

    @Override
    public void dropFewItems() {
        Item item1 = ClaySoldiers.dirtHorse;
        this.dropItem(item1.itemID, 1);
    }

    @Override
    public boolean attackEntityFrom(Entity e, int i, DamageType type) {
        if(e == null || !(e instanceof EntityClayMan)) {
            i = 30;
        }

        boolean fred = super.attackEntityFrom(e, i, (DamageType) null);
        if(fred && this.health <= 0) {
            Item item1 = ClaySoldiers.dirtHorse;

            for(int q = 0; q < 24; ++q) {
                double a = this.posX + (double)(this.rand.nextFloat() - this.rand.nextFloat()) * 0.125D;
                double b = this.posY + 0.25D + (double)(this.rand.nextFloat() - this.rand.nextFloat()) * 0.125D;
                double c = this.posZ + (double)(this.rand.nextFloat() - this.rand.nextFloat()) * 0.125D;
                Minecraft.getMinecraft().effectRenderer.addEffect(new EntitySlimeFX(this.worldObj, a, b, c, item1));
            }

            this.isDead = true;
        }

        return fred;
    }

    @Override
    public void knockBack(Entity entity, int i, double d, double d1) {
        super.knockBack(entity, i, d, d1);
        if(entity != null && entity instanceof EntityClayMan) {
            this.motionX *= 0.6D;
            this.motionY *= 0.75D;
            this.motionZ *= 0.6D;
        }

    }

    @Override
    public boolean isOnLadder() {
        return false;
    }
}
