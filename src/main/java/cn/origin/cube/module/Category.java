package cn.origin.cube.module;

import cn.origin.cube.utils.IconFontKt;

import java.awt.*;

public enum Category {
    COMBAT("Combat", IconFontKt.TARGET, false,new Color(255, 106, 90)),
    MOVEMENT("Movement", IconFontKt.METER, false,new Color(144, 255, 99)),
    VISUAL("Visual", IconFontKt.EYE, false,new Color(102, 160, 227)),
    WORLD("World", IconFontKt.EARTH, false,new Color(255, 163, 0)),
    FUNCTION("Function", IconFontKt.COGS, false,new Color(161, 86, 255)),
    CLIENT("Client", IconFontKt.EQUALIZER, false,new Color(255, 211, 120)),
    HUD("Hud", IconFontKt.PENCLI, true,new Color(245, 188, 248, 232));

    private final String name;
    private final String icon;
    public final boolean isHud;
    public Color color;

    Category(String name, String icon, boolean isHud,Color color) {
        this.name = name;
        this.icon = icon;
        this.isHud = isHud;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public String getIcon() {
        return icon;
    }
    public Color color(){
        return color;
    }
}
