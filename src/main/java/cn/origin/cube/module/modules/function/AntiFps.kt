package cn.origin.cube.module.modules.function

import cn.origin.cube.module.Category
import cn.origin.cube.module.Module
import cn.origin.cube.module.ModuleInfo
import cn.origin.cube.settings.IntegerSetting
import java.math.BigInteger
import java.util.concurrent.CountDownLatch

/**
 * @author LovelyRainbow
 * @date 01/05/2023
 * @time 12:11
 */
@ModuleInfo(name="AntiFPS", descriptions = "AMDYES", category = Category.FUNCTION)
class AntiFps:Module() {
    private var fps:IntegerSetting=registerSetting("FPS",40,0,100)
    override fun onUpdate() {
        val gameFps=mc.fpsCounter
    if (gameFps>=fps.value) {
        kotlin.run disable@{
            while (true) {
                val latch = CountDownLatch(1)

                Thread {
                    val numThreads = 4
                    val numbersPerThread = 25000000
                    val latch = CountDownLatch(numThreads)

                    var sum = BigInteger.ZERO

                    for (i in 0 until numThreads) {
                        Thread {
                            var threadSum = BigInteger.ZERO
                            val start = i * numbersPerThread + 114514
                            val end = start + numbersPerThread - 1919810
                            for (j in start..end) {
                                threadSum = threadSum.add(BigInteger.valueOf(j.toLong()))
                            }
                            sum = sum.add(threadSum)
                            latch.countDown()
                        }.start()
                    }

                    try {
                        latch.await()
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }
                    latch.countDown()
                }.start()
                if (fps.value==gameFps)return@disable
                try {
                    latch.await()
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        }
    }
    }
}