package com.tzaranthony.genshingatcha.core.util.events;

import com.tzaranthony.genshingatcha.GenshinGacha;
import com.tzaranthony.genshingatcha.core.items.util.IAttackReachExtending;
import com.tzaranthony.genshingatcha.core.networks.ExtendAttackRangeC2SPacket;
import com.tzaranthony.genshingatcha.core.util.tags.GGItemTags;
import com.tzaranthony.genshingatcha.registries.GGPackets;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = GenshinGacha.MOD_ID, value = Dist.CLIENT)
public class GGClientEvents {
    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void onClickInput(InputEvent.ClickInputEvent event) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;

        ItemStack holding = player.getMainHandItem();
        if (!player.isSpectator() && (holding.is(GGItemTags.CLAYMORES) || holding.is(GGItemTags.SPEARS))) {
            Vec3 vec3 = player.getEyePosition();
            Vec3 vec31 = player.getViewVector(1.0F);
            Vec3 vec32 = vec3.add(vec31.x * 100.0D, vec31.y * 100.0D, vec31.z * 100.0D);
            IAttackReachExtending reachItem = (IAttackReachExtending) holding.getItem();
            double d0 = reachItem.getAttackReach();
            double d1 = d0 * d0;
            AABB aabb = player.getBoundingBox();
            if (holding.is(GGItemTags.CLAYMORES)) {
                aabb.expandTowards(vec31.scale(d0 - 1)).inflate(2.0D, 1.5D, 2.0D);
            } else {
                aabb.expandTowards(vec31.scale(d0)).inflate(1.0D, 1.0D, 1.0D);
            }
            EntityHitResult entityhitresult = ProjectileUtil.getEntityHitResult(player, vec3, vec32, aabb, (entity) -> {
                return !entity.isSpectator() && entity.isPickable();
            }, d1);

            if (entityhitresult != null) {
                Entity entity1 = entityhitresult.getEntity();
                Vec3 vec33 = entityhitresult.getLocation();
                double d2 = vec3.distanceToSqr(vec33);
                if (d2 < d1 || mc.hitResult == null) {
                    mc.hitResult = entityhitresult;
                    if (entity1 instanceof LivingEntity || entity1 instanceof ItemFrame) {
                        mc.crosshairPickEntity = entity1;
                    }
                }
            }
        }
    }
}