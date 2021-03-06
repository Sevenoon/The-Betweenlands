package thebetweenlands.client.render.render.entity.renderfactory;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thebetweenlands.client.render.render.entity.render.RenderDragonFly;
import thebetweenlands.common.entity.mobs.EntityDragonFly;

@SideOnly(Side.CLIENT)
public class RenderFactoryDragonFly implements IRenderFactory<EntityDragonFly> {
    @Override
    public Render<? super EntityDragonFly> createRenderFor(RenderManager manager) {
        return new RenderDragonFly(manager);
    }
}