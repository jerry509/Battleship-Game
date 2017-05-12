package com.example.Battleship_Game;

import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

/**
 * Created by Gerardo on 5/21/2015.
 */
public class BaseActivity extends Activity {

  public static Gamer currentUser = null;
  public final int gridTop = 50;
  public static String loginUsername = "";
  public static String loginPassword = "";
  public static int gameID = 0;

  public enum ServerCommands {
    LOGIN, GET_USERS, GET_AVATAR
  }

  @Override
  public boolean onCreateOptionsMenu( Menu menu ) {
    getMenuInflater().inflate( R.menu.mastermenu, menu );
    MenuItem item = menu.findItem( R.id.switchToPreferences );
    if( currentUser == null )
      item.setVisible( false );
    return true;
  }



  @Override
  public boolean onOptionsItemSelected( MenuItem item ) {
    switch( item.getItemId() ) {

      case R.id.switchToPreferences:
        startActivity( new Intent( this, Preferences.class ) );
        break;

//      case R.id.switchToGame:
//        startActivity( new Intent( this, Game.class ) );
//        break;

      case R.id.switchToHomePage:
        startActivity( new Intent( this, MainActivity.class ) );
        break;

//      case R.id.switchToTouch:
//        startActivity( new Intent( this, TouchActivity.class ) );
//        break;

      default:
        return super.onOptionsItemSelected( item );
    }
    return true;
  }

  public void toastIt( String msg ) {
    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
  }
}
