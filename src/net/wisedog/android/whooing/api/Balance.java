package net.wisedog.android.whooing.api;

import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

/**
 * @see	https://whooing.com/api/bs.format
 * */
public class Balance extends AbstractAPI {

	/**
	 * Get balance 
	 * */
	public JSONObject getBalance(String appSection, String appID, String token
			,String appKey, String tokenSecret, String endDate){
		String date = "";
		if(endDate == null){
			Calendar rightNow = Calendar.getInstance(); 
			
			int month = rightNow.get(Calendar.MONTH)+1;
			int day = rightNow.get(Calendar.DAY_OF_MONTH);
			String monthString = "";
			String dayString = "";
			if(day < 10)
				dayString += "0";
			if(month < 10)
				monthString += "0";
			monthString +=month;
			dayString += day;
			
			int year = rightNow.get(Calendar.YEAR);
			date += year + monthString + dayString;
		}
		else{
			date = endDate;
		}
		String budgetURL = "https://whooing.com/api/bs.json_array"
				+"?section_id="	+appSection +"&end_date="+date;
		JSONObject result = callAPI(budgetURL, appID, token, appKey, tokenSecret);
		try {
			result = (JSONObject) result.getJSONObject("results");
			
		} catch (JSONException e) {
			Log.e(Budget.class.toString(), "JSON error in API_BALANCE");
			result = null;
		}
		
		return result;
		
	}
}
