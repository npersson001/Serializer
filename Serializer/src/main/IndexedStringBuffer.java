package main;

public class IndexedStringBuffer {
	public StringBuffer buffer;
	public int index;
	
	public IndexedStringBuffer() {
		buffer = new StringBuffer();
		index = 0;
	}
	
	public IndexedStringBuffer(String str) {
		buffer = new StringBuffer(str);
		index = 0;
	}
	
	public int getIndex() {
		return index;
	}
	
	public void setIndex(int i) {
		index = i;
	}
	
	public void append(String str) {
		buffer.append(str);
	}
	
	public int indexOf(String str) {
		return buffer.indexOf(str);
	}
	
	public int indexOf(String str, int idx) {
		return buffer.indexOf(str, idx);
	}
	
	public String substring(int start, int end) {
		return buffer.toString().substring(start, end);
	}
	
	public String toString() {
		return buffer.toString();
	}
	
	public void delete(int start, int end) {
		buffer.delete(start, end);
	}
	
	public int length() {
		return buffer.length();
	}
}
