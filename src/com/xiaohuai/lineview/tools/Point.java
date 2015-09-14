package com.xiaohuai.lineview.tools;

public abstract class Point {
	private float x;
	private float y;
	private float size;
	
	public Point(float x,float y,float size)
	{
		this.x = x;
		this.y = y;
		this.size = size;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getSize() {
		return size;
	}

	public void setSize(float size) {
		this.size = size;
	}
}
