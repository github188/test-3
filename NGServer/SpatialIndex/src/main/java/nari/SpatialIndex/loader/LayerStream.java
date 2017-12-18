package nari.SpatialIndex.loader;

import java.util.ArrayList;
import java.util.List;

import nari.SpatialIndex.buffer.ByteHelper;
import nari.SpatialIndex.searcher.SubIndexLayer;

public class LayerStream {

	public static byte[] create(SubIndexLayer[] layers) throws Exception {
		
		List<byte[]> list = new ArrayList<byte[]>();
		for(SubIndexLayer layer:layers){
			int index = 0;
			String layerId = layer.getSubLayerID();
			byte[] b = layerId.getBytes("utf-8");
			
			int blockSize = 44+b.length;
			byte[] block = new byte[blockSize];
			
			byte[] bytes = ByteHelper.toByte(blockSize);
			System.arraycopy(bytes, 0, block, index, bytes.length);
			index +=bytes.length;
			
			bytes =ByteHelper.toByte(layer.getXMin());
			System.arraycopy(bytes, 0, block, index, bytes.length);
			index +=bytes.length;
			
			bytes =ByteHelper.toByte(layer.getYMin());
			System.arraycopy(bytes, 0, block, index, bytes.length);
			index +=bytes.length;
			
			bytes =ByteHelper.toByte(layer.getXMax());
			System.arraycopy(bytes, 0, block, index, bytes.length);
			index +=bytes.length;
			
			bytes =ByteHelper.toByte(layer.getYMax());
			System.arraycopy(bytes, 0, block, index, bytes.length);
			index +=bytes.length;
			
//			bytes =ByteHelper.toByte(layer.getCount());
			bytes =ByteHelper.toByte(0);
			System.arraycopy(bytes, 0, block, index, bytes.length);
			index +=bytes.length;
			
			bytes =ByteHelper.toByte(b.length);
			System.arraycopy(bytes, 0, block, index, bytes.length);
			index +=bytes.length;
			
			System.arraycopy(b, 0, block, index, b.length);
			list.add(block);
		}
		int total = 8;
		for(byte[] b:list){
			total = total + b.length;
		}
		
		byte[] allBytes = new byte[total];
		
		byte[] bytes =ByteHelper.toByte(total);
		System.arraycopy(bytes, 0, allBytes, 0, bytes.length);
		
		
		bytes =ByteHelper.toByte(layers.length);
		System.arraycopy(bytes, 0, allBytes, 4, bytes.length);
		
		int start = 8;
		
		for(byte[] b:list){
			System.arraycopy(b, 0, allBytes, start, b.length);
			start = start + b.length;
		}
		
		return allBytes;
	}
	
}
