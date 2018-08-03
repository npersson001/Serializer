package demoSimulation;

import examples.gipc.counter.customization.ATracingFactorySetter;
import examples.gipc.counter.customization.FactorySetterFactory;
import main.ANilsTracingFactorySetter;
import main.server.AServer;
import part1ServersAndClients.Part1Server;
import part1ServersAndClients.Part1TracingFactorySetter;
import part2ServersAndClients.Part2Server;
import part2ServersAndClients.Part2TracingFactorySetter;
import util.annotations.Comp533Tags;
import util.annotations.Tags;
import util.trace.port.objects.ObjectTraceUtility;
import util.trace.port.rpc.RPCTraceUtility;
import util.trace.port.serialization.extensible.ExtensibleSerializationTraceUtility;

@Tags({Comp533Tags.CUSTOM_SERIALIZER})
public class ServerLauncher implements Launcher{
	public static void main (String[] args) {
//		util.trace.port.objects.ObjectTraceUtility.setTracing();
//		util.trace.port.rpc.RPCTraceUtility.setTracing();
//		ExtensibleSerializationTraceUtility.setTracing();
		assignments.util.A4TraceUtility.setTracing();
		ObjectTraceUtility.setTracing();
		RPCTraceUtility.setTracing();
		FactorySetterFactory.setSingleton(new A6TracingFactorySetter());
		AServer.launchServer(SERVER_PORT,RMI_PORT,"localhost",GIPC_PORT);
	}

}
