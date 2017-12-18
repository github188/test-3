package nari.model.device;

import java.util.concurrent.atomic.AtomicReference;

import nari.model.device.filter.CriteriaBuilder;
import nari.model.device.filter.DefaultCriteriaBuilder;
import nari.model.device.filter.EntryManager;

public class AbstractDeviceModel {

	private static final AtomicReference<EntryManager> ref = new AtomicReference<EntryManager>();
	
	public EntryManager getEntryManager(){
		if(ref.get()==null){
			EntryManager entry = new EntryManager() {
				
				private AtomicReference<CriteriaBuilder> ref = new AtomicReference<CriteriaBuilder>();
				
				@Override
				public CriteriaBuilder getCriteriaBuilder() {
					if(ref.get()==null){
						CriteriaBuilder bd = new DefaultCriteriaBuilder();
						ref.compareAndSet(null, bd);
					}
					return ref.get();
				}
			};
			ref.compareAndSet(null, entry);
		}
		return ref.get();
	}
}
