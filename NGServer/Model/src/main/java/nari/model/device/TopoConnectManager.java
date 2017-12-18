package nari.model.device;

import java.sql.Blob;
import java.util.Arrays;

public class TopoConnectManager {
	
	public static CNode[] readConnectNode(Blob connect) {
		if (connect == null) {
			return new CNode[0];
		}
		CNode[] nodes = null;
		try {
			byte[] bytes = connect.getBytes(1L, (int)connect.length());
			if ((bytes == null) || (bytes.length == 0)) {
				return new CNode[0];
			}
			if ((bytes.length - 1) % 8 != 0) {
				return new CNode[0];
			}
			System.out.println(Arrays.toString(bytes));
//			byte[] newsize = { bytes[0] };
			int size = (int)bytes[0] ;
//			int size = (int)ByteUtil.getLong(newsize);
			nodes = new CNode[size];
			int offset = 0;
			for (int i = 0; i < size; i++) {
				offset++;
				byte[] newBytes = new byte[8];
				System.arraycopy(bytes, offset, newBytes, 0, 8);
				nodes[i] = new CNode(i + 1, ByteUtil.getLong(newBytes));
				offset += 7;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return nodes;
	}

	public static byte[] createConnectNode(Long[] nodes) {
		int size = nodes.length;
		int len = size * 8 + 1;
		byte[] bytes = new byte[len];
		int pos = 1;
		bytes[0] = ByteUtil.getBytes(size)[0];
		for (int i = 0; i < nodes.length; i++) {
			byte[] bs = ByteUtil.getBytes(nodes[i].longValue());
			System.arraycopy(bs, 0, bytes, pos, bs.length);
			pos += 8;
		}
		return bytes;
	}
}