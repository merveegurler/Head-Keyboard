package com.example.deneme;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.util.AttributeSet;

public class CustomKeyboardView extends KeyboardView {
    // Keep track of the key that needs a different background color
    private Keyboard.Key targetKey;

    public CustomKeyboardView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setTargetKey(Keyboard.Key key) {
        this.targetKey = key;
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (targetKey != null) {
            // Set the background color for the target key
            Paint paint = new Paint();
            paint.setColor(Color.RED); // Set the desired background color

            canvas.drawRect(targetKey.x, targetKey.y, targetKey.x + targetKey.width, targetKey.y + targetKey.height, paint);
        }
    }
}
