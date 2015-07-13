package uiview;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import leokapanen.learningenglish.R;

/**
 * Created by Leonid Kabanen on 12.07.15.
 * Search field with listener on change state
 */
public class SearchField extends EditText {

    private OnStateChanged listener;

    // Listener interface
    public interface OnStateChanged {
        void onChange(boolean isEmpty, String text);
    }

    public SearchField(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SearchField(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void setCustomFont() {
        Typeface tf = Typefaces.INSTANCE.get(getContext(), getFontName());
        this.setTypeface(tf);
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        setCustomFont();
        onEDTextChanged("");
        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                onEDTextChanged(getText().toString());
            }
        });
    }

    // Gets custom font path
    public String getFontName() {
        return "fonts/OpenSans-Semibold.ttf";
    }

    // on change text into field
    public void onEDTextChanged(String text) {
        if (getText().length() != 0) {
            if (listener != null) {
                listener.onChange(false, text);
            }
        } else {
            if (listener != null) {
                listener.onChange(true, null);
            }
        }

        if (getText().length() != 0) {
            Drawable crossIc = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_cancel_black_18dp, null);
            crossIc.setBounds(0, 0, crossIc.getIntrinsicWidth(), crossIc.getIntrinsicHeight());
            setCompoundDrawables(null, null, crossIc, null);
            setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View v, final MotionEvent event) {
                    final int DRAWABLE_RIGHT = 2;
                    final int HOR_PADDING = getResources().getDimensionPixelSize(R.dimen.search_field_hor_padding);

                    final int RIGHT_BTN_X = getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width() + HOR_PADDING;

                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        post(new Runnable() {
                            @Override
                            public void run() {

                                if (event.getRawX() >= (getRight() - RIGHT_BTN_X)) {
                                    setText("");
                                }

                            }
                        });

                    }
                    return false;
                }
            });
        } else {
            Drawable searchIc = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_search_black_18dp, null);
            searchIc.setBounds(0, 0, searchIc.getIntrinsicWidth(), searchIc.getIntrinsicHeight());
            setCompoundDrawables(searchIc, null, null, null);
            setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return false;
                }
            });
        }
    }

    public void setListener(OnStateChanged listener) {
        this.listener = listener;
    }

}
