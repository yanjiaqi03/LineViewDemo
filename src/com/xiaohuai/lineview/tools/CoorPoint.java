package com.xiaohuai.lineview.tools;

public class CoorPoint extends Point {
	private String xValue;
	private int yValue;
	private int pointColor;
	private Boolean isSolid = true;

	public CoorPoint(float pointSize, String xValue, int yValue, int pointColor, Boolean isSolid) {
		super(0f, 0f, pointSize);
		this.xValue = xValue;
		this.yValue = yValue;
		this.pointColor = pointColor;
		this.isSolid = isSolid;
	}
	
	public String getxValue() {
		return xValue;
	}

	public void setxValue(String xValue) {
		this.xValue = xValue;
	}

	public int getyValue() {
		return yValue;
	}

	public void setyValue(int yValue) {
		this.yValue = yValue;
	}

	public int getPointColor() {
		return pointColor;
	}

	public void setPointColor(int pointColor) {
		this.pointColor = pointColor;
	}

	public Boolean getIsSolid() {
		return isSolid;
	}

	public void setIsSolid(Boolean isSolid) {
		this.isSolid = isSolid;
	}
}
