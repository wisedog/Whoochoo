package net.wisedog.android.whooing.db;

import net.wisedog.android.whooing.utils.WhooingCalendar;

import org.json.JSONObject;

public class RecentMovieInfo {
    private int mEntityId;
    private String mName;
    private String mGenre;
    private int mScore;
    private String mImageUrl;
    private String mImagePath;
    private String mDateDiff;
    private String mAgeLimit;
    private String mCountry;
    private String mRuntime;    
    private String mBookingRate;
    private int mUpdateDate;
    
    public RecentMovieInfo(JSONObject jsonObj) {
        if(jsonObj == null){
            return;
        }
        try{
            mEntityId = jsonObj.getInt("entity_id");
            mName = jsonObj.getString("name");
            mGenre = jsonObj.getString("genre");
            mScore = jsonObj.getInt("score");
            mImageUrl = jsonObj.getString("img");
            mImagePath = "";
            mDateDiff = jsonObj.getString("date_diff");
            mAgeLimit = jsonObj.getString("age_limit");
            mCountry = jsonObj.getString("country");
            mRuntime = jsonObj.getString("runtime");
            double rate = jsonObj.getDouble("rate");
            mBookingRate = String.valueOf(rate);
            mUpdateDate = WhooingCalendar.getTodayYYYYMMDDint();
        }
        catch(org.json.JSONException e){
            
        }
    }
    
    public RecentMovieInfo(){
        
    }
    
    public int getEntityId() {
        return mEntityId;
    }
    public void setEntityId(int mEntityId) {
        this.mEntityId = mEntityId;
    }
    public String getName() {
        return mName;
    }
    public void setName(String mName) {
        this.mName = mName;
    }
    public String getAgeLimit() {
        return mAgeLimit;
    }
    public void setAgeLimit(String mAgeLimit) {
        this.mAgeLimit = mAgeLimit;
    }
    public String getCountry() {
        return mCountry;
    }
    public void setCountry(String mCountry) {
        this.mCountry = mCountry;
    }
    public String getDateDiff() {
        return mDateDiff;
    }
    public void setDateDiff(String mDateDiff) {
        this.mDateDiff = mDateDiff;
    }
    public String getGenre() {
        return mGenre;
    }
    public void setGenre(String mGenre) {
        this.mGenre = mGenre;
    }
    public int getScore() {
        return mScore;
    }
    public void setScore(int mScore) {
        this.mScore = mScore;
    }
    public String getRuntime() {
        return mRuntime;
    }
    public void setRuntime(String mRuntime) {
        this.mRuntime = mRuntime;
    }
    public String getImageUrl() {
        return mImageUrl;
    }
    public void setImageUrl(String mImageUrl) {
        this.mImageUrl = mImageUrl;
    }
    public String getImagePath() {
        return mImagePath;
    }
    public void setImagePath(String mImagePath) {
        this.mImagePath = mImagePath;
    }
    public String getBookingRate() {
        return mBookingRate;
    }
    public void setBookingRate(String mBookingRate) {
        this.mBookingRate = mBookingRate;
    }
    public int getUpdateDate() {
        return mUpdateDate;
    }
    public void setUpdateDate(int mUpdateDate) {
        this.mUpdateDate = mUpdateDate;
    }
    
}
