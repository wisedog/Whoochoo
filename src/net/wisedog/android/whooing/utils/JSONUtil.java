/*
 * Copyright (C) 2013 Jongha Kim
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.wisedog.android.whooing.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

/**
 * JSON Util Class
 * @author Wisedog(me@wisedog.net)
 * */
public class JSONUtil {
	/**
	 * Get JSON with given URL,header
	 * @param	url		URL to call
	 * @param	headerkey	header key. If it is null, none header is added
	 * @param	headerValye	header value. If it is null, none header is added
	 * @throws	JSONException
	 * @return	Return JSONObject if it success or return null 
	 * */
	static public JSONObject getJSONObject(String url, String headerKey, String headerValue) throws JSONException{
	    HttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(url);
		if(headerKey != null && headerValue != null){
			httpGet.addHeader(headerKey, headerValue);
		}
		
		try {
			HttpResponse response = client.execute(httpGet);
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if (statusCode == 200) {
				HttpEntity entity = response.getEntity();
				InputStream content = entity.getContent();
				
				String strContent = StringUtil.convertStreamToString(content);
                if(strContent == null){
                    return null;
                }
				// Load the requested page converted to a string into a JSONObject.
                JSONObject result;
				// Get the token value
				result = new JSONObject(strContent);
				return result;
			} else {
				Log.e(JSONUtil.class.toString(), "Failed to download file");
			}
		} catch (ClientProtocolException e) {
			Log.e(JSONUtil.class.toString(), "HttpResponse Failed");
		} catch (IOException e) {
			Log.e(JSONUtil.class.toString(), "HttpResponse IO Failed");
		}

		return null;
	}

	//TODO Need to be refactored    
    static public JSONObject getJSONObjectPost(String url, String headerKey, String headerValue,
            List<NameValuePair> postValue) throws JSONException {
        HttpClient client = new DefaultHttpClient();
        client.getParams().setParameter("http.protocol.content-charset", HTTP.UTF_8);
        HttpPost httpPost = new HttpPost(url);
        if (headerKey != null && headerValue != null) {
            httpPost.addHeader(headerKey, headerValue);
        }
	    try {
	        httpPost.setEntity(new UrlEncodedFormEntity(postValue, HTTP.UTF_8));
	        
            HttpResponse response = client.execute(httpPost);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) {
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                String strContent = StringUtil.convertStreamToString(content);
                if(strContent == null){
                    return null;
                }
                // Load the requested page converted to a string into a JSONObject.
                JSONObject result;
                // Get the token value
                result = new JSONObject(strContent);
                return result;
            } else {
                Log.e(JSONUtil.class.toString(), "Failed to download file");
            }
        } catch (ClientProtocolException e) {
            Log.e(JSONUtil.class.toString(), "HttpResponse Failed");
        } catch (IOException e) {
            Log.e(JSONUtil.class.toString(), "HttpResponse IO Failed");
        }
	    return null;
	}
    
  //TODO Need to be refactored    
    static public JSONObject getJSONObjectDelete(String url, String headerKey, String headerValue) throws JSONException {
        HttpClient client = new DefaultHttpClient();
        client.getParams().setParameter("http.protocol.content-charset", HTTP.UTF_8);
        HttpDelete httpDelete = new HttpDelete(url);
        if (headerKey != null && headerValue != null) {
            httpDelete.addHeader(headerKey, headerValue);
        }
        try {
            HttpResponse response = client.execute(httpDelete);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) {
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                String strContent = StringUtil.convertStreamToString(content);
                // Load the requested page converted to a string into a JSONObject.
                if(strContent == null){
                    return null;
                }
             // Get the token value
                JSONObject result = new JSONObject(strContent);
                return result;
            } else {
                Log.e(JSONUtil.class.toString(), "Failed to download file");
            }
        } catch (ClientProtocolException e) {
            Log.e(JSONUtil.class.toString(), "HttpResponse Failed");
        } catch (IOException e) {
            Log.e(JSONUtil.class.toString(), "HttpResponse IO Failed");
        }
        return null;
    }

    /**
     * @param url
     * @param string
     * @param headerValue
     * @param putValue
     * @return
     */
    public static JSONObject getJSONObjectPut(String url, String headerKey, String headerValue,
            List<NameValuePair> putValue) throws JSONException {
        HttpClient client = new DefaultHttpClient();
        client.getParams().setParameter("http.protocol.content-charset", HTTP.UTF_8);
        HttpPut httpPut = new HttpPut(url);
        if (headerKey != null && headerValue != null) {
            httpPut.addHeader(headerKey, headerValue);
        }
        try {
            httpPut.setEntity(new UrlEncodedFormEntity(putValue, HTTP.UTF_8));
            
            HttpResponse response = client.execute(httpPut);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) {
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                String strContent = StringUtil.convertStreamToString(content);
                if(strContent == null){
                    return null;
                }
                // Load the requested page converted to a string into a JSONObject.
                JSONObject result;
                // Get the token value
                result = new JSONObject(strContent);
                return result;
            } else {
                Log.e(JSONUtil.class.toString(), "Failed to download file");
            }
        } catch (ClientProtocolException e) {
            Log.e(JSONUtil.class.toString(), "HttpResponse Failed");
        } catch (IOException e) {
            Log.e(JSONUtil.class.toString(), "HttpResponse IO Failed");
        }
        return null;
    }
    
}
