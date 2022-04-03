package net.slowpnir.movedammit.utils;

public class OrdMonoid {
    public static int mconcat(int ...items) {
        for (var i : items) {
            if (i != 0) return i;
        }
        return 0;
    }
}
