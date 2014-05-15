package com.e1858.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CheckBox;

public class FocusCheckBox extends CheckBox {

	public FocusCheckBox(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public FocusCheckBox(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public FocusCheckBox(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean isFocused() {
		// TODO Auto-generated method stub
		return true;
	}
	

}
