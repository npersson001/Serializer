package main.serializers;

import java.io.NotSerializableException;
import java.io.StreamCorruptedException;
import java.io.StringReader;
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

@Tags({Comp533Tags.FLOAT_SERIALIZER})
public class FloatSerializer implements ValueSerializer{

	public void objectToBuffer(Object anOutputBuffer, Object anObject, ArrayList<Object> visitedObjects)
			throws NotSerializableException {
		if(anObject instanceof Float) {
			ExtensibleValueSerializationInitiated.newCase(this, anObject, anOutputBuffer);
//			visitedObjects.add(anObject);
			Float obj = (Float)anObject;
			if(anOutputBuffer instanceof IndexedStringBuffer) {
				((IndexedStringBuffer)anOutputBuffer).append(Float.class.toString() + SerializerRegistry.CLASS_DELIMITER + obj.toString() + SerializerRegistry.OBJECT_DELIMITER);
			}
			else if(anOutputBuffer instanceof ByteBuffer) {
				((ByteBuffer)anOutputBuffer).put((Float.class.toString() + SerializerRegistry.CLASS_DELIMITER).getBytes());
				((ByteBuffer)anOutputBuffer).putFloat(obj);
			}
			else {
				throw new NotSerializableException("Incorrect buffer type (StringReader or ByteBuffer)");
			}
			ExtensibleValueSerializationFinished.newCase(this, anObject, anOutputBuffer, visitedObjects);
		}
		else {
			throw new NotSerializableException("Object is not an Float");
		}
	}

	public Object objectFromBuffer(Object anInputBuffer, Class aClass, ArrayList<Object> retrievedObjects)
			throws StreamCorruptedException, NotSerializableException {
		if(aClass == Float.class) {
			ExtensibleBufferDeserializationInitiated.newCase(this, null, anInputBuffer, aClass);
			Float obj = null;
//			retrievedObjects.add(obj); // add to retrieved objects so references to it can be evaluated
			if(anInputBuffer instanceof IndexedStringBuffer) {
				int endIndex = ((IndexedStringBuffer)anInputBuffer).indexOf(SerializerRegistry.OBJECT_DELIMITER, ((IndexedStringBuffer)anInputBuffer).getIndex());
				obj = Float.parseFloat(((IndexedStringBuffer)anInputBuffer).substring(((IndexedStringBuffer)anInputBuffer).getIndex(), endIndex));
				((IndexedStringBuffer)anInputBuffer).setIndex(endIndex + 1);
			}
			else if(anInputBuffer instanceof ByteBuffer) {
				obj = ((ByteBuffer)anInputBuffer).getFloat();
			}
			else {
				throw new NotSerializableException("Incorrect buffer type (StringReader or ByteBuffer)");
			}
			ExtensibleBufferDeserializationFinished.newCase(this, null, anInputBuffer, obj, retrievedObjects);
			return obj;
		}
		else {
			throw new NotSerializableException("Class is not an Float");
		}
	}


	
}
