package net.wisedog.android.whooing.api;

import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

/**
 * API package for main screen
 * @author	Wisedog(me@wisedog.net)
 * */
public class MainInfo extends AbstractAPI {
	/**
	 * Get budget information
	 * @param	appSection		Section in use
	 * @return	Returns JSONObject if it success or null
	 * */
	public JSONObject getInfo(String appSection, String appID, String token, 
			String appKey, String tokenSecret){
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
		
		String startDate = ""+monthString+"01";
		String endDate = ""+monthString+dayString;
    	/*
    	 * 요청주소 : /api_jongha/mixed.format
파라미터  예시 :$params = array(
 'section_id' => 's1234', 
 'budget_start_date' => 201108, 'budget_end_date' => 201108,
 'bs_end_date' => 20120502, 
 'bill_start_date' => 201108, 'bill_end_date' => 201108,
 'in_out_start_date' => 20120403, 'in_out_end_date' => 20120502, 
 'goal_start_date' => 201104, 'goal_end_date' => 201205,
);
    	 * */
		String mainInfoURL = "https://whooing.com/api_jongha/mixed.json_array"
				+"?section_id="	+appSection + "&budget_start_date="+year+monthString
				+"&budget_end_date="+year+monthString 
				+"&bs_end_date=" +year+endDate 
				+"&bill_start_date="+year+monthString
				+"&bill_end_date="+year+monthString
				+"&in_out_start_date="+year+startDate
				+"&in_out_end_date="+year+endDate
				+"&goal_start_date="+year+monthString
				+"&goal_end_date="+(year+1)+monthString;
		JSONObject result = callAPI(mainInfoURL, appID, token, appKey, tokenSecret);
		try {
			result = (JSONObject) result.getJSONObject("results");//.getJSONArray("rows").get(0);
			
		} catch (JSONException e) {
			Log.e(Budget.class.toString(), "JSON error in API_BUDGET");
			result = null;
		}
		
		return result;
	}
}
