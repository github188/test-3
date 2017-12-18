package nari.SpatialIndex.searcher;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import nari.SpatialIndex.handler.Notifier;
import nari.SpatialIndex.handler.NotifyEvent;
import nari.SpatialIndex.index.Indexer;

public class NotifierManager {

	private static final AtomicReference<NotifierManager> ref = new AtomicReference<NotifierManager>();
	
	private final EnumMap<NotifyEvent, List<Notifier>> INSTALL_HOOKS = new EnumMap<NotifyEvent, List<Notifier>>(NotifyEvent.class);
	
	private final EnumMap<NotifyEvent, List<Notifier>> INIT_HOOKS = new EnumMap<NotifyEvent, List<Notifier>>(NotifyEvent.class);
	
	private final EnumMap<NotifyEvent, List<Notifier>> INDEX_MODIFY_HOOKS = new EnumMap<NotifyEvent, List<Notifier>>(NotifyEvent.class);
	
	private final EnumMap<NotifyEvent, List<Notifier>> INDEX_REMOVE_HOOKS = new EnumMap<NotifyEvent, List<Notifier>>(NotifyEvent.class);
	
	private final EnumMap<NotifyEvent, List<Notifier>> INDEX_ADD_HOOKS = new EnumMap<NotifyEvent, List<Notifier>>(NotifyEvent.class);
	
	public static NotifierManager get(){
		if(ref.get()==null){
			NotifierManager manager = new NotifierManager();
			ref.compareAndSet(null, manager);
		}
		return ref.get();
	}
	
	public void registHook(NotifyEvent event,Notifier hook){
		switch (event) {
			case INSTALL:
				if(INSTALL_HOOKS.containsKey(event)){
					INSTALL_HOOKS.get(event).add(hook);
				}else{
					List<Notifier> list = new ArrayList<Notifier>();
					list.add(hook);
					INSTALL_HOOKS.put(event, list);
				}
				break;
			case INIT:
				if(INIT_HOOKS.containsKey(event)){
					INIT_HOOKS.get(event).add(hook);
				}else{
					List<Notifier> list = new ArrayList<Notifier>();
					list.add(hook);
					INIT_HOOKS.put(event, list);
				}
				break;
			case INDEX_ADD:
				if(INDEX_ADD_HOOKS.containsKey(event)){
					INDEX_ADD_HOOKS.get(event).add(hook);
				}else{
					List<Notifier> list = new ArrayList<Notifier>();
					list.add(hook);
					INDEX_ADD_HOOKS.put(event, list);
				}
				break;
			case INDEX_MODIFY:
				if(INDEX_MODIFY_HOOKS.containsKey(event)){
					INDEX_MODIFY_HOOKS.get(event).add(hook);
				}else{
					List<Notifier> list = new ArrayList<Notifier>();
					list.add(hook);
					INDEX_MODIFY_HOOKS.put(event, list);
				}
				break;
			case INDEX_REMOVE:
				if(INDEX_REMOVE_HOOKS.containsKey(event)){
					INDEX_REMOVE_HOOKS.get(event).add(hook);
				}else{
					List<Notifier> list = new ArrayList<Notifier>();
					list.add(hook);
					INDEX_REMOVE_HOOKS.put(event, list);
				}
				break;

			default:
				break;
		}
	}
	
	public void unregistHook(NotifyEvent event,Notifier hook){
		switch (event) {
			case INSTALL:
				INSTALL_HOOKS.get(event).remove(hook);
				break;
			case INIT:
				INIT_HOOKS.get(event).remove(hook);
				break;
			case INDEX_ADD:
				INDEX_ADD_HOOKS.get(event).remove(hook);
				break;
			case INDEX_MODIFY:
				INDEX_MODIFY_HOOKS.get(event).remove(hook);
				break;
			case INDEX_REMOVE:
				INDEX_REMOVE_HOOKS.get(event).remove(hook);
				break;
	
			default:
				break;
		}
	}
	
	public void notify(NotifyEvent event,Indexer index) throws Exception{
		List<Notifier> list = null;
		switch (event) {
			case INSTALL:
				list = INSTALL_HOOKS.get(event);
				if(list!=null && list.size()>=0){
					for(Notifier nt:list){
						nt.notify(event, index);
					}
				}
				break;
			case INIT:
				list = INIT_HOOKS.get(event);
				if(list!=null && list.size()>=0){
					for(Notifier nt:list){
						nt.notify(event, index);
					}
				}
				break;
			case INDEX_ADD:
				list = INDEX_ADD_HOOKS.get(event);
				if(list!=null && list.size()>=0){
					for(Notifier nt:list){
						nt.notify(event, index);
					}
				}
				break;
			case INDEX_MODIFY:
				list = INDEX_MODIFY_HOOKS.get(event);
				if(list!=null && list.size()>=0){
					for(Notifier nt:list){
						nt.notify(event, index);
					}
				}
				break;
			case INDEX_REMOVE:
				list = INDEX_REMOVE_HOOKS.get(event);
				if(list!=null && list.size()>=0){
					for(Notifier nt:list){
						nt.notify(event, index);
					}
				}
				break;
	
			default:
				break;
		}
	}
}
