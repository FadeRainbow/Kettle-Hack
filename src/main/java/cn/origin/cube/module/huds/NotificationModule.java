package cn.origin.cube.module.huds;


import cn.origin.cube.Cube;
import cn.origin.cube.module.Category;
import cn.origin.cube.module.Module;
import cn.origin.cube.module.ModuleInfo;
import cn.origin.cube.notification.NotificationManager;
import cn.origin.cube.settings.FloatSetting;

@ModuleInfo(name = "Notifications", category = Category.CLIENT, descriptions = "Show notification")
public class NotificationModule extends Module {

    public FloatSetting speed = registerSetting("Speed", 0.8f, 0.1f, 1.5f);


    public static NotificationModule INSTANCE;

    public NotificationModule() {
        INSTANCE = this;
    }

    @Override
    public void onRender2D() {
        NotificationManager.INSTANCE.draw();
    }
}
