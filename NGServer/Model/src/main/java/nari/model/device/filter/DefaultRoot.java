package nari.model.device.filter;

import java.util.concurrent.atomic.AtomicReference;

public class DefaultRoot implements Root {

	private static final AtomicReference<Root> ref = new AtomicReference<Root>();
	
	@Override
	public Selection select(final String name) {
		return new Selection() {
			
			@Override
			public boolean isCompoundSelection() {
				return true;
			}
			
			@Override
			public String getSelection() {
				return name;
			}
			
			@Override
			public Selection alias(String paramString) {
				return null;
			}
		};
	}

	@Override
	public Field get(String name, Class<?> type) {
		Field field = new DefaultField(name,type,new FieldProcessor() {

			@Override
			public Object convert(Object value) {
				return "'"+value+"'";
			}
			
		});
		return field;
	}

	@Override
	public Field get(String name, Class<?> type, FieldProcessor processor) {
		return new DefaultField(name,type,processor);
	}

	public static Root getRoot(){
		if(ref.get()==null){
			ref.compareAndSet(null, new DefaultRoot());
		}
		return ref.get();
	}
}
