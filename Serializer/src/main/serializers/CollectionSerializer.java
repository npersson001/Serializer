package main.serializers;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.NotSerializableException;
import java.io.StreamCorruptedException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Vector;

import main.IndexedStringBuffer;
import main.SerializerRegistry;
import util.annotations.Comp533Tags;
import util.annotations.Tags;
import util.trace.port.serialization.extensible.ExtensibleBufferDeserializationFinished;
import util.trace.port.serialization.extensible.ExtensibleBufferDeserializationInitiated;
import util.trace.port.serialization.extensible.ExtensibleValueSerializationFinished;
import util.trace.port.serialization.extensible.ExtensibleValueSerializationInitiated;

@Tags({Comp533Tags.COLLECTION_SERIALIZER})
public class CollectionSerializer implements ValueSerializer{
	
	public void objectToBuffer(Object anOutputBuffer, Object anObject, ArrayList<Object> visitedObjects)
			throws NotSerializableException {
		if(anObject instanceof ArrayList) {
			ExtensibleValueSerializationInitiated.newCase(this, anObject, anOutputBuffer);
			visitedObjects.add(anObject);
			ArrayList obj = (ArrayList)anObject;
			if(anOutputBuffer instanceof IndexedStringBuffer) {
				((IndexedStringBuffer)anOutputBuffer).append(ArrayList.class.toString() + SerializerRegistry.CLASS_DELIMITER + obj.size() + SerializerRegistry.OBJECT_DELIMITER);
			}
			else if(anOutputBuffer instanceof ByteBuffer) {
				((ByteBuffer)anOutputBuffer).put((ArrayList.class.toString() + SerializerRegistry.CLASS_DELIMITER).getBytes());
				((ByteBuffer)anOutputBuffer).putShort((short)obj.size());
			}
			else {
				throw new NotSerializableException("Incorrect buffer type (StringReader or ByteBuffer)");
			}
			for(int i = 0; i < obj.size(); i++) {
				SerializerRegistry.getDispatchingSerializer().objectToBuffer(anOutputBuffer, obj.get(i), visitedObjects);
			}
			ExtensibleValueSerializationFinished.newCase(this, anObject, anOutputBuffer, visitedObjects);
		}
		else if(anObject instanceof Vector) {
			ExtensibleValueSerializationInitiated.newCase(this, anObject, anOutputBuffer);
			visitedObjects.add(anObject);
			Vector obj = (Vector)anObject;
			if(anOutputBuffer instanceof IndexedStringBuffer) {
				((IndexedStringBuffer)anOutputBuffer).append(Vector.class.toString() + SerializerRegistry.CLASS_DELIMITER + obj.size() + SerializerRegistry.OBJECT_DELIMITER);
			}
			else if(anOutputBuffer instanceof ByteBuffer) {
				((ByteBuffer)anOutputBuffer).put((Vector.class.toString() + SerializerRegistry.CLASS_DELIMITER).getBytes());
				((ByteBuffer)anOutputBuffer).putShort((short)obj.size());
			}
			else {
				throw new NotSerializableException("Incorrect buffer type (StringReader or ByteBuffer)");
			}
			for(int i = 0; i < obj.size(); i++) {
				SerializerRegistry.getDispatchingSerializer().objectToBuffer(anOutputBuffer, obj.get(i), visitedObjects);
			}
			ExtensibleValueSerializationFinished.newCase(this, anObject, anOutputBuffer, visitedObjects);
		}
		else if(anObject instanceof HashSet) {
			ExtensibleValueSerializationInitiated.newCase(this, anObject, anOutputBuffer);
			visitedObjects.add(anObject);
			HashSet obj = (HashSet)anObject;
			if(anOutputBuffer instanceof IndexedStringBuffer) {
				((IndexedStringBuffer)anOutputBuffer).append(HashSet.class.toString() + SerializerRegistry.CLASS_DELIMITER + obj.size() + SerializerRegistry.OBJECT_DELIMITER);
			}
			else if(anOutputBuffer instanceof ByteBuffer) {
				((ByteBuffer)anOutputBuffer).put((HashSet.class.toString() + SerializerRegistry.CLASS_DELIMITER).getBytes());
				((ByteBuffer)anOutputBuffer).putShort((short)obj.size());
			}
			else {
				throw new NotSerializableException("Incorrect buffer type (StringReader or ByteBuffer)");
			}
			Iterator<Object> iter = obj.iterator();
			while(iter.hasNext()) {
				SerializerRegistry.getDispatchingSerializer().objectToBuffer(anOutputBuffer, iter.next(), visitedObjects);
			}
			ExtensibleValueSerializationFinished.newCase(this, anObject, anOutputBuffer, visitedObjects);
		}
		// need this for arbitrary collections
		else if(anObject instanceof Collection) {
			ExtensibleValueSerializationInitiated.newCase(this, anObject, anOutputBuffer);
			visitedObjects.add(anObject);
			Collection<Object> obj = (Collection)anObject;
			if(anOutputBuffer instanceof IndexedStringBuffer) {
				((IndexedStringBuffer)anOutputBuffer).append(Collection.class.toString() + SerializerRegistry.CLASS_DELIMITER + anObject.getClass().getName() + ":" 
			+ obj.size() + SerializerRegistry.OBJECT_DELIMITER);
			}
			else if(anOutputBuffer instanceof ByteBuffer) {
				((ByteBuffer)anOutputBuffer).put((Collection.class.toString() + SerializerRegistry.CLASS_DELIMITER).getBytes());
				String colRep = obj.getClass().getName() + ":" + obj.size();
				((ByteBuffer)anOutputBuffer).putShort((short)colRep.length());
				((ByteBuffer)anOutputBuffer).put(colRep.getBytes());
			}
			else {
				throw new NotSerializableException("Incorrect buffer type (StringReader or ByteBuffer)");
			}
			Iterator<Object> iter = obj.iterator();
			while(iter.hasNext()) {
				SerializerRegistry.getDispatchingSerializer().objectToBuffer(anOutputBuffer, iter.next(), visitedObjects);
			}
			ExtensibleValueSerializationFinished.newCase(this, anObject, anOutputBuffer, visitedObjects);
		}
		else {
			throw new NotSerializableException("Object is not a Collection");
		}
	}

