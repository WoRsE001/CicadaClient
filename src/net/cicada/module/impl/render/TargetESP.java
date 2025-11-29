package net.cicada.module.impl.render;

import net.cicada.event.api.Event;
import net.cicada.event.impl.Render3DEvent;
import net.cicada.module.api.Category;
import net.cicada.module.api.Module;
import net.cicada.module.api.ModuleInfo;
import net.cicada.module.api.ModuleManager;
import net.cicada.module.setting.impl.ListSetting;
import net.cicada.utility.Player.PlayerUtil;
import net.cicada.utility.Render.RenderUtil;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;

import java.util.List;

import static net.minecraft.client.renderer.vertex.DefaultVertexFormats.POSITION_TEX_COLOR;

@ModuleInfo(name = "TargetESP", category = Category.Render)
public class TargetESP extends Module {
    ListSetting mode = new ListSetting("Mode", "Image", List.of("Circle", "Image"), () -> true, this);
    ListSetting image = new ListSetting("Image", "Expensive", List.of("Expensive", "SUS"), () -> this.mode.is("Image"), this);

    EntityLivingBase target;
    float angle = 0;

    @Override
    public void listen(Event event) {
        if (event instanceof Render3DEvent e && e.getPriority() == Event.Priority.High) {
            this.target = ModuleManager.ATTACK_AURA.target;
            if (target != null) {
                if (this.mode.is("Image")) {
                    Vec3 targetRenderPos = PlayerUtil.getSmoothPos(target);
                    double funkAngle = (Math.sin(Math.toRadians(this.angle)) * 2 + Math.toRadians(this.angle)) * 180;
                    this.angle += 1;

                    RenderUtil.start2D();
                    GlStateManager.enableTexture2D();
                    mc.getTextureManager().bindTexture(Images.valueOf(this.image.getValue().toUpperCase()).get());

                    double renderPosX = mc.getRenderManager().viewerPosX;
                    double renderPosY = mc.getRenderManager().viewerPosY;
                    double renderPosZ = mc.getRenderManager().viewerPosZ;

                    GL11.glPushMatrix();

                    GL11.glTranslated(targetRenderPos.xCoord - renderPosX, targetRenderPos.yCoord - renderPosY + this.target.height / 2, targetRenderPos.zCoord - renderPosZ);
                    GL11.glRotatef(-mc.getRenderManager().playerViewY, 0.0F, 1.0F, 0.0F);
                    GL11.glRotatef(mc.getRenderManager().playerViewX, 1.0F, 0.0F, 0.0F);
                    GL11.glRotatef((float) funkAngle, 0.0F, 0.0F, 1.0F);

                    Tessellator tessellator = Tessellator.getInstance();
                    WorldRenderer worldrenderer = tessellator.getWorldRenderer();
                    worldrenderer.begin(GL11.GL_QUADS, POSITION_TEX_COLOR);

                    worldrenderer.pos(-1 / 2F, -1 / 2F, 0.0D).tex(0.0D, 1.0D).color(255, 255, 255, 255).endVertex();
                    worldrenderer.pos(1 / 2F, -1 / 2F, 0.0D).tex(1.0D, 1.0D).color(255, 255, 255, 255).endVertex();
                    worldrenderer.pos(1 / 2F, 1 / 2F, 0.0D).tex(1.0D, 0.0D).color(255, 255, 255, 255).endVertex();
                    worldrenderer.pos(-1 / 2F, 1 / 2F, 0.0D).tex(0.0D, 0.0D).color(255, 255, 255, 255).endVertex();

                    tessellator.draw();
                    GL11.glPopMatrix();
                    GlStateManager.disableTexture2D();
                    RenderUtil.stop2D();
                }
            }
        }
    }

    private enum Images {
        EXPENSIVE("targetESP"),
        SUS("targetESP1");

        final String image;

        Images(String image) {
            this.image = image;
        }

        ResourceLocation get() {
            return new ResourceLocation("cicada/images/" + image + ".png");
        }
    }
}