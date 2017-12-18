package nari.model.bean;

import java.sql.SQLException;
import java.util.Arrays;

import nari.model.ModelActivator;
import nari.model.device.CNode;

public class DefaultTopoDef implements TopoDef {

	private long[] nodes = null;
	
	public DefaultTopoDef(CNode[] nodes) {
		this.nodes = new long[nodes.length];
		Arrays.sort(nodes);
		int i=0;
		for(CNode node:nodes){
			this.nodes[i++] = node.getConnectNode();
		}
	}
	
	@Override
	public int nodeCount() {
		return nodes==null?0:nodes.length;
	}

	@Override
	public long newConnectionNode() {
		long node = 0;
		try {
			node = ModelActivator.dbAdaptor.getSequence("CONF_TOPORELATION");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return node;
	}

	@Override
	public void setConnectionNode(int index, long node) {
		if(nodes!=null && index>=0 && index<nodes.length){
			nodes[index] = node;
		}else{
			throw new ArrayIndexOutOfBoundsException(index);
		}
	}

	@Override
	public long getConnectionNode(int index) {
		if(nodes!=null && index>=0 && index<nodes.length){
			return nodes[index];
		}
		throw new ArrayIndexOutOfBoundsException(index);
	}

	@Override
	public long[] nodes() {
		return nodes;
	}

	@Override
	public void setConnectionNode(long[] nodes) {
		this.nodes = nodes;
	}

}