	public Object objectFromBuffer(Object anInputBuffer, Class aClass, ArrayList<Object> retrievedObjects)
			throws StreamCorruptedException, NotSerializableException {
		if(aClass == ArrayList.class) {
			ExtensibleBufferDeserializationInitiated.newCase(this, null, anInputBuffer, aClass);
			ArrayList<Object> obj = new ArrayList<Object>();
			retrievedObjects.add(obj); // add to retrieved objects so references to it can be evaluated
			int length;
			
			if(anInputBuffer instanceof IndexedStringBuffer) {
				int endIndex = ((IndexedStringBuffer)anInputBuffer).indexOf(SerializerRegistry.OBJECT_DELIMITER, ((IndexedStringBuffer)anInputBuffer).getIndex());
				length = Integer.parseInt(((IndexedStringBuffer)anInputBuffer).substring(((IndexedStringBuffer)anInputBuffer).getIndex(), endIndex));
				((IndexedStringBuffer)anInputBuffer).setIndex(endIndex + 1);
			}
			else if(anInputBuffer instanceof ByteBuffer) {
				length = ((ByteBuffer)anInputBuffer).getShort();
			}
			else {
				throw new NotSerializableException("Incorrect buffer type (StringReader or ByteBuffer)");
			}
			for(int i = 0; i < length; i++) {
				obj.add(SerializerRegistry.getDispatchingSerializer().objectFromBuffer(anInputBuffer, retrievedObjects));
			}
			ExtensibleBufferDeserializationFinished.newCase(this, null, anInputBuffer, obj, retrievedObjects);
			return obj;
		}
		else if(aClass == Vector.class) {
			ExtensibleBufferDeserializationInitiated.newCase(this, null, anInputBuffer, aClass);
			Vector<Object> obj = new Vector<Object>();
			retrievedObjects.add(obj); // add to retrieved objects so references to it can be evaluated
			int length;
			
			if(anInputBuffer instanceof IndexedStringBuffer) {
				int endIndex = ((IndexedStringBuffer)anInputBuffer).indexOf(SerializerRegistry.OBJECT_DELIMITER, ((IndexedStringBuffer)anInputBuffer).getIndex());
				length = Integer.parseInt(((IndexedStringBuffer)anInputBuffer).substring(((IndexedStringBuffer)anInputBuffer).getIndex(), endIndex));
				((IndexedStringBuffer)anInputBuffer).setIndex(endIndex + 1);
			}
			else if(anInputBuffer instanceof ByteBuffer) {
				length = ((ByteBuffer)anInputBuffer).getShort();
			}
			else {
				throw new NotSerializableException("Incorrect buffer type (StringReader or ByteBuffer)");
			}
			for(int i = 0; i < length; i++) {
				obj.add(SerializerRegistry.getDispatchingSerializer().objectFromBuffer(anInputBuffer, retrievedObjects));
			}
			ExtensibleBufferDeserializationFinished.newCase(this, null, anInputBuffer, obj, retrievedObjects);
			return obj;
		}
		else if(aClass == HashSet.class) {
			ExtensibleBufferDeserializationInitiated.newCase(this, null, anInputBuffer, aClass);
			HashSet<Object> obj = new HashSet<Object>();
			retrievedObjects.add(obj); // add to retrieved objects so references to it can be evaluated
			int length;
			
			if(anInputBuffer instanceof IndexedStringBuffer) {
				int endIndex = ((IndexedStringBuffer)anInputBuffer).indexOf(SerializerRegistry.OBJECT_DELIMITER, ((IndexedStringBuffer)anInputBuffer).getIndex());
				length = Integer.parseInt(((IndexedStringBuffer)anInputBuffer).substring(((IndexedStringBuffer)anInputBuffer).getIndex(), endIndex));
				((IndexedStringBuffer)anInputBuffer).setIndex(endIndex + 1);
			}
			else if(anInputBuffer instanceof ByteBuffer) {
				length = ((ByteBuffer)anInputBuffer).getShort();
			}
			else {
				throw new NotSerializableException("Incorrect buffer type (StringReader or ByteBuffer)");
			}
			for(int i = 0; i < length; i++) {
				obj.add(SerializerRegistry.getDispatchingSerializer().objectFromBuffer(anInputBuffer, retrievedObjects));
			}
			ExtensibleBufferDeserializationFinished.newCase(this, null, anInputBuffer, obj, retrievedObjects);
			return obj;
		}
		// need this for arbitrary collections
		else if(aClass == Collection.class) {
			ExtensibleBufferDeserializationInitiated.newCase(this, null, anInputBuffer, aClass);
//			Collection<Object> obj = new HashSet<Object>();
//			retrievedObjects.add(obj); // add to retrieved objects so references to it can be evaluated
			int length;
			String className;
			
			if(anInputBuffer instanceof IndexedStringBuffer) {
				int endIndex = ((IndexedStringBuffer)anInputBuffer).indexOf(SerializerRegistry.OBJECT_DELIMITER, ((IndexedStringBuffer)anInputBuffer).getIndex());
				String collectionString = ((IndexedStringBuffer)anInputBuffer).substring(((IndexedStringBuffer)anInputBuffer).getIndex(), endIndex);
				className = collectionString.split(":")[0];
				length = Integer.parseInt(collectionString.split(":")[1]);
				((IndexedStringBuffer)anInputBuffer).setIndex(endIndex + 1);
			}
			else if(anInputBuffer instanceof ByteBuffer) {
				int lengthBuffer = ((ByteBuffer)anInputBuffer).getShort();
				byte[] byteArray = new byte[lengthBuffer];
				((ByteBuffer)anInputBuffer).get(byteArray, 0, lengthBuffer);
				String collectionString = new String(byteArray);
				className = collectionString.split(":")[0];
				length = Integer.parseInt(collectionString.split(":")[1]);
			}
			else {
				throw new NotSerializableException("Incorrect buffer type (StringReader or ByteBuffer)");
			}
			Collection obj = null;
			Class<?> specificClass;
			try {
				specificClass = Class.forName(className);
				obj = (Collection) specificClass.newInstance();
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}
			for(int i = 0; i < length; i++) {
				obj.add(SerializerRegistry.getDispatchingSerializer().objectFromBuffer(anInputBuffer, retrievedObjects));
			}
			ExtensibleBufferDeserializationFinished.newCase(this, null, anInputBuffer, obj, retrievedObjects);
			return obj;
		}
		else {
			throw new NotSerializableException("Class is not a Collection");
		}
	}
}
