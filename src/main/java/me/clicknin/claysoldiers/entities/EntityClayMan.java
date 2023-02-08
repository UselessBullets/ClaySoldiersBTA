package me.clicknin.claysoldiers.entities;

import me.clicknin.claysoldiers.ClaySoldiers;
import me.clicknin.claysoldiers.items.ItemClayMan;
import net.minecraft.client.Minecraft;
import net.minecraft.src.*;
import net.minecraft.src.helper.DamageType;

import java.util.List;

public class EntityClayMan extends EntityAnimal {
    public int clayTeam;
    public int weaponPoints;
    public int armorPoints;
    public int foodLeft;
    public int sugarTime;
    public int resPoints;
    public int entCount;
    public int strikeTime;
    public int climbTime;
    public int gooTime;
    public int smokeTime;
    public int gooStock;
    public int smokeStock;
    public int logs;
    public int rocks;
    public int blockX;
    public int blockY;
    public int blockZ;
    public int throwTime;
    public float swingLeft;
    public boolean gunPowdered;
    public boolean king;
    public boolean glowing;
    public boolean isSwinging;
    public boolean stickSharp;
    public boolean armorPadded;
    public boolean heavyCore;
    public boolean isSwingingLeft;
    public Entity targetFollow;
    public Entity killedByPlayer;

    public EntityClayMan(World world) {
        super(world);
        this.health = 20;
        this.clayTeam = 0;
        this.yOffset = 0.0F;
        this.stepHeight = 0.1F;
        this.moveSpeed = 0.3F;
        this.setSize(0.15F, 0.4F);
        this.setPosition(this.posX, this.posY, this.posZ);
        this.texture = this.clayManTexture(0);
    }

