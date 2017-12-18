package nari.MemCache;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashSet;

import nari.Logger.LoggerManager;

import sun.misc.Unsafe;

@SuppressWarnings("restriction")
public class UnsafeMemAllocater extends AbstractMemAllocater {

	private nari.Logger.Logger logger = LoggerManager.getLogger(this.getClass());
	
	private Unsafe unsafe;
	
	public UnsafeMemAllocater() {
		unsafe = UnsafeManager.getUnsafe();
	}
	
	@Override
	protected long allocateDirectMem(long size) throws Exception {
		return unsafe.allocateMemory(size);
	}

	@Override
	protected void freeDirectMem(long address) throws Exception {
		unsafe.freeMemory(address);
	}

	@Override
	protected long getObjectSize(Object obj) throws Exception {
		HashSet<Field> fields = new HashSet<Field>();
		
		Class<?> c = obj.getClass();
		while(c!=Object.class){
			for(Field f:c.getDeclaredFields()){
				if((f.getModifiers()&Modifier.STATIC)==0){
					fields.add(f);
				}
			}
			
			c = c.getSuperclass();
		}
		long maxSize = 0;
		for(Field f:fields){
			long offset = unsafe.objectFieldOffset(f);
			if(offset>maxSize){
				maxSize = offset;
			}
		}
		return ((maxSize/8)+1)*8;
	}
	
//	private long normalize(int value){
//		if(value>=0){
//			return value;
//		}
//		return (~0L>>>32)&value;
//	}
	
	@Override
	public Pointer doPutCache(Object cacheObject, long address) throws Exception {
		long size = sizeOf(cacheObject);
		long start = toAddress(cacheObject);
		
		unsafe.copyMemory(start, address, size);
		
		Pointer ptr = new MemPointer(address,size,this);
		return ptr;
	}

	@Override
	public Object doGetCache(long address) throws Exception {
		return fromAddress(address);
	}
	
	private long toAddress(Object obj){
		Object[] array = new Object[]{obj};
		long baseOffset = unsafe.arrayBaseOffset(Object[].class);
//		return normalize(unsafe.getInt(array, baseOffset));
		return unsafe.getLong(array, baseOffset);
//		return unsafe.getInt(obj.getClass(), 84L);
//		return unsafe.getLong(obj, 8L);
	}
	
	private Object fromAddress(long address){
		Object[] array = new Object[]{null};
		long baseOffset = unsafe.arrayBaseOffset(Object[].class);
		unsafe.putLong(array, baseOffset, address);
//		unsafe.putLong(array, 8L, address);
		return array[0];
	}

	@Override
	protected long getObjectOffset(Object obj, String field) throws Exception {
		Field f = obj.getClass().getDeclaredField(field);
		long offset = unsafe.objectFieldOffset(f);
//		long offset = unsafe.getInt(obj, 8L);
		return offset;
	}

	@Override
	public boolean getBooleanVolatile(Object obj, long offset) throws Exception {
		return unsafe.getBoolean(obj, offset);
	}
	
	@Override
	public byte getByteVolatile(Object obj, long offset) throws Exception {
		return unsafe.getByte(obj, offset);
	}
	
	@Override
	public char getCharVolatile(Object obj, long offset) throws Exception {
		return unsafe.getChar(obj, offset);
	}
	
	@Override
	public double getDoubleVolatile(Object obj, long offset) throws Exception {
		return unsafe.getDouble(obj, offset);
	}
	
	@Override
	public float getFloatVolatile(Object obj, long offset) throws Exception {
		return unsafe.getFloat(obj, offset);
	}
	
	@Override
	public int getIntVolatile(Object obj, long offset) throws Exception {
		return unsafe.getInt(obj, offset);
	}
	
	@Override
	public long getLongVolatile(Object obj, long offset) throws Exception {
		return unsafe.getLong(obj, offset);
	}
	
	@Override
	public short getShortVolatile(Object obj, long offset) throws Exception {
		return unsafe.getShort(obj, offset);
	}
	
	@Override
	public Object getObjectVolatile(Object obj, long offset) throws Exception {
		return unsafe.getObject(obj, offset);
	}
	
	@Override
	public void putBooleanVolatile(Object obj, long offset, boolean value) throws Exception {
		unsafe.putBoolean(obj, offset, value);
	}
	
	@Override
	public void putByteVolatile(Object obj, long offset, byte value) throws Exception {
		unsafe.putByte(obj, offset, value);
	}
	
