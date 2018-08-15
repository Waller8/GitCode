package com.wll.interf;

public interface Moveable {
    //具有移动功能的接口,只要具备此接口,都可以校验碰撞
    public boolean cheakHit(Blockable block);
}
