package com.t2pellet.haybale.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import org.jetbrains.annotations.Nullable;

public class TestParticle extends TextureSheetParticle {

    protected TestParticle(ClientLevel $$0, double $$1, double $$2, double $$3) {
        super($$0, $$1, $$2, $$3);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    public static class Factory implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public Factory(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Nullable
        @Override
        public Particle createParticle(SimpleParticleType simpleParticleType, ClientLevel clientLevel, double v, double v1, double v2, double v3, double v4, double v5) {
            TestParticle test = new TestParticle(clientLevel, v, v1, v2);
            test.setColor(1.0F, 1.0F, 1.0F);
            test.pickSprite(spriteSet);
            return test;
        }
    }
}
