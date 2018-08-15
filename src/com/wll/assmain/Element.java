package com.wll.assmain;
/*
墙类:
     属性: 坐标,宽高,血量
     行为: 画

     有四种墙,可以定义一个父类将墙的共性属性放入,只需继承父类即可
 */
public abstract class Element {
    protected int x;    //x坐标
    protected int y;    //y坐标
    protected int width;   //墙的宽度
    protected int height;   //墙的高度

    //无参构造方法
    public Element() {
    }

//有参构造方法
    public Element(int x, int y) {
        this.x = x;
        this.y = y;
    }

    //绘制图画的方法
    public abstract void draw();

    //判断等级的方法
    public int getLevel(){
        return 0;
    }

}
