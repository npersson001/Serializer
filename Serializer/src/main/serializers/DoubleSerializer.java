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

@Tags({Comp533Tags.DOUBLE_SERIALIZER})
public class DoubleSerializer implements ValueSerializer{

	public void objectToBuffer(Object anOutputBuffer, Object anObject, ArrayList<Object> visitedObjects)
			throws NotSerializableException {
		if(anObject instanceof Double) {
			ExtensibleValueSerializationInitiated.newCase(this, anObject, anOutputBuffer);
//			visitedObjects.add(anObject);
			Double obj = (Double)anObject;
			if(anOutputBuffer instanceof IndexedStringBuffer) {
				((IndexedStringBuffer)anOutputBuffer).append(Double.class.toString() + SerializerRegistry.CLASS_DELIMITER + obj.toString() + SerializerRegistry.OBJECT_DELIMITER);
			}
			else if(anOutputBuffer instanceof ByteBuffer) {
				((ByteBuffer)anOutputBuffer).put((Double.class.toString() + SerializerRegistry.CLASS_DELIMITER).getBytes());
				((ByteBuffer)anOutputBuffer).putDouble(obj);
			}
			else {
				throw new NotSerializableException("Incorrect buffer type (StringReader or ByteBuffer)");
			}
			ExtensibleValueSerializationFinished.newCase(this, anObject, anOutputBuffer, visitedObjects);
		}
		else {
			throw new NotSerializableException("Object is not an Double");
		}
	}

	public Object objectFromBuffer(Object anInputBuffer, Class aClass, ArrayList<Object> retrievedObjects)
			throws StreamCorruptedException, NotSerializableException {
		if(aClass == Double.class) {
			ExtensibleBufferDeserializationInitiated.newCase(this, null, anInputBuffer, aClass);
			Double obj = null;
//			retrievedObjects.add(obj); // add to retrieved objects so references to it can be evaluated
			if(anInputBuffer instanceof IndexedStringBuffer) {
				int endIndex = ((IndexedStringBuffer)anInputBuffer).indexOf(SerializerRegistry.OBJECT_DELIMITER, ((IndexedStringBuffer)anInputBuffer).getIndex());
				obj = Double.parseDouble(((IndexedStringBuffer)anInputBuffer).substring(((IndexedStringBuffer)anInputBuffer).getIndex(), endIndex));
				((IndexedStringBuffer)anInputBuffer).setIndex(endIndex + 1);
			}
			else if(anInputBuffer instanceof ByteBuffer) {
				obj = ((ByteBuffer)anInputBuffer).getDouble();
			}
			else {
				throw new NotSerializableException("Incorrect buffer type (StringReader or ByteBuffer)");
			}
			ExtensibleBufferDeserializationFinished.newCase(this, null, anInputBuffer, obj, retrievedObjects);
			return obj;
		}
		else {
			throw new NotSerializableException("Class is not an Double");
		}
	}


	
}
