package demoSimulation;

import assignments.util.MiscAssignmentUtils;
import assignments.util.mainArgs.ClientArgsProcessor;
import examples.gipc.counter.customization.ATracingFactorySetter;
import examples.gipc.counter.customization.FactorySetterFactory;
import examples.gipc.counter.layers.AMultiLayerCounterClient;
import examples.gipc.counter.layers.AMultiLayerCounterClient1;
import main.ANilsTracingFactorySetter;
import main.client.AClient;
import part1ServersAndClients.Part1Client1;
import part1ServersAndClients.Part1TracingFactorySetter;
import part2ServersAndClients.Part2Client1;
import part2ServersAndClients.Part2TracingFactorySetter;
import util.annotations.Comp533Tags;
import util.annotations.Tags;
import util.trace.port.objects.ObjectTraceUtility;
import util.trace.port.rpc.RPCTraceUtility;
import util.trace.port.serialization.extensible.ExtensibleSerializationTraceUtility;

@Tags({Comp533Tags.CUSTOM_SERIALIZER})
public class ClientLauncher implements Launcher{
	public static void main (String[] args) {
//		util.trace.port.objects.ObjectTraceUtility.setTracing();
//		util.trace.port.rpc.RPCTraceUtility.setTracing();
//		ExtensibleSerializationTraceUtility.setTracing();
		assignments.util.A4TraceUtility.setTracing();
		ObjectTraceUtility.setTracing();
		RPCTraceUtility.setTracing();
		FactorySetterFactory.setSingleton(new A6TracingFactorySetter());
		MiscAssignmentUtils.setHeadless(true);
		String clientName = CLIENT_NAME + System.currentTimeMillis();
		AClient.launchClient(SERVER_HOST, SERVER_PORT, clientName, SERVER_HOST, RMI_PORT, GIPC_PORT);
	}

}
