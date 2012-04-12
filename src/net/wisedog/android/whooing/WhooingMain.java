package net.wisedog.android.whooing;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class WhooingMain extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.whooing_main);
		//TODO make a thread to handshake to server
		Toast.makeText(this, initHandshake(), 2000).show();
	}

	/**
	 * Do initial handshake to server. Getting a token in this phase
	 * @return	Returns token
	 * */
	private String initHandshake(){
		String token = null;
	    HttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(
				"https://whooing.com/app_auth/request_token?app_id="+ Define.APP_ID+"&app_secret="+Define.APP_KEY+"");
		try {
			HttpResponse response = client.execute(httpGet);
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if (statusCode == 200) {
				HttpEntity entity = response.getEntity();
				InputStream content = entity.getContent();
				
				// Load the requested page converted to a string into a JSONObject.
                JSONObject result;
				try {
					// Get the token value
					result = new JSONObject(convertStreamToString(content));
					token = result.getString("token");
				} catch (JSONException e) {
					Log.e(WhooingMain.class.toString(), "Failed to get JSON token value");
					return null;
				}
			} else {
				Log.e(WhooingMain.class.toString(), "Failed to download file");
				return null;
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return token.toString();
	}

    private static String convertStreamToString(InputStream is) {
        /*
         * To convert the InputStream to String we use the BufferedReader.readLine()
         * method. We iterate until the BufferedReader return null which means
         * there's no more data to read. Each line will appended to a StringBuilder
         * and returned as String.
         */
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
 
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
    

    @Override
	public void onBackPressed() {
		// block back event
    	//TODO Ask exit
	}
	
}
