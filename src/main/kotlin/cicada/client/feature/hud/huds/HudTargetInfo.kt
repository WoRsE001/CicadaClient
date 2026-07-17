package cicada.client.feature.hud.huds

import cicada.client.feature.hud.HUD
import cicada.client.feature.module.modules.combat.attackaura.ModuleAttackAura
import cicada.client.font.Fonts
import cicada.client.render.engine.text
import cicada.client.render.engine.width
import cicada.client.render.rect
import cicada.client.render.sprite
import cicada.client.utils.client.connection
import cicada.client.utils.math.Color4f
import cicada.client.utils.math.gazLarpit
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player

// SCWGxD regrets everything he did. 13.04.2026 15:09.
object HudTargetInfo : HUD(0f, 0f, 150f, 50f, "TargetInfo", true) {
    private val backgroundColor by color("Background color", Color4f(0f, 0f, 0f, 1f))
    private val rounded by int("Rounded", 5, 0..40)
    private val gaps by int ("Gaps", 5, 0..20)

    val font = Fonts["jetbrains-mono"]!!

    override fun render(graphics: GuiGraphicsExtractor) {
        val target = ModuleAttackAura.target ?: return

        graphics.rect(x, y, w, h, backgroundColor.toInt(), r0 = rounded.toFloat())
        graphics.drawHead(target, x + gaps, y + gaps, h - gaps * 2, h - gaps * 2, -1, rounded)

        graphics.text(font, target.name.string, x + h, y + h / 2 - gaps / 2 - 8, 9f)
        val hpText = "%.1f".format(target.health)
        graphics.text(font, hpText, x + w - gaps - font.width(hpText, 9f), y + h / 2 - gaps / 2 - 8, 9f)

        graphics.rect(x + h, y + h / 2 + gaps / 2, w - h - gaps, h / 5, -1, r0 = rounded.toFloat())
        graphics.rect(x + h, y + h / 2 + gaps / 2,
            gazLarpit(target.health / target.maxHealth, 0f, w - h - gaps), h / 5, 0xFFFF0000.toInt(), r0 = rounded.toFloat())
    }

    private fun GuiGraphicsExtractor.drawHead(entity: LivingEntity, x: Float, y: Float, w: Float, h: Float, color: Int, round: Int) {
        if (entity !is Player) return
        val headTexture = (connection.getPlayerInfo(entity.uuid)?.skin ?: return).body.texturePath()
        sprite(headTexture, 8f, 8f, 8f, 8f, x, y, w, h, r0 = round.toFloat())
    }
}