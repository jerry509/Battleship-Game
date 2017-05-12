package com.example.Battleship_Game;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;
import android.util.DisplayMetrics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * User: lockersoft
 * Date: 2/23/14
 * Time: 3:31 PM
 * <p/>
 * Class: Game
 * @author lockersoft
 */
public class Game extends BaseActivity {

  // Users Cell Board
  public static BoardCell[][] board = new BoardCell[11][11];
  // Computer Cell Board
  public static BoardCell[][] computer_board = new BoardCell[11][11];
  // Users Board view
  public View gameBoard = null;
  // Computer Board view
  public View computer_gameBoard = null;

  Activity customGrid = this;

  Button addShip;
  Button attackShip;

  public static boolean gameStarted = false;

  Map<String, Integer> shipsMap = new HashMap<String, Integer>();
  Spinner directionsSpinner;
  String[] directionsArray;

  Map<String, Integer> directionsMap = new HashMap<String, Integer>();
  Spinner shipSpinner;
  String[] shipsArray;

  Spinner lettersSpinner;

  Spinner numbersSpinner;

  Integer[] dirNumbersArray;


  ArrayAdapter<String> spinnerArrayAdapter;

  // #TODO: Change names of theses
  String selectedShip;

  boolean placeAble = false;
  public int setShipNumber;
  int computerAttackingRowNum;

  @Override
  protected void onCreate( Bundle savedInstanceState ) {
    super.onCreate(savedInstanceState);
    initializeApp();
    setContentView(R.layout.gameboard);
    shipSpinner = (Spinner)findViewById( R.id.shipSpinner );
    directionsSpinner = (Spinner)findViewById( R.id.directionsSpinner );

    gameBoard = findViewById(R.id.boardView);

    computer_gameBoard = findViewById(R.id.computerView);

    lettersSpinner = (Spinner)findViewById( R.id.lettersSpinner );

    numbersSpinner = (Spinner)findViewById( R.id.numbersSpinner );

    addShip = (Button) findViewById(R.id.addShip);
    attackShip = (Button) findViewById(R.id.attackShip);

    gameStarted = false;

  }

  private void initializeApp() {


    // Initialize the gameGrid
    for (int y = 0; y < 11; y++) {
      for (int x = 0; x < 11; x++) {
        board[y][x] = new BoardCell();
        computer_board[y][x] = new BoardCell();
      }
    }
    // API Call to create a game.
    ChallengeComputer();
    GetShips();
    GetDirections();
  }

