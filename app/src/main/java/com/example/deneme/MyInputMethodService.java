package com.example.deneme;

import static androidx.core.view.KeyEventDispatcher.dispatchKeyEvent;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.os.SystemClock;
import android.text.TextUtils;
import android.text.format.Time;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputConnection;
import android.inputmethodservice.Keyboard.Key;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class MyInputMethodService extends InputMethodService implements KeyboardView.OnKeyboardActionListener {
    private MyKeyboardView keyboardView;
    Keyboard.Key defaultKey;
    Keyboard keyboard;

    String direction = "";
    MyBroadcastReceiver receiver;

    int keyCodes[];
    String keys[];

    public class MyBroadcastReceiver extends BroadcastReceiver {
        Time time;
        int start;
        int count = 0;
        String previousDirection = "";

        int flag = 0, frontCount = 0, sameCount = 0;

        @SuppressLint("RestrictedApi")
        @Override
        public void onReceive(Context context, Intent intent) {
            Keyboard.Key newKey;

            if ("com.example.ACTION_VALUE".equals(intent.getAction())) {
                String value = intent.getStringExtra("value");
                // Use the received value in your application
                Log.e("directionnnn", value);
                direction = value;
                Log.e("previous", previousDirection);
                Log.e("count", String.valueOf(count));
                Log.e("flag", String.valueOf(flag));
                previousDirection = direction;
                Log.e("same", String.valueOf(sameCount));

                if(count != 0 && Objects.equals(direction, "front")) {
                    if(frontCount == 15) {
                        Log.e("keys", Arrays.toString(defaultKey.codes));

                        InputConnection ic = getCurrentInputConnection();
                        int[] primaryCodes = defaultKey.codes;
                        int primaryCode = primaryCodes[0];

                        if (ic == null) return;
                        switch (primaryCode) {
                            case Keyboard.KEYCODE_DELETE:
                                CharSequence selectedText = ic.getSelectedText(0);
                                if (TextUtils.isEmpty(selectedText)) {
                                    // no selection, so delete previous character
                                    ic.deleteSurroundingText(1, 0);
                                } else {
                                    // delete the selection
                                    ic.commitText("", 1);
                                }
                                break;
                            default:
                                char code = (char) primaryCode;
                                ic.commitText(String.valueOf(code), 1);
                        }
                        Log.e("select", "seçmeliii");
                        frontCount = 0;
                        flag = 0;
                        count = 0;
                    } else {
                        frontCount += 1;
                        flag = 1;
                    }
                }else if(count == 0 && !Objects.equals(direction, "front") && flag != 1) {
                    if (direction.equalsIgnoreCase("left")) {
                        newKey = findKeyToLeftOf(keyboard.getKeys(), defaultKey);
                        Log.e("key", newKey.label.toString());
                        if (newKey != null) {
                            defaultKey.onReleased(true);
                            newKey.onPressed(); // Set the pressed state of the key to the left of A to true
                            defaultKey = newKey;
                        }
                    } else if (direction.equalsIgnoreCase("right")) {
                        newKey = findKeyToRightOf(keyboard.getKeys(), defaultKey);
                        Log.e("key", newKey.label.toString());
                        if (newKey != null) {
                            defaultKey.onReleased(true);
                            newKey.onPressed(); // Set the pressed state of the key to the left of A to true
                            defaultKey = newKey;
                        }
                    } else if (direction.equalsIgnoreCase("up")) {
                        newKey = findKeyAbove(keyboard.getKeys(), defaultKey);
                        Log.e("key", newKey.label.toString());
                        if (newKey != null) {
                            defaultKey.onReleased(true);
                            newKey.onPressed(); // Set the pressed state of the key above A to true
                            defaultKey = newKey;
                        }
                    } else if (direction.equalsIgnoreCase("down")) {
                        newKey = findKeyBelow(keyboard.getKeys(), defaultKey);
                        Log.e("key", newKey.label.toString());
                        if (newKey != null) {
                            defaultKey.onReleased(true);
                            newKey.onPressed(); // Set the pressed state of the key below A to true
                            defaultKey = newKey;
                        }
                    }
                    previousDirection = direction;
                    keyboardView.setKeyboard(keyboard);
                    keyboardView.setOnKeyboardActionListener(MyInputMethodService.this);
                    count++;
                } else {
                    if(Objects.equals(previousDirection, direction)) {
                        sameCount++;
                    }
                    if (sameCount == 5) {
                        if (direction.equalsIgnoreCase("left")) {
                            newKey = findKeyToLeftOf(keyboard.getKeys(), defaultKey);
                            Log.e("key", newKey.label.toString());
                            if (newKey != null) {
                                defaultKey.onReleased(true);
                                newKey.onPressed(); // Set the pressed state of the key to the left of A to true
                                defaultKey = newKey;
                            }
                        } else if (direction.equalsIgnoreCase("right")) {
                            newKey = findKeyToRightOf(keyboard.getKeys(), defaultKey);
                            Log.e("key", newKey.label.toString());
                            if (newKey != null) {
                                defaultKey.onReleased(true);
                                newKey.onPressed(); // Set the pressed state of the key to the left of A to true
                                defaultKey = newKey;
                            }
                        } else if (direction.equalsIgnoreCase("up")) {
                            newKey = findKeyAbove(keyboard.getKeys(), defaultKey);
                            Log.e("key", newKey.label.toString());
                            if (newKey != null) {
                                defaultKey.onReleased(true);
                                newKey.onPressed(); // Set the pressed state of the key above A to true
                                defaultKey = newKey;
                            }
                        } else if (direction.equalsIgnoreCase("down")) {
                            newKey = findKeyBelow(keyboard.getKeys(), defaultKey);
                            Log.e("key", newKey.label.toString());
                            if (newKey != null) {
                                defaultKey.onReleased(true);
                                newKey.onPressed(); // Set the pressed state of the key below A to true
                                defaultKey = newKey;
                            }
                        }
                        keyboardView.setKeyboard(keyboard);
                        keyboardView.setOnKeyboardActionListener(MyInputMethodService.this);
                        frontCount = 0;
                        sameCount = 0;
                    }
                    previousDirection = direction;
                    count++;
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }

    // Register the BroadcastReceiver in your activity or service
    public void registerReceiver() {
        IntentFilter intentFilter = new IntentFilter("com.example.ACTION_VALUE");
        receiver = new MyBroadcastReceiver();
        registerReceiver(receiver, intentFilter);
    }

    @Override
    public View onCreateInputView() {
        registerReceiver();

        // get the KeyboardView and add our Keyboard layout to it
        this.keyboardView = (MyKeyboardView) getLayoutInflater().inflate(R.layout.keyboard_view, null);
        keyboard = new Keyboard(this, R.xml.number_pad);
        this.keyboardView.setKeyboard(keyboard);
        this.keyboardView.setOnKeyboardActionListener(this);

        defaultKey = findKeyByCode(keyboard.getKeys(), 65);

        if (defaultKey != null) {
            defaultKey.onPressed();
        }

        return this.keyboardView;
    }

    @Override
    public void onKey(int primaryCode, int[] keyCodes) {
        InputConnection ic = getCurrentInputConnection();
        if (ic == null) return;
        switch (primaryCode) {
            case Keyboard.KEYCODE_DELETE:
                CharSequence selectedText = ic.getSelectedText(0);
                if (TextUtils.isEmpty(selectedText)) {
                    // no selection, so delete previous character
                    ic.deleteSurroundingText(1, 0);
                } else {
                    // delete the selection
                    ic.commitText("", 1);
                }
                break;
            default:
                char code = (char) primaryCode;
                ic.commitText(String.valueOf(code), 1);
        }
    }

    private Key findKeyToLeftOf(List<Keyboard.Key> keys, Key key) {
        int keyIndex = keys.indexOf(key);
        if (keyIndex > 1 && keyIndex < keys.size()) {
            return keys.get(keyIndex - 1);
        }
        if(keyIndex == 1) {
            return keys.get(keyIndex);
        }
        return null;
    }

    private Key findKeyToRightOf(List<Keyboard.Key> keys, Key key) {
        int keyIndex = keys.indexOf(key);

        if(keyIndex == keys.size() - 1) {
            return keys.get(keyIndex);
        }

        if (keyIndex > 0 && keyIndex < keys.size() - 1) {
            return keys.get(keyIndex + 1);
        }

        return null;
    }

    private Key findKeyByCode(List<Keyboard.Key> keys, int code) {
        for (Keyboard.Key key : keys) {
            if (key.codes[0] == code) {
                return key;
            }
        }
        return null;
    }

    private Key findKeyAbove(List<Key> keys, Key defaultKey) {
        int defaultX = defaultKey.x;
        int defaultY = defaultKey.y;

        for (Key key : keys) {
            if(defaultY == 0) {
                return defaultKey;
            }
            if (key.x == defaultX && key.y == defaultY - 170) {
                return key;
            }
        }

        return null; // Key not found above the default key
    }

    private Key findKeyBelow(List<Key> keys, Key defaultKey) {
        int defaultX = defaultKey.x;
        int defaultY = defaultKey.y;

        for (Key key : keys) {
            if(defaultY == 680) {
                return defaultKey;
            }
            if (key.x == defaultX && key.y == defaultY + 170) {
                return key;
            }
        }

        return null; // Key not found below the default key
    }


    @Override
    public void onPress(int primaryCode) {
    }

    @Override
    public void onRelease(int primaryCode) {
    }

    @Override
    public void onText(CharSequence text) {
    }

    @Override
    public void swipeLeft() {
    }

    @Override
    public void swipeRight() {
    }

    @Override
    public void swipeDown() {
    }

    @Override
    public void swipeUp() {
    }
}

