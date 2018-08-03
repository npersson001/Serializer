package demoCustom;

import examples.gipc.counter.customization.ACustomCounterClient;
import examples.gipc.counter.customization.FactorySetterFactory;
import examples.gipc.counter.layers.AMultiLayerCounterClient;
import examples.gipc.counter.layers.AMultiLayerCounterClient1;
import examples.gipc.counter.layers.AMultiLayerCounterClient2;
import util.annotations.Comp533Tags;
import util.annotations.Tags;
import util.trace.port.objects.ObjectTraceUtility;
import util.trace.port.serialization.extensible.ExtensibleSerializationTraceUtility;

@Tags({Comp533Tags.CUSTOM_SERIALIZER_CLIENT2})
public class ASerializingCounterClient2Launcher extends AMultiLayerCounterClient2 {
	public static void main (String[] args) {
//		ExtensibleSerializationTraceUtility.setTracing();
		assignments.util.A4TraceUtility.setTracing();
		ObjectTraceUtility.setTracing();
		FactorySetterFactory.setSingleton(new ASerializingFactorySetter());
		ACustomCounterClient.launch(CLIENT2_NAME);
	}

}
