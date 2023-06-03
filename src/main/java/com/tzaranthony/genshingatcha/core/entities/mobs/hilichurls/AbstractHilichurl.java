package com.tzaranthony.genshingatcha.core.entities.mobs.hilichurls;

import com.tzaranthony.genshingatcha.core.util.Element;
import com.tzaranthony.genshingatcha.core.util.EntityElementDamageSource;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biome;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractHilichurl extends Monster implements ElementalMonster {
    protected int element;

    public AbstractHilichurl(EntityType<? extends AbstractHilichurl> type, Level level) {
        super(type, level);
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(8, new RandomStrollGoal(this, 0.6D));
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 15.0F, 1.0F));
        this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Mob.class, 15.0F));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, AbstractHilichurl.class)).setAlertOthers());
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, Slime.class)).setAlertOthers());
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, AbstractVillager.class, false));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Raider.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
    }

    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("Element", this.element);
    }

    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.element = tag.getInt("Element");
    }

    public MobType getMobType() {
        //TODO: maybe change this to a custom Hilichurl type
        return MobType.ILLAGER;
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor accessor, DifficultyInstance difficulty, MobSpawnType spawnType, @Nullable SpawnGroupData groupData, @Nullable CompoundTag tag) {
        SpawnGroupData spawngroupdata = super.finalizeSpawn(accessor, difficulty, spawnType, groupData, tag);
        Holder<Biome> holder = accessor.getBiome(this.blockPosition());
        Biome biome = holder.value();
        Biome.BiomeCategory cat = Biome.getBiomeCategory(holder);
        //**
        // Cold = cryo (0)
        // Neutral = anemo (6)
        // Hot = pyro (1)
        // Mountains = geo (3)
        // Forest = dendro (5)
        // Plains = electro (2)
        // Wetlands = hydro (4)
        // **//
        boolean isCold = biome.getBaseTemperature() < 0.15F;
        boolean isHot = biome.getBaseTemperature() > 1.0F;
        boolean isWarm = !isCold && !isHot;
        boolean isMountains = cat == Biome.BiomeCategory.EXTREME_HILLS || cat == Biome.BiomeCategory.MESA || cat == Biome.BiomeCategory.MOUNTAIN;
        boolean isForest = cat == Biome.BiomeCategory.JUNGLE || cat == Biome.BiomeCategory.FOREST || cat == Biome.BiomeCategory.TAIGA;
        boolean isPlains = cat == Biome.BiomeCategory.PLAINS || cat == Biome.BiomeCategory.SAVANNA;
        boolean isHumid = biome.isHumid() && biome.getPrecipitation() != Biome.Precipitation.SNOW;

        return spawngroupdata;
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        return source == Element.ElementGetter.get(this.element).getDamage() || (source instanceof EntityElementDamageSource eds && eds.getElement() == this.element) || super.isInvulnerableTo(source);
    }

    public boolean isOnFire() {
        if (this.element == Element.E.PYRO.getId()) {
            return false;
        }
        return super.isOnFire();
    }

    public boolean isAlliedTo(Entity entity) {
        if (super.isAlliedTo(entity)) {
            return true;
        } else if (entity instanceof AbstractHilichurl || entity instanceof Slime) {
            return this.getTeam() == null && entity.getTeam() == null;
        } else {
            return false;
        }
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.PILLAGER_AMBIENT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.PILLAGER_DEATH;
    }

    protected SoundEvent getHurtSound(DamageSource p_33306_) {
        return SoundEvents.PILLAGER_HURT;
    }

    public void setElement(int element) {
        this.element = element;
    }

    public int getElement() {
        return this.element;
    }

    public HilichurlArmPose getArmPose() {
        return HilichurlArmPose.NEUTRAL;
    }

    public static enum HilichurlArmPose {
        ATTACKING,
        SPELLCASTING,
        BOW_AND_ARROW,
        CROSSBOW_HOLD,
        CROSSBOW_CHARGE,
        NEUTRAL;
    }

    protected float getStandingEyeHeight(Pose p_34146_, EntityDimensions p_34147_) {
        return 1.62F;
    }
}