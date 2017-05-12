package com.example.Battleship_Game;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by Gerardo on 6/8/2015.
 */
public class Drawing extends Activity {

  GameView gv;

  @Override
  protected void onCreate( Bundle savedInstanceState ) {
    super.onCreate( savedInstanceState );
    gv = new GameView(this, null);
    setContentView(gv);
  //  setContentView( R.layout.gameboard );
  }
}
