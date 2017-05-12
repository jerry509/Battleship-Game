package com.example.Battleship_Game;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

/**
 * Created by Gerardo on 6/8/2015.
 */
public class GameView extends View {

  // Globals
  public double board_height, board_width;
  int top_left_x, top_left_y, col_width;
  Paint paint;

  String[] letters = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J" };
  String[] numbers = { " 1", " 2", " 3", " 4", " 5", " 6", " 7", " 8", " 9", "10" };

  public GameView(Context context, AttributeSet attrs) {
    super(context, attrs);


    DisplayMetrics dm  = context.getResources().getDisplayMetrics();
    int width = dm.widthPixels;
    int height = dm.heightPixels;
    float dens = dm.density;

    board_width = (double)width / (double)dens;
    board_height = (double)height / (double)dens;

    board_height = board_height > board_width ? board_height : board_width;
    board_width = board_height;

    top_left_x = 0;
    top_left_y = 0;
    col_width = (int) board_width / 11;
    paint = new Paint();

    for(int row=0;row<11; row++){
      for(int col=0;col<11; col++) {

        Game.board[row][col] = new BoardCell(col_width, col_width,
            new Point( top_left_x + (col_width * col),
                top_left_y + (col_width * row) ), // TopLeft
            new Point( top_left_x + (col_width * col) + col_width,
                top_left_y + (col_width * row) + col_width ) // TopBottom
        );
      }
    }



  }

  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);

    paint.setStyle(Paint.Style.FILL);
    paint.setColor(Color.BLUE);
    canvas.drawPaint(paint);

    paint.setColor(Color.WHITE);
    paint.setStrokeWidth(2);

    for (int col = 0; col <= 11; col++) {
      canvas.drawLine(top_left_x + (col_width * col), top_left_y,  // Vertical Lines
          top_left_x + (col_width * col), (top_left_y + (float)board_height),
          paint);
      canvas.drawLine(top_left_x, top_left_y + (col_width * col),
          (float)(top_left_x + board_width), top_left_y + (col_width * col),
          paint);
    }

    //#TODO: Draw Test for columns and rows

    paint.setTextSize( col_width * 0.85f);


    for(int row=0; row < 10;row++) {
      canvas.drawText(letters[row], 10, col_width*(row+2) - 10, paint);
      canvas.drawText(numbers[row], col_width*(row+1), col_width -10, paint);
    }

    for(int row=0; row < 11;row++){
      for(int col=0; col < 11;col++){
        if(Game.board[col][row].hasShip())
          drawCell("S", row,col,canvas);
        if(Game.board[col][row].isWaiting())
          drawCell("W", row,col,canvas);
        if(Game.board[col][row].hasMiss())
          drawCell("M", row,col,canvas);
        if(Game.board[col][row].hasHit())
          drawCell("*", row,col,canvas);

      }
    }

  }

  void drawCell( String contents, int row, int col, Canvas canvas ){
    canvas.drawText( contents, Game.board[col][row].getBottomLeft().x,
        Game.board[col][row].getBottomLeft().y , paint);
  }


}