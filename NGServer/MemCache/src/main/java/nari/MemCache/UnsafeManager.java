package nari.MemCache;

import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicReference;

import sun.misc.Unsafe;

@SuppressWarnings("restriction")
public class UnsafeManager {

	private static final AtomicReference<Unsafe> ref = new AtomicReference<Unsafe>();
	
	private UnsafeManager(){
		
	}
	
	public static Unsafe getUnsafe(){
		if(ref.get()==null){
			try {
				Field field = Unsafe.class.getDeclaredField("theUnsafe");
				field.setAccessible(true);
				Unsafe unsafe = (Unsafe)field.get(null);
				ref.compareAndSet(null, unsafe);
			} catch (Exception e) {
				throw new RuntimeException("getting unsafe object error",e);
			}
		}
		return ref.get();
	}
	
}
