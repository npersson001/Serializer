package main.test;

import java.io.Serializable;

public class Coordinate implements Serializable{
	private int x;
	private int y; 
	
	public Coordinate(int X, int Y) {
		x = X;
		y = Y;
	}
	
	public Coordinate() {}
	
	public void setX(int X) {
		x = X;
	}
	
	public int getX() {
		return x;
	}
	
	public void setY(int Y) {
		y = Y;
	}
	
	public int getY() {
		return y;
	}
}
