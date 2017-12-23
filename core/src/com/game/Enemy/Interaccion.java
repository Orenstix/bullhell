/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.game.Enemy;

import java.awt.Point;
import java.awt.geom.Point2D;

/**
 *
 * @author 22b05
 */
public interface Interaccion {
    public Point2D.Double getPos();
    public int getSprite();
    public int getSize();
    public int getHealth();
    public int getStatus();
    public boolean life();
    public void update();
    public int maxHealth();
    public boolean getSide();

    public void setSpeedY(int i);

    public void setSpeedX(int i);
    public void damage(int x);

    public void shoot();

    public void stopShoot();

    public void limitSpeed();
    public int getPoints();
    public void regularSpeed();
}
