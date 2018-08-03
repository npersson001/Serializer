package main.test;

import java.awt.Color;
import java.io.NotSerializableException;
import java.io.StreamCorruptedException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import examples.serialization.AnotherBMISpreadsheet;
import examples.serialization.BMISpreadsheet;
import main.LogicalBinarySerializerFactory;
import main.LogicalTextualSerializerFactory;
import serialization.Serializer;
import serialization.SerializerSelector;
import util.trace.factories.FactoryTraceUtility;

public class Tester {
	
	public static enum Family{
		MOM, DAD
	}
	public static void main(String args[]) {
		System.out.println("********************************************************");
		System.out.println("******************** TESTING LOGICAL *******************");
		System.out.println("********************************************************\n");
		
		SerializerSelector.setSerializerFactory(new LogicalTextualSerializerFactory());
		Serializer serializer = SerializerSelector.createSerializer();
		System.out.println("--------------------------------------------------------");
		
		Collection<Integer> col = new MyCollection<>();
		col.add(11);
		col.add(13);
		runTest(serializer, col);
		
		runTest(serializer, 5);
		runTest(serializer, false);
		runTest(serializer, true);
		runTest(serializer, (long)7);
		runTest(serializer, (short)8);
		runTest(serializer, (float)9);
		runTest(serializer, (double)10);
		runTest(serializer, "this is a string!");
		
		ArrayList<Object> arrayList = new ArrayList<>();
		arrayList.add(3);
		arrayList.add(4);
		runTest(serializer, arrayList);
		arrayList.removeAll(arrayList);
		
		arrayList.add(false);
		arrayList.add(true);
		runTest(serializer, arrayList);
		arrayList.removeAll(arrayList);
		
		arrayList.add((long)5);
		arrayList.add((long)6);
		runTest(serializer, arrayList);
		arrayList.removeAll(arrayList);
		
		arrayList.add((short)7);
		arrayList.add((short)8);
		runTest(serializer, arrayList);
		arrayList.removeAll(arrayList);
		
		arrayList.add((float)9);
		arrayList.add((float)10);
		runTest(serializer, arrayList);
		arrayList.removeAll(arrayList);
		
		arrayList.add((double)1);
		arrayList.add((double)2);
		runTest(serializer, arrayList);
		arrayList.removeAll(arrayList);
		
		arrayList.add("String 1");
		arrayList.add("String 2");
		runTest(serializer, arrayList);
		arrayList.removeAll(arrayList);
		
		Vector<Object> vector = new Vector<>();
		vector.add(5);
		vector.add(6);
		runTest(serializer, vector);
		vector.removeAll(vector);
		
		vector.add(false);
		vector.add(true);
		runTest(serializer, vector);
		vector.removeAll(vector);
		
		vector.add((long)5);
		vector.add((long)6);
		runTest(serializer, vector);
		vector.removeAll(vector);
		
		vector.add((short)7);
		vector.add((short)8);
		runTest(serializer, vector);
		vector.removeAll(vector);
		
		vector.add((float)9);
		vector.add((float)10);
		runTest(serializer, vector);
		vector.removeAll(vector);
		
		vector.add((double)1);
		vector.add((double)2);
		runTest(serializer, vector);
		vector.removeAll(vector);
		
		vector.add("String 1");
		vector.add("String 2");
		runTest(serializer, vector);
		vector.removeAll(vector);
		
		HashSet<Object> hashSet = new HashSet<>();
		hashSet.add(2);
		hashSet.add(3);
		runTest(serializer, hashSet);
		hashSet.removeAll(hashSet);
		
		hashSet.add(false);
		hashSet.add(true);
		runTest(serializer, hashSet);
		hashSet.removeAll(hashSet);
		
		hashSet.add((long)5);
		hashSet.add((long)6);
		runTest(serializer, hashSet);
		hashSet.removeAll(hashSet);
		
		hashSet.add((short)7);
		hashSet.add((short)8);
		runTest(serializer, hashSet);
		hashSet.removeAll(hashSet);
		
		hashSet.add((float)9);
		hashSet.add((float)10);
		runTest(serializer, hashSet);
		hashSet.removeAll(hashSet);
		
		hashSet.add((double)1);
		hashSet.add((double)2);
		runTest(serializer, hashSet);
		hashSet.removeAll(hashSet);
		
		hashSet.add("String 1");
		hashSet.add("String 2");
		runTest(serializer, hashSet);
		hashSet.removeAll(hashSet);
		
		HashMap<Object, Object> hashMap = new HashMap<>();
		hashMap.put(1, "one");
		hashMap.put(2, "two");
		runTest(serializer, hashMap);
		hashMap.clear();
		
		hashMap.put((long)1, false);
		hashMap.put((long)2, true);
		runTest(serializer, hashMap);
		hashMap.clear();
		
		hashMap.put((double)3, (float)4);
		hashMap.put((double)5, (float)6);
		runTest(serializer, hashMap);
		hashMap.clear();
		
		Hashtable<Object, Object> hashtable = new Hashtable<>();
		hashtable.put(1, "one");
		hashtable.put(2, "two");
		runTest(serializer, hashtable);
		hashtable.clear();
		
		hashtable.put((long)1, false);
		hashtable.put((long)2, true);
		runTest(serializer, hashtable);
		hashtable.clear();
		
		hashtable.put((double)3, (float)4);
		hashtable.put((double)5, (float)6);
		runTest(serializer, hashtable);
		hashtable.clear();
		
		runTest(serializer, Family.DAD);
		runTest(serializer, null);
		
		Integer[] array = {1, 2};
		runTest(serializer, array);
		
		Coordinate coor = new Coordinate(5, 10);
		runTest(serializer, coor);
		
		Node node = new Node(5);
		runTest(serializer, node);
		
		List<Integer> list = new LinkedList<Integer>();
		list.add(6);
		list.add(11);
		runTest(serializer, list);
		
		Node node1 = new Node(7);
		Node node2 = new Node(8);
		node1.setNext(node2);
		node2.setNext(node1);
		runTest(serializer, node1);
		
		BMISpreadsheet bmi = new AnotherBMISpreadsheet();
		bmi.setHeight(2.0);
		bmi.setMale(true);
		runTest(serializer, bmi);
		
		System.out.println("\n********************************************************");
		System.out.println("******************** TESTING BINARY ********************");
		System.out.println("********************************************************\n");
		
		SerializerSelector.setSerializerFactory(new LogicalBinarySerializerFactory());
		serializer = SerializerSelector.createSerializer();
		System.out.println("--------------------------------------------------------");
		
		runTest(serializer, col);
		
		runTest(serializer, 5);
		runTest(serializer, false);
		runTest(serializer, true);
		runTest(serializer, (long)7);
		runTest(serializer, (short)8);
		runTest(serializer, (float)9);
		runTest(serializer, (double)10);
		runTest(serializer, "this is a string!");
		
		arrayList.add(3);
		arrayList.add(4);
		runTest(serializer, arrayList);
		arrayList.removeAll(arrayList);
		
		arrayList.add(false);
		arrayList.add(true);
		runTest(serializer, arrayList);
		arrayList.removeAll(arrayList);
		
		arrayList.add((long)5);
		arrayList.add((long)6);
		runTest(serializer, arrayList);
		arrayList.removeAll(arrayList);
		
		arrayList.add((short)7);
		arrayList.add((short)8);
		runTest(serializer, arrayList);
		arrayList.removeAll(arrayList);
		
		arrayList.add((float)9);
		arrayList.add((float)10);
		runTest(serializer, arrayList);
		arrayList.removeAll(arrayList);
		
		arrayList.add((double)1);
		arrayList.add((double)2);
		runTest(serializer, arrayList);
		arrayList.removeAll(arrayList);
		
		arrayList.add("String 1");
		arrayList.add("String 2");
		runTest(serializer, arrayList);
		arrayList.removeAll(arrayList);
		
		vector.add(5);
		vector.add(6);
		runTest(serializer, vector);
		vector.removeAll(vector);
		
		vector.add(false);
		vector.add(true);
		runTest(serializer, vector);
		vector.removeAll(vector);
		
		vector.add((long)5);
		vector.add((long)6);
		runTest(serializer, vector);
		vector.removeAll(vector);
		
		vector.add((short)7);
		vector.add((short)8);
		runTest(serializer, vector);
		vector.removeAll(vector);
		
		vector.add((float)9);
		vector.add((float)10);
		runTest(serializer, vector);
		vector.removeAll(vector);
		
		vector.add((double)1);
		vector.add((double)2);
		runTest(serializer, vector);
		vector.removeAll(vector);
		
		vector.add("String 1");
		vector.add("String 2");
		runTest(serializer, vector);
		vector.removeAll(vector);
		
		hashSet.add(2);
		hashSet.add(3);
		runTest(serializer, hashSet);
		hashSet.removeAll(hashSet);
		
		hashSet.add(false);
		hashSet.add(true);
		runTest(serializer, hashSet);
		hashSet.removeAll(hashSet);
		
		hashSet.add((long)5);
		hashSet.add((long)6);
		runTest(serializer, hashSet);
		hashSet.removeAll(hashSet);
		
		hashSet.add((short)7);
		hashSet.add((short)8);
		runTest(serializer, hashSet);
		hashSet.removeAll(hashSet);
		
		hashSet.add((float)9);
		hashSet.add((float)10);
		runTest(serializer, hashSet);
		hashSet.removeAll(hashSet);
		
		hashSet.add((double)1);
		hashSet.add((double)2);
		runTest(serializer, hashSet);
		hashSet.removeAll(hashSet);
		
		hashSet.add("String 1");
		hashSet.add("String 2");
		runTest(serializer, hashSet);
		hashSet.removeAll(hashSet);
		
		hashMap.put(1, "one");
		hashMap.put(2, "two");
		runTest(serializer, hashMap);
		hashMap.clear();
		
		hashMap.put((long)1, false);
		hashMap.put((long)2, true);
		runTest(serializer, hashMap);
		hashMap.clear();
		
		hashMap.put((double)3, (float)4);
		hashMap.put((double)5, (float)6);
		runTest(serializer, hashMap);
		hashMap.clear();
		
		hashtable.put(1, "one");
		hashtable.put(2, "two");
		runTest(serializer, hashtable);
		hashtable.clear();
		
		hashtable.put((long)1, false);
		hashtable.put((long)2, true);
		runTest(serializer, hashtable);
		hashtable.clear();
		
		hashtable.put((double)3, (float)4);
		hashtable.put((double)5, (float)6);
		runTest(serializer, hashtable);
		hashtable.clear();
		
		runTest(serializer, Family.DAD);
		runTest(serializer, null);
		
		runTest(serializer, array);
		
		runTest(serializer, coor);
		runTest(serializer, node);
		
		runTest(serializer, list);
		
		runTest(serializer, node1);
		
		bmi = new AnotherBMISpreadsheet();
		bmi.setHeight(2.0);
		bmi.setMale(true);
		runTest(serializer, bmi);
	}
	
	public static void runTest(Serializer serializer, Object obj) {
		try {
			System.out.println("object sent: " + (obj == null ? "null" : (obj.getClass().isArray() ? Arrays.deepToString((Object[]) obj) : obj.toString())));
			ByteBuffer buf = serializer.outputBufferFromObject(obj);
			System.out.println("buffer received: " + new String(buf.array(), buf.position(), buf.limit()));
			Object objRec = serializer.objectFromInputBuffer(buf);
			System.out.println("object received: " + (objRec == null ? "null" : (objRec.getClass().isArray() ? Arrays.deepToString((Object[]) objRec) : objRec.toString())));
			boolean output = ((obj != null) ? obj.getClass() == objRec.getClass() : objRec == null) ;
			if (output) {
				System.out.println("*** result: " + output + " ***");
			}
			else {
				System.err.println(output);
			}
			System.out.println("--------------------------------------------------------");
		} catch (NotSerializableException e) {
			e.printStackTrace();
		} catch (StreamCorruptedException e) {
			e.printStackTrace();
		}
	}
}
