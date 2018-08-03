package main.test;

import java.io.Serializable;

public class Node implements Serializable{
	int data;
	Node next = null;
	
	public Node() {}
	
	public Node(int d) {
		data = d;
	}
	
	public void setData(int d) {
		data = d;
	}
	
	public int getData() {
		return data;
	}
	
	public void setNext(Node n) {
		next = n;
	}
	
	public Node getNext() {
		return next;
	}
	
}
