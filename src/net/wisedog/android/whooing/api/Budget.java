package net.wisedog.android.whooing.api;

import java.util.Calendar;

import org.json.JSONObject;

/**
 * Section API routine class
 * @author	Wisedog(me@wisedog.net)
 * @see		https://whooing.com/#forum/developer/en/api_reference/budget
 * */
public class Budget extends AbstractAPI {
	/**
	 * Get budget information
	 * @param	appSection		Section in use
	 * @return	Returns JSONObject if it success or null
	 * */
	public JSONObject getBudget(String appSection, String appID, String token, 
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
		String budgetURL = "https://whooing.com/api/budget/expenses.json_array"
				+"?section_id="	+appSection + "&start_date="+year+startDate
				+"&end_date="+year+endDate;
		JSONObject result = callApi(budgetURL, appID, token, appKey, tokenSecret);
		return result;
	}
}
