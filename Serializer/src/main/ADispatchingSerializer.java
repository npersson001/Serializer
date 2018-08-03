package main;

import java.io.NotSerializableException;
import java.io.Serializable;
import java.io.StreamCorruptedException;
import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import util.annotations.Comp533Tags;
import util.annotations.Tags;

@Tags({Comp533Tags.DISPATCHING_SERIALIZER })
public class ADispatchingSerializer implements DispatchingSerializer{

	public void objectToBuffer(Object anOutputBuffer, Object anObject, ArrayList<Object> visitedObjects)
			throws NotSerializableException {
		// need some type of error handling for no serializer returned
		if(visitedObjects.contains(anObject)) {
			addReference(anOutputBuffer, anObject, visitedObjects);
		}
		else if(anObject == null) {
			SerializerRegistry.getNullSerializer().objectToBuffer(anOutputBuffer, anObject, visitedObjects);
		}
		else if(anObject instanceof Integer) {
			SerializerRegistry.getValueSerializer(Integer.class).objectToBuffer(anOutputBuffer, anObject, visitedObjects);
		}
		else if(anObject instanceof Boolean) {
			SerializerRegistry.getValueSerializer(Boolean.class).objectToBuffer(anOutputBuffer, anObject, visitedObjects);
		}
		else if(anObject instanceof Long) {
			SerializerRegistry.getValueSerializer(Long.class).objectToBuffer(anOutputBuffer, anObject, visitedObjects);
		}
		else if(anObject instanceof Short) {
			SerializerRegistry.getValueSerializer(Short.class).objectToBuffer(anOutputBuffer, anObject, visitedObjects);
		}
		else if(anObject instanceof Float) {
			SerializerRegistry.getValueSerializer(Float.class).objectToBuffer(anOutputBuffer, anObject, visitedObjects);
		}
		else if(anObject instanceof Double) {
			SerializerRegistry.getValueSerializer(Double.class).objectToBuffer(anOutputBuffer, anObject, visitedObjects);
		}
		else if(anObject instanceof String) {
			SerializerRegistry.getValueSerializer(String.class).objectToBuffer(anOutputBuffer, anObject, visitedObjects);
		}
		else if(anObject instanceof ArrayList) {
			SerializerRegistry.getValueSerializer(ArrayList.class).objectToBuffer(anOutputBuffer, anObject, visitedObjects);
		}
		else if(anObject instanceof Vector) {
			SerializerRegistry.getValueSerializer(Vector.class).objectToBuffer(anOutputBuffer, anObject, visitedObjects);
		}
		else if(anObject instanceof HashSet) {
			SerializerRegistry.getValueSerializer(HashSet.class).objectToBuffer(anOutputBuffer, anObject, visitedObjects);
		}
		else if(anObject instanceof HashMap) {
			SerializerRegistry.getValueSerializer(HashMap.class).objectToBuffer(anOutputBuffer, anObject, visitedObjects);
		}
		else if(anObject instanceof Hashtable) {
			SerializerRegistry.getValueSerializer(Hashtable.class).objectToBuffer(anOutputBuffer, anObject, visitedObjects);
		}
		else if(anObject.getClass().isEnum()) {
			SerializerRegistry.getEnumSerializer().objectToBuffer(anOutputBuffer, anObject, visitedObjects);
		}
		else if(anObject.getClass().isArray()) {
			SerializerRegistry.getArraySerializer().objectToBuffer(anOutputBuffer, anObject, visitedObjects);
		}
		else if(util.misc.RemoteReflectionUtility.isList(anObject.getClass())) { 
			SerializerRegistry.getListPatternSerializer().objectToBuffer(anOutputBuffer, anObject, visitedObjects);
		}
		else if(anObject instanceof Collection) {
			SerializerRegistry.getValueSerializer(Collection.class).objectToBuffer(anOutputBuffer, anObject, visitedObjects);
		}
		else if(anObject instanceof Serializable) {
			SerializerRegistry.getBeanSerializer().objectToBuffer(anOutputBuffer, anObject, visitedObjects);
		}
	}

