package net.slowpnir.movedammit.utils;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.LiteralText;

public class Log {
    public static final void info(PlayerEntity player, String str) {
        player.sendMessage(new LiteralText(str), false);
    }
}
