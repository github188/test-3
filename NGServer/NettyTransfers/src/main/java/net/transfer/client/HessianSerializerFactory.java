package net.transfer.client;

import net.transfer.hessian.io.SerializerFactory;

public class HessianSerializerFactory extends SerializerFactory {

	public static final SerializerFactory SERIALIZER_FACTORY = new HessianSerializerFactory();

	private HessianSerializerFactory() {
		
	}

	@Override
	public ClassLoader getClassLoader() {
		return Thread.currentThread().getContextClassLoader();
	}

}