	@Override
	public void putCharVolatile(Object obj, long offset, char value) throws Exception {
		unsafe.putChar(obj, offset, value);
	}
	
	@Override
	public void putDoubleVolatile(Object obj, long offset, double value) throws Exception {
		unsafe.putDouble(obj, offset, value);
	}
	
	@Override
	public void putFloatVolatile(Object obj, long offset, float value) throws Exception {
		unsafe.putFloat(obj, offset, value);
	}
	
	@Override
	public void putIntVolatile(Object obj, long offset, int value) throws Exception {
		unsafe.putInt(obj, offset, value);
	}
	
	@Override
	public void putLongVolatile(Object obj, long offset, long value)throws Exception {
		unsafe.putLong(obj, offset, value);
	}
	
	@Override
	public void putShortVolatile(Object obj, long offset, short value) throws Exception {
		unsafe.putShort(obj, offset, value);
	}
	
	@Override
	public void putObjectVolatile(Object obj, long offset, Object value) throws Exception {
		unsafe.putObject(obj, offset, value);
	}

	@Override
	public void putByte(long address, byte value) throws Exception {
		unsafe.putByte(address, value);
	}

	@Override
	public byte getByte(long address) throws Exception {
		return unsafe.getByte(address);
	}

	@Override
	protected long getObjectOffset(Object obj, Field field) throws Exception {
		long offset = unsafe.objectFieldOffset(field);
		return offset;
	}
	
//	long shallowCopy(Object obj){
//		long off = 0;
//		long address = getUnsafe().allocateMemory(100);
//		
//		Cat cat2 = new Cat(2,"cat2",null);
//		
//		Cat cat1 = new Cat(1,"cat1",cat2);
//		String cat1 = "建林023-建林040";
//		long size1 = sizeOf(cat1);
//		
//		long start1 = toAddress(cat1);
//		
//		getUnsafe().copyMemory(start1, address, size1);
//		Cat nCat1 = (Cat)fromAddress(address+off);
//		logger.info(nCat1+":"+nCat1.getA()+","+nCat1.getN()+","+Cat.st+","+Arrays.toString(nCat1.getArray()));
//		off = off + size1;
		
//		Cat cat2 = new Cat(2,"cat2");
//		long size2 = sizeOf(cat2);
//		
//		long start2 = toAddress(cat2);
//		
//		getUnsafe().copyMemory(start2, address+off, size2);
//		Cat nCat2 = (Cat)fromAddress(address+off);
//		logger.info(nCat2+":"+nCat2.getA()+","+nCat2.getN()+","+Cat.st+","+Arrays.toString(nCat2.getArray()));
//		off = off + size2;
//		
//		Cat cat3 = new Cat(3,"cat3");
//		
//		long size3 = sizeOf(cat3);
//		
//		long start3 = toAddress(cat3);
//		
//		getUnsafe().copyMemory(start3, address+off, size3);
//		Cat nCat3 = (Cat)fromAddress(address+off);
//		logger.info(nCat3+":"+nCat3.getA()+","+nCat3.getN()+","+Cat.st+","+Arrays.toString(nCat3.getArray()));
//		getUnsafe().freeMemory(address);
//		return address;
//	}

	public static void main(String[] args) {
//		Cat cat = new Cat(4);
		
//		Unsafe unsafe = UnsafeManager.getUnsafe();
//		
//		Object[] array = new Object[]{cat};
//		long baseOffset = unsafe.arrayBaseOffset(Object[].class);
//		logger.info(baseOffset);
//		
//		long a1 = unsafe.getInt(array, baseOffset);
//		long a4 = unsafe.getLong(array, baseOffset);
//		
//		long a2 = unsafe.getLong(cat, 8L);
//		long a3 = unsafe.getLong(Cat.class, 160L);
//		
//		logger.info(a1);
//		logger.info(a4);
//		logger.info(a2);
//		logger.info(a3);
//
//		Object[] arr = new Object[]{null};
//		unsafe.putLong(arr, baseOffset, a4);
//		logger.info(((Cat)arr[0]).s);
		
//		try {
//			UnsafeMemAllocater a = new UnsafeMemAllocater();
//			logger.info(a.getObjectSize(cat));
//			
//			long add = a.toAddress(cat);
//			logger.info(add);
//			
//			Cat c = (Cat)a.fromAddress(add);
//			logger.info(c==null?"null":c.s);
//			
//			logger.info(UnsafeManager.getUnsafe().ADDRESS_SIZE);
//		} catch (Exception e) {
//			
//		}
		
	}
}
