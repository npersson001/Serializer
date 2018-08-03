package main.serializers;

import java.io.NotSerializableException;
import java.io.StreamCorruptedException;
import java.lang.reflect.Array;
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

@Tags({Comp533Tags.ARRAY_SERIALIZER})
public class ArraySerializer implements ValueSerializer{

	@Override
	public void objectToBuffer(Object anOutputBuffer, Object anObject, ArrayList<Object> visitedObjects)
			throws NotSerializableException {
		if(anObject.getClass().isArray()) {
			ExtensibleValueSerializationInitiated.newCase(this, anObject, anOutputBuffer);
			visitedObjects.add(anObject);
			Object obj;
			int size = Array.getLength(anObject);
			String arrayRep = anObject.getClass().getComponentType().getName() + ":" + size;
			if(anOutputBuffer instanceof IndexedStringBuffer) {
				((IndexedStringBuffer)anOutputBuffer).append(Array.class.toString() + SerializerRegistry.CLASS_DELIMITER + 
						arrayRep + SerializerRegistry.OBJECT_DELIMITER);
			}
			else if(anOutputBuffer instanceof ByteBuffer) {
				((ByteBuffer)anOutputBuffer).put((Array.class.toString() + SerializerRegistry.CLASS_DELIMITER).getBytes());
				((ByteBuffer)anOutputBuffer).putShort((short)arrayRep.length());
				((ByteBuffer)anOutputBuffer).put(arrayRep.getBytes());
			}
			else {
				throw new NotSerializableException("Incorrect buffer type (StringReader or ByteBuffer)");
			}
			for(int i = 0; i < size; i++) {
				SerializerRegistry.getDispatchingSerializer().objectToBuffer(anOutputBuffer, Array.get(anObject, i), visitedObjects);
			}
			ExtensibleValueSerializationFinished.newCase(this, anObject, anOutputBuffer, visitedObjects);
		}
		else {
			throw new NotSerializableException("Class is not an Array");
		}
		
	}

	@Override
	public Object objectFromBuffer(Object anInputBuffer, Class aClass, ArrayList<Object> retrievedObjects)
			throws StreamCorruptedException, NotSerializableException {
		if(aClass == Array.class) {
			ExtensibleBufferDeserializationInitiated.newCase(this, null, anInputBuffer, aClass);
			int size;
			String arrayRep;
			if(anInputBuffer instanceof IndexedStringBuffer) {
				int endIndex = ((IndexedStringBuffer)anInputBuffer).indexOf(SerializerRegistry.OBJECT_DELIMITER, ((IndexedStringBuffer)anInputBuffer).getIndex());
				arrayRep = ((IndexedStringBuffer)anInputBuffer).substring(((IndexedStringBuffer)anInputBuffer).getIndex(), endIndex);
				((IndexedStringBuffer)anInputBuffer).setIndex(endIndex + 1);
			}
			else if(anInputBuffer instanceof ByteBuffer) {
				int length = ((ByteBuffer)anInputBuffer).getShort();
				byte[] byteArray = new byte[length];
				((ByteBuffer)anInputBuffer).get(byteArray, 0, length);
				arrayRep = new String(byteArray);
			}
			else {
				throw new NotSerializableException("Incorrect buffer type (StringReader or ByteBuffer)");
			}
			String compType = arrayRep.split(":")[0];
			size = Integer.parseInt(arrayRep.split(":")[1]);
			Object obj = null;
			retrievedObjects.add(obj); // add to retrieved objects so references to it can be evaluated
			try {
				obj = Array.newInstance(Class.forName(compType), size);
			} catch (NegativeArraySizeException | ClassNotFoundException e) {
				e.printStackTrace();
			}
			for(int i = 0; i < size; i++) {
				Array.set(obj, i, SerializerRegistry.getDispatchingSerializer().objectFromBuffer(anInputBuffer, retrievedObjects));
			}
			ExtensibleBufferDeserializationFinished.newCase(this, null, anInputBuffer, obj, retrievedObjects);
			return obj;
		}
		else {
			throw new NotSerializableException("Class is not an Array");
		}
	}

}
