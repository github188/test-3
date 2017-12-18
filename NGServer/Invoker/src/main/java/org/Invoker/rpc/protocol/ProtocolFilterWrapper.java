package org.Invoker.rpc.protocol;

import java.util.List;

import org.Invoker.Identity;
import org.Invoker.rpc.exception.InvokerException;
import org.Invoker.rpc.exporter.Exporter;
import org.Invoker.rpc.extension.ExtensionLoader;
import org.Invoker.rpc.filter.Filter;
import org.Invoker.rpc.invoker.Invocation;
import org.Invoker.rpc.invoker.Invoker;
import org.Invoker.rpc.result.Result;

public class ProtocolFilterWrapper implements Protocol {

	private Protocol protocol = null;
	
	public ProtocolFilterWrapper(Protocol protocol){
		this.protocol = protocol;
	}
	
	public <T> Exporter<T> export(Invoker<T> invoker) throws InvokerException {
		return protocol.export(buildInvokerChain(invoker,"provider"));
	}

	public <T> Invoker<T> refer(Class<T> type, Identity url) throws InvokerException {
		return buildInvokerChain(protocol.refer(type, url),"custorm");
	}

	private <T> Invoker<T> buildInvokerChain(final Invoker<T> invoker,String group){
		List<Filter> filters = ExtensionLoader.getExtensionLoader(Filter.class).getActivateExtension(group);
		
		Invoker<T> last = invoker;
		for(int i = filters.size()-1; i>=0; i--){
			final Filter filter = filters.get(i);
			final Invoker<T> next = last;
			last = new Invoker<T>() {

				private static final long serialVersionUID = -1355572607743726519L;

				public void destroy() {
					invoker.destroy();
				}

				public Class<T> getInterface() {
					return invoker.getInterface();
				}

				public Result invoke(Invocation inv) throws InvokerException {
					return filter.invoke(next, inv);
				}

				@Override
				public Identity getIdentity() {
					return invoker.getIdentity();
				}
			};
		}
		return last;
	}
}
