package com.tzaranthony.genshingatcha.client.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class MovingParticle extends TextureSheetParticle {
    private final double xStart;
    private final double yStart;
    private final double zStart;

    protected MovingParticle(ClientLevel level, double x, double y, double z, double d0, double d1, double d2) {
        super(level, x, y, z);
        this.xd = d0 * 2.0D;
        this.yd = d1 * 2.0D;
        this.zd = d2 * 2.0D;
        this.x = x;
        this.y = y;
        this.z = z;
        this.xStart = this.x;
        this.yStart = this.y;
        this.zStart = this.z;
        this.lifetime = (int)(Math.random() * 10.0D) + 10;
    }

    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    public void move(double p_107560_, double p_107561_, double p_107562_) {
        this.setBoundingBox(this.getBoundingBox().move(p_107560_, p_107561_, p_107562_));
        this.setLocationFromBoundingbox();
    }

    public float getQuadSize(float sizeMod) {
        float f = ((float)this.age + sizeMod) / (float)this.lifetime;
        return this.quadSize * (1.0F - f * f * 0.5F);
    }

    public int getLightColor(float colorMod) {
        int i = super.getLightColor(colorMod);
        float f = (float)this.age / (float)this.lifetime;
        f *= f;
        f *= f;
        int j = i & 255;
        int k = i >> 16 & 255;
        k += (int)(f * 15.0F * 16.0F);
        if (k > 240) {
            k = 240;
        }

        return j | k << 16;
    }

    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.age++ >= this.lifetime) {
            this.remove();
        } else {
            float f = (float)this.age / (float)this.lifetime;
            float f1 = -f + f * f * 2.0F;
            float f2 = 1.0F - f1;
            this.x = this.xStart + this.xd * (double)f2;
            this.y = this.yStart + this.yd * (double)f2 + (double)(1.0F - f);
            this.z = this.zStart + this.zd * (double)f2;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprite;

        public Provider(SpriteSet spriteSet) {
            this.sprite = spriteSet;
        }

        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double d0, double d1, double d2) {
            MovingParticle particle = new MovingParticle(level, x, y, z, d0, d1, d2);
            particle.pickSprite(this.sprite);
//            particle.scale(4.0F);
            return particle;
        }
    }
}