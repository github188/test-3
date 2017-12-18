package org.Invoker.compiler;

import org.Invoker.rpc.Adaptive;
import org.Invoker.rpc.SPI;

@SPI("javassist")
public interface Compiler {

	@Adaptive
	public Class<?> compile(String code, ClassLoader classLoader);
}
