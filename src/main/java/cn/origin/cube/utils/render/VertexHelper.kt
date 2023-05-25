package cn.origin.cube.utils.render


import com.sun.javafx.geom.Vec2d
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.util.math.Vec3d
import java.awt.Color

@Suppress("NOTHING_TO_INLINE")
object VertexHelper {
    val tessellator = Tessellator.getInstance()
    val buffer = tessellator.buffer

    inline fun begin(mode: Int) {
        buffer.begin(mode, DefaultVertexFormats.POSITION_COLOR)
    }


    inline fun put(pos: Vec3d, color: Color) {
        put(pos.x, pos.y, pos.z, color)
    }

    inline fun put(x: Double, y: Double, z: Double, color: Color) {
        buffer.pos(x, y, z).color(color.red, color.green, color.blue, color.alpha).endVertex()
    }

    inline fun put(pos: Vec2d, color: Color) {
        put(pos.x, pos.y, color)
    }

    inline fun put(x: Int, y: Int, color: Color) {
        buffer.pos(x.toDouble(), y.toDouble(), 0.0).color(color.red, color.green, color.blue, color.alpha).endVertex()
    }



    inline fun put(x: Double, y: Double, color: Color) {
        buffer.pos(x, y, 0.0).color(color.red, color.green, color.blue, color.alpha).endVertex()
    }

    inline fun end() {
        tessellator.draw()
    }
}