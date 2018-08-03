package main;

import inputport.nio.manager.factories.classes.AConnectCommandFactory;
import serialization.Serializer;
import serialization.SerializerFactory;
import util.annotations.Comp533Tags;
import util.annotations.Tags;
import util.trace.port.serialization.extensible.ExtensibleSerializationTraceUtility;

@Tags({Comp533Tags.LOGICAL_TEXTUAL_SERIALIZER_FACTORY})
public class LogicalTextualSerializerFactory implements SerializerFactory{
	LogicalTextualSerializer serializer;
	
	public Serializer createSerializer() {
		ExtensibleSerializationTraceUtility.setTracing();
		if(serializer == null) {
			serializer =  new LogicalTextualSerializer();
		}
		return serializer;
	}
}
