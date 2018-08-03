package main.serializers;

import java.io.NotSerializableException;
import java.io.StreamCorruptedException;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import main.IndexedStringBuffer;
import main.SerializerRegistry;
import util.annotations.Comp533Tags;
import util.annotations.Tags;
import util.trace.port.serialization.extensible.ExtensibleBufferDeserializationFinished;
import util.trace.port.serialization.extensible.ExtensibleBufferDeserializationInitiated;
import util.trace.port.serialization.extensible.ExtensibleValueSerializationFinished;
import util.trace.port.serialization.extensible.ExtensibleValueSerializationInitiated;

@Tags({Comp533Tags.NULL_SERIALIZER})
public class NullSerializer implements ValueSerializer{

	@Override
	public void objectToBuffer(Object anOutputBuffer, Object anObject, ArrayList<Object> visitedObjects)
			throws NotSerializableException {
		if(anObject == null) {
			ExtensibleValueSerializationInitiated.newCase(this, anObject, anOutputBuffer);
//			visitedObjects.add(anObject);
			if(anOutputBuffer instanceof IndexedStringBuffer) {
				((IndexedStringBuffer)anOutputBuffer).append(SerializerRegistry.NULL_CLASS + SerializerRegistry.CLASS_DELIMITER + "null" + SerializerRegistry.OBJECT_DELIMITER);
			}
			else if(anOutputBuffer instanceof ByteBuffer) {
				((ByteBuffer)anOutputBuffer).put((SerializerRegistry.NULL_CLASS + SerializerRegistry.CLASS_DELIMITER).getBytes());
				((ByteBuffer)anOutputBuffer).putShort((short)SerializerRegistry.NULL_CLASS.length());
				((ByteBuffer)anOutputBuffer).put(SerializerRegistry.NULL_CLASS.getBytes());
			}
			else {
				throw new NotSerializableException("Incorrect buffer type (StringReader or ByteBuffer)");
			}
			ExtensibleValueSerializationFinished.newCase(this, anObject, anOutputBuffer, visitedObjects);
		}
		else {
			throw new NotSerializableException("Object is not null");
		}
	}

	@Override
	public Object objectFromBuffer(Object anInputBuffer, Class aClass, ArrayList<Object> retrievedObjects)
			throws StreamCorruptedException, NotSerializableException {
		if(aClass == null) {
			ExtensibleBufferDeserializationInitiated.newCase(this, null, anInputBuffer, Integer.class); // this needs to be some type of class
			String obj = null;
//			retrievedObjects.add(obj); // add to retrieved objects so references to it can be evaluated
			if(anInputBuffer instanceof IndexedStringBuffer) {
				int endIndex = ((IndexedStringBuffer)anInputBuffer).indexOf(SerializerRegistry.OBJECT_DELIMITER, ((IndexedStringBuffer)anInputBuffer).getIndex());
				obj = ((IndexedStringBuffer)anInputBuffer).substring(((IndexedStringBuffer)anInputBuffer).getIndex(), endIndex);
				((IndexedStringBuffer)anInputBuffer).setIndex(endIndex + 1);
			}
			else if(anInputBuffer instanceof ByteBuffer) {
				int length = ((ByteBuffer)anInputBuffer).getShort();
				byte[] byteArray = new byte[length];
				((ByteBuffer)anInputBuffer).get(byteArray, 0, length);
				obj = new String(byteArray);
			}
			else {
				throw new NotSerializableException("Incorrect buffer type (StringReader or ByteBuffer)");
			}
			ExtensibleBufferDeserializationFinished.newCase(this, null, anInputBuffer, obj, retrievedObjects);
			return null; // the string processing was only to move the pointers in the buffers for future reads
		}
		else {
			throw new NotSerializableException("Class is not null");
		}
	}

}
