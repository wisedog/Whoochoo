/**
 * 
 */
package net.wisedog.android.whooing.utils;

/**
 * @author Wisedog(me@wisedog.net)
 *
 */
public class FragmentUtil {
    static public int getBarWidth(double value, double maxValue, int limitWidth, int labelWidth){
        double ratio = value / (double)maxValue;
        double width = (float)limitWidth * ratio;
        if(width <= 5){
            return 5;
        }
        if((int)width >= limitWidth - labelWidth){
            return (int)width - labelWidth;
        }
        return (int)width;
    }
}
