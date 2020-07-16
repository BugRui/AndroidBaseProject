package com.bugrui.jetpackproject.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.LayoutRes;

import com.bugrui.jetpackproject.R;

/**
 * @Author: BugRui
 * @CreateDate: 2019/9/20 9:30
 * @Description: 红点
 */
public class RedDotView extends FrameLayout {

    @LayoutRes
    private static final int layoutId = R.layout.view_red_dot;

    public RedDotView(Context context) {
        this(context, null);
    }

    public RedDotView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RedDotView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private TextView tvRedDotNumber;
    private ImageView tvRedDot;

    private void initView() {
        View rootView = LayoutInflater.from(getContext()).inflate(layoutId, null);
        tvRedDotNumber = rootView.findViewById(R.id.tv_red_dot_number);
        tvRedDot = rootView.findViewById(R.id.tv_red_dot);
        addView(rootView);
    }

    public void setRedDotNumber(int number) {
        tvRedDotNumber.setText(String.valueOf(number));
        tvRedDotNumber.setVisibility(VISIBLE);
        tvRedDot.setVisibility(GONE);
    }

    public void setRedDotNumber(String number) {
        tvRedDotNumber.setText(number);
        tvRedDotNumber.setVisibility(VISIBLE);
        tvRedDot.setVisibility(GONE);
    }

    public void showRedDot() {
        tvRedDotNumber.setVisibility(GONE);
        tvRedDot.setVisibility(VISIBLE);
    }

}
