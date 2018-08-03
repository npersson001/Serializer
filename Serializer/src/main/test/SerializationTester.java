package main.test;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import examples.serialization.ANamedBMISpreadsheet;
import examples.serialization.AStringHistory;
import examples.serialization.AnObjectHistory;
import examples.serialization.AnotherBMISpreadsheet;
import examples.serialization.BMISpreadsheet;
import examples.serialization.NamedBMISpreadsheet;
import examples.serialization.ObjectHistory;
import examples.serialization.StringHistory;
import main.LogicalBinarySerializerFactory;
import main.LogicalTextualSerializerFactory;
import serialization.Serializer;
import serialization.SerializerSelector;
import util.trace.port.serialization.extensible.ExtensibleSerializationTraceUtility;

public class SerializationTester {

	public static void main(String[] args) {

		testSerialization();
	}
	
	enum Color {RED,GREEN, BLUE}

	public static void testSerialization() {
//		ExtensibleSerializationTraceUtility.setTracing();
		// part 1
		SerializerSelector.setSerializerFactory(new LogicalTextualSerializerFactory());
		Serializer serializer = SerializerSelector.createSerializer();
		translate(serializer, 5);
		translate(serializer, (short)5);
		translate(serializer, (long)5);
		translate(serializer, 5.5);
		translate(serializer, (float) 5.5);
		translate (serializer, "hello world");
		translate(serializer, true);
		translate(serializer, Color.RED);
		Object[] values = { "Hello World", "Goodbye World", Color.GREEN };
		translate(serializer, values);
		List list = new ArrayList();
		list.add("Hello world");
		list.add(3);
		list.add(Color.BLUE);
		list.add(null);
		translate(serializer, list);
		list = new Vector();
		list.add("Hello world");
		list.add(3);
		list.add(Color.BLUE);
		list.add(null);
		translate(serializer, list);
		Map map = new HashMap();
		map.put("greeting", "ni hao");
		map.put(5, 4.0);
		translate(serializer, map);
		map = new Hashtable();
		map.put("greeting", "ni hao");
		map.put(5, 4.0);
		translate(serializer, map);
		Set<String> set = new HashSet();
		set.add("Hello world");
		set.add("Goodbye world");
		translate(serializer, set);
		list.add(set);
		list.add(map);
		translate(serializer, list);
		
		// part 2
		List recursive = new ArrayList();
		recursive.add(null);
		recursive.add(values);
		recursive.add(recursive);
		recursive.add(list);
		translate(serializer, recursive);
		BMISpreadsheet bmi = new AnotherBMISpreadsheet();
		bmi.setHeight(2.0);
		bmi.setMale(true);
	    translate(serializer, bmi);
		NamedBMISpreadsheet namedBMI = new ANamedBMISpreadsheet();
		namedBMI.setName("Joe Doe");
		namedBMI.setHeight(2.0);
		translate(serializer, namedBMI);
		StringHistory stringHistory = new AStringHistory();
		stringHistory.add("James Dean");
		stringHistory.add("Joe Doe");
		stringHistory.add("Jane Smith");
		stringHistory.add("John Smith");
		translate(serializer, stringHistory);
		ObjectHistory objectHistory = new AnObjectHistory();
		objectHistory.add(objectHistory);
		objectHistory.add("hello");
		translate(serializer, objectHistory);
		List recursiveList = new ArrayList();
		recursiveList.add(recursiveList);
		translate(serializer, recursiveList);

	}

	static void translate(Serializer serializer, Object object) {
		try {
			System.out.println("Serializing " + object);
			ByteBuffer buffer = serializer.outputBufferFromObject(object);
			Object readVal = serializer.objectFromInputBuffer(buffer);
			System.out.println("Deserialized " + readVal);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//
	// static void testClassLoading() {
	// System.out.println( RMIClassLoader.getClassAnnotation
	// (ABMISpreadsheet.class));
	//
	// }

}
