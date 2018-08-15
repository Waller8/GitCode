package com.wll.game;

import com.wll.assmain.*;
import com.wll.interf.*;
import org.itheima.game.Window;
import org.lwjgl.input.Keyboard;

import java.util.Collections;
import java.util.Comparator;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

public class GameWindow extends Window {

    public GameWindow(String title, int width, int height, int fps) {
        super(title, width, height, fps);
    }

    CopyOnWriteArrayList<Element> list = new CopyOnWriteArrayList<>(); //定义数组存储元素,这个集合可以处理并发修改异常
    MyTank mt;   //定义我方坦克
    EnemyTank et1 ;  //定义敌方坦克;
    EnemyTank et2 ;  //定义敌方坦克;

    boolean flag = true;    //定义旗帜变量

    @Override
    protected void onCreate() { //这个方法只能执行一次
        //循环添加土墙到集合中
        for (int i = 0; i < Config.WIDTH / 64 - 1; i++) {
            if (i == 3) {
                continue;
            }
            BrickWall bk = new BrickWall(i * 64, 64);
            list.add(bk);
        }
        //循环添加水墙到集合中
        for (int i = 1; i < Config.WIDTH / 64 ; i++) {
            WaterWall ww = new WaterWall(i * 64, 64 * 3);
            list.add(ww);
        }
        //循环添加铁墙到集合中
        for (int i = 0; i < Config.WIDTH / 64 - 1; i++) {
            IronWall ik = new IronWall(i * 64, 64 * 5);
            list.add(ik);
        }

        //循环添加草墙到集合中
        for (int i = 1; i < Config.WIDTH / 64 ; i++) {
            GrassWall gk = new GrassWall(i * 64, 64 * 7);
            list.add(gk);
        }
        //添加坦克
        mt = new MyTank(Config.WIDTH / 2 - 32, Config.HEIGHT - 64);
        list.add(mt);    //将坦克存入集合

        et1 = new EnemyTank(0,0);
        et2 = new EnemyTank(Config.WIDTH - 64,0);
        list.add(et1);
        list.add(et2);


        /*
        实现坦克在草丛的隐藏: 越晚绘制的图片,遍历时出现的越晚,
        只需要将能够遮挡坦克的图片比坦克晚绘制即可,在Element创建一个方法表示等级的高低,使用 Collections.sort()方法,进行排序
        等级高的晚绘制.
         */
        Collections.sort(list, new Comparator<Element>() {
            @Override
            public int compare(Element o1, Element o2) {
                /*
                前减后   升序
                后减前   降序
                 */
                return o1.getLevel() - o2.getLevel();
            }
        });

    }

    @Override
    //这是一个监控鼠标的方法
    protected void onMouseEvent(int key, int x, int y) {
    }


    @Override
    //这是监控键盘的方法   按键，{@code Keyboard.key_xx}
    protected void onKeyEvent(int key) {
        //使用方向键控制坦克移动
        if (key == Keyboard.KEY_UP) {
            mt.move(Direction.UP);
        } else if (key == Keyboard.KEY_DOWN) {
            mt.move(Direction.DOWN);
        } else if (key == Keyboard.KEY_LEFT) {
            mt.move(Direction.LEFT);
        } else if (key == Keyboard.KEY_RIGHT) {
            mt.move(Direction.RIGHT);
        } else if (key == Keyboard.KEY_SPACE) {
            //创建子弹将子弹,添加到集合里
            Bullet bullet = mt.shoot();
            //如果子弹不为空,就添加到集合中
            if (bullet != null) {
                list.add(bullet);
            }
        }

    }

    /*
   实时刷新,动态监控
    */
    @Override
    protected void onDisplayUpdate() {
        //循环遍历集合,调用绘制的方法.
        for (Element e : list) {
            e.draw();

            if (e instanceof EnemyTank){
                EnemyTank et = (EnemyTank)e;
                et.move();

                //产生一个随机数,只要当随机数等于30,就发射子弹,这是给敌方子弹设置时间间隔
                int num = new Random().nextInt(50);
                if (num == 30){
                    Bullet shoot = et.shoot();
                    if (shoot != null){
                        list.add(shoot);
                    }
                }
            }
            //判断元素是不是子弹
            if (e instanceof Bullet) {
                Bullet b = (Bullet) e;   //向下转型,将元素转为Bullet类
                b.move();   //调用子弹移动的方法
            }

        }
        //集合中元素移除
        for (Element e : list) {
            if (e instanceof Destroyable) {
                Destroyable b = (Destroyable) e;   //向下转型,将元素转为Bullet类
                boolean res = ((Destroyable) e).isDestroy();
                if (res) {
                    list.remove(e);
                }
            }
        }

        //在集合中遍历出坦克与铁墙,拿到后判断两个是否碰撞
        for (Element e1 : list) {
            if (e1 instanceof Moveable) {
                for (Element e2 : list) {
                    if (e2 instanceof Blockable) {
                        //当e1 与 e2 是同一个坦克,肯定碰撞,因此要使两个不是同一个坦克
                        if (e1 == e2){
                            continue;//当两个坦克相同时,不产生碰撞
                        }
                        Moveable moveable = (Moveable)e1; //将e1转型为坦克类型,调用坦克的校验碰撞方法.
                        Blockable bk = (Blockable)e2;
                        boolean res = moveable.cheakHit(bk); //调用校验方法
                        if (res){
                            System.out.println("碰撞上了");
                            break;
                        }
                    }
                }
            }
        }
        //子弹攻击墙
        for (Element e1 : list) {
            if (e1 instanceof Bullet) {
                for (Element e2 : list) {
                    if (e2 instanceof Hitable) {
                        Bullet bullet = (Bullet)e1; //将e1转型为子弹类型,调用子弹的校验碰撞方法.
                        Hitable hit = (Hitable)e2;
                        boolean res = bullet.cheakAttack(hit); //调用校验方法
                        if (res){
                            System.out.println("碰撞上了");
                            list.remove(e1);
                            //墙被攻击显示爆炸效果,
                           Blast blast = hit.showBlast();
                            list.add(blast);
                            break;
                        }
                    }
                }
            }
        }


        System.out.println(list.size());
    }
}
