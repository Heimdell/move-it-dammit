package net.slowpnir.movedammit.utils;

import java.util.List;

public class ABlock<Dir> {
    public static class Frame<Dir> extends ABlock<Dir> {
        public List<Dir> Dirs;
        public Frame(List<Dir> dirs) {
            Dirs = dirs;
        }
    }
    public static class Motor<Dir> extends ABlock<Dir> {
        public Dir Dir;
        public Motor(Dir dir) {
            Dir = dir;
        }
    }
    public static class Other<Dir> extends ABlock<Dir> {
        public final boolean Inert;
        public final boolean Fragile;
        public Other(boolean inert, boolean fragile) {
            Inert = inert;
            Fragile = fragile;
        }
    }
}
