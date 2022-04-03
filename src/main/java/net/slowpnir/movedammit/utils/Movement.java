package net.slowpnir.movedammit.utils;

import net.minecraft.entity.player.PlayerEntity;
import oshi.util.tuples.Pair;

import java.util.*;
import java.util.stream.Collectors;

public abstract class Movement<Point, Dir> {
    public abstract Point move(Point p, Dir d);
    public abstract int compareByDir(Dir dir, Point o1, Point o2);
    public abstract void moveAtTo(Point p, Dir d);
    public abstract ABlock<Dir> get(Point p);

    public static class Ship<Point> {
        public static class Good<Point> extends Ship<Point> {
            public Set<Point> Volume;
            public Good(Set<Point> collect) { Volume = collect; }
        }

        public static class TooBig<Point> extends Ship<Point> {
            public Set<Point> Exceeding;
            public TooBig(Set<Point> collect) { Exceeding = collect; }
        }

        public static class Blocked<Point> extends Ship<Point> {
            public Set<Point> Roadblocks;
            public Blocked(Set<Point> collect) { Roadblocks = collect; }
        }
    }

    public Ship<Point> collect(PlayerEntity player, Point origin, Dir facing, Dir dirToMove, int capacity) {
        var ship  = new HashSet<Point>();
        var seen  = new HashSet<Point>();
        var queue = new ArrayDeque<Pair<Point, Dir>>();
        var cap   = new IntRef(capacity);

        queue.add(new Pair<>(move(origin, facing), facing));  // add block originator faces
        seen.add(origin);                                     // never add originator

        while (!queue.isEmpty() && cap.positive()) {
            var pair = queue.poll();
            var point = pair.getA();
            var incoming = pair.getB();

            if (!seen.contains(point)) {
                var block = get(point);
                if (block instanceof ABlock.Frame<Dir> frame) {
                    register(cap, ship, point);
                    Log.info(player, "Frame: %s at %s".formatted(frame.Dirs, point));
                    proceed(queue, point, frame.Dirs);
                }
                if (block instanceof ABlock.Motor<Dir> motor) {
                    if (!point.equals(origin)) {
                        Dir dir = motor.Dir;
                        if (dir.equals(incoming)) {
                            register(cap, ship, point);
                            Log.info(player, "Motor: %s at %s".formatted(motor.Dir, point));
                            proceed(queue, point, List.of(dir));
                        }
                    }
                }
                if (block instanceof ABlock.Other<Dir> other) {
                    if (!other.Inert) {
//                        Log.info(player, "Other: %s at %s".formatted(motor.Dir, point));
                        register(cap, ship, point);
                    }
                }
            }
            seen.add(point);
        }

        if (!queue.isEmpty()) {
            Set<Point> remaining = queue.stream().map(Pair::getA).collect(Collectors.toSet());
            return new Ship.TooBig<>(remaining);
        }

        var blocks = collectRoadblocks(ship, dirToMove);

        if (!blocks.isEmpty()) {
            return new Ship.Blocked<>(blocks);
        }

        return new Ship.Good<>(ship);
    }

    public Set<Point> collectRoadblocks(Set<Point> ship, Dir dirToMove) {
        return ship.stream()
            .map(point -> move(point, dirToMove))
            .filter(point -> {
                if (ship.contains(point)) return false;
                var block = get(point);
                if (block instanceof ABlock.Other<Dir>) {
                    return !((ABlock.Other<Dir>) block).Fragile;
                }
                return true;
            })
            .collect(Collectors.toSet());
    }

    public void performMovement(Set<Point> ship, Dir dir) {
        ship.stream()
            .sorted(new Comparator<Point>() {
                @Override
                public int compare(Point o1, Point o2) {
                    return compareByDir(dir, o1, o2);
                }
            })
            .forEach(point -> {
                moveAtTo(point, dir);
            });
        ;
    }

    private void proceed(Queue<Pair<Point, Dir>> queue, Point point, List<Dir> dirs) {
        for (var dir : dirs) {
            var point1 = move(point, dir);
            queue.add(new Pair<>(point1, dir));
        }
    }

    private void register(IntRef capacity, Set<Point> toMove, Point point) {
        toMove.add(point);
        capacity.decrement();
    }

    private static class IntRef {
        int i;
        public         IntRef(int i) { this.i = i; }
        public boolean positive()    { return i > 0; }
        public void    decrement()   { i -= 1; }
    }
}
