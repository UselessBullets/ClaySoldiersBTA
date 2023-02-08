package me.clicknin.claysoldiers.model;

import net.minecraft.src.EntityLiving;
import net.minecraft.src.ModelBiped;
import net.minecraft.src.RenderBiped;
import org.lwjgl.opengl.GL11;

public class RenderDirtHorse extends RenderBiped {
    public RenderDirtHorse(ModelBiped model, float f) {
        super(model, f);
    }

    @Override
    protected void preRenderCallback(EntityLiving entityliving, float f) {
        GL11.glScalef(0.7F, 0.7F, 0.7F);
    }

    @Override
    public void doRenderLiving(EntityLiving entityliving, double d, double d1, double d2, float f, float f1) {
        f1 *= 2.0F;
        super.doRenderLiving(entityliving, d, d1, d2, f, f1);
    }
}
