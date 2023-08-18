package com.example.deneme;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.util.AttributeSet;
import android.util.Log;

public class MyKeyboardView extends KeyboardView {

    // Keep track of the key that needs a different background color
    private Keyboard.Key targetKey;

    public MyKeyboardView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setTargetKey(Keyboard.Key key) {
        this.targetKey = key;
    }
    public Keyboard.Key getTargetKey() {return this.targetKey;}

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.v("ikinci", "null");

        if (this.targetKey != null) {
            // Set the background color for the target key
            Log.v("ikinci", "değil");
            Paint paint = new Paint();
            paint.setColor(R.drawable.key_background); // Set the desired background color

            canvas.drawRect(this.targetKey.x, this.targetKey.y, this.targetKey.x + this.targetKey.width, this.targetKey.y + this.targetKey.height, paint);
        }
    }
}
