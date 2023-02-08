package me.clicknin.claysoldiers.entities;

import net.minecraft.client.Minecraft;
import net.minecraft.src.*;
import net.minecraft.src.helper.DamageType;

import java.util.List;

public class EntityGravelChunk extends Entity {
    private int xTile = -1;
    private int yTile = -1;
    private int zTile = -1;
    private int inTile = 0;
    private int field_28019_h = 0;
    private boolean inGround = false;
    public boolean doesArrowBelongToPlayer = false;
    public int arrowShake = 0;
    public EntityLiving owner;
    private int ticksInGround;
    private int ticksInAir = 0;
    public int clayTeam;
    public int entityAge;

    public EntityGravelChunk(World world) {
        super(world);
        this.setSize(0.1F, 0.1F);
        this.renderDistanceWeight = 5.0D;
        this.setPosition(this.posX, this.posY, this.posZ);
    }

    public EntityGravelChunk(World world, double d, double d1, double d2, int i) {
        super(world);
        this.setSize(0.1F, 0.1F);
        this.renderDistanceWeight = 5.0D;
        this.setPosition(d, d1, d2);
        this.yOffset = 0.0F;
        this.clayTeam = i;
    }

    public EntityGravelChunk(World world, EntityLiving entityliving, int i) {
        super(world);
        this.owner = entityliving;
        this.doesArrowBelongToPlayer = entityliving instanceof EntityPlayer;
        this.setSize(0.1F, 0.1F);
        this.renderDistanceWeight = 5.0D;
        this.setLocationAndAngles(entityliving.posX, entityliving.posY + (double)entityliving.getEyeHeight(), entityliving.posZ, entityliving.rotationYaw, entityliving.rotationPitch);
        this.posX -= (double)(MathHelper.cos(this.rotationYaw / 180.0F * 3.141593F) * 0.16F);
        this.posY -= (double)0.1F;
        this.posZ -= (double)(MathHelper.sin(this.rotationYaw / 180.0F * 3.141593F) * 0.16F);
        this.setPosition(this.posX, this.posY, this.posZ);
        this.yOffset = 0.0F;
        this.motionX = (double)(-MathHelper.sin(this.rotationYaw / 180.0F * 3.141593F) * MathHelper.cos(this.rotationPitch / 180.0F * 3.141593F));
        this.motionZ = (double)(MathHelper.cos(this.rotationYaw / 180.0F * 3.141593F) * MathHelper.cos(this.rotationPitch / 180.0F * 3.141593F));
        this.motionY = (double)(-MathHelper.sin(this.rotationPitch / 180.0F * 3.141593F));
        this.setArrowHeading(this.motionX, this.motionY, this.motionZ, 1.5F, 1.0F);
        this.clayTeam = i;
    }

    @Override
    protected void entityInit() {
    }

    public void setArrowHeading(double d, double d1, double d2, float f, float f1) {
        float f2 = MathHelper.sqrt_double(d * d + d1 * d1 + d2 * d2);
        d /= (double)f2;
        d1 /= (double)f2;
        d2 /= (double)f2;
        d += this.rand.nextGaussian() * (double)0.0075F * (double)f1;
        d1 += this.rand.nextGaussian() * (double)0.0075F * (double)f1;
        d2 += this.rand.nextGaussian() * (double)0.0075F * (double)f1;
        d *= (double)f;
        d1 *= (double)f;
        d2 *= (double)f;
        this.motionX = d;
        this.motionY = d1;
        this.motionZ = d2;
        float f3 = MathHelper.sqrt_double(d * d + d2 * d2);
        this.prevRotationYaw = this.rotationYaw = (float)(Math.atan2(d, d2) * 180.0D / (double)(float)Math.PI);
        this.prevRotationPitch = this.rotationPitch = (float)(Math.atan2(d1, (double)f3) * 180.0D / (double)(float)Math.PI);
        this.ticksInGround = 0;
    }

    @Override
    public void setVelocity(double d, double d1, double d2) {
        this.motionX = d;
        this.motionY = d1;
        this.motionZ = d2;
        if(this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F) {
            float f = MathHelper.sqrt_double(d * d + d2 * d2);
            this.prevRotationYaw = this.rotationYaw = (float)(Math.atan2(d, d2) * 180.0D / (double)(float)Math.PI);
            this.prevRotationPitch = this.rotationPitch = (float)(Math.atan2(d1, (double)f) * 180.0D / (double)(float)Math.PI);
            this.prevRotationPitch = this.rotationPitch;
            this.prevRotationYaw = this.rotationYaw;
            this.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
            this.ticksInGround = 0;
        }

    }

