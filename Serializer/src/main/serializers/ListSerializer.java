package main.serializers;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.NotSerializableException;
import java.io.Serializable;
import java.io.StreamCorruptedException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import main.IndexedStringBuffer;
import main.SerializerRegistry;
import util.annotations.Comp533Tags;
import util.annotations.Tags;
import util.misc.RemoteReflectionUtility;
import util.trace.port.serialization.extensible.ExtensibleBufferDeserializationFinished;
import util.trace.port.serialization.extensible.ExtensibleBufferDeserializationInitiated;
import util.trace.port.serialization.extensible.ExtensibleValueSerializationFinished;
import util.trace.port.serialization.extensible.ExtensibleValueSerializationInitiated;

@Tags({Comp533Tags.LIST_PATTERN_SERIALIZER})
public class ListSerializer implements ValueSerializer {

	@Override
	public void objectToBuffer(Object anOutputBuffer, Object anObject, ArrayList<Object> visitedObjects)
			throws NotSerializableException {
		if(util.misc.RemoteReflectionUtility.isList(anObject.getClass())) {
			ExtensibleValueSerializationInitiated.newCase(this, anObject, anOutputBuffer);
			visitedObjects.add(anObject);
			int length = util.misc.RemoteReflectionUtility.listSize(anObject);
			if(anOutputBuffer instanceof IndexedStringBuffer) {
				((IndexedStringBuffer)anOutputBuffer).append(List.class.toString() + SerializerRegistry.CLASS_DELIMITER + 
						anObject.getClass().getName() + ":" + length + SerializerRegistry.OBJECT_DELIMITER);
			}
			else if(anOutputBuffer instanceof ByteBuffer) {
				((ByteBuffer)anOutputBuffer).put((List.class.toString() + SerializerRegistry.CLASS_DELIMITER).getBytes());
				String listRep = anObject.getClass().getName() + ":" + length;
				((ByteBuffer)anOutputBuffer).putShort((short)listRep.length());
				((ByteBuffer)anOutputBuffer).put(listRep.getBytes());
			}
			else {
				throw new NotSerializableException("Incorrect buffer type (StringReader or ByteBuffer)");
			}
			for(int i = 0; i < length; i++) {
				SerializerRegistry.getDispatchingSerializer().objectToBuffer(anOutputBuffer, util.misc.RemoteReflectionUtility.listGet(anObject, i), visitedObjects);
			}
			RemoteReflectionUtility.invokeInitSerializedObject(anObject);
			ExtensibleValueSerializationFinished.newCase(this, anObject, anOutputBuffer, visitedObjects);
		}
		else {
			throw new NotSerializableException("Class is not a List");
		}
	}

	@Override
	public Object objectFromBuffer(Object anInputBuffer, Class aClass, ArrayList<Object> retrievedObjects)
			throws StreamCorruptedException, NotSerializableException {
		if(aClass == List.class) {
			ExtensibleBufferDeserializationInitiated.newCase(this, null, anInputBuffer, aClass);
			String listString;
			Object obj = null;
			retrievedObjects.add(obj); // add to retrieved objects so references to it can be evaluated
			if(anInputBuffer instanceof IndexedStringBuffer) {
				int endIndex = ((IndexedStringBuffer)anInputBuffer).indexOf(SerializerRegistry.OBJECT_DELIMITER);
				listString = ((IndexedStringBuffer)anInputBuffer).substring(((IndexedStringBuffer)anInputBuffer).getIndex(), endIndex);
				((IndexedStringBuffer)anInputBuffer).setIndex(endIndex + 1);
			}
			else if(anInputBuffer instanceof ByteBuffer) {
				int size = ((ByteBuffer)anInputBuffer).getShort();
				byte[] byteArray = new byte[size];
				((ByteBuffer)anInputBuffer).get(byteArray, 0, size);
				listString = new String(byteArray);
			}
			else {
				throw new NotSerializableException("Incorrect buffer type (StringReader or ByteBuffer)");
			}
			String listName = listString.split(":")[0];
			int length = Integer.parseInt(listString.split(":")[1]);
			try {
				Class<?> specificList = Class.forName(listName);
				obj = specificList.newInstance();
			} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
				e.printStackTrace();
			}
			for(int i = 0; i < length; i++) {
				util.misc.RemoteReflectionUtility.listAdd(obj, 
						SerializerRegistry.getDispatchingSerializer().objectFromBuffer(anInputBuffer, retrievedObjects));
			}
			ExtensibleBufferDeserializationFinished.newCase(this, null, anInputBuffer, obj, retrievedObjects);
			return obj;
		}
		else {
			throw new NotSerializableException("Class is not a List");
		}
	}

}
