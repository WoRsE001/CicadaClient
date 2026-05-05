package cicada.client.hud.impl

import cicada.client.hud.HUD
import cicada.client.module.impl.combat.ModuleAttackAura
import cicada.client.utils.connection
import cicada.client.utils.math.Color4f
import cicada.client.utils.math.gazLarpit
import cicada.client.utils.mc
import cicada.client.utils.render.rect
import cicada.client.utils.render.sprite
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player

// SCWGxD regrets everything he did. 13.04.2026 15:09.
object HudTargetInfo : HUD(0f, 0f, 150f, 50f, "TargetInfo", false) {
    private val backgroundColor by color("Background color", Color4f(0f, 0f, 0f, 1f))
    private val rounded by int("Rounded", 5, 0..40)
    private val gaps by int ("Gaps", 5, 0..20)

    override fun render(graphics: GuiGraphicsExtractor) {
        val target = ModuleAttackAura.targetFinder.target ?: return

        graphics.rect(x, y, w, h, backgroundColor.toInt(), rounded.toFloat())
        graphics.drawHead(target, x + gaps, y + gaps, h - gaps * 2, h - gaps * 2, -1, rounded)

        //graphics.drawText(target.name.string, x + h, y + h / 2 - gaps / 2 - 8, -1)
        val hpText = "%.1f".format(target.health)
        //graphics.drawText(hpText, x + w - gaps - mc.font.width(hpText), y + h / 2 - gaps / 2 - 8, -1)

        graphics.rect(x + h, y + h / 2 + gaps / 2, w - h - gaps, h / 5, -1, rounded.toFloat())
        graphics.rect(x + h, y + h / 2 + gaps / 2,
            gazLarpit(target.health / target.maxHealth, 0f, w - h - gaps), h / 5, 0xFFFF0000.toInt(), rounded.toFloat())
    }

    private fun GuiGraphicsExtractor.drawHead(entity: LivingEntity, x: Float, y: Float, w: Float, h: Float, color: Int, round: Int) {
        if (entity !is Player) return
        val headTexture = (connection.getPlayerInfo(entity.uuid)?.skin ?: return).body.texturePath()
        sprite(8f, 8f, 8f, 8f, x, y, w, h, round.toFloat(), headTexture)
    }
}