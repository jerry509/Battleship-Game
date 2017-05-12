package com.example.Battleship_Game;

import android.util.Log;
import com.loopj.android.http.AsyncHttpClient;
import org.apache.http.Header;
import com.loopj.android.http.*;
import org.json.JSONObject;

/**
 * Created by Gerardo on 5/21/2015.
 */
public class BattleShipAPI extends BaseActivity {
  final String loginUrl = "http://battlegameserver.com/api/v1/login.json";
  String userName, userPassword;

  BattleShipAPI( String _userName, String _password ) {
    userName = _userName;
    userPassword = _password;
  }

  public void challengeComputer() {
    AsyncHttpClient client = new AsyncHttpClient();
    client.setBasicAuth( loginUsername, loginPassword );
    String challengeUrl = "http://battlegameserver.com/api/v1/challenge_computer.json";

    client.get( challengeUrl, new JsonHttpResponseHandler() {
      @Override
      public void onSuccess(int statusCode, Header[] headers, org.json.JSONObject game ) {
        // Successfully got a response so parse JSON object
       // #TODO: Fight with computer
        Game.challengeComputerSuccess( game );

      }

      @Override
      public void onFailure(int statusCode, org.apache.http.Header[] headers, java.lang.Throwable throwable, org.json.JSONObject errorResponse) {
        // called when response HTTP status is "4XX" (eg. 401, 403, 404)
        Log.i("API", errorResponse.toString());

      }
    } );
  }

  public void apiFailure( int statusCode, Header[] headers, byte[] responseBody, Throwable error ) {
    // Response failed :(String decodedResponse = ;
    try {
      toastIt( new String( responseBody, "UTF-8" ) );
    } catch( Exception e ) {
      e.printStackTrace();
    }
  }

}
