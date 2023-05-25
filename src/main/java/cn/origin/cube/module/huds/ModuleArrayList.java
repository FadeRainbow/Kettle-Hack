package cn.origin.cube.module.huds;

import cn.origin.cube.Cube;
import cn.origin.cube.module.*;
import cn.origin.cube.module.modules.client.ClickGui;
import cn.origin.cube.settings.BooleanSetting;
import cn.origin.cube.settings.ModeSetting;
import cn.origin.cube.utils.render.ColorUtils;
import cn.origin.cube.utils.render.Render2DUtil;
import net.minecraft.client.Minecraft;

import java.awt.*;
import java.util.Comparator;
import java.util.stream.Collectors;

@HudModuleInfo(name = "ModuleArrayList", descriptions = "Show all enable module", category = Category.HUD, y = 100, x = 100)
public class ModuleArrayList extends HudModule {

    public ModeSetting<?> alignSetting = registerSetting("Align", alignMode.Left);
    public ModeSetting<?> sortSetting = registerSetting("Sort", sortMode.Top);
    public ModeSetting<?> colorSetting = registerSetting("Color", colorMode.Fade);
    public int count = 0;

    @Override
    public void onRender2D() {
        count = 0;
        Cube.moduleManager.getModuleList().stream()
                .filter(AbstractModule::isEnabled)
                .filter(module -> module.visible.getValue())
                .sorted(Comparator.comparing(module -> Cube.fontManager.CustomFont.getStringWidth(module.getFullHud())
                        * (sortSetting.getValue().equals(sortMode.Bottom) ? 1 : -1)))
                .forEach(module -> {
                    int index = 0;
                    float modWidth = Cube.fontManager.CustomFont.getStringWidth(module.getFullHud());
                    String modText = module.getFullHud();
                    Color c;
                    if(colorSetting.getValue().equals(colorMode.Fade)){
                        c = ClickGui.getCurrentColor();
                    }
                    else {
                        c = ClickGui.getCurrentColor();
                    }
                    if(colorSetting.getValue().equals(colorMode.Static)) {
                        if (alignMode.Right.equals(alignSetting.getValue())) {
                            Cube.fontManager.CustomFont.drawStringWithShadow(modText,
                                    (int) (this.x - 2 - modWidth + this.width),
                                    this.y + (10 * count),
                                    c.getRGB());
                        } else {
                            Cube.fontManager.CustomFont.drawStringWithShadow(modText,
                                    this.x - 2,
                                    this.y + (10 * count),
                                    c.getRGB());
                        }
                    }else{
                        if (alignMode.Right.equals(alignSetting.getValue())) {
                            Cube.fontManager.drawFadeFont(modText,
                                    (int) (this.x - 2 - modWidth + this.width),
                                    this.y + (10 * count), ColorUtils.rainbow(500),Color.WHITE);
                        } else {
                            Cube.fontManager.drawFadeFont(modText,
                                    this.x - 2,
                                    this.y + (10 * count),ColorUtils.rainbow(500),Color.WHITE);
                        }
                    }
                    count++;
                });
        width = Cube.moduleManager.getModuleList().stream()
                .filter(AbstractModule::isEnabled)
                .noneMatch(module -> module.visible.getValue()) ? 20 :
                Cube.fontManager.CustomFont.getStringWidth(Cube.moduleManager.getModuleList()
                        .stream().filter(AbstractModule::isEnabled)
                        .filter(module -> module.visible.getValue())
                        .sorted(Comparator.comparing(module -> Cube.fontManager.CustomFont.getStringWidth(module.getFullHud()) * (-1)))
                        .collect(Collectors.toList()).get(0).getFullHud());
        height = ((Cube.fontManager.CustomFont.getHeight() + 1) *
                (int) Cube.moduleManager.getModuleList().stream()
                        .filter(AbstractModule::isEnabled).count());
    }





    enum alignMode {
        Left,
        Right
    }

    enum colorMode {
      Fade,Static
    }

    enum sortMode {
        Top,
        Bottom
    }
}
