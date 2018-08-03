package main.serializers;

import java.io.NotSerializableException;
import java.io.StreamCorruptedException;
import java.util.ArrayList;

import util.annotations.Comp533Tags;
import util.annotations.Tags;

@Tags({Comp533Tags.VALUE_SERIALIZER})
public interface ValueSerializer {
	
	void objectToBuffer (Object anOutputBuffer, 
			Object anObject, ArrayList<Object> visitedObjects) 
	throws NotSerializableException;
	
	Object objectFromBuffer(Object anInputBuffer, 
			Class aClass, ArrayList<Object> retrievedObjects) 
	throws StreamCorruptedException, NotSerializableException;
}
