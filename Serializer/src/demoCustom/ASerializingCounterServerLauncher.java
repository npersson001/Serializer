package demoCustom;

import examples.gipc.counter.customization.ACustomCounterServer;
import examples.gipc.counter.customization.FactorySetterFactory;
import util.annotations.Comp533Tags;
import util.annotations.Tags;
import util.trace.port.objects.ObjectTraceUtility;
import util.trace.port.serialization.extensible.ExtensibleSerializationTraceUtility;

@Tags({Comp533Tags.CUSTOM_SERIALIZER_SERVER})
public class ASerializingCounterServerLauncher {
	public static void main (String[] args) {
//		ExtensibleSerializationTraceUtility.setTracing();
		assignments.util.A4TraceUtility.setTracing();
		ObjectTraceUtility.setTracing();
		FactorySetterFactory.setSingleton(new ASerializingFactorySetter());
		ACustomCounterServer.launch();
	}

}
