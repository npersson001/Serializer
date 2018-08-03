package main.serializers;

import java.io.NotSerializableException;
import java.io.StreamCorruptedException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import main.IndexedStringBuffer;
import main.SerializerRegistry;
import util.annotations.Comp533Tags;
import util.annotations.Tags;
import util.trace.port.serialization.extensible.ExtensibleBufferDeserializationFinished;
import util.trace.port.serialization.extensible.ExtensibleBufferDeserializationInitiated;
import util.trace.port.serialization.extensible.ExtensibleValueSerializationFinished;
import util.trace.port.serialization.extensible.ExtensibleValueSerializationInitiated;

@Tags({Comp533Tags.MAP_SERIALIZER})
public class MapSerializer implements ValueSerializer{
	
	public void objectToBuffer(Object anOutputBuffer, Object anObject, ArrayList<Object> visitedObjects)
			throws NotSerializableException {
		if(anObject instanceof HashMap) {
			ExtensibleValueSerializationInitiated.newCase(this, anObject, anOutputBuffer);
			visitedObjects.add(anObject);
			HashMap obj = (HashMap)anObject;
			if(anOutputBuffer instanceof IndexedStringBuffer) {
				((IndexedStringBuffer)anOutputBuffer).append(HashMap.class.toString() + SerializerRegistry.CLASS_DELIMITER + obj.size() + SerializerRegistry.OBJECT_DELIMITER);
			}
			else if(anOutputBuffer instanceof ByteBuffer) {
				((ByteBuffer)anOutputBuffer).put((HashMap.class.toString() + SerializerRegistry.CLASS_DELIMITER).getBytes());
				((ByteBuffer)anOutputBuffer).putShort((short)obj.size());
			}
			else {
				throw new NotSerializableException("Incorrect buffer type (StringReader or ByteBuffer)");
			}
			Iterator<Map.Entry<Object, Object>> iter = obj.entrySet().iterator();
			while(iter.hasNext()) {
				Map.Entry<Object, Object> entry = iter.next();
				SerializerRegistry.getDispatchingSerializer().objectToBuffer(anOutputBuffer, entry.getKey(), visitedObjects);
				SerializerRegistry.getDispatchingSerializer().objectToBuffer(anOutputBuffer, entry.getValue(), visitedObjects);
			}
			ExtensibleValueSerializationFinished.newCase(this, anObject, anOutputBuffer, visitedObjects);
		}
		else if(anObject instanceof Hashtable) {
			ExtensibleValueSerializationInitiated.newCase(this, anObject, anOutputBuffer);
			visitedObjects.add(anObject);
			Hashtable obj = (Hashtable)anObject;
			if(anOutputBuffer instanceof IndexedStringBuffer) {
				((IndexedStringBuffer)anOutputBuffer).append(Hashtable.class.toString() + SerializerRegistry.CLASS_DELIMITER + obj.size() + SerializerRegistry.OBJECT_DELIMITER);
			}
			else if(anOutputBuffer instanceof ByteBuffer) {
				((ByteBuffer)anOutputBuffer).put((Hashtable.class.toString() + SerializerRegistry.CLASS_DELIMITER).getBytes());
				((ByteBuffer)anOutputBuffer).putShort((short)obj.size());
			}
			else {
				throw new NotSerializableException("Incorrect buffer type (StringReader or ByteBuffer)");
			}
			Iterator<Map.Entry<Object, Object>> iter = obj.entrySet().iterator();
			while(iter.hasNext()) {
				Map.Entry<Object, Object> entry = iter.next();
				SerializerRegistry.getDispatchingSerializer().objectToBuffer(anOutputBuffer, entry.getKey(), visitedObjects);
				SerializerRegistry.getDispatchingSerializer().objectToBuffer(anOutputBuffer, entry.getValue(), visitedObjects);
			}
			ExtensibleValueSerializationFinished.newCase(this, anObject, anOutputBuffer, visitedObjects);
		}
		else {
			throw new NotSerializableException("Object is not a Map");
		}
	}

	public Object objectFromBuffer(Object anInputBuffer, Class aClass, ArrayList<Object> retrievedObjects)
			throws StreamCorruptedException, NotSerializableException {
		if(aClass == HashMap.class) {
			ExtensibleBufferDeserializationInitiated.newCase(this, null, anInputBuffer, aClass);
			HashMap<Object, Object> obj = new HashMap<Object, Object>();
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
				Object key = SerializerRegistry.getDispatchingSerializer().objectFromBuffer(anInputBuffer, retrievedObjects);
				obj.put(key, SerializerRegistry.getDispatchingSerializer().objectFromBuffer(anInputBuffer, retrievedObjects));
			}
			ExtensibleBufferDeserializationFinished.newCase(this, null, anInputBuffer, obj, retrievedObjects);
			return obj;
		}
		else if(aClass == Hashtable.class) {
			ExtensibleBufferDeserializationInitiated.newCase(this, null, anInputBuffer, aClass);
			Hashtable<Object, Object> obj = new Hashtable<>();
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
				Object key = SerializerRegistry.getDispatchingSerializer().objectFromBuffer(anInputBuffer, retrievedObjects);
				obj.put(key, SerializerRegistry.getDispatchingSerializer().objectFromBuffer(anInputBuffer, retrievedObjects));
			}
			ExtensibleBufferDeserializationFinished.newCase(this, null, anInputBuffer, obj, retrievedObjects);
			return obj;
		}
		else {
			throw new NotSerializableException("Class is not a Map");
		}
	}
}
