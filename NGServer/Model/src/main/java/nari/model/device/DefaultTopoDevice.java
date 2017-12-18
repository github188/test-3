package nari.model.device;

import java.sql.Blob;
import java.util.concurrent.atomic.AtomicReference;

import nari.model.bean.DefaultTopoDef;
import nari.model.bean.TopoDef;

public class DefaultTopoDevice implements TopoDevice {

	private final AtomicReference<TopoDef> ref = new AtomicReference<TopoDef>();
	
	private Device device = null;
	
	public DefaultTopoDevice(Device device){
		this.device = device;
	}
	
	@Override
	public TopoDef getTopo() {
		if(ref.get()==null){
			Object obj = device.getValue("connection");
			if(obj==null){
				return TopoDef.NONE;
			}
			CNode[] nodes = TopoConnectManager.readConnectNode((Blob)obj);
			
			TopoDef def = new DefaultTopoDef(nodes);
			
			ref.compareAndSet(null, def);
		}
		
		return ref.get();
	}

	@Override
	public void setTopo(TopoDef topo) {
		ref.set(topo);
		Long[] l = new Long[topo.nodeCount()];
		int i=0;
		for(long ll:topo.nodes()){
			l[i++] = ll;
		}
		byte[] value = TopoConnectManager.createConnectNode(l);
		device.setValue("connection", value);
	}

	@Override
	public void topoSearch() {
		
	}

	@Override
	public void topoCheck() {
		
	}

	@Override
	public void setTopo(Long[] nodes) {
		CNode[] c = new CNode[nodes.length];
		int i=0;
		for(Long n:nodes){
			c[i++] = new CNode(i+1,n);
		}
		
		TopoDef topo = new DefaultTopoDef(c);
		ref.set(topo);
		byte[] value = TopoConnectManager.createConnectNode(nodes);
		device.setValue("connection", value);
	}

}
