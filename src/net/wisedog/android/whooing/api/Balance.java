package net.wisedog.android.whooing.api;

import net.wisedog.android.whooing.utils.WhooingCalendar;

import org.json.JSONObject;

/**
 * @see https://whooing.com/api/bs.format
 * */
public class Balance extends AbstractAPI {

    /**
     * Get balance
     * @param appSection    사용자 Section
     * @param appID         App ID
     * @param token         Token
     * @param appKey        어플리케이션 Key
     * @param tokenSecret   Secret Token 키
     * @param endDate       마지막날짜. null이면 오늘로 세팅된다.
     * @return API 결과물. JSONObject로 리턴한다.
     * */
    public JSONObject getBalance(String appSection, String appID, String token, String appKey,
            String tokenSecret, String endDate) {
        String date = "";
        if (endDate == null) {
            date = WhooingCalendar.getTodayYYYYMMDD();
        } else {
            date = endDate;
        }
        String budgetURL = "https://whooing.com/api/bs.json_array" + "?section_id=" + appSection
                + "&end_date=" + date;
        JSONObject result = callAPI(budgetURL, appID, token, appKey, tokenSecret);
        return result;
    }
}
