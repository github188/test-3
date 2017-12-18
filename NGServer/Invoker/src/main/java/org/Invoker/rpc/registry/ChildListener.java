package org.Invoker.rpc.registry;

import java.util.List;

public interface ChildListener {

	public void childChanged(String path, List<String> children);
}