    @Override
    public void onUpdate() {
        ++this.entityAge;
        if(this.entityAge > 99999) {
            this.entityAge = 0;
        }

        super.onUpdate();
        if(this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F) {
            float i = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
            this.prevRotationYaw = this.rotationYaw = (float)(Math.atan2(this.motionX, this.motionZ) * 180.0D / (double)(float)Math.PI);
            this.prevRotationPitch = this.rotationPitch = (float)(Math.atan2(this.motionY, (double)i) * 180.0D / (double)(float)Math.PI);
        }

        int i18 = this.worldObj.getBlockId(this.xTile, this.yTile, this.zTile);
        if(i18 > 0) {
            Block.blocksList[i18].setBlockBoundsBasedOnState(this.worldObj, this.xTile, this.yTile, this.zTile);
            AxisAlignedBB vec3d = Block.blocksList[i18].getCollisionBoundingBoxFromPool(this.worldObj, this.xTile, this.yTile, this.zTile);
            if(vec3d != null && vec3d.isVecInside(Vec3D.createVector(this.posX, this.posY, this.posZ))) {
                this.inGround = true;
            }
        }

        if(this.arrowShake > 0) {
            --this.arrowShake;
        }

        if(this.inGround) {
            int i20 = this.worldObj.getBlockId(this.xTile, this.yTile, this.zTile);
            int i21 = this.worldObj.getBlockMetadata(this.xTile, this.yTile, this.zTile);
            if(i20 == this.inTile && i21 == this.field_28019_h) {
                ++this.ticksInGround;
                if(this.ticksInGround == 1200) {
                    this.setEntityDead();
                }

            } else {
                this.inGround = false;
                this.motionX *= (double)(this.rand.nextFloat() * 0.2F);
                this.motionY *= (double)(this.rand.nextFloat() * 0.2F);
                this.motionZ *= (double)(this.rand.nextFloat() * 0.2F);
                this.ticksInGround = 0;
                this.ticksInAir = 0;
            }
        } else {
            ++this.ticksInAir;
            Vec3D vec3D19 = Vec3D.createVector(this.posX, this.posY, this.posZ);
            Vec3D vec3d1 = Vec3D.createVector(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
            MovingObjectPosition movingobjectposition = this.worldObj.func_28105_a(vec3D19, vec3d1, false, true);
            vec3D19 = Vec3D.createVector(this.posX, this.posY, this.posZ);
            vec3d1 = Vec3D.createVector(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
            if(movingobjectposition != null) {
                vec3d1 = Vec3D.createVector(movingobjectposition.hitVec.xCoord, movingobjectposition.hitVec.yCoord, movingobjectposition.hitVec.zCoord);
            }

            Entity entity = null;
            List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.addCoord(this.motionX, this.motionY, this.motionZ).expand(1.0D, 1.0D, 1.0D));
            double d = 0.0D;

            float f5;
            double b;
            for(int f2 = 0; f2 < list.size(); ++f2) {
                Entity f3 = (Entity)list.get(f2);
                if(f3.canBeCollidedWith() && (f3 != this.owner || this.ticksInAir >= 5)) {
                    f5 = 0.3F;
                    AxisAlignedBB a = f3.boundingBox.expand((double)f5, (double)f5, (double)f5);
                    MovingObjectPosition f6 = a.func_1169_a(vec3D19, vec3d1);
                    if(f6 != null) {
                        b = vec3D19.distanceTo(f6.hitVec);
                        if(b < d || d == 0.0D) {
                            entity = f3;
                            d = b;
                        }
                    }
                }
            }

            if(entity != null) {
                movingobjectposition = new MovingObjectPosition(entity);
            }

            float f24;
            if(movingobjectposition != null) {
                if(movingobjectposition.entityHit != null) {
                    byte b22 = 4;
                    if(!(movingobjectposition.entityHit instanceof EntityClayMan)) {
                        b22 = 0;
                    }

                    if(movingobjectposition.entityHit.attackEntityFrom(this.owner, b22, (DamageType) null)) {
                        this.setEntityDead();
                    }
                } else {
                    this.xTile = movingobjectposition.blockX;
                    this.yTile = movingobjectposition.blockY;
                    this.zTile = movingobjectposition.blockZ;
                    this.inTile = this.worldObj.getBlockId(this.xTile, this.yTile, this.zTile);
                    this.field_28019_h = this.worldObj.getBlockMetadata(this.xTile, this.yTile, this.zTile);
                    this.motionX = (double)((float)(movingobjectposition.hitVec.xCoord - this.posX));
                    this.motionY = (double)((float)(movingobjectposition.hitVec.yCoord - this.posY));
                    this.motionZ = (double)((float)(movingobjectposition.hitVec.zCoord - this.posZ));
                    f24 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
                    this.posX -= this.motionX / (double)f24 * (double)0.05F;
                    this.posY -= this.motionY / (double)f24 * (double)0.05F;
                    this.posZ -= this.motionZ / (double)f24 * (double)0.05F;
                    this.inGround = true;
                    this.arrowShake = 7;
                }
            }

            this.posX += this.motionX;
            this.posY += this.motionY;
            this.posZ += this.motionZ;
            f24 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
            this.rotationYaw = (float)(Math.atan2(this.motionX, this.motionZ) * 180.0D / (double)(float)Math.PI);

            for(this.rotationPitch = (float)(Math.atan2(this.motionY, (double)f24) * 180.0D / (double)(float)Math.PI); this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F) {
            }

            while(this.rotationPitch - this.prevRotationPitch >= 180.0F) {
                this.prevRotationPitch += 360.0F;
            }

            while(this.rotationYaw - this.prevRotationYaw < -180.0F) {
                this.prevRotationYaw -= 360.0F;
            }

            while(this.rotationYaw - this.prevRotationYaw >= 180.0F) {
                this.prevRotationYaw += 360.0F;
            }

            this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F;
            this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F;
            float f23 = 0.99F;
            f5 = 0.03F;
            if(this.isInWater()) {
                for(int i25 = 0; i25 < 4; ++i25) {
                    float f27 = 0.25F;
                    this.worldObj.spawnParticle("bubble", this.posX - this.motionX * (double)f27, this.posY - this.motionY * (double)f27, this.posZ - this.motionZ * (double)f27, this.motionX, this.motionY, this.motionZ);
                }

                f23 = 0.8F;
            }

            this.motionX *= (double)f23;
            this.motionY *= (double)f23;
            this.motionZ *= (double)f23;
            this.motionY -= (double)f5;
            this.setPosition(this.posX, this.posY, this.posZ);
            if(this.ticksInGround > 0 || this.inGround) {
                this.isDead = true;
            }

            if(this.isDead) {
                double d26 = this.posX + (double)(this.rand.nextFloat() - this.rand.nextFloat()) * 0.125D;
                b = this.boundingBox.minY + 0.125D + (double)(this.rand.nextFloat() - this.rand.nextFloat()) * 0.25D;
                double c = this.posZ + (double)(this.rand.nextFloat() - this.rand.nextFloat()) * 0.125D;
                Minecraft.getMinecraft().effectRenderer.addEffect(new EntityDiggingFX(this.worldObj, d26, b, c, 0.0D, 0.0D, 0.0D, Block.gravel, 0, 0));
                this.worldObj.playSoundAtEntity(this, "step.gravel", 0.6F, 1.0F / (this.rand.nextFloat() * 0.2F + 0.9F));
            }

        }
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
        nbttagcompound.setShort("xTile", (short)this.xTile);
        nbttagcompound.setShort("yTile", (short)this.yTile);
        nbttagcompound.setShort("zTile", (short)this.zTile);
        nbttagcompound.setByte("inTile", (byte)this.inTile);
        nbttagcompound.setByte("inData", (byte)this.field_28019_h);
        nbttagcompound.setByte("shake", (byte)this.arrowShake);
        nbttagcompound.setByte("inGround", (byte)(this.inGround ? 1 : 0));
        nbttagcompound.setBoolean("player", this.doesArrowBelongToPlayer);
        nbttagcompound.setByte("ClayTeam", (byte)this.clayTeam);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
        this.xTile = nbttagcompound.getShort("xTile");
        this.yTile = nbttagcompound.getShort("yTile");
        this.zTile = nbttagcompound.getShort("zTile");
        this.inTile = nbttagcompound.getByte("inTile") & 255;
        this.field_28019_h = nbttagcompound.getByte("inData") & 255;
        this.arrowShake = nbttagcompound.getByte("shake") & 255;
        this.inGround = nbttagcompound.getByte("inGround") == 1;
        this.doesArrowBelongToPlayer = nbttagcompound.getBoolean("player");
        this.clayTeam = nbttagcompound.getByte("ClayTeam") & 255;
    }

    @Override
    public void onCollideWithPlayer(EntityPlayer entityplayer) {
        if(!this.worldObj.isMultiplayerAndNotHost) {
            if(this.inGround && this.doesArrowBelongToPlayer && this.arrowShake <= 0 && entityplayer.inventory.addItemStackToInventory(new ItemStack(Item.ammoArrow, 1))) {
                this.worldObj.playSoundAtEntity(this, "random.pop", 0.2F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
                entityplayer.onItemPickup(this, 1);
                this.setEntityDead();
            }

        }
    }

    @Override
    public float getShadowSize() {
        return 0.0F;
    }
}
