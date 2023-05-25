package cn.origin.cube.utils.animations

class Easing1D (pos : Float) {
    private var lastPos : Float
    private var newPos : Float
    private val offset: Float
        get() = (newPos - lastPos)

    private val animationX: AnimationFlag
    private var startTime : Long

    init {
        lastPos = pos
        newPos = pos
        animationX = AnimationFlag(Easing.OUT_EXPO,600f).also {
            it.forceUpdate(pos,pos)
        }
        startTime = System.currentTimeMillis()
    }

    fun reset(){
        animationX.forceUpdate(0f,0f)
    }

    fun updatePos(pos : Float){
        lastPos = newPos
        newPos = pos
    }

    fun getUpdate() = animationX.getAndUpdate(offset + lastPos)
}