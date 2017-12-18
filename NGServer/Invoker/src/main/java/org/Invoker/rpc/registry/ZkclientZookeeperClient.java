package org.Invoker.rpc.registry;

import java.util.List;

import nari.Logger.LoggerManager;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkStateListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkNoNodeException;
import org.I0Itec.zkclient.exception.ZkNodeExistsException;
import org.Invoker.Identity;
import org.apache.zookeeper.Watcher.Event.KeeperState;

public class ZkclientZookeeperClient extends AbstractZookeeperClient<IZkChildListener> {

	private nari.Logger.Logger logger = LoggerManager.getLogger(this.getClass());
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2339643146519242912L;

	private ZkClient client;

	private volatile KeeperState state = KeeperState.SyncConnected;

	public ZkclientZookeeperClient(Identity ident,String serverString) {
		super(ident);
		try {
			client = new ZkClient(serverString,10000);
		} catch (Exception e) {
			logger.info("Zookeeper server can not connect.");
		}
		if(client!=null){
			client.subscribeStateChanges(new IZkStateListener() {
				public void handleStateChanged(KeeperState state) throws Exception {
					ZkclientZookeeperClient.this.state = state;
					if (state == KeeperState.Disconnected) {
						stateChanged(StateListener.DISCONNECTED);
					} else if (state == KeeperState.SyncConnected) {
						stateChanged(StateListener.CONNECTED);
					}
				}
				
				public void handleNewSession() throws Exception {
					stateChanged(StateListener.RECONNECTED);
				}
			});
		}
	}

	public void createPersistent(String path) {
		if(client==null || !isConnected()){
			return;
		}
		try {
			client.createPersistent(path, true);
		} catch (ZkNodeExistsException e) {
			
		}
	}

	public void createEphemeral(String path) {
		if(client==null || !isConnected()){
			return;
		}
		try {
			client.createEphemeral(path);
		} catch (ZkNodeExistsException e) {
			
		}
	}

	public void delete(String path) {
		if(client==null || !isConnected()){
			return;
		}
		try {
			client.delete(path);
		} catch (ZkNoNodeException e) {
			
		}
	}

	public List<String> getChildren(String path) {
		if(client==null || !isConnected()){
			return null;
		}
		try {
			return client.getChildren(path);
        } catch (ZkNoNodeException e) {
            return null;
        }
	}

	public boolean isConnected() {
		return state == KeeperState.SyncConnected;
	}

	public void doClose() {
		if(client==null || !isConnected()){
			return;
		}
		client.close();
	}

	public IZkChildListener createTargetChildListener(String path, final ChildListener listener) {
		return new IZkChildListener() {
			public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
				listener.childChanged(parentPath, currentChilds);
			}
		};
	}

	public List<String> addTargetChildListener(String path, final IZkChildListener listener) {
		if(client==null || !isConnected()){
			return null;
		}
		return client.subscribeChildChanges(path, listener);
	}

	public void removeTargetChildListener(String path, IZkChildListener listener) {
		if(client==null || !isConnected()){
			return;
		}
		client.unsubscribeChildChanges(path,  listener);
	}

}