  private void GetDirections() {
    AsyncHttpClient client = new AsyncHttpClient();
    client.setBasicAuth(loginUsername, loginPassword);
    String challengeUrl = "http://battlegameserver.com/api/v1/available_directions.json";
    client.get(challengeUrl, new JsonHttpResponseHandler() {
      @Override
      public void onSuccess(int statusCode, Header[] headers, org.json.JSONObject directions) {
        // Successfully got a response so parse JSON object
        try {
          // Fill in the spinner with ship info
          Iterator iter = directions.keys();
          while (iter.hasNext()) {
            String key = (String) iter.next();
            Integer value = directions.getInt(key);
            directionsMap.put(key + "" + "" + "", value);
          }
          int size = directionsMap.keySet().size();
          directionsArray = new String[size];
          directionsArray = directionsMap.keySet().toArray(new String[0]);

          spinnerArrayAdapter = new ArrayAdapter<String>(getApplicationContext(),
              android.R.layout.simple_spinner_item, directionsArray);
          spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
          directionsSpinner.setAdapter(spinnerArrayAdapter);

        } catch (Exception e) {
          e.printStackTrace();
          toastIt(e.getLocalizedMessage());
        }
      }

      @Override
      public void onFailure(int statusCode, org.apache.http.Header[] headers, java.lang.Throwable throwable, org.json.JSONObject errorResponse) {
        // Response failed :(
        try {
          // TODO: ADD in a toast it
          toastIt("Failed");

        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
  }

  public void GetShips() {
    AsyncHttpClient client = new AsyncHttpClient();
    client.setBasicAuth( loginUsername, loginPassword );
    String challengeUrl = "http://battlegameserver.com/api/v1/available_ships.json";
    client.get(challengeUrl, new JsonHttpResponseHandler() {
      @Override
      public void onSuccess(int statusCode, Header[] headers, org.json.JSONObject ships) {
        // Successfully got a response so parse JSON object
        try {
          // Fill in the spinner with ship info
          Iterator iter = ships.keys();
          while (iter.hasNext()) {
            String key = (String) iter.next();
            Integer value = ships.getInt(key);
           // shipsMap.put(key + "(" + value + ")", value);
            shipsMap.put(key, value);
          }
          int size = shipsMap.keySet().size();
          shipsArray = new String[size];
          shipsArray = shipsMap.keySet().toArray(new String[0]);

          spinnerArrayAdapter = new ArrayAdapter<String>(getApplicationContext(),
              android.R.layout.simple_spinner_item, shipsArray);
          spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
          shipSpinner.setAdapter(spinnerArrayAdapter);

        } catch (Exception e) {
          e.printStackTrace();
          toastIt(e.getLocalizedMessage());
        }
      }

      @Override
      public void onFailure(int statusCode, org.apache.http.Header[] headers, java.lang.Throwable throwable, org.json.JSONObject errorResponse) {
        // Response failed :(
        try {
          // TODO: ADD in a toast it
          toastIt("Failed");

        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
  }



  public void drawShip( String ship ) {

    String Selected_Letter = lettersSpinner.getSelectedItem().toString();

    String Selected_Number = numbersSpinner.getSelectedItem().toString();

    setShipNumber = Integer.parseInt(Selected_Number);

    // North
    int ship_value = 0;
    for (int row = 0; row < shipsMap.get(ship); row++) {

      if (directionsSpinner.getSelectedItem().equals("north")) {

        if (lettersSpinner.getSelectedItemPosition() - shipsMap.get(ship) > 0) {
          board[lettersSpinner.getSelectedItemPosition() + 1 - row][numbersSpinner.getSelectedItemPosition() + 1].addShip();

          if (gameBoard != null) {
            gameBoard.invalidate();
            placeAble = true;
          }
        } else {
          placeAble = false;
          toastIt("Not Placeable");
          break;
        }
      }


      // South
      else if (directionsSpinner.getSelectedItem().equals("south")) {
        // #TODO: Works but not when its outside
        if (lettersSpinner.getSelectedItemPosition() + shipsMap.get(ship) < 11) {
          board[lettersSpinner.getSelectedItemPosition() + 1 + row][numbersSpinner.getSelectedItemPosition() + 1 ].addShip();
          placeAble = true;

          if (gameBoard != null) {
            gameBoard.invalidate();
          }
        } else {
          placeAble = false;
          toastIt("Not Placeable");
          break;
        }
      }

      // East
      else if (directionsSpinner.getSelectedItem().equals("east")) {
        if (numbersSpinner.getSelectedItemPosition() + shipsMap.get(ship)  - 1 < 11) {
          board[lettersSpinner.getSelectedItemPosition() + 1][numbersSpinner.getSelectedItemPosition() + 1 + row].addShip();

          if (gameBoard != null) {
            gameBoard.invalidate();
          }
        }else {
          placeAble = false;
          toastIt("Not Placeable");
          break;
        }
      }
      // West
      else if (directionsSpinner.getSelectedItem().equals("west")) {

        if (numbersSpinner.getSelectedItemPosition() - shipsMap.get(ship) + 1 > 0) {
          board[lettersSpinner.getSelectedItemPosition() + 1][numbersSpinner.getSelectedItemPosition() + 1 - row].addShip();
          if (gameBoard != null) {
            gameBoard.invalidate();
          }

        }
        else {
          placeAble = false;
          toastIt("Not Placeable");
          break;
        }
      }
    }
    AddShip();
  }

  public void onClickAddShip( View view ) {

    String selectedShip = shipSpinner.getSelectedItem().toString();
//Places the ship on game board
    drawShip(selectedShip);



    if(placeAble) {
      shipsMap.remove(selectedShip);
    }

    int size = shipsMap.keySet().size();
    shipsArray = new String[size];

    shipsArray = shipsMap.keySet().toArray(new String[0]);
    spinnerArrayAdapter = new ArrayAdapter<String>(getApplicationContext(),
        android.R.layout.simple_spinner_item, shipsArray);
    shipSpinner.setAdapter(spinnerArrayAdapter);
    if( size == 0 ) {
      gameStarted = true;
      directionsSpinner.setEnabled(false);

      directionsSpinner.setVisibility(View.GONE);

      shipSpinner.setEnabled(false);

      shipSpinner.setVisibility(View.GONE);

      lettersSpinner.setEnabled(true);
      numbersSpinner.setEnabled(true);

      addShip.setEnabled(false);
      addShip.setVisibility(View.INVISIBLE);

      attackShip.setEnabled(true);
      attackShip.setVisibility(View.VISIBLE);

      computer_gameBoard.setVisibility(View.VISIBLE);
      gameBoard.setEnabled(true);
      gameBoard.invalidate();
    }

  }

  public static void challengeComputerSuccess(JSONObject game) {
    try {
      gameID = Integer.parseInt(game.getString("game_id"));
      Log.i("API", game.toString());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void ChallengeComputer() {
    new BattleShipAPI(loginUsername, loginPassword).challengeComputer();
  }

  public void AddShip( ){


    dirNumbersArray = directionsMap.values().toArray(new Integer[0]);
    int currentNumberDirection = dirNumbersArray[directionsSpinner.getSelectedItemPosition()];

    AsyncHttpClient client = new AsyncHttpClient();
    client.setBasicAuth( loginUsername, loginPassword );
    String challengeUrl = "http://battlegameserver.com/api/v1/game/"+gameID+"/add_ship/"+shipSpinner.getSelectedItem()+"/"+lettersSpinner.getSelectedItem()+"/"+numbersSpinner.getSelectedItem()+"/"+ currentNumberDirection + ".json";
    toastIt(challengeUrl);
    Log.i("ShipAPI", challengeUrl.toString());
    client.get(challengeUrl, new JsonHttpResponseHandler() {
      @Override
      public void onSuccess(int statusCode, Header[] headers, org.json.JSONObject ships) {
        Log.i("Ship", ships.toString());
      }

      @Override
      public void onFailure(int statusCode, org.apache.http.Header[] headers, java.lang.Throwable throwable, org.json.JSONObject errorResponse) {
        // Response failed :(
        try {
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
  }


  public void onClickAttackShip( View view ) {

    //Set Waiting based on my attack
    computer_gameBoard.invalidate();
    // #TODO: Set Hit/Miss

    AsyncHttpClient client = new AsyncHttpClient();
    client.setBasicAuth( loginUsername, loginPassword );
    String attackUrl = "http://battlegameserver.com/api/v1/game/"+gameID+"/attack/"+lettersSpinner.getSelectedItem()+"/"+numbersSpinner.getSelectedItem()+".json";
  //  Log.i("TOAST", attackUrl);
    client.get( attackUrl, new JsonHttpResponseHandler() {
      @Override
      public void onSuccess(int statusCode, Header[] headers, JSONObject attack) {
        try {

          String hit = attack.getString("hit");
          String comp_row = attack.getString("comp_row");
          String comp_col = attack.getString("comp_col");
          String comp_hit = attack.getString("comp_hit");
          String user_ship_sunk = attack.getString("user_ship_sunk");
          String comp_ship_sunk = attack.getString("comp_ship_sunk");
          String num_computer_ships_sunk = attack.getString("num_computer_ships_sunk");
          String num_your_ships_sunk = attack.getString("num_your_ships_sunk");
          String winner = attack.getString("winner");

          Log.i("Attack", String.valueOf(attack));

          if ("a".equals(comp_row)) {
            computerAttackingRowNum = 0;
          } else if ("b".equals(comp_row)) {
            computerAttackingRowNum = 1;
          } else if ("c".equals(comp_row)) {
            computerAttackingRowNum = 2;
          } else if ("d".equals(comp_row)) {
            computerAttackingRowNum = 3;
          } else if ("e".equals(comp_row)) {
            computerAttackingRowNum = 4;
          } else if ("f".equals(comp_row)) {
            computerAttackingRowNum = 5;
          } else if ("g".equals(comp_row)) {
            computerAttackingRowNum = 6;
          } else if ("h".equals(comp_row)) {
            computerAttackingRowNum = 7;
          } else if ("i".equals(comp_row)) {
            computerAttackingRowNum = 8;
          } else if ("j".equals(comp_row)) {
            computerAttackingRowNum = 9;
          }

          //My Board
          if (comp_hit == "true") {
            board[computerAttackingRowNum+1][Integer.parseInt(comp_col)+1].setHit();
            board[computerAttackingRowNum+1][Integer.parseInt(comp_col)+1].setWaiting(false);

          } else if (comp_hit == "false") {
            board[computerAttackingRowNum+1][Integer.parseInt(comp_col)+1].setMiss();
            board[computerAttackingRowNum+1][Integer.parseInt(comp_col)+1].setWaiting(false);

          }
          //Computer Board

          if (hit == "true") {
            computer_board[lettersSpinner.getSelectedItemPosition() + 1][numbersSpinner.getSelectedItemPosition() +1].setHit();
    
          } else if (hit == "false") {
            computer_board[lettersSpinner.getSelectedItemPosition() + 1][numbersSpinner.getSelectedItemPosition() +1].setMiss();
          }
  
          Log.i("computerAttackCol", comp_col );
          Log.i("computerAttackRow", comp_row);


          if (!attack.getString("comp_ship_sunk").equals("no")) {
            toastIt((new StringBuilder()).append("User: You sunk his ").append(attack.getString("comp_ship_sunk")).toString());
          }
          // Its backwards but it works on the toast it
          if (winner.equals("you")) {
            toastIt("sorry you lost");
          }else if (winner.equals("computer")){
            toastIt("Looks like you won");
          }
        } catch (JSONException e) {
          e.printStackTrace();
        }
        gameBoard.invalidate();
        computer_gameBoard.invalidate();
      }


      @Override

      public void onFailure(int statusCode, org.apache.http.Header[] headers, java.lang.Throwable throwable, org.json.JSONObject errorResponse) {
        // Response failed :(
        try {

        } catch( Exception e ) {
          e.printStackTrace();
        }
      }
    } );
  }






}
