package nari.SpatialIndex.buffer;

import java.nio.channels.FileChannel;
import java.util.concurrent.ConcurrentHashMap;

public class ChannelHelper {

	private static final ConcurrentHashMap<String,FileChannel> map = new ConcurrentHashMap<String,FileChannel>();
	
	public static Object CHANNELLOCK = new Object();
	
	public static FileChannel getChannel(String key){
		return map.get(key);
	}
	
	public static void setChannel(String key,FileChannel channel){
		map.putIfAbsent(key, channel);
	}
}
