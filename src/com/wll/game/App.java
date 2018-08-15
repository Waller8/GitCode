package com.wll.game;

import com.wll.interf.Config;

public class App {
    public static void main(String[] args) {
        //创建窗体,小括号里可以传入Config接口中的属性
        GameWindow gw = new GameWindow(Config.TITLE,Config.WIDTH,Config.HEIGHT,Config.FPS);
        //启动窗体
        gw.start();

    }
}
