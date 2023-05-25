package cn.origin.cube.utils.animations

import net.minecraft.util.math.Vec2f

class Easing2D (pos : Vec2f,size : Float) {
    private var lastPos : Vec2f
    private var newPos : Vec2f
    private val offset: Vec2f
        get() = Vec2f(
            (newPos.x - lastPos.x),
            (newPos.y - lastPos.y)
        )
    private val animationX: AnimationFlag
    private val animationY: AnimationFlag
    private val animationSize : Easing1D
    private var startTime : Long

    init {
        lastPos = pos
        newPos = pos
        animationX = AnimationFlag(Easing.IN_OUT_EXPO,600f).also {
            it.forceUpdate(pos.x,pos.x)
        }
        animationY = AnimationFlag(Easing.IN_OUT_EXPO,600f).also {
            it.forceUpdate(pos.y,pos.y)
        }
        animationSize = Easing1D(size)
        startTime = System.currentTimeMillis()
    }

    fun reset(){
        animationX.forceUpdate(0f,0f)
        animationY.forceUpdate(0f,0f)
        animationSize.reset()
    }
    fun updatePos(pos : Vec2f){
        lastPos = newPos
        newPos = pos
    }
    fun updateSize(size : Float){
        animationSize.updatePos(size)
    }

    fun getUpdate() = Vec2f(
        animationX.getAndUpdate(offset.x + lastPos.x),
        animationY.getAndUpdate(offset.y + lastPos.y)
    )

    fun getUpdateSize() = animationSize.getUpdate()
}