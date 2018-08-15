package com.wll.assmain;

import com.wll.interf.Direction;

public class Tank extends Element {

    public Direction direction = Direction.UP;//坦克方向

    public Tank() {
    }

    public Tank(int x, int y) {
        super(x, y);
    }

    @Override
    public void draw() {

    }
}
