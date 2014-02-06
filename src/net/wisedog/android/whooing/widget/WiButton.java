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
