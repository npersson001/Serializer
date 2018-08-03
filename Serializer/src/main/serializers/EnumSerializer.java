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

@Tags({Comp533Tags.ENUM_SERIALIZER})
public class EnumSerializer implements ValueSerializer{

	@Override
	public void objectToBuffer(Object anOutputBuffer, Object anObject, ArrayList<Object> visitedObjects)
			throws NotSerializableException {
		if(anObject.getClass().isEnum()) {
			ExtensibleValueSerializationInitiated.newCase(this, anObject, anOutputBuffer);
			visitedObjects.add(anObject);
			Enum<?> obj = (Enum<?>)anObject;
			if(anOutputBuffer instanceof IndexedStringBuffer) {
				((IndexedStringBuffer)anOutputBuffer).append(Enum.class.toString() + SerializerRegistry.CLASS_DELIMITER + 
						obj.getClass().getName() + ":" + obj.toString() + SerializerRegistry.OBJECT_DELIMITER);
			}
			else if(anOutputBuffer instanceof ByteBuffer) {
				((ByteBuffer)anOutputBuffer).put((Enum.class.toString() + SerializerRegistry.CLASS_DELIMITER).getBytes());
				String enumRep = obj.getClass().getName() + ":" + obj.toString();
				((ByteBuffer)anOutputBuffer).putShort((short)enumRep.length());
				((ByteBuffer)anOutputBuffer).put(enumRep.getBytes());
			}
			else {
				throw new NotSerializableException("Incorrect buffer type (StringReader or ByteBuffer)");
			}
			ExtensibleValueSerializationFinished.newCase(this, anObject, anOutputBuffer, visitedObjects);
		}
		else {
			throw new NotSerializableException("Object is not an Enum");
		}
		
	}

	@Override
	public Object objectFromBuffer(Object anInputBuffer, Class aClass, ArrayList<Object> retrievedObjects)
			throws StreamCorruptedException, NotSerializableException {
		if(aClass == Enum.class) {
			ExtensibleBufferDeserializationInitiated.newCase(this, null, anInputBuffer, aClass);
			Object obj = null;
			retrievedObjects.add(obj); // add to retrieved objects so references to it can be evaluated
			if(anInputBuffer instanceof IndexedStringBuffer) {
				int endIndex = ((IndexedStringBuffer)anInputBuffer).indexOf(SerializerRegistry.OBJECT_DELIMITER, ((IndexedStringBuffer)anInputBuffer).getIndex());
				String enumString = ((IndexedStringBuffer)anInputBuffer).substring(((IndexedStringBuffer)anInputBuffer).getIndex(), endIndex);
				try {
					aClass = Class.forName(enumString.split(":")[0]);
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
				obj = Enum.valueOf(aClass, enumString.split(":")[1]);
				((IndexedStringBuffer)anInputBuffer).setIndex(endIndex + 1);
			}
			else if(anInputBuffer instanceof ByteBuffer) {
				int length = ((ByteBuffer)anInputBuffer).getShort();
				byte[] byteArray = new byte[length];
				((ByteBuffer)anInputBuffer).get(byteArray, 0, length);
				String enumString = new String(byteArray);
				try {
					aClass = Class.forName(enumString.split(":")[0]);
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
				obj = Enum.valueOf(aClass, enumString.split(":")[1]);
			}
			else {
				throw new NotSerializableException("Incorrect buffer type (StringReader or ByteBuffer)");
			}
			ExtensibleBufferDeserializationFinished.newCase(this, null, anInputBuffer, obj, retrievedObjects);
			return obj;
		}
		else {
			throw new NotSerializableException("Class is not a Enum");
		}
	}

}
