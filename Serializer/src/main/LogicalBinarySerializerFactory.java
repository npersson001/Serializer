package main;

import serialization.Serializer;
import serialization.SerializerFactory;
import util.annotations.Comp533Tags;
import util.annotations.Tags;
import util.trace.port.serialization.extensible.ExtensibleSerializationTraceUtility;

@Tags({Comp533Tags.LOGICAL_BINARY_SERIALIZER_FACTORY})
public class LogicalBinarySerializerFactory implements SerializerFactory{
	LogicalBinarySerializer serializer;
	
	public Serializer createSerializer() {
		ExtensibleSerializationTraceUtility.setTracing();
		if(serializer == null) {
			serializer =  new LogicalBinarySerializer();
		}
		return serializer;
	}
}
