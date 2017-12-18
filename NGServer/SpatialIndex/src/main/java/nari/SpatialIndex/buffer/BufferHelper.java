package nari.SpatialIndex.buffer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.MappedByteBuffer;
import java.security.AccessController;
import java.security.PrivilegedAction;

public class BufferHelper {

	public static void ummap(final MappedByteBuffer buf){
		
		AccessController.doPrivileged(new PrivilegedAction<Object>() {

			@Override
			public Object run() {
				
				try {
					Method m = buf.getClass().getMethod("cleaner", new Class[0]);
					m.setAccessible(true);
					sun.misc.Cleaner cleaner = (sun.misc.Cleaner)m.invoke(buf, new Object[0]);
					cleaner.clean();
					
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
				return null;
			}
		});
	}
}
