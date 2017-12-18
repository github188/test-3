package org.Invoker.rpc.registry;

import java.io.Serializable;
import java.util.List;

import org.Invoker.Identity;

public interface ZookeeperClient extends Serializable{

	public void create(String path, boolean ephemeral);

	public void delete(String path);

	public List<String> getChildren(String path);

	public List<String> addChildListener(String path, ChildListener listener);

	public void removeChildListener(String path, ChildListener listener);

	public void addStateListener(StateListener listener);
	
	public void removeStateListener(StateListener listener);

	public boolean isConnected();

	public void close();

	public Identity getIdentity();
}
