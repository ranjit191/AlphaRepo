package com.textmaxx.Fonts;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by afzal on 29/8/16.
 */
public class PoppinsLight extends TextView {
    public PoppinsLight(Context context) {
        super(context);
    }

    public PoppinsLight(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PoppinsLight(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public PoppinsLight(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Poppins-Light.ttf");
        setTypeface(tf);

    }
}
