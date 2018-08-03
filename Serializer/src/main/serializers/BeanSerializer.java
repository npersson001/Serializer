package main.serializers;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.NotSerializableException;
import java.io.Serializable;
import java.io.StreamCorruptedException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import main.IndexedStringBuffer;
import main.SerializerRegistry;
import util.annotations.Comp533Tags;
import util.annotations.Tags;
import util.misc.RemoteReflectionUtility;
import util.trace.port.serialization.extensible.ExtensibleBufferDeserializationFinished;
import util.trace.port.serialization.extensible.ExtensibleBufferDeserializationInitiated;
import util.trace.port.serialization.extensible.ExtensibleValueSerializationFinished;
import util.trace.port.serialization.extensible.ExtensibleValueSerializationInitiated;

@Tags({Comp533Tags.BEAN_SERIALIZER})
public class BeanSerializer implements ValueSerializer {

	@Override
	public void objectToBuffer(Object anOutputBuffer, Object anObject, ArrayList<Object> visitedObjects)
			throws NotSerializableException {
		if(anObject instanceof Serializable) {
			ExtensibleValueSerializationInitiated.newCase(this, anObject, anOutputBuffer);
			visitedObjects.add(anObject);
			try {
				BeanInfo beanInfo = Introspector.getBeanInfo(anObject.getClass());
				PropertyDescriptor[] properties = beanInfo.getPropertyDescriptors();
				if(anOutputBuffer instanceof IndexedStringBuffer) {
					((IndexedStringBuffer)anOutputBuffer).append(Class.class.toString() + SerializerRegistry.CLASS_DELIMITER + 
							anObject.getClass().getName() + SerializerRegistry.OBJECT_DELIMITER);
				}
				else if(anOutputBuffer instanceof ByteBuffer) {
					((ByteBuffer)anOutputBuffer).put((Class.class.toString() + SerializerRegistry.CLASS_DELIMITER).getBytes());
					((ByteBuffer)anOutputBuffer).putShort((short)anObject.getClass().getName().length());
					((ByteBuffer)anOutputBuffer).put(anObject.getClass().getName().getBytes());
				}
				else {
					throw new NotSerializableException("Incorrect buffer type (StringReader or ByteBuffer)");
				}
				for(PropertyDescriptor property : properties) {
					if(property.getReadMethod() != null && property.getWriteMethod() != null 
							&& !RemoteReflectionUtility.isTransient(property.getReadMethod())) { // excludes the class itself
						SerializerRegistry.getDispatchingSerializer().objectToBuffer(anOutputBuffer, property.getReadMethod().invoke(anObject, null), visitedObjects);
					}
				}
			} catch (IntrospectionException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				e.printStackTrace();
			}
			RemoteReflectionUtility.invokeInitSerializedObject(anObject);
			ExtensibleValueSerializationFinished.newCase(this, anObject, anOutputBuffer, visitedObjects);
		}
		else {
			throw new NotSerializableException("Class is not a Bean");
		}
	}

	@Override
	public Object objectFromBuffer(Object anInputBuffer, Class aClass, ArrayList<Object> retrievedObjects)
			throws StreamCorruptedException, NotSerializableException {
		if(aClass == Class.class) {
			ExtensibleBufferDeserializationInitiated.newCase(this, null, anInputBuffer, aClass);
			String className = null;
			if(anInputBuffer instanceof IndexedStringBuffer) {
				int endIndex = ((IndexedStringBuffer)anInputBuffer).indexOf(SerializerRegistry.OBJECT_DELIMITER, ((IndexedStringBuffer)anInputBuffer).getIndex());
				className = ((IndexedStringBuffer)anInputBuffer).substring(((IndexedStringBuffer)anInputBuffer).getIndex(), endIndex);
				((IndexedStringBuffer)anInputBuffer).setIndex(endIndex + 1);
			}
			else if(anInputBuffer instanceof ByteBuffer) {
				int length = ((ByteBuffer)anInputBuffer).getShort();
				byte[] byteArray = new byte[length];
				((ByteBuffer)anInputBuffer).get(byteArray, 0, length);
				className = new String(byteArray);
			}
			else {
				throw new NotSerializableException("Incorrect buffer type (StringReader or ByteBuffer)");
			}
			Object obj = null;
			try {
				Class<?> specificClass = Class.forName(className);
				PropertyDescriptor[] properties = null;
				BeanInfo beanInfo = Introspector.getBeanInfo(specificClass);
				properties = beanInfo.getPropertyDescriptors();
				obj = specificClass.newInstance();
				retrievedObjects.add(obj); // add to retrieved objects so references to it can be evaluated
				for(PropertyDescriptor property : properties) {
					if(property.getWriteMethod() != null && property.getReadMethod() != null && 
							!RemoteReflectionUtility.isTransient(property.getReadMethod())){
						Object propObj = SerializerRegistry.getDispatchingSerializer().objectFromBuffer(anInputBuffer, retrievedObjects);
						property.getWriteMethod().invoke(obj, propObj);
					}
				}
			} catch (ClassNotFoundException | IntrospectionException | IllegalAccessException 
					| IllegalArgumentException | InvocationTargetException | InstantiationException e) {
				e.printStackTrace();
			}
			ExtensibleBufferDeserializationFinished.newCase(this, null, anInputBuffer, obj, retrievedObjects);
			return obj;
		}
		else {
			throw new NotSerializableException("Class is not a Bean");
		}
	}

}
