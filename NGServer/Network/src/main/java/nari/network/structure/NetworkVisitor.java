package nari.network.structure;

import nari.graph.structure.GraphVisitor;
import nari.network.device.TopoDevice;

public interface NetworkVisitor {
	
	int PASS_AND_CONTINUE = GraphVisitor.PASS_AND_CONTINUE;
    int PASS_AND_STOP = GraphVisitor.PASS_AND_STOP;
    int FAIL_QUERY = GraphVisitor.FAIL_QUERY;
    
    int visit(TopoDevice device);

}
