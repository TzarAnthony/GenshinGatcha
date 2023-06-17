package com.tzaranthony.genshingatcha.core.entities.elements.mobs;

import com.tzaranthony.genshingatcha.core.entities.elements.projectiles.AbstractArrowLikeElementalProjectile;
import com.tzaranthony.genshingatcha.core.entities.mobs.slimes.*;
import com.tzaranthony.genshingatcha.core.util.Element;
import com.tzaranthony.genshingatcha.registries.GGEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.Level;

public class SlimeToss extends AbstractArrowLikeElementalProjectile implements ItemSupplier {
    public SlimeToss(EntityType<? extends SlimeToss> entityType, Level level) {
        super(entityType, level);
    }

    public SlimeToss(LivingEntity owner, int elementId, Level level) {
        super(GGEntities.SLIME_TOSS.get(), owner, elementId, level);
        this.setSoundEvent(SoundEvents.SLIME_SQUISH);
    }

    protected void doPostHitEffects(BlockPos pos, Direction dir) {
        if (!this.level.isClientSide()) {
            AbstractElementalSlime slime = this.createNewSlimeFromElement();
            BlockPos nPos = pos.relative(dir);
            slime.moveTo(nPos.getX(), nPos.getY(), nPos.getZ());
            slime.finalizeSpawn((ServerLevel) this.level, this.level.getCurrentDifficultyAt(nPos), MobSpawnType.TRIGGERED, null, null);
            this.level.addFreshEntity(slime);
        }
    }

    private AbstractElementalSlime createNewSlimeFromElement() {
        switch (this.getElement()) {
            case 0:
                return new CryoSlime(GGEntities.CRYO_SLIME.get(), this.level);
            case 1:
                return new PyroSlime(GGEntities.PYRO_SLIME.get(), this.level);
            case 2:
                return new ElectroSlime(GGEntities.ELECTRO_SLIME.get(), this.level);
            case 3:
                return new GeoSlime(GGEntities.GEO_SLIME.get(), this.level);
            case 4:
                return new HydroSlime(GGEntities.HYDRO_SLIME.get(), this.level);
            case 5:
                return new DendroSlime(GGEntities.DENDRO_SLIME.get(), this.level);
            default:
                return new AnemoSlime(GGEntities.ANEMO_SLIME.get(), this.level);
        }
    }

    public ItemStack getItem() {
        return PotionUtils.setPotion(new ItemStack(Items.LINGERING_POTION), Element.ElementGetter.get(this.getElement()).getPotion());
    }
}