package main.test;

import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MyCollection<T> extends AbstractCollection<T>{
	
	List<T> list;
	
	public MyCollection() {
		list = new ArrayList<T>();
	}

	@Override
	public Iterator<T> iterator() {
		return list.iterator();
	}

	@Override
	public int size() {
		return list.size();
	}
	
	public boolean add(T t) {
		return list.add(t);
	}

}