    public EntityClayMan(World world, double x, double y, double z, int i) {
        super(world);
        this.health = 20;
        this.clayTeam = i;
        this.yOffset = 0.0F;
        this.stepHeight = 0.1F;
        this.moveSpeed = 0.3F;
        this.setSize(0.15F, 0.4F);
        this.setPosition(x, y, z);
        this.texture = this.clayManTexture(i);
        this.renderDistanceWeight = 5.0;
        this.worldObj.playSoundAtEntity(this, "step.gravel", 0.8F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F) * 0.9F);
    }

    public String clayManTexture(int i) {
        String base = "/assets/claysoldiers/entity/clay_";
        if (i == 0) {
            base = base + "grey";
        } else if (i == 1) {
            base = base + "red";
        } else if (i == 2) {
            base = base + "yellow";
        } else if (i == 3) {
            base = base + "green";
        } else if (i == 4) {
            base = base + "blue";
        } else if (i == 5) {
            base = base + "orange";
        } else {
            base = base + "purple";
        }
        return base + ".png";
    }

    public int teamCloth(int teamNum) {
        if (teamNum == 0) {
            return 8;
        } else if (teamNum == 1) {
            return 14;
        } else if (teamNum == 2) {
            return 4;
        } else if (teamNum == 3) {
            return 13;
        } else if (teamNum == 4) {
            return 11;
        } else {
            return teamNum == 5 ? 1 : 10;
        }
    }

    public int teamDye(int teamNum) {
        if (teamNum == 0) {
            return 8;
        } else if (teamNum == 1) {
            return 1;
        } else if (teamNum == 2) {
            return 11;
        } else if (teamNum == 3) {
            return 2;
        } else if (teamNum == 4) {
            return 4;
        } else {
            return teamNum == 5 ? 14 : 5;
        }
    }

    @Override
    public void updatePlayerActionState() {
        super.updatePlayerActionState();
        if(this.strikeTime > 0) {
            --this.strikeTime;
        }

        if(this.sugarTime > 0) {
            this.moveSpeed = 0.6F + (this.entityToAttack == null && this.targetFollow == null ? 0.0F : 0.15F);
            --this.sugarTime;
        } else {
            this.moveSpeed = 0.3F + (this.entityToAttack == null && this.targetFollow == null ? 0.0F : 0.15F);
        }

        if(this.handleWaterMovement()) {
            this.isJumping = true;
        }

        int list;
        double item;
        double a;
        if(this.foodLeft > 0 && this.health <= 15 && this.health > 0) {
            for(list = 0; list < 12; ++list) {
                item = this.posX + (double)(this.rand.nextFloat() - this.rand.nextFloat()) * 0.125D;
                double gottam = this.posY + 0.25D + (double)(this.rand.nextFloat() - this.rand.nextFloat()) * 0.125D;
                a = this.posZ + (double)(this.rand.nextFloat() - this.rand.nextFloat()) * 0.125D;
                Minecraft.getMinecraft().effectRenderer.addEffect(new EntitySlimeFX(this.worldObj, item, gottam, a, Item.foodPorkchopRaw));
            }

            this.health += 15;
            --this.foodLeft;
        }

        if(this.onGround) {
            this.climbTime = 10;
        }

        if(this.smokeTime > 0) {
            --this.smokeTime;
        }

        if(this.throwTime > 0) {
            --this.throwTime;
        }

        int i13;
        int i17;
        if(this.gooTime > 0) {
            this.motionX = 0.0D;
            this.motionY = 0.0D;
            this.motionZ = 0.0D;
            this.moveForward = 0.0F;
            this.moveStrafing = 0.0F;
            this.isJumping = false;
            this.moveSpeed = 0.0F;
            --this.gooTime;
            list = MathHelper.floor_double(this.posX);
            i13 = MathHelper.floor_double(this.boundingBox.minY - 1.0D);
            int stack = MathHelper.floor_double(this.posZ);
            i17 = this.worldObj.getBlockId(list, i13, stack);
            if(i13 > 0 && i13 < 128 && (i17 == 0 || Block.blocksList[i17].getCollisionBoundingBoxFromPool(this.worldObj, list, i13, stack) == null)) {
                this.gooTime = 0;
            }
        }

        if(this.throwTime > 6) {
            this.moveSpeed = -this.moveSpeed;
        }

        ++this.entCount;
        if(this.entityToAttack != null && this.entityToAttack.isDead) {
            this.entityToAttack = null;
            this.setPathToEntity((PathEntity)null);
        } else if(this.entityToAttack != null && this.rand.nextInt(25) == 0 && ((double)this.getDistanceToEntity(this.entityToAttack) > 8.0D || !this.canEntityBeSeen(this.entityToAttack))) {
            this.entityToAttack = null;
            this.setPathToEntity((PathEntity)null);
        }

        if(this.targetFollow != null && this.targetFollow.isDead) {
            this.targetFollow = null;
            this.setPathToEntity((PathEntity)null);
        } else if(this.targetFollow != null && this.rand.nextInt(25) == 0 && ((double)this.getDistanceToEntity(this.targetFollow) > 8.0D || !this.canEntityBeSeen(this.targetFollow))) {
            this.targetFollow = null;
            this.setPathToEntity((PathEntity)null);
        }

        if(this.smokeTime <= 0 && this.entCount > 2 + this.rand.nextInt(2) && this.health > 0) {
            this.entCount = 0;
            List list12 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand(8.0D, 5.0D, 8.0D));

            EntityClayMan entityClayMan21;
            for(i13 = 0; i13 < list12.size(); ++i13) {
                Entity entity14 = (Entity)list12.get(i13);
                if(entity14 instanceof EntityClayMan && this.rand.nextInt(3) == 0 && this.canEntityBeSeen(entity14)) {
                    EntityClayMan entityClayMan20 = (EntityClayMan)entity14;
                    if(entityClayMan20.health > 0 && entityClayMan20.clayTeam != this.clayTeam && this.clayTeam > 0 && this.logs <= 0) {
                        if(entityClayMan20.king) {
                            if(this.entityToAttack == null || !(this.entityToAttack instanceof EntityClayMan)) {
                                this.entityToAttack = entityClayMan20;
                                break;
                            }

                            entityClayMan21 = (EntityClayMan)this.entityToAttack;
                            if(!entityClayMan21.king) {
                                this.entityToAttack = entityClayMan20;
                                break;
                            }
                        } else if(this.entityToAttack == null) {
                            this.entityToAttack = entityClayMan20;
                            break;
                        }
                    } else if(entityClayMan20.health > 0 && this.targetFollow == null && this.entityToAttack == null && entityClayMan20.king && entityClayMan20.clayTeam == this.clayTeam && (double)this.getDistanceToEntity(entityClayMan20) > 3.0D) {
                        this.targetFollow = entityClayMan20;
                        break;
                    }
                } else if(this.entityToAttack == null && entity14 instanceof EntityMob && this.canEntityBeSeen(entity14)) {
                    EntityMob entityMob19 = (EntityMob)entity14;
                    if(entityMob19.entityToAttack != null) {
                        this.entityToAttack = entityMob19;
                        break;
                    }
                } else {
                    if(this.entityToAttack == null && this.targetFollow == null && !this.heavyCore && this.logs <= 0 && this.ridingEntity == null && entity14 instanceof EntityDirtHorse && entity14.riddenByEntity == null && this.canEntityBeSeen(entity14)) {
                        this.targetFollow = entity14;
                        break;
                    }

                    if(this.entityToAttack == null && this.targetFollow == null && entity14 instanceof EntityFish && this.canEntityBeSeen(entity14)) {
                        this.targetFollow = entity14;
                        break;
                    }

                    if(this.entityToAttack == null && (this.targetFollow == null || this.targetFollow instanceof EntityClayMan) && entity14 instanceof EntityItem && this.canEntityBeSeen(entity14)) {
                        EntityItem entityItem18 = (EntityItem)entity14;
                        if(entityItem18.item != null) {
                            ItemStack ec = entityItem18.item;
                            if(ec.stackSize > 0) {
                                if(this.weaponPoints <= 0 && ec.itemID == Item.stick.itemID) {
                                    this.targetFollow = entityItem18;
                                    break;
                                }

                                if(this.armorPoints <= 0 && ec.itemID == Item.leather.itemID) {
                                    this.targetFollow = entityItem18;
                                    break;
                                }

                                if(this.rocks <= 0 && ec.itemID == Block.gravel.blockID) {
                                    this.targetFollow = entityItem18;
                                    break;
                                }

                                if(!this.glowing && ec.itemID == Item.dustGlowstone.itemID) {
                                    this.targetFollow = entityItem18;
                                    break;
                                }

                                if(!this.king && ec.itemID == Item.ingotGold.itemID) {
                                    boolean z29 = false;
                                    List b = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand(24.0D, 16.0D, 24.0D));

                                    for(int b1 = 0; b1 < b.size(); ++b1) {
                                        Entity c = (Entity)b.get(b1);
                                        if(c instanceof EntityClayMan) {
                                            EntityClayMan c1 = (EntityClayMan)c;
                                            if(c1.clayTeam == this.clayTeam && c1.king) {
                                                z29 = true;
                                                break;
                                            }
                                        }
                                    }

                                    if(!z29) {
                                        this.targetFollow = entityItem18;
                                        break;
                                    }
                                } else {
                                    if(!this.gunPowdered && ec.itemID == Item.sulphur.itemID) {
                                        this.targetFollow = entityItem18;
                                        break;
                                    }

                                    if(this.sugarTime <= 0 && ec.itemID == Item.dustSugar.itemID) {
                                        this.targetFollow = entityItem18;
                                        break;
                                    }

                                    if(this.foodLeft <= 0 && ec.getItem() != null && ec.getItem() instanceof ItemFood) {
                                        this.targetFollow = entityItem18;
                                        break;
                                    }

                                    if(this.resPoints <= 0 && ec.itemID == Item.clay.itemID) {
                                        this.targetFollow = entityItem18;
                                        break;
                                    }

                                    if(this.gooStock <= 0 && ec.itemID == Item.slimeball.itemID) {
                                        this.targetFollow = entityItem18;
                                        break;
                                    }

                                    if(this.smokeStock <= 0 && ec.itemID == Item.dustRedstone.itemID) {
                                        this.targetFollow = entityItem18;
                                        break;
                                    }

                                    if(this.weaponPoints > 0 && !this.stickSharp && ec.itemID == Item.flint.itemID) {
                                        this.targetFollow = entityItem18;
                                        break;
                                    }

                                    if(this.armorPoints > 0 && !this.armorPadded && ec.itemID == Block.wool.blockID) {
                                        this.targetFollow = entityItem18;
                                        break;
                                    }

                                    if(!this.heavyCore && this.ridingEntity == null && ec.itemID == Item.ingotIron.itemID) {
                                        this.targetFollow = entityItem18;
                                        break;
                                    }

                                    if(this.resPoints > 0 && ec.getItem() != null && ec.getItem() instanceof ItemClayMan) {
                                        ItemClayMan itemClayMan28 = (ItemClayMan)ec.getItem();
                                        if(itemClayMan28.clayTeam == this.clayTeam) {
                                            this.targetFollow = entityItem18;
                                            break;
                                        }
                                    } else {
                                        if(ec.itemID == Item.dye.itemID && ec.getMetadata() == this.teamDye(this.clayTeam)) {
                                            this.targetFollow = entityItem18;
                                            break;
                                        }

                                        if(ec.itemID == Block.planksOak.blockID && this.ridingEntity == null || ec.itemID == Block.planksOakPainted.blockID && this.ridingEntity == null) {
                                            byte b25 = 0;
                                            if(this.logs < 20 && ec.stackSize >= 5) {
                                                b25 = 1;
                                            }

                                            if(b25 > 0) {
                                                this.targetFollow = entityItem18;
                                                break;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if(this.entityToAttack != null) {
                if(this.strikeTime <= 0 && this.canEntityBeSeen(this.entityToAttack) && (double)this.getDistanceToEntity(this.entityToAttack) < (this.weaponPoints > 0 ? 1.0D : 0.7D) + (double)this.rand.nextFloat() * 0.1D) {
                    if(this.hitTargetMakesDead(this.entityToAttack)) {
                        this.entityToAttack = null;
                        this.setPathToEntity((PathEntity)null);
                    }
                } else if(this.rocks > 0 && this.throwTime <= 0 && this.canEntityBeSeen(this.entityToAttack)) {
                    item = (double)this.getDistanceToEntity(this.entityToAttack);
                    if(item >= 1.75D && item <= 7.0D) {
                        --this.rocks;
                        this.throwTime = 20;
                        this.throwRockAtEnemy(this.entityToAttack);
                    }
                }
            } else if(this.targetFollow != null) {
                if(!this.hasPath() || this.rand.nextInt(10) == 0) {
                    this.setPathToEntity(this.worldObj.getPathToEntity(this.targetFollow, this, 16.0F));
                }

                if(this.targetFollow instanceof EntityItem) {
                    EntityItem entityItem15 = (EntityItem)this.targetFollow;
                    if(entityItem15.item != null && this.canEntityBeSeen(entityItem15) && (double)this.getDistanceToEntity(entityItem15) < 0.75D) {
                        ItemStack itemStack16 = entityItem15.item;
                        if(itemStack16.stackSize > 0) {
                            if(itemStack16.itemID == Item.stick.itemID) {
                                this.weaponPoints = 15;
                                this.stickSharp = false;
                                this.gotcha((EntityItem)this.targetFollow);
                            } else if(itemStack16.itemID == Item.leather.itemID) {
                                this.armorPoints = 15;
                                this.armorPadded = false;
                                this.gotcha((EntityItem)this.targetFollow);
                            } else if(itemStack16.itemID == Block.gravel.blockID) {
                                this.rocks = 15;
                                this.gotcha((EntityItem)this.targetFollow);
                            } else if(itemStack16.itemID == Item.dustGlowstone.itemID) {
                                this.glowing = true;
                                this.gotcha((EntityItem)this.targetFollow);
                            } else if(itemStack16.itemID == Item.ingotGold.itemID) {
                                boolean z22 = false;
                                List list23 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand(24.0D, 16.0D, 24.0D));

                                for(int i32 = 0; i32 < list23.size(); ++i32) {
                                    Entity entity27 = (Entity)list23.get(i32);
                                    if(entity27 instanceof EntityClayMan) {
                                        EntityClayMan entityClayMan33 = (EntityClayMan)entity27;
                                        if(entityClayMan33.clayTeam == this.clayTeam && entityClayMan33.king) {
                                            z22 = true;
                                            break;
                                        }
                                    }
                                }

                                if(!z22) {
                                    this.king = true;
                                    this.gotcha((EntityItem)this.targetFollow);
                                    entityItem15.setEntityDead();
                                } else {
                                    this.targetFollow = null;
                                    this.setPathToEntity((PathEntity)null);
                                }
                            } else if(itemStack16.itemID == Item.sulphur.itemID) {
                                this.gunPowdered = true;
                                this.gotcha((EntityItem)this.targetFollow);
                            } else if(itemStack16.itemID == Item.dustSugar.itemID) {
                                this.sugarTime = 1200;
                                this.gotcha((EntityItem)this.targetFollow);
                            } else if(itemStack16.getItem() != null && itemStack16.getItem() instanceof ItemFood) {
                                this.foodLeft = 4;
                                this.gotcha((EntityItem)this.targetFollow);
                            } else if(itemStack16.itemID == Item.clay.itemID) {
                                this.resPoints = 4;
                                this.gotcha((EntityItem)this.targetFollow);
                            } else if(itemStack16.itemID == Item.dustRedstone.itemID) {
                                this.smokeStock = 2;
                                this.gotcha((EntityItem)this.targetFollow);
                            } else if(itemStack16.itemID == Item.slimeball.itemID) {
                                this.gooStock = 2;
                                this.gotcha((EntityItem)this.targetFollow);
                            } else if(itemStack16.itemID == Item.ingotIron.itemID) {
                                this.heavyCore = true;
                                this.gotcha((EntityItem)this.targetFollow);
                            } else {
                                double d24;
                                double d30;
                                double d36;
                                if(itemStack16.itemID == Item.flint.itemID) {
                                    if(this.weaponPoints > 0) {
                                        this.stickSharp = true;

                                        for(i17 = 0; i17 < 4; ++i17) {
                                            d24 = this.posX + (double)(this.rand.nextFloat() - this.rand.nextFloat()) * 0.125D;
                                            d30 = this.boundingBox.minY + 0.125D + (double)(this.rand.nextFloat() - this.rand.nextFloat()) * 0.25D;
                                            d36 = this.posZ + (double)(this.rand.nextFloat() - this.rand.nextFloat()) * 0.125D;
                                            Minecraft.getMinecraft().effectRenderer.addEffect(new EntityDiggingFX(this.worldObj, d24, d30, d36, 0.0D, 0.0D, 0.0D, Block.planksOak, 0, 0));
                                        }

                                        this.worldObj.playSoundAtEntity(this, "random.wood click", 0.75F, 1.0F / (this.rand.nextFloat() * 0.2F + 0.9F));
                                    }

                                    this.targetFollow = null;
                                } else if(itemStack16.itemID == Block.wool.blockID) {
                                    if(this.armorPoints > 0) {
                                        this.armorPadded = true;

                                        for(i17 = 0; i17 < 4; ++i17) {
                                            d24 = this.posX + (double)(this.rand.nextFloat() - this.rand.nextFloat()) * 0.125D;
                                            d30 = this.boundingBox.minY + 0.125D + (double)(this.rand.nextFloat() - this.rand.nextFloat()) * 0.25D;
                                            d36 = this.posZ + (double)(this.rand.nextFloat() - this.rand.nextFloat()) * 0.125D;
                                            Minecraft.getMinecraft().effectRenderer.addEffect(new EntityDiggingFX(this.worldObj, d24, d30, d36, 0.0D, 0.0D, 0.0D, Block.wool, 0, 0));
                                        }

                                        this.worldObj.playSoundAtEntity(this, "step.cloth", 0.75F, 1.0F / (this.rand.nextFloat() * 0.2F + 0.9F));
                                    }

                                    this.targetFollow = null;
                                } else if(itemStack16.getItem() != null && itemStack16.getItem() instanceof ItemClayMan) {
                                    this.swingArm();
                                    this.worldObj.playSoundAtEntity(entityItem15, "step.gravel", 0.8F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F) * 0.9F);
                                    Item item34 = ClaySoldiers.greyDoll;
                                    if(this.clayTeam == 1) {
                                        item34 = ClaySoldiers.redDoll;
                                    } else if(this.clayTeam == 2) {
                                        item34 = ClaySoldiers.yellowDoll;
                                    } else if(this.clayTeam == 3) {
                                        item34 = ClaySoldiers.greenDoll;
                                    } else if(this.clayTeam == 4) {
                                        item34 = ClaySoldiers.blueDoll;
                                    } else if(this.clayTeam == 5) {
                                        item34 = ClaySoldiers.orangeDoll;
                                    } else if(this.clayTeam == 6) {
                                        item34 = ClaySoldiers.purpleDoll;
                                    }

                                    for(int i26 = 0; i26 < 18; ++i26) {
                                        a = this.posX + (double)(this.rand.nextFloat() - this.rand.nextFloat()) * 0.125D;
                                        double d35 = this.posY + 0.25D + (double)(this.rand.nextFloat() - this.rand.nextFloat()) * 0.125D;
                                        double d37 = this.posZ + (double)(this.rand.nextFloat() - this.rand.nextFloat()) * 0.125D;
                                        Minecraft.getMinecraft().effectRenderer.addEffect(new EntitySlimeFX(this.worldObj, a, d35, d37, item34));
                                    }

                                    entityClayMan21 = new EntityClayMan(this.worldObj, entityItem15.posX, entityItem15.posY, entityItem15.posZ, this.clayTeam);
                                    this.worldObj.entityJoinedWorld(entityClayMan21);
                                    this.gotcha((EntityItem)this.targetFollow);
                                    --this.resPoints;
                                } else if(itemStack16.itemID == Block.planksOak.blockID && this.ridingEntity == null || itemStack16.itemID == Block.planksOakPainted.blockID && this.ridingEntity == null) {
                                    byte b31 = 0;
                                    if(this.logs < 20 && itemStack16.stackSize >= 5) {
                                        b31 = 1;
                                    }

                                    if(b31 > 0) {
                                        this.worldObj.playSoundAtEntity(this, "random.pop", 0.2F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
                                        if(b31 == 1) {
                                            this.logs += 5;
                                            if(entityItem15.item != null) {
                                                entityItem15.item.stackSize -= 5;
                                            }
                                        }

                                        if(entityItem15.item == null || entityItem15.item.stackSize <= 0) {
                                            entityItem15.setEntityDead();
                                        }
                                    }

                                    this.setPathToEntity((PathEntity)null);
                                    this.targetFollow = null;
                                } else if(itemStack16.itemID == Item.dye.itemID) {
                                    this.targetFollow = null;
                                }
                            }
                        }
                    }
                } else if(this.targetFollow instanceof EntityClayMan && (double)this.getDistanceToEntity(this.targetFollow) < 1.75D) {
                    this.targetFollow = null;
                } else if(this.targetFollow instanceof EntityFish && (double)this.getDistanceToEntity(this.targetFollow) < 1.0D) {
                    this.targetFollow = null;
                } else if(this.targetFollow instanceof EntityDirtHorse && (double)this.getDistanceToEntity(this.targetFollow) < 0.75D && this.gooTime <= 0) {
                    if(this.ridingEntity == null && this.targetFollow.riddenByEntity == null && !this.heavyCore && this.logs <= 0) {
                        this.mountEntity(this.targetFollow);
                        this.worldObj.playSoundAtEntity(this, "step.gravel", 0.6F, 1.0F / (this.rand.nextFloat() * 0.2F + 0.9F));
                    }

                    this.targetFollow = null;
                }
            } else {
                this.updateBlockFinder();
                if(this.logs > 0 && this.rand.nextInt(16) == 0) {
                    this.updateBuildings();
                }
            }
        }

        if(this.isSwinging) {
            this.prevSwingProgress += 0.15F;
            this.swingProgress += 0.15F;
            if(this.prevSwingProgress > 1.0F || this.swingProgress > 1.0F) {
                this.isSwinging = false;
                this.prevSwingProgress = 0.0F;
                this.swingProgress = 0.0F;
            }
        }

        if(this.isSwingingLeft) {
            this.swingLeft += 0.15F;
            if(this.swingLeft > 1.0F) {
                this.isSwingingLeft = false;
                this.swingLeft = 0.0F;
            }
        }
    }

    public void updateBlockFinder() {
        int x = MathHelper.floor_double(this.posX);
        int y = MathHelper.floor_double(this.boundingBox.minY);
        int z = MathHelper.floor_double(this.posZ);
        if(this.blockX != 0 && this.blockY != 0 && this.blockZ != 0 && !this.hasPath()) {
            PathEntity i = this.worldObj.getEntityPathToXYZ(this, this.blockX, this.blockY, this.blockZ, 16.0F);
            if(i != null && this.rand.nextInt(5) != 0) {
                this.setPathToEntity(i);
            } else {
                this.blockX = 0;
                this.blockY = 0;
                this.blockZ = 0;
            }
        }

        int i11 = x;
        int j = y;
        int k = z;

        for(int q = 0; q < 16; ++q) {
            if(j >= 4 && j <= 124 && this.isAirySpace(i11, j, k) && !this.isAirySpace(i11, j - 1, k)) {
                int b = j - 1;
                if(this.checkSides(i11, b, k, i11, j, k, this.blocDist(i11, b, k, x, y, z), q == 0)) {
                    break;
                }

                ++b;
                int a = i11 - 1;
                if(this.checkSides(a, b, k, i11, j, k, this.blocDist(a, b, k, x, y, z), q == 0)) {
                    break;
                }

                a += 2;
                if(this.checkSides(a, b, k, i11, j, k, this.blocDist(a, b, k, x, y, z), q == 0)) {
                    break;
                }

                --a;
                int c = k - 1;
                if(this.checkSides(a, b, c, i11, j, k, this.blocDist(a, b, c, x, y, z), q == 0)) {
                    break;
                }

                c += 2;
                if(this.checkSides(a, b, c, i11, j, k, this.blocDist(a, b, c, x, y, z), q == 0)) {
                    break;
                }

                i11 = x + this.rand.nextInt(6) - this.rand.nextInt(6);
                j = y + this.rand.nextInt(3) - this.rand.nextInt(3);
                k = z + this.rand.nextInt(6) - this.rand.nextInt(6);
            }
        }

    }

    public double blocDist(int a, int b, int c, int x, int y, int z) {
        double i = (double)(a - x);
        double j = (double)(b - y);
        double k = (double)(c - z);
        return Math.sqrt(i * i + j * j + k * k);
    }

    public boolean isAirySpace(int x, int y, int z) {
        int p = this.worldObj.getBlockId(x, y, z);
        return p == 0 || Block.blocksList[p].getCollisionBoundingBoxFromPool(this.worldObj, x, y, z) == null;
    }

    public boolean checkSides(int a, int b, int c, int i, int j, int k, double dist, boolean first) {
        if(b > 4 && b < 124 && this.worldObj.getBlockId(a, b, c) == Block.chestPlanksOak.blockID || b > 4 && b < 124 && this.worldObj.getBlockId(a, b, c) == Block.chestPlanksOakPainted.blockID) {
            if(first && this.blockX == i && this.blockY == j && this.blockZ == k) {
                this.setPathToEntity((PathEntity)null);
                this.blockX = 0;
                this.blockY = 0;
                this.blockZ = 0;
                this.chestOperations(a, b, c, true);
                return true;
            }

            if(this.blockX == 0 && this.blockY == 0 && this.blockZ == 0 && this.chestOperations(a, b, c, false)) {
                PathEntity emily = this.worldObj.getEntityPathToXYZ(this, i, j, k, 16.0F);
                if(emily != null) {
                    this.setPathToEntity(emily);
                    this.blockX = i;
                    this.blockY = j;
                    this.blockZ = k;
                    return true;
                }
            }
        }

        return false;
    }

    public boolean chestOperations(int x, int y, int z, boolean arrived) {
        TileEntity te = this.worldObj.getBlockTileEntity(x, y, z);
        if(te != null && te instanceof TileEntityChest) {
            TileEntityChest chest = (TileEntityChest)te;

            for(int q = 0; q < chest.getSizeInventory(); ++q) {
                if(chest.getStackInSlot(q) != null) {
                    ItemStack stack = chest.getStackInSlot(q);
                    if(stack.stackSize > 0) {
                        if(this.weaponPoints <= 0 && stack.itemID == Item.stick.itemID) {
                            if(arrived) {
                                this.weaponPoints = 15;
                                this.stickSharp = false;
                                this.gotcha(chest, q);
                            }

                            return true;
                        }

                        if(this.armorPoints <= 0 && stack.itemID == Item.leather.itemID) {
                            if(arrived) {
                                this.armorPoints = 15;
                                this.armorPadded = false;
                                this.gotcha(chest, q);
                            }

                            return true;
                        }

                        if(this.rocks <= 0 && stack.itemID == Block.gravel.blockID) {
                            if(arrived) {
                                this.rocks = 15;
                                this.gotcha(chest, q);
                            }

                            return true;
                        }

                        if(!this.glowing && stack.itemID == Item.dustGlowstone.itemID) {
                            if(arrived) {
                                this.glowing = true;
                                this.gotcha(chest, q);
                            }

                            return true;
                        }

                        int a;
                        if(!this.king && stack.itemID == Item.ingotGold.itemID) {
                            boolean z21 = false;
                            List list24 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand(24.0D, 16.0D, 24.0D));

                            for(a = 0; a < list24.size(); ++a) {
                                Entity entity23 = (Entity)list24.get(a);
                                if(entity23 instanceof EntityClayMan) {
                                    EntityClayMan entityClayMan25 = (EntityClayMan)entity23;
                                    if(entityClayMan25.clayTeam == this.clayTeam && entityClayMan25.king) {
                                        z21 = true;
                                        break;
                                    }
                                }
                            }

                            if(!z21) {
                                if(arrived) {
                                    this.king = true;
                                    this.gotcha(chest, q);
                                }

                                return true;
                            }
                        } else {
                            if(!this.gunPowdered && stack.itemID == Item.sulphur.itemID) {
                                if(arrived) {
                                    this.gunPowdered = true;
                                    this.gotcha(chest, q);
                                }

                                return true;
                            }

                            if(this.sugarTime <= 0 && stack.itemID == Item.dustSugar.itemID) {
                                if(arrived) {
                                    this.sugarTime = 1200;
                                    this.gotcha(chest, q);
                                }

                                return true;
                            }

                            if(this.foodLeft <= 0 && stack.getItem() != null && stack.getItem() instanceof ItemFood) {
                                if(arrived) {
                                    this.foodLeft = 4;
                                    this.gotcha(chest, q);
                                }

                                return true;
                            }

                            if(this.resPoints <= 0 && stack.itemID == Item.clay.itemID) {
                                if(arrived) {
                                    this.resPoints = 4;
                                    this.gotcha(chest, q);
                                }

                                return true;
                            }

                            if(this.gooStock <= 0 && stack.itemID == Item.slimeball.itemID) {
                                if(arrived) {
                                    this.gooStock = 2;
                                    this.gotcha(chest, q);
                                }

                                return true;
                            }

                            if(this.smokeStock <= 0 && stack.itemID == Item.dustRedstone.itemID) {
                                if(arrived) {
                                    this.smokeStock = 2;
                                    this.gotcha(chest, q);
                                }

                                return true;
                            }

                            if(stack.itemID == Block.planksOak.blockID && this.ridingEntity == null || stack.itemID == Block.planksOakPainted.blockID && this.ridingEntity == null) {
                                byte b19 = 0;
                                if(this.logs < 20 && stack.stackSize >= 5) {
                                    b19 = 1;
                                }

                                if(arrived && b19 > 0) {
                                    this.worldObj.playSoundAtEntity(this, "random.pop", 0.2F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
                                    if(b19 == 1) {
                                        this.logs += 5;
                                        chest.decrStackSize(q, 5);
                                    }
                                }

                                return b19 > 0 || arrived;
                            }

                            double a1;
                            double b1;
                            int i18;
                            double d22;
                            if(this.weaponPoints > 0 && !this.stickSharp && stack.itemID == Item.flint.itemID) {
                                if(arrived) {
                                    this.stickSharp = true;

                                    for(i18 = 0; i18 < 4; ++i18) {
                                        d22 = this.posX + (double)(this.rand.nextFloat() - this.rand.nextFloat()) * 0.125D;
                                        a1 = this.boundingBox.minY + 0.125D + (double)(this.rand.nextFloat() - this.rand.nextFloat()) * 0.25D;
                                        b1 = this.posZ + (double)(this.rand.nextFloat() - this.rand.nextFloat()) * 0.125D;
                                        Minecraft.getMinecraft().effectRenderer.addEffect(new EntityDiggingFX(this.worldObj, d22, a1, b1, 0.0D, 0.0D, 0.0D, Block.planksOak, 0, 0));
                                    }

                                    this.worldObj.playSoundAtEntity(this, "random.wood click", 0.75F, 1.0F / (this.rand.nextFloat() * 0.2F + 0.9F));
                                }

                                return true;
                            }

                            if(this.armorPoints > 0 && !this.armorPadded && stack.itemID == Block.wool.blockID) {
                                if(arrived) {
                                    this.armorPadded = true;

                                    for(i18 = 0; i18 < 4; ++i18) {
                                        d22 = this.posX + (double)(this.rand.nextFloat() - this.rand.nextFloat()) * 0.125D;
                                        a1 = this.boundingBox.minY + 0.125D + (double)(this.rand.nextFloat() - this.rand.nextFloat()) * 0.25D;
                                        b1 = this.posZ + (double)(this.rand.nextFloat() - this.rand.nextFloat()) * 0.125D;
                                        Minecraft.getMinecraft().effectRenderer.addEffect(new EntityDiggingFX(this.worldObj, d22, a1, b1, 0.0D, 0.0D, 0.0D, Block.wool, 0, 0));
                                    }

                                    this.worldObj.playSoundAtEntity(this, "step.cloth", 0.75F, 1.0F / (this.rand.nextFloat() * 0.2F + 0.9F));
                                }

                                return true;
                            }

                            if(!this.heavyCore && this.ridingEntity == null && stack.itemID == Item.ingotIron.itemID) {
                                if(arrived) {
                                    this.heavyCore = true;
                                    this.gotcha(chest, q);
                                }

                                return true;
                            }

                            if(this.resPoints > 0 && stack.getItem() != null && stack.getItem() instanceof ItemClayMan) {
                                ItemClayMan ic = (ItemClayMan)stack.getItem();
                                if(ic.clayTeam == this.clayTeam) {
                                    if(arrived) {
                                        this.swingArm();
                                        Item item1 = ClaySoldiers.greyDoll;
                                        if(this.clayTeam == 1) {
                                            item1 = ClaySoldiers.redDoll;
                                        } else if(this.clayTeam == 2) {
                                            item1 = ClaySoldiers.yellowDoll;
                                        } else if(this.clayTeam == 3) {
                                            item1 = ClaySoldiers.greenDoll;
                                        } else if(this.clayTeam == 4) {
                                            item1 = ClaySoldiers.blueDoll;
                                        } else if(this.clayTeam == 5) {
                                            item1 = ClaySoldiers.orangeDoll;
                                        } else if(this.clayTeam == 6) {
                                            item1 = ClaySoldiers.purpleDoll;
                                        }

                                        for(a = 0; a < 18; ++a) {
                                            a1 = this.posX + (double)(this.rand.nextFloat() - this.rand.nextFloat()) * 0.125D;
                                            b1 = this.posY + 0.25D + (double)(this.rand.nextFloat() - this.rand.nextFloat()) * 0.125D;
                                            double c1 = this.posZ + (double)(this.rand.nextFloat() - this.rand.nextFloat()) * 0.125D;
                                            Minecraft.getMinecraft().effectRenderer.addEffect(new EntitySlimeFX(this.worldObj, a1, b1, c1, item1));
                                        }

                                        double d20 = this.posX + (double)(this.rand.nextFloat() - this.rand.nextFloat()) * 0.125D;
                                        double b = this.posY + (double)this.rand.nextFloat() * 0.125D;
                                        double c = this.posZ + (double)(this.rand.nextFloat() - this.rand.nextFloat()) * 0.125D;
                                        EntityClayMan ec = new EntityClayMan(this.worldObj, d20, b, c, this.clayTeam);
                                        this.worldObj.entityJoinedWorld(ec);
                                        this.gotcha(chest, q);
                                        --this.resPoints;
                                    }

                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }

        return false;
    }

    public void updateBuildings() {
        int x = MathHelper.floor_double(this.posX);
        int y = MathHelper.floor_double(this.boundingBox.minY);
        int z = MathHelper.floor_double(this.posZ);
        if(y >= 4 && y <= 120) {
            byte broad = 2;
            byte high = 3;
            if(this.logs == 20) {
                broad = 3;
                high = 4;
            }

            boolean flag = false;

            for(int gee = -broad; gee < broad + 1 && !flag; ++gee) {
                for(int b = -1; b < high + 1 && !flag; ++b) {
                    for(int list = -broad; list < broad + 1 && !flag; ++list) {
                        if(b == -1) {
                            if(this.isAirySpace(x + gee, y + b, z + list)) {
                                flag = true;
                            }
                        } else if(!this.isAirySpace(x + gee, y + b, z + list) || this.worldObj.getBlockMaterial(x + gee, y + b, z + list) == Material.water) {
                            flag = true;
                        }
                    }
                }
            }

            if(!flag) {
                double d10 = (double)broad;
                List list11 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand(d10, d10, d10));
                if(list11.size() > 0) {
                    flag = true;
                }
            }

            if(!flag) {
                if(this.logs == 20 && this.rand.nextInt(2) == 0) {
                    this.buildHouseThree();
                } else if(this.logs >= 10 && this.rand.nextInt(3) > 0) {
                    this.buildHouseTwo();
                } else if(this.logs >= 5) {
                    this.buildHouseOne();
                }
            }

        }
    }

    public void dropLogs() {
        this.dropItem(Block.wool.blockID, this.logs);
        this.logs = 0;
    }

    public void buildHouseOne() {
        int x = MathHelper.floor_double(this.posX + 0.5D);
        int y = MathHelper.floor_double(this.boundingBox.minY);
        int z = MathHelper.floor_double(this.posZ + 0.5D);
        int direction = this.rand.nextInt(4);

        for(int j = 0; j < 3; ++j) {
            int b = j;

            for(int i = -1; i < 3; ++i) {
                for(int k = -1; k < 2; ++k) {
                    int a = i;
                    int c = k;
                    if(direction % 2 == 0) {
                        a = -i;
                        c = -k;
                    }

                    if(direction / 2 == 0) {
                        int swap = a;
                        a = c;
                        c = swap;
                    }

                    if(j == 0) {
                        if(i != -1 && i != 2 && k != -1) {
                            this.worldObj.setBlockWithNotify(x + a, y + b, z + c, 0);
                        } else {
                            this.worldObj.setBlockWithNotify(x + a, y + b, z + c, Block.planksOak.blockID);
                        }
                    } else if(j == 1) {
                        if(i == -1) {
                            this.worldObj.setBlock(x + a, y + b, z + c, Block.stairsPlanksOak.blockID);
                            this.worldObj.setBlockMetadataWithNotify(x + a, y + b, z + c, (direction + 2 + (direction % 2 == 0 ? 1 : -1)) % 4);
                        } else if(i == 2) {
                            this.worldObj.setBlock(x + a, y + b, z + c, Block.stairsPlanksOak.blockID);
                            this.worldObj.setBlockMetadataWithNotify(x + a, y + b, z + c, (direction + 2) % 4);
                        } else if(k == -1) {
                            this.worldObj.setBlockWithNotify(x + a, y + b, z + c, Block.planksOak.blockID);
                        } else {
                            this.worldObj.setBlockWithNotify(x + a, y + b, z + c, 0);
                        }
                    } else if(i == 0) {
                        this.worldObj.setBlock(x + a, y + b, z + c, Block.stairsPlanksOak.blockID);
                        this.worldObj.setBlockMetadataWithNotify(x + a, y + b, z + c, (direction + 2 + (direction % 2 == 0 ? 1 : -1)) % 4);
                    } else if(i == 1) {
                        this.worldObj.setBlock(x + a, y + b, z + c, Block.stairsPlanksOak.blockID);
                        this.worldObj.setBlockMetadataWithNotify(x + a, y + b, z + c, (direction + 2) % 4);
                    } else {
                        this.worldObj.setBlockWithNotify(x + a, y + b, z + c, 0);
                    }
                }
            }
        }

        this.worldObj.playSoundAtEntity(this, "random.wood click", 1.75F, 1.0F / (this.rand.nextFloat() * 0.2F + 0.9F));
        this.worldObj.playSoundAtEntity(this, "step.wood", 1.75F, 1.0F / (this.rand.nextFloat() * 0.2F + 0.9F));
        this.logs -= 5;
    }

    public void buildHouseTwo() {
        int x = MathHelper.floor_double(this.posX);
        int y = MathHelper.floor_double(this.boundingBox.minY);
        int z = MathHelper.floor_double(this.posZ);
        int direction = this.rand.nextInt(4);

        for(int j = 0; j < 3; ++j) {
            int b = j;

            for(int i = -2; i < 3; ++i) {
                for(int k = -2; k < 3; ++k) {
                    int a = i;
                    int c = k;
                    if(direction % 2 == 0) {
                        a = -i;
                        c = -k;
                    }

                    if(direction / 2 == 0) {
                        int swap = a;
                        a = c;
                        c = swap;
                    }

                    if(i != -2 && i != 2 || k != -2 && k != 2) {
                        if(j != 0 && j != 1) {
                            if(j == 2) {
                                if(i == -2) {
                                    this.worldObj.setBlock(x + a, y + b, z + c, Block.stairsPlanksOak.blockID);
                                    this.worldObj.setBlockMetadataWithNotify(x + a, y + b, z + c, (direction + 2 + (direction % 2 == 0 ? 1 : -1)) % 4);
                                } else if(i == 2) {
                                    this.worldObj.setBlock(x + a, y + b, z + c, Block.stairsPlanksOak.blockID);
                                    this.worldObj.setBlockMetadataWithNotify(x + a, y + b, z + c, (direction + 2) % 4);
                                } else if(k == -2) {
                                    this.worldObj.setBlock(x + a, y + b, z + c, Block.stairsPlanksOak.blockID);
                                    this.worldObj.setBlockMetadataWithNotify(x + a, y + b, z + c, (direction + (direction % 2 == 0 ? 1 : -1)) % 4);
                                } else if(k == 2) {
                                    this.worldObj.setBlock(x + a, y + b, z + c, Block.stairsPlanksOak.blockID);
                                    this.worldObj.setBlockMetadataWithNotify(x + a, y + b, z + c, direction % 4);
                                } else {
                                    this.worldObj.setBlockWithNotify(x + a, y + b, z + c, Block.planksOak.blockID);
                                }
                            }
                        } else if(i != -2 && i != 2 && k != -2 && (k != 2 || i == 0 && j != 1)) {
                            this.worldObj.setBlockWithNotify(x + a, y + b, z + c, 0);
                        } else {
                            this.worldObj.setBlockWithNotify(x + a, y + b, z + c, Block.planksOak.blockID);
                        }
                    }
                }
            }
        }

        this.worldObj.playSoundAtEntity(this, "random.wood click", 1.75F, 1.0F / (this.rand.nextFloat() * 0.2F + 0.9F));
        this.worldObj.playSoundAtEntity(this, "step.wood", 1.75F, 1.0F / (this.rand.nextFloat() * 0.2F + 0.9F));
        this.logs -= 10;
    }

    public void buildHouseThree() {
        int x = MathHelper.floor_double(this.posX);
        int y = MathHelper.floor_double(this.boundingBox.minY);
        int z = MathHelper.floor_double(this.posZ);
        int direction = this.rand.nextInt(4);

        for(int j = 0; j < 4; ++j) {
            int b = j;

            for(int i = -3; i < 4; ++i) {
                for(int k = -2; k < 3; ++k) {
                    int a = i;
                    int c = k;
                    if(direction % 2 == 0) {
                        a = -i;
                        c = -k;
                    }

                    if(direction / 2 == 0) {
                        int chest = a;
                        a = c;
                        c = chest;
                    }

                    if(i != -3 && i != 3 || k != -2 && k != 2) {
                        if(j < 3) {
                            if(i != -3 && i != 3 && k != -2 && (k != 2 || i == 0 && j <= 0)) {
                                if(i == -2 && j == 0 && k == 0) {
                                    this.worldObj.setBlock(x + a, y + b, z + c, Block.chestPlanksOak.blockID);
                                    this.worldObj.setBlockMetadataWithNotify(x + a, y + b, z + c, (direction + 2) % 4);
                                    TileEntityChest tileEntityChest12 = (TileEntityChest)this.worldObj.getBlockTileEntity(x + a, y + b, z + c);
                                    tileEntityChest12.setInventorySlotContents(0, new ItemStack(Item.stick, 16, 0));
                                } else if(i == 0 && j == 0 && k == -1) {
                                    this.worldObj.setBlock(x + a, y + b, z + c, Block.stairsPlanksOak.blockID);
                                    this.worldObj.setBlockMetadataWithNotify(x + a, y + b, z + c, (direction + 2 + (direction % 2 == 0 ? 1 : -1)) % 4);
                                } else if(i == 1 && j == 1 && k == -1) {
                                    this.worldObj.setBlock(x + a, y + b, z + c, Block.stairsPlanksOak.blockID);
                                    this.worldObj.setBlockMetadataWithNotify(x + a, y + b, z + c, (direction + 2 + (direction % 2 == 0 ? 1 : -1)) % 4);
                                } else if(i == 2 && j == 1 && k == -1) {
                                    this.worldObj.setBlockWithNotify(x + a, y + b, z + c, Block.planksOak.blockID);
                                } else if(i == 2 && j == 2 && k == 0) {
                                    this.worldObj.setBlock(x + a, y + b, z + c, Block.stairsPlanksOak.blockID);
                                    this.worldObj.setBlockMetadataWithNotify(x + a, y + b, z + c, (direction + (direction % 2 == 0 ? 1 : -1)) % 4);
                                } else if(i == 0 && j == 2 && k == -1) {
                                    this.worldObj.setBlockWithNotify(x + a, y + b, z + c, 0);
                                } else if(i == 1 && j == 2 && k == -1) {
                                    this.worldObj.setBlockWithNotify(x + a, y + b, z + c, 0);
                                } else if(i == 2 && j == 2 && k == -1) {
                                    this.worldObj.setBlockWithNotify(x + a, y + b, z + c, 0);
                                } else if(j == 2) {
                                    this.worldObj.setBlockWithNotify(x + a, y + b, z + c, Block.planksOak.blockID);
                                } else {
                                    this.worldObj.setBlockWithNotify(x + a, y + b, z + c, 0);
                                }
                            } else {
                                this.worldObj.setBlockWithNotify(x + a, y + b, z + c, Block.planksOak.blockID);
                            }
                        } else if(j == 3) {
                            if(i != -3 && i != 3 && k != -2 && (k != 2 || i == 0 && j <= 0)) {
                                this.worldObj.setBlockWithNotify(x + a, y + b, z + c, 0);
                            } else if(i != -2 && i != 0 && i != 2 && k != 0) {
                                this.worldObj.setBlockWithNotify(x + a, y + b, z + c, Block.stairsPlanksOak.blockID);
                                this.worldObj.setBlockMetadataWithNotify(x + a, y + b, z + c, 2);
                            } else {
                                this.worldObj.setBlockWithNotify(x + a, y + b, z + c, Block.planksOak.blockID);
                            }
                        }
                    }
                }
            }
        }

        this.worldObj.playSoundAtEntity(this, "random.wood click", 1.75F, 1.0F / (this.rand.nextFloat() * 0.2F + 0.9F));
        this.worldObj.playSoundAtEntity(this, "step.wood", 1.75F, 1.0F / (this.rand.nextFloat() * 0.2F + 0.9F));
        this.logs -= 20;
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

    public void swingArm() {
        if(!this.isSwinging) {
            this.isSwinging = true;
            this.prevSwingProgress = 0.0F;
            this.swingProgress = 0.0F;
        }

    }

    public void swingLeftArm() {
        if(!this.isSwingingLeft) {
            this.isSwingingLeft = true;
            this.swingLeft = 0.01F;
        }

    }

    public boolean hitTargetMakesDead(Entity e) {
        this.strikeTime = 12;
        this.swingArm();
        int power = this.weaponPoints > 0 ? 3 + this.rand.nextInt(2) + (this.stickSharp ? 1 : 0) : 2;
        if(this.weaponPoints > 0) {
            --this.weaponPoints;
        }

        boolean flag = e.attackEntityFrom(this, power, (DamageType) null);
        if(flag && e instanceof EntityLiving) {
            EntityLiving el = (EntityLiving)e;
            if(el.health <= 0) {
                return true;
            }
        }

        return false;
    }

    public void throwRockAtEnemy(Entity entity) {
        double d = entity.posX - this.posX;
        double d1 = entity.posZ - this.posZ;
        EntityGravelChunk entitygravelchunk = new EntityGravelChunk(this.worldObj, this, this.clayTeam);
        entitygravelchunk.posY += 0.3999999761581421D;
        double d2 = entity.posY + (double)entity.getEyeHeight() - 0.10000000298023223D - entitygravelchunk.posY;
        float f1 = MathHelper.sqrt_double(d * d + d1 * d1) * 0.2F;
        this.worldObj.entityJoinedWorld(entitygravelchunk);
        entitygravelchunk.setArrowHeading(d, d2 + (double)f1, d1, 0.6F, 12.0F);
        this.attackTime = 30;
        this.moveForward = -this.moveForward;
        this.rotationYaw = (float)(Math.atan2(d1, d) * 180.0D / (double)(float)Math.PI) - 90.0F;
        this.hasAttacked = true;
        this.swingLeftArm();
    }

    public void gotcha(EntityItem item) {
        this.worldObj.playSoundAtEntity(item, "random.pop", 0.2F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
        if(item.item != null) {
            --item.item.stackSize;
            if(item.item.stackSize <= 0) {
                item.setEntityDead();
            }
        } else {
            item.setEntityDead();
        }

        this.targetFollow = null;
        this.setPathToEntity((PathEntity)null);
    }

    public void gotcha(TileEntityChest chest, int q) {
        this.worldObj.playSoundAtEntity(this, "random.pop", 0.2F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
        chest.decrStackSize(q, 1);
    }

    @Override
    public void writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setShort("ClayTeam", (short)this.clayTeam);
        nbttagcompound.setShort("WeaponPoints", (short)this.weaponPoints);
        nbttagcompound.setShort("ArmorPoints", (short)this.armorPoints);
        nbttagcompound.setShort("FoodLeft", (short)this.foodLeft);
        nbttagcompound.setShort("SugarTime", (short)this.sugarTime);
        nbttagcompound.setShort("ResPoints", (short)this.resPoints);
        nbttagcompound.setShort("StrikeTime", (short)this.strikeTime);
        nbttagcompound.setShort("ClimbTime", (short)this.climbTime);
        nbttagcompound.setShort("GooTime", (short)this.gooTime);
        nbttagcompound.setShort("SmokeTime", (short)this.smokeTime);
        nbttagcompound.setShort("GooStock", (short)this.gooStock);
        nbttagcompound.setShort("SmokeStock", (short)this.smokeStock);
        nbttagcompound.setShort("Logs", (short)this.logs);
        nbttagcompound.setShort("Rocks", (short)this.rocks);
        nbttagcompound.setBoolean("Gunpowdered", this.gunPowdered);
        nbttagcompound.setBoolean("King", this.king);
        nbttagcompound.setBoolean("Glowing", this.glowing);
        nbttagcompound.setBoolean("StickSharp", this.stickSharp);
        nbttagcompound.setBoolean("ArmorPadded", this.armorPadded);
        nbttagcompound.setBoolean("HeavyCore", this.heavyCore);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.clayTeam = nbttagcompound.getShort("ClayTeam");
        this.texture = this.clayManTexture(this.clayTeam);
        this.weaponPoints = nbttagcompound.getShort("WeaponPoints");
        this.armorPoints = nbttagcompound.getShort("ArmorPoints");
        this.foodLeft = nbttagcompound.getShort("FoodLeft");
        this.sugarTime = nbttagcompound.getShort("SugarTime");
        this.resPoints = nbttagcompound.getShort("ResPoints");
        this.strikeTime = nbttagcompound.getShort("StrikeTime");
        this.climbTime = nbttagcompound.getShort("ClimbTime");
        this.gooTime = nbttagcompound.getShort("GooTime");
        this.smokeTime = nbttagcompound.getShort("SmokeTime");
        this.gooStock = nbttagcompound.getShort("GooStock");
        this.smokeStock = nbttagcompound.getShort("SmokeStock");
        this.logs = nbttagcompound.getShort("Logs");
        this.rocks = nbttagcompound.getShort("Rocks");
        this.gunPowdered = nbttagcompound.getBoolean("Gunpowdered");
        this.king = nbttagcompound.getBoolean("King");
        this.glowing = nbttagcompound.getBoolean("Glowing");
        this.stickSharp = nbttagcompound.getBoolean("StickSharp");
        this.armorPadded = nbttagcompound.getBoolean("ArmorPadded");
        this.heavyCore = nbttagcompound.getBoolean("HeavyCore");
    }

    @Override
    protected String getHurtSound() {
        this.worldObj.playSoundAtEntity(this, "random.hurt", 0.6F, 1.0F * (this.rand.nextFloat() * 0.2F + 1.6F));
        this.worldObj.playSoundAtEntity(this, "step.gravel", 0.6F, 1.0F / (this.rand.nextFloat() * 0.2F + 0.9F));
        return "";
    }

    @Override
    protected String getDeathSound() {
        this.worldObj.playSoundAtEntity(this, "random.hurt", 0.6F, 1.0F * (this.rand.nextFloat() * 0.2F + 1.6F));
        return "step.gravel";
    }

    @Override
    protected void jump() {
        if(this.gooTime <= 0) {
            if(this.sugarTime > 0) {
                this.motionY = 0.375D;
            } else {
                this.motionY = 0.275D;
            }

        }
    }

    @Override
    public boolean isOnLadder() {
        if(this.logs <= 0 && this.isCollidedHorizontally && !this.onGround && this.climbTime > 0) {
            if(this.climbTime != 10) {
                this.throwTime = 5;
                --this.climbTime;
                return true;
            }

            if(this.motionY < 0.05D) {
                --this.climbTime;
                this.throwTime = 5;
                return true;
            }
        }

        return false;
    }

    @Override
    protected boolean canDespawn() {
        return false;
    }

    public boolean hasStick() {
        return this.weaponPoints > 0;
    }

    public boolean hasArmor() {
        return this.armorPoints > 0;
    }

    public boolean hasSpecks() {
        return this.gunPowdered;
    }

    public boolean hasCrown() {
        return this.king;
    }

    public boolean isGlowing() {
        return this.glowing;
    }

    public boolean isSharpened() {
        return this.stickSharp;
    }

    public boolean isPadded() {
        return this.armorPadded;
    }

    public boolean isGooey() {
        return this.gooTime > 0;
    }

    public boolean hasLogs() {
        return this.logs > 0;
    }

    public float armLeft() {
        return this.swingLeft;
    }

    public boolean hasRocks() {
        return this.rocks > 0 && this.throwTime <= 0 && this.logs <= 0;
    }

    @Override
    public void dropFewItems() {
        if(!this.gunPowdered) {
            Item item1 = ClaySoldiers.greyDoll;
            if(this.clayTeam == 1) {
                item1 = ClaySoldiers.redDoll;
            } else if(this.clayTeam == 2) {
                item1 = ClaySoldiers.yellowDoll;
            } else if(this.clayTeam == 3) {
                item1 = ClaySoldiers.greenDoll;
            } else if(this.clayTeam == 4) {
                item1 = ClaySoldiers.blueDoll;
            } else if(this.clayTeam == 5) {
                item1 = ClaySoldiers.orangeDoll;
            } else if(this.clayTeam == 6) {
                item1 = ClaySoldiers.purpleDoll;
            }

            this.dropItem(item1.itemID, 1);
            if(this.resPoints > 0) {
                this.dropItem(Item.clay.itemID, 1);
            }

            if(this.weaponPoints > 7 && this.rand.nextInt(2) == 0) {
                this.dropItem(Item.stick.itemID, 1);
            }

            if(this.armorPoints > 7 && this.rand.nextInt(2) == 0) {
                this.dropItem(Item.leather.itemID, 1);
            }

            if(this.rocks > 7 && this.rand.nextInt(2) == 0) {
                this.dropItem(Block.gravel.blockID, 1);
            }

            if(this.smokeStock > 1 && this.rand.nextInt(2) == 0) {
                this.dropItem(Item.dustRedstone.itemID, 1);
            }

            if(this.gooStock > 1 && this.rand.nextInt(2) == 0) {
                this.dropItem(Item.slimeball.itemID, 1);
            }

            if(this.smokeStock > 1 && this.rand.nextInt(2) == 0) {
                this.dropItem(Item.dustRedstone.itemID, 1);
            }

            if(this.gooStock > 1 && this.rand.nextInt(2) == 0) {
                this.dropItem(Item.slimeball.itemID, 1);
            }

            if(this.glowing && this.rand.nextInt(2) == 0) {
                this.dropItem(Item.dustGlowstone.itemID, 1);
            }

            if(this.king) {
                this.dropItem(Item.ingotGold.itemID, 1);
            }

            if(this.heavyCore) {
                this.dropItem(Item.ingotIron.itemID, 1);
            }

            if(this.logs > 0) {
                this.dropLogs();
            }
        }

    }

    @Override
    public boolean attackEntityFrom(Entity e, int i, DamageType type) {
        if(this.ridingEntity != null && i < 100 && this.rand.nextInt(2) == 0) {
            return this.ridingEntity.attackEntityFrom(e, i, (DamageType)null);
        } else {
            if(e != null && e instanceof EntityClayMan) {
                EntityClayMan fred = (EntityClayMan)e;
                if(fred.clayTeam == this.clayTeam) {
                    return false;
                }

                if(this.logs > 0) {
                    this.dropLogs();
                }

                if(this.smokeTime <= 0) {
                    this.entityToAttack = e;
                }

                if(this.armorPoints > 0) {
                    i /= 2;
                    if(this.armorPadded) {
                        --i;
                    }

                    --this.armorPoints;
                    if(i < 0) {
                        i = 0;
                    }
                }

                if(this.health - i > 0) {
                    int item1;
                    double q;
                    double b;
                    double c;
                    if((fred.smokeStock <= 0 || this.smokeTime <= 0 || this.rand.nextInt(2) == 0) && fred.gooStock > 0 && this.gooTime <= 0 && this.onGround) {
                        --fred.gooStock;
                        this.gooTime = 150;
                        this.worldObj.playSoundAtEntity(this, "mob.slimeattack", 0.75F, 1.0F / (this.rand.nextFloat() * 0.2F + 0.9F));

                        for(item1 = 0; item1 < 4; ++item1) {
                            q = this.posX + (double)(this.rand.nextFloat() - this.rand.nextFloat()) * 0.125D;
                            b = this.boundingBox.minY + 0.125D + (double)(this.rand.nextFloat() - this.rand.nextFloat()) * 0.125D;
                            c = this.posZ + (double)(this.rand.nextFloat() - this.rand.nextFloat()) * 0.125D;
                            this.worldObj.spawnParticle("slime", q, b, c, 0.0D, 0.1D, 0.0D);
                        }

                        this.motionX = 0.0D;
                        this.motionY = 0.0D;
                        this.motionZ = 0.0D;
                        this.moveForward = 0.0F;
                        this.moveStrafing = 0.0F;
                        this.isJumping = false;
                    } else if(fred.smokeStock > 0 && this.smokeTime <= 0) {
                        --fred.smokeStock;
                        this.smokeTime = 100;
                        this.worldObj.playSoundAtEntity(this, "random.fizz", 0.75F, 1.0F / (this.rand.nextFloat() * 0.2F + 0.9F));

                        for(item1 = 0; item1 < 8; ++item1) {
                            q = this.posX + (double)(this.rand.nextFloat() - this.rand.nextFloat()) * 0.125D;
                            b = this.boundingBox.minY + 0.25D + (double)this.rand.nextFloat() * 0.25D;
                            c = this.posZ + (double)(this.rand.nextFloat() - this.rand.nextFloat()) * 0.125D;
                            this.worldObj.spawnParticle("reddust", q, b, c, 0.0D, 0.1D, 0.0D);
                        }

                        this.targetFollow = null;
                        this.entityToAttack = null;
                        this.setPathToEntity((PathEntity)null);
                    }
                }
            } else {
                i = 20;
                if(e instanceof EntityFish) {
                    return false;
                }
            }

            boolean z12 = super.attackEntityFrom(e, i, (DamageType)null);
            if(z12 && this.health <= 0) {
                Item item13 = ClaySoldiers.greyDoll;
                if(this.clayTeam == 1) {
                    item13 = ClaySoldiers.redDoll;
                } else if(this.clayTeam == 2) {
                    item13 = ClaySoldiers.yellowDoll;
                } else if(this.clayTeam == 3) {
                    item13 = ClaySoldiers.greenDoll;
                } else if(this.clayTeam == 4) {
                    item13 = ClaySoldiers.blueDoll;
                } else if(this.clayTeam == 5) {
                    item13 = ClaySoldiers.orangeDoll;
                } else if(this.clayTeam == 6) {
                    item13 = ClaySoldiers.purpleDoll;
                }

                for(int i14 = 0; i14 < 24; ++i14) {
                    double a = this.posX + (double)(this.rand.nextFloat() - this.rand.nextFloat()) * 0.125D;
                    double b1 = this.posY + 0.25D + (double)(this.rand.nextFloat() - this.rand.nextFloat()) * 0.125D;
                    double c1 = this.posZ + (double)(this.rand.nextFloat() - this.rand.nextFloat()) * 0.125D;
                    Minecraft.getMinecraft().effectRenderer.addEffect(new EntitySlimeFX(this.worldObj, a, b1, c1, item13));
                }

                this.isDead = true;
                if(e != null && e instanceof EntityPlayer) {
                    this.killedByPlayer = e;
                }

                if(this.gunPowdered) {
                    this.worldObj.createExplosion((Entity)null, this.posX, this.posY, this.posZ, 1.0F);
                }
            }

            return z12;
        }
    }

    @Override
    public void addVelocity(double d, double d1, double d2) {
        if(this.gooTime <= 0) {
            this.motionX += d;
            this.motionY += d1;
            this.motionZ += d2;
        }
    }

    @Override
    public void knockBack(Entity entity, int i, double d, double d1) {
        if(this.gooTime <= 0) {
            super.knockBack(entity, i, d, d1);
            if(entity != null && entity instanceof EntityClayMan) {
                EntityClayMan ec = (EntityClayMan)entity;
                if((!ec.heavyCore || !this.heavyCore) && (ec.heavyCore || this.heavyCore)) {
                    if(!ec.heavyCore && this.heavyCore) {
                        this.motionX *= 0.2D;
                        this.motionY *= 0.4D;
                        this.motionZ *= 0.2D;
                    } else {
                        this.motionX *= 1.5D;
                        this.motionZ *= 1.5D;
                    }
                } else {
                    this.motionX *= 0.6D;
                    this.motionY *= 0.75D;
                    this.motionZ *= 0.6D;
                }
            } else if(entity != null && entity instanceof EntityGravelChunk) {
                this.motionX *= 0.6D;
                this.motionY *= 0.75D;
                this.motionZ *= 0.6D;
            }

        }
    }
}
