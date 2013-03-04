/**
 * 
 */
package net.wisedog.android.whooing.widget;

import net.wisedog.android.whooing.Define;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * @author Wisedog(me@wisedog.net)
 *
 */
public class WiButton extends Button {

    /**
     * @param context
     */
    public WiButton(Context context) {
        super(context);
        setTypeface(Define.ROBOFONT);
    }

    public WiButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setTypeface(Define.ROBOFONT);
    }

    public WiButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTypeface(Define.ROBOFONT);
    }

}
