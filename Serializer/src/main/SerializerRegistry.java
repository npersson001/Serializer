package main;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Vector;

import main.serializers.ArraySerializer;
import main.serializers.BeanSerializer;
import main.serializers.BooleanSerializer;
import main.serializers.CollectionSerializer;
import main.serializers.DoubleSerializer;
import main.serializers.EnumSerializer;
import main.serializers.FloatSerializer;
import main.serializers.IntegerSerializer;
import main.serializers.ListSerializer;
import main.serializers.LongSerializer;
import main.serializers.MapSerializer;
import main.serializers.NullSerializer;
import main.serializers.ShortSerializer;
import main.serializers.StringSerializer;
import main.serializers.ValueSerializer;
import util.annotations.Comp533Tags;
import util.annotations.Tags;

@Tags({Comp533Tags.SERIALIZER_REGISTRY})
public class SerializerRegistry {
	public static final String OBJECT_DELIMITER = "#";
	public static final String CLASS_DELIMITER = "%";
	public static final String LENGTH_DELIMITER = "@";
	public static final String NULL_CLASS = "class java.lang.Null";
	public static final String REFERENCE = "referenceToObject";
	
	static DispatchingSerializer dispatchingSerializer;
	static ValueSerializer arraySerializer;
	static ValueSerializer beanSerializer;
	static ValueSerializer listPatternSerializer;
	static ValueSerializer enumSerializer;
	static ValueSerializer nullSerializer;
	static HashMap<Class, ValueSerializer> registry;
	
	// make these not static in final version, maybe use a factory or something
	static {
		registry = new HashMap<>();
		registerDispatchingSerializer(new ADispatchingSerializer());
		registerSerializer(Integer.class, new IntegerSerializer());
		registerSerializer(Boolean.class, new BooleanSerializer());
		registerSerializer(Long.class, new LongSerializer());
		registerSerializer(Short.class, new ShortSerializer());
		registerSerializer(Float.class, new FloatSerializer());
		registerSerializer(Double.class, new DoubleSerializer());
		registerSerializer(String.class, new StringSerializer());
		registerSerializer(ArrayList.class, new CollectionSerializer());
		registerSerializer(Vector.class, new CollectionSerializer());
		registerSerializer(HashSet.class, new CollectionSerializer());
		registerSerializer(Collection.class, new CollectionSerializer());
		registerSerializer(HashMap.class, new MapSerializer());
		registerSerializer(Hashtable.class, new MapSerializer());
		
		registerEnumSerializer(new EnumSerializer());
		registerNullSerializer(new NullSerializer());
		registerArraySerializer(new ArraySerializer());
		registerBeanSerializer(new BeanSerializer());
		registerListPatternSerializer(new ListSerializer());
		
	}
	
	// value serializers
	public static void registerValueSerializer (Class aClass, ValueSerializer anExternalSerializer) {
		registry.put(aClass, anExternalSerializer);
	}
	
	public static ValueSerializer getValueSerializer(Class aClass) {
		return registry.get(aClass);
	}
	
	public static void registerSerializer(Class aClass, ValueSerializer anExternalSerializer) {
		registry.put(aClass, anExternalSerializer);
	}
	
	// dispatching serializer
	public static void registerDispatchingSerializer (DispatchingSerializer anExternalSerializer) {
		dispatchingSerializer = anExternalSerializer;
	}
	
	public static DispatchingSerializer getDispatchingSerializer() {
		return dispatchingSerializer;
	}
	
	// enum serializer
	public static void registerEnumSerializer (ValueSerializer anExternalSerializer) {
		enumSerializer = anExternalSerializer;
	}
	
	public static ValueSerializer getEnumSerializer() {
		return enumSerializer;
	}
	
	// null serializer
	public static void registerNullSerializer (ValueSerializer anExternalSerializer) {
		nullSerializer = anExternalSerializer;
	}
	
	public static ValueSerializer getNullSerializer() {
		return nullSerializer;
	}
	
	// array serializer
	public static void registerArraySerializer (ValueSerializer anExternalSerializer) {
		arraySerializer = anExternalSerializer;
	}
	
	public static ValueSerializer getArraySerializer() {
		return arraySerializer;
	}
	
	// bean serializer 
	public static void registerBeanSerializer (ValueSerializer anExternalSerializer) {
		beanSerializer = anExternalSerializer;
	}
	
	public static ValueSerializer getBeanSerializer() {
		return beanSerializer;
	}
	
	// list serializer 
	public static void registerListPatternSerializer (ValueSerializer anExternalSerializer) {
		listPatternSerializer = anExternalSerializer;
	}
	
	public static ValueSerializer getListPatternSerializer() {
		return listPatternSerializer;
	}
	
}
