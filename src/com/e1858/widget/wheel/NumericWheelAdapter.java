package com.e1858.widget.wheel;
/**
 * 
 * @author jia
 *
 */
public class NumericWheelAdapter implements WheelAdapter {
	public  static final int DEFAULT_MAX_VALUE = 9;
	private static final int DEFAULT_MIN_VALUE = 0;
	private int minValue;
	private int maxValue;
	private String format;

	public NumericWheelAdapter() {
		this(DEFAULT_MIN_VALUE, DEFAULT_MAX_VALUE);
	}

	public NumericWheelAdapter(int minValue, int maxValue) {
		this(minValue, maxValue, null);
	}
	public NumericWheelAdapter(int minValue, int maxValue, String format) {
		this.minValue = minValue;
		this.maxValue = maxValue;
		this.format = format;
	}

	public String getItem(int index) {
		if (index >= 0 && index < getItemsCount()) {
			int value = minValue + index;
			return format != null ? String.format(format, value) : Integer.toString(value);
		}
		return null;
	}

	public int getItemsCount() {
		return maxValue - minValue + 1;
	}
	
	public int getMaximumLength() {
		int max = Math.max(Math.abs(maxValue), Math.abs(minValue));
		int maxLen = Integer.toString(max).length();
		if (minValue < 0) {
			maxLen++;
		}
		return maxLen;
	}
}
