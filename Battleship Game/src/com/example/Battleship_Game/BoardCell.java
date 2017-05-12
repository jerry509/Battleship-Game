package com.example.Battleship_Game;

import android.graphics.Point;

/**
 * Created by Gerardo on 6/8/2015.
 */
public class BoardCell {

  private int height, width;
  private Point topLeft, bottomRight;
  private Boolean has_ship, hit, miss, waiting;
  private Point viewOrigin;
  private int cellHeight, cellWidth;

  public BoardCell(){
    height = 0;
    width = 0;
    miss = false;
    hit = false;
    has_ship = false;
    waiting = false;
    topLeft = new Point(0,0);
    bottomRight = new Point(0,0);
    cellHeight = 0;
    cellWidth = 0;
  }

  public BoardCell( int _height,int _width, Point _topLeft, Point _bottomRight){
    height = _height;
    width = _width;
    topLeft = _topLeft;
    bottomRight = _bottomRight;
    height = 0;
    width = 0;
    miss = false;
    hit = false;
    has_ship = false;
    waiting = false;
  }

  public boolean hasShip(){
    return has_ship;
  }

  public boolean hasMiss(){
    return miss;
  }

  public boolean hasHit(){
    return hit;
  }
  public boolean isWaiting(){
    return waiting;
  }

  public void addShip(){
    has_ship = true;
  }

  public void setMiss(){
    miss = true;
    waiting = false;
    hit = false;
  }

  public void setHit(){
    hit = true;
    waiting = false;
  }

  public Point getBottomLeft(){
    return new Point( topLeft.x, topLeft.y + (bottomRight.y - topLeft.y));
  }

  public void setWaiting( boolean _waiting ){
    waiting = _waiting;
  }

  /**
   * @param _cellHeight
   */
  public void setCellHeight( int _cellHeight ) {
    cellHeight = _cellHeight;
  }

  /**
   * @return cellHeight
   */
  public int getCellHeight() {
    return cellHeight;
  }

  /**
   * @param _cellWidth
   */
  public void setCellWidth( int _cellWidth ) {
    cellWidth = _cellWidth;
  }

  /**
   * @return cellWidth
   */
  public int getCellWidth() {
    return cellWidth;
  }


  /**
   * @param _origin
   */
  public void setViewOrigin( Point _origin ) {
    viewOrigin = _origin;
  }

  /**
   * @return viewOrigin
   */
  public Point getViewOrigin() {
    return viewOrigin;
  }


}
