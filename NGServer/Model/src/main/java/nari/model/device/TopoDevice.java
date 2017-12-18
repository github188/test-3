package nari.model.device;

import nari.model.bean.TopoDef;

public interface TopoDevice {

	public static final TopoDevice NONE = new TopoDevice(){

		@Override
		public TopoDef getTopo() {
			return null;
		}

		@Override
		public void setTopo(TopoDef topo) {
			
		}

		@Override
		public void topoSearch() {
			
		}

		@Override
		public void topoCheck() {
			
		}

		@Override
		public void setTopo(Long[] nodes) {
			
		}
		
	};
	
	public TopoDef getTopo();
	
	public void setTopo(TopoDef topo);
	
	public void setTopo(Long[] nodes);
	
	public void topoSearch();
	
	public void topoCheck();
}
