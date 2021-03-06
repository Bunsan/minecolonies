package com.minecolonies.coremod.entity.ai.mobs.barbarians;

import com.minecolonies.api.configuration.Configurations;
import com.minecolonies.api.util.CompatibilityUtils;
import com.minecolonies.coremod.colony.Colony;
import com.minecolonies.coremod.colony.ColonyManager;
import com.minecolonies.coremod.entity.ai.mobs.util.BarbarianSpawnUtils;
import com.minecolonies.coremod.entity.ai.mobs.util.BarbarianUtils;
import com.minecolonies.coremod.items.ItemChiefSword;
import com.minecolonies.coremod.sounds.BarbarianSounds;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.stream.Stream;

/**
 * Abstract for all Barbarian entities.
 */
public abstract class AbstractEntityBarbarian extends EntityMob
{
    /**
     * The amount of EXP to drop on entity death.
     */
    private static final int BARBARIAN_EXP_DROP = 1;

    private static final int    BARBARIAN_HORDE_DIFFICULTY_FIVE = 5;
    /**
     * Values used to choose whether or not to play sound
     */
    private static final int    OUT_OF_ONE_HUNDRED              = 100;
    private static final int    ONE                             = 1;
    /**
     * Values used for sword effect.
     */
    private static final Potion SPEED_EFFECT                    = Potion.getPotionById(1);
    private static final int    TIME_TO_COUNTDOWN               = 240;
    private static final int    COUNTDOWN_SECOND_MULTIPLIER     = 4;
    private static final int    SPEED_EFFECT_DISTANCE           = 7;
    private static final int    SPEED_EFFECT_DURATION           = 160;
    private static final int    SPEED_EFFECT_MULTIPLIER         = 2;
    /**
     * The Entity's world.
     */
    private final        World  world                           = CompatibilityUtils.getWorld(this);
    /**
     * Sets the barbarians target colony on spawn Thus it never changes.
     */
    private final        Colony colony                          = ColonyManager.getClosestColony(CompatibilityUtils.getWorld(this), this.getPosition());

    private int currentCount = 0;

    /**
     * Constructor method for Abstract Barbarians.
     */
    public AbstractEntityBarbarian(final World worldIn)
    {
        super(worldIn);
    }

    @Override
    protected void initEntityAI()
    {
        BarbarianSpawnUtils.setBarbarianAI(this);
    }

    @Override
    protected void entityInit()
    {
        this.enablePersistence();
        super.entityInit();
    }

    @Override
    public void playLivingSound()
    {
        final SoundEvent soundevent = this.getAmbientSound();

        if (soundevent != null && world.rand.nextInt(OUT_OF_ONE_HUNDRED) <= ONE)
        {
            this.playSound(soundevent, this.getSoundVolume(), this.getSoundPitch());
        }
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound()
    {
        return BarbarianSounds.barbarianSay;
    }

    @Nullable
    @Override
    protected ResourceLocation getLootTable()
    {
        return BarbarianSpawnUtils.getBarbarianLootTable(this);
    }

    @Override
    protected boolean canDespawn()
    {
        return colony == null;
    }

    /**
     * We have loot_tables for a reason, this is done to disable equipped items dropping on death.
     *
     * @param wasRecentlyHit  Was the barbarian recently hit?
     * @param lootingModifier Was the barbarian hit with a sword with looting Enchantment?
     */
    @Override
    protected void dropEquipment(final boolean wasRecentlyHit, final int lootingModifier)
    {
        // We have loot_tables for a reason, this is done to disable equipped items dropping on death.
    }

    @Nullable
    @Override
    public IEntityLivingData onInitialSpawn(final DifficultyInstance difficulty, @Nullable final IEntityLivingData livingdata)
    {
        BarbarianSpawnUtils.setBarbarianEquipment(this);
        return super.onInitialSpawn(difficulty, livingdata);
    }

    @Override
    public void onLivingUpdate()
    {
        if (this.getHeldItemMainhand() != null && SPEED_EFFECT != null && this.getHeldItemMainhand().getItem() instanceof ItemChiefSword
              && Configurations.gameplay.barbarianHordeDifficulty >= BARBARIAN_HORDE_DIFFICULTY_FIVE
              && currentCount <= 0)
        {
            final Stream<AbstractEntityBarbarian> barbarians = BarbarianUtils.getBarbariansCloseToEntity(this, SPEED_EFFECT_DISTANCE).stream();
            barbarians.forEach(entity -> entity.addPotionEffect(new PotionEffect(SPEED_EFFECT, SPEED_EFFECT_DURATION, SPEED_EFFECT_MULTIPLIER)));
            currentCount = COUNTDOWN_SECOND_MULTIPLIER * TIME_TO_COUNTDOWN;
        }
        else
        {
            --currentCount;
        }

        super.onLivingUpdate();
    }

    @Override
    protected SoundEvent getHurtSound()
    {
        return BarbarianSounds.barbarianHurt;
    }

    @Override
    protected SoundEvent getDeathSound()
    {
        return BarbarianSounds.barbarianDeath;
    }

    @Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        BarbarianSpawnUtils.setBarbarianAttributes(this, colony);
    }

    @Override
    protected void onDeathUpdate()
    {
        if (!(this.getAttackingEntity() instanceof EntityPlayer) && (this.recentlyHit > 0 && this.canDropLoot() && world.getGameRules().getBoolean("doMobLoot")))
        {
            final int experience = EntityXPOrb.getXPSplit(BARBARIAN_EXP_DROP);
            CompatibilityUtils.spawnEntity(world, new EntityXPOrb(world, this.posX, this.posY, this.posZ, experience));
        }
        super.onDeathUpdate();
    }
}