	public Object objectFromBuffer(Object anInputBuffer, ArrayList<Object> retrievedObjects)
			throws StreamCorruptedException, NotSerializableException {	
		String classType = getClassType(anInputBuffer);
		if(classType.equals(SerializerRegistry.REFERENCE)) {
			return retrieveReference(anInputBuffer, retrievedObjects);
		}
		else if(classType.equals(SerializerRegistry.NULL_CLASS)) {
			return SerializerRegistry.getNullSerializer().objectFromBuffer(anInputBuffer, null, retrievedObjects);
		}
		else if(classType.equals(Integer.class.toString())) {
			return SerializerRegistry.getValueSerializer(Integer.class).objectFromBuffer(anInputBuffer, Integer.class, retrievedObjects);
		}
		else if(classType.equals(Boolean.class.toString())) {
			return SerializerRegistry.getValueSerializer(Boolean.class).objectFromBuffer(anInputBuffer, Boolean.class, retrievedObjects);
		}
		else if(classType.equals(Long.class.toString())) {
			return SerializerRegistry.getValueSerializer(Long.class).objectFromBuffer(anInputBuffer, Long.class, retrievedObjects);
		}
		else if(classType.equals(Short.class.toString())) {
			return SerializerRegistry.getValueSerializer(Short.class).objectFromBuffer(anInputBuffer, Short.class, retrievedObjects);
		}
		else if(classType.equals(Double.class.toString())) {
			return SerializerRegistry.getValueSerializer(Double.class).objectFromBuffer(anInputBuffer, Double.class, retrievedObjects);
		}
		else if(classType.equals(Float.class.toString())) {
			return SerializerRegistry.getValueSerializer(Float.class).objectFromBuffer(anInputBuffer, Float.class, retrievedObjects);
		}
		else if(classType.equals(String.class.toString())) {
			return SerializerRegistry.getValueSerializer(String.class).objectFromBuffer(anInputBuffer, String.class, retrievedObjects);
		}
		else if(classType.equals(ArrayList.class.toString())) {
			return SerializerRegistry.getValueSerializer(ArrayList.class).objectFromBuffer(anInputBuffer, ArrayList.class, retrievedObjects);
		}
		else if(classType.equals(Vector.class.toString())) {
			return SerializerRegistry.getValueSerializer(Vector.class).objectFromBuffer(anInputBuffer, Vector.class, retrievedObjects);
		}
		else if(classType.equals(HashSet.class.toString())) {
			return SerializerRegistry.getValueSerializer(HashSet.class).objectFromBuffer(anInputBuffer, HashSet.class, retrievedObjects);
		}
		else if(classType.equals(HashMap.class.toString())) {
			return SerializerRegistry.getValueSerializer(HashMap.class).objectFromBuffer(anInputBuffer, HashMap.class, retrievedObjects);
		}
		else if(classType.equals(Hashtable.class.toString())) {
			return SerializerRegistry.getValueSerializer(Hashtable.class).objectFromBuffer(anInputBuffer, Hashtable.class, retrievedObjects);
		}
		else if(classType.equals(Enum.class.toString())) {
			return SerializerRegistry.getEnumSerializer().objectFromBuffer(anInputBuffer, Enum.class, retrievedObjects);
		}
		else if(classType.equals(Array.class.toString())) {
			return SerializerRegistry.getArraySerializer().objectFromBuffer(anInputBuffer, Array.class, retrievedObjects);
		}
		else if(classType.equals(Collection.class.toString())) {
			return SerializerRegistry.getValueSerializer(Collection.class).objectFromBuffer(anInputBuffer, Collection.class, retrievedObjects);
		}
		else if(classType.equals(Class.class.toString())) {
			return SerializerRegistry.getBeanSerializer().objectFromBuffer(anInputBuffer, Class.class, retrievedObjects);
		}
		else if(classType.equals(List.class.toString())) {
			return SerializerRegistry.getListPatternSerializer().objectFromBuffer(anInputBuffer, List.class, retrievedObjects);
		}
		else {
			return null;
		}
	}
	
	public String getClassType(Object anInputBuffer) throws NotSerializableException {
		if(anInputBuffer instanceof IndexedStringBuffer) {
			int endIndex = ((IndexedStringBuffer)anInputBuffer).indexOf(SerializerRegistry.CLASS_DELIMITER, ((IndexedStringBuffer)anInputBuffer).getIndex());
			String classString = ((IndexedStringBuffer)anInputBuffer).substring(((IndexedStringBuffer)anInputBuffer).getIndex(), endIndex);
			((IndexedStringBuffer)anInputBuffer).setIndex(endIndex + 1);
			return classString;
		}
		else if(anInputBuffer instanceof ByteBuffer) {
			((ByteBuffer)anInputBuffer).mark();
			byte[] byteArray = new byte[((ByteBuffer)anInputBuffer).remaining()];
			((ByteBuffer)anInputBuffer).get(byteArray);
			String fullString = new String(byteArray);
			int endIndex = fullString.indexOf(SerializerRegistry.CLASS_DELIMITER);
			String classString = fullString.substring(0, endIndex);
			((ByteBuffer)anInputBuffer).reset();
			((ByteBuffer)anInputBuffer).get(new byte[endIndex + 1]);
			return classString;
		}
		else {
			throw new NotSerializableException("Incorrect buffer type (StringReader or ByteBuffer)");
		}
	}
	
	private void addReference(Object anOutputBuffer, Object anObject, ArrayList<Object> visitedObjects) throws NotSerializableException {
		int index = visitedObjects.indexOf(anObject);
		if(index >= 0)  {
			if(anOutputBuffer instanceof IndexedStringBuffer) {
				((IndexedStringBuffer)anOutputBuffer).append(SerializerRegistry.REFERENCE + SerializerRegistry.CLASS_DELIMITER + 
						index + SerializerRegistry.OBJECT_DELIMITER);
			}
			else if(anOutputBuffer instanceof ByteBuffer) {
				((ByteBuffer)anOutputBuffer).put((SerializerRegistry.REFERENCE + SerializerRegistry.CLASS_DELIMITER).getBytes());
				((ByteBuffer)anOutputBuffer).putShort((short)index);
			}
			else {
				throw new NotSerializableException("Incorrect buffer type (StringReader or ByteBuffer)");
			}
		}
		else {
			throw new RuntimeException("Reference not found in visited objects");
		}
	}
	
	private Object retrieveReference(Object anInputBuffer, ArrayList<Object> retrievedObjects) throws NotSerializableException {
		int index;
		if(anInputBuffer instanceof IndexedStringBuffer) {
			int endIndex = ((IndexedStringBuffer)anInputBuffer).indexOf(SerializerRegistry.OBJECT_DELIMITER, ((IndexedStringBuffer)anInputBuffer).getIndex());
			index = Integer.parseInt(((IndexedStringBuffer)anInputBuffer).substring(((IndexedStringBuffer)anInputBuffer).getIndex(), endIndex));
			((IndexedStringBuffer)anInputBuffer).setIndex(endIndex + 1);
		}
		else if(anInputBuffer instanceof ByteBuffer) {
			index = (int)((ByteBuffer)anInputBuffer).getShort();
		}
		else {
			throw new NotSerializableException("Incorrect buffer type (StringReader or ByteBuffer)");
		}
		return retrievedObjects.get(index);
	}
}
