package main;

import java.io.NotSerializableException;
import java.io.StreamCorruptedException;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import serialization.Serializer;
import util.annotations.Comp533Tags;
import util.annotations.Tags;

@Tags({Comp533Tags.LOGICAL_TEXTUAL_SERIALIZER})
public class LogicalTextualSerializer implements Serializer{

	public ByteBuffer outputBufferFromObject(Object object) {
		IndexedStringBuffer buffer = new IndexedStringBuffer();
		ArrayList<Object> visited = new ArrayList<>();
		try {
			SerializerRegistry.getDispatchingSerializer().objectToBuffer(buffer, object, visited);
		} catch (NotSerializableException e) {
			e.printStackTrace();
		}
		return ByteBuffer.wrap(buffer.toString().getBytes());
	}

	public Object objectFromInputBuffer(ByteBuffer inputBuffer) {
		byte[] byteArray = new byte[inputBuffer.remaining()];
		inputBuffer.mark();
		inputBuffer.get(byteArray);
		IndexedStringBuffer buffer = new IndexedStringBuffer(new String(byteArray));
		try {
			Object object = SerializerRegistry.getDispatchingSerializer().objectFromBuffer(buffer, new ArrayList());
			int index = byteArray.length - buffer.length();
			inputBuffer.reset();
			inputBuffer.get(new byte[index]);
			inputBuffer.flip();
			return object;
		} catch (StreamCorruptedException e) {
//			e.printStackTrace();
			inputBuffer.reset();
			return null;
		} catch (NotSerializableException e) {
			inputBuffer.reset();
//			e.printStackTrace();
			return null;
		} catch (Exception e) {
			inputBuffer.reset();
			return null;
		}
//		return null;
	}

}
