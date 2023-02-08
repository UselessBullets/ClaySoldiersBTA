package me.clicknin.claysoldiers;

import me.clicknin.claysoldiers.entities.EntityClayMan;
import me.clicknin.claysoldiers.entities.EntityDirtHorse;
import me.clicknin.claysoldiers.items.ItemClayDisruptor;
import me.clicknin.claysoldiers.items.ItemClayMan;
import me.clicknin.claysoldiers.items.ItemDirtHorse;
import me.clicknin.claysoldiers.model.ModelClayMan;
import me.clicknin.claysoldiers.model.ModelDirtHorse;
import me.clicknin.claysoldiers.model.RenderClayMan;
import me.clicknin.claysoldiers.model.RenderDirtHorse;
import net.fabricmc.api.ModInitializer;
import net.minecraft.src.Block;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import turniplabs.halplibe.helper.EntityHelper;
import turniplabs.halplibe.helper.ItemHelper;
import turniplabs.halplibe.helper.RecipeHelper;


public class ClaySoldiers implements ModInitializer {
    public static final String MOD_ID = "claysoldiers";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static int waveTime;

    public static final Item greyDoll = ItemHelper.createItem(MOD_ID, new ItemClayMan(2000, 0), "grey_soldier", "doll_grey.png");
    public static final Item redDoll = ItemHelper.createItem(MOD_ID, new ItemClayMan(2001, 1), "red_soldier", "doll_red.png");
    public static final Item yellowDoll = ItemHelper.createItem(MOD_ID, new ItemClayMan(2002, 2), "yellow_soldier", "doll_yellow.png");
    public static final Item greenDoll = ItemHelper.createItem(MOD_ID, new ItemClayMan(2003, 3), "green_soldier", "doll_green.png");
    public static final Item blueDoll = ItemHelper.createItem(MOD_ID, new ItemClayMan(2004, 4), "blue_soldier", "doll_blue.png");
    public static final Item orangeDoll = ItemHelper.createItem(MOD_ID, new ItemClayMan(2005, 5), "orange_soldier", "doll_orange.png");
    public static final Item purpleDoll = ItemHelper.createItem(MOD_ID, new ItemClayMan(2006, 6), "purple_soldier", "doll_purple.png");
    public static final Item dirtHorse = ItemHelper.createItem(MOD_ID, new ItemDirtHorse(2007), "dirt_horse", "doll_horse.png");
    public static final Item clayDisruptor = ItemHelper.createItem(MOD_ID, new ItemClayDisruptor(2008), "clay_disruptor", "disruptor.png");

    @Override
    public void onInitialize() {
        EntityHelper.createEntity(EntityClayMan.class, new RenderClayMan(new ModelClayMan(0.0F, 13.0F), 0.125F), 200, "ClaySoldier");
        EntityHelper.createEntity(EntityDirtHorse.class, new RenderDirtHorse(new ModelDirtHorse(0.0F, 12.75F), 0.15F), 201, "DirtHorse");

        RecipeHelper.Crafting.createRecipe(greyDoll, 4, new Object[]{"$", "#", '$', Block.sand, '#', Block.blockClay});
        RecipeHelper.Crafting.createRecipe(dirtHorse, 4, new Object[]{"#$#", "# #", '$', Block.sand, '#', Block.dirt});
        RecipeHelper.Crafting.createRecipe(clayDisruptor, 4, new Object[]{"#$#", "#@#", '$', Item.stick, '#', Block.blockClay, '@', Item.dustRedstone});

        RecipeHelper.Crafting.createShapelessRecipe(redDoll, 1, new Object[]{new ItemStack(greyDoll, 1), new ItemStack(Item.dye, 1, 1)});
        RecipeHelper.Crafting.createShapelessRecipe(yellowDoll, 1, new Object[]{new ItemStack(greyDoll, 1), new ItemStack(Item.dye, 1, 11)});
        RecipeHelper.Crafting.createShapelessRecipe(greenDoll, 1, new Object[]{new ItemStack(greyDoll, 1), new ItemStack(Item.dye, 1, 2)});
        RecipeHelper.Crafting.createShapelessRecipe(blueDoll, 1, new Object[]{new ItemStack(greyDoll, 1), new ItemStack(Item.dye, 1, 4)});
        RecipeHelper.Crafting.createShapelessRecipe(orangeDoll, 1, new Object[]{new ItemStack(greyDoll, 1), new ItemStack(Item.dye, 1, 14)});
        RecipeHelper.Crafting.createShapelessRecipe(purpleDoll, 1, new Object[]{new ItemStack(greyDoll, 1), new ItemStack(Item.dye, 1, 5)});

        LOGGER.info("Initialized!");
        LOGGER.info("This is a port of Clay Soldiers to Better than Adventure!");
        LOGGER.info("Get the original mod here: https://mcarchive.net/mods/claysoldiers?gvsn=b1.7.3");
    }
}
