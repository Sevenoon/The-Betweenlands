package thebetweenlands.common.registries;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import thebetweenlands.common.TheBetweenlands;
import thebetweenlands.common.entity.mobs.EntityAngler;
import thebetweenlands.common.entity.mobs.EntityBlindCaveFish;
import thebetweenlands.common.entity.mobs.EntityBloodSnail;
import thebetweenlands.common.entity.mobs.EntityChiromaw;
import thebetweenlands.common.entity.mobs.EntityDragonFly;
import thebetweenlands.common.entity.mobs.EntityFrog;
import thebetweenlands.common.entity.mobs.EntityGiantToad;
import thebetweenlands.common.entity.mobs.EntityLurker;
import thebetweenlands.common.entity.mobs.EntityMireSnail;
import thebetweenlands.common.entity.mobs.EntityMireSnailEgg;
import thebetweenlands.common.entity.mobs.EntitySporeling;
import thebetweenlands.common.entity.mobs.EntitySwampHag;
import thebetweenlands.common.entity.projectiles.EntitySnailPoisonJet;

public class EntityRegistry {
    private static int id;

    private static void registerEntity(Class<? extends Entity> entityClass, String name, int trackingRange, int trackingFrequency, boolean velocityUpdates) {
        net.minecraftforge.fml.common.registry.EntityRegistry.registerModEntity(entityClass, name, id, TheBetweenlands.INSTANCE, trackingRange, trackingFrequency, velocityUpdates);
        id++;
    }

    private static void registerEntity(Class<? extends Entity> entityClass, String name) {
        net.minecraftforge.fml.common.registry.EntityRegistry.registerModEntity(entityClass, name, id, TheBetweenlands.INSTANCE, 64, 3, true);
        id++;
    }

    private static void registerEntity(Class<? extends EntityLiving> entityClass, String name, int eggBackgroundColor, int eggForegroundColor, int trackingRange, int trackingFrequency, boolean velocityUpdates) {
        registerEntity(entityClass, name, trackingRange, trackingFrequency, velocityUpdates);
        net.minecraftforge.fml.common.registry.EntityRegistry.registerEgg(entityClass, eggBackgroundColor, eggForegroundColor);
    }

    private static void registerEntity(Class<? extends EntityLiving> entityClass, String name, int eggBackgroundColor, int eggForegroundColor) {
        registerEntity(entityClass, name);
        net.minecraftforge.fml.common.registry.EntityRegistry.registerEgg(entityClass, eggBackgroundColor, eggForegroundColor);
    }

    public void preInit() {
        id = 0;
        registerEntity(EntityAngler.class, "angler", 0x243B0B, 0x00FFFF);
        registerEntity(EntitySwampHag.class, "swamp_hag", 0x0B3B0B, 0xDBA901);
        registerEntity(EntitySporeling.class, "sporeling", 0x696144, 0xFFFB00, 64, 1, true);
        registerEntity(EntityDragonFly.class, "dragonfly", 0x31B53C, 0x779E3C);
        registerEntity(EntityBloodSnail.class, "blood_snail", 0x8E9456, 0xB3261E);
        registerEntity(EntityMireSnail.class, "mire_snail", 0x8E9456, 0xF2FA96);
        registerEntity(EntityMireSnailEgg.class, "mire_snail_egg");
        registerEntity(EntitySnailPoisonJet.class, "snail_poison_jet");
        registerEntity(EntityLurker.class, "lurker", 0x283320, 0x827856);
        registerEntity(EntityGiantToad.class, "toad", 0x405C3B, 0x7ABA45);
        registerEntity(EntityBlindCaveFish.class, "blind_cave_fish", 0xD0D1C2, 0xECEDDF);
        registerEntity(EntityChiromaw.class, "chiromaw", 0x3F5A69, 0xA16A77);
        registerEntity(EntityFrog.class, "frog", 0x559653, 0xC72C2C, 64, 20, true);
    }
}