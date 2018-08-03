package main;

import java.io.NotSerializableException;
import java.io.StreamCorruptedException;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import serialization.Serializer;
import util.annotations.Comp533Tags;
import util.annotations.Tags;

@Tags({Comp533Tags.LOGICAL_BINARY_SERIALIZER})
public class LogicalBinarySerializer implements Serializer{

	@Override
	public ByteBuffer outputBufferFromObject(Object object) {
		byte[] byteArray = new byte[1000000];
		ByteBuffer buffer = ByteBuffer.wrap(byteArray);
		try {
			SerializerRegistry.getDispatchingSerializer().objectToBuffer(buffer, object, new ArrayList<Object>());
		} catch (NotSerializableException e) {
			e.printStackTrace();
		}
		buffer.flip();
		return buffer;
	}

	@Override
	public Object objectFromInputBuffer(ByteBuffer inputBuffer) {
		Object obj = null;
		inputBuffer.mark();
		try {
			obj = SerializerRegistry.getDispatchingSerializer().objectFromBuffer(inputBuffer, new ArrayList<Object>());
			inputBuffer.flip();
			return obj;
		} catch (StreamCorruptedException e) {
			inputBuffer.reset();
			e.printStackTrace();
			return null;
		} catch (NotSerializableException e) {
			inputBuffer.reset();
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			inputBuffer.reset();
			return null;
		}
	}

}
