package nari.Dao.bundle.simple;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.SQLException;

/**
 * LOB字段类型转换工具类
 * @author 
 * @version
 */
public class LobUtils {
	
	private static int builderSize = 1024;
	
	private static int bufferSize  = 1024;
	
	public static void setBufferSize(int bufferSize) {
		LobUtils.bufferSize = bufferSize;
	}
	
	public static void setBuilderSize(int builderSize) {
		LobUtils.builderSize = builderSize;
	}
	
	public static int getBufferSize() {
		return bufferSize;
	}

	public static int getBuilderSize() {
		return builderSize;
	}
	
	/**
	 * Blob转换成String
	 * @param blob
	 * @return
	 */
	public static String blobToString(Blob blob) {
		char[] buffer = new char[bufferSize];
		StringBuilder builder = new StringBuilder(builderSize);
		try {
			Reader reader  = new InputStreamReader(blobToStream(blob));			
			while (reader.read(buffer) != -1) {
				builder.append(buffer);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return builder.toString().trim();
	}
	
	/**
	 * Blob转成byte数组
	 * @param blob
	 * @return
	 */
	public static byte[] blobToBytes(Blob blob) {
		byte[] b = null;
		try {
			b = blob.getBytes(0L, (int)blob.length());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return b;
	}
	
	/**
	 * Blob转换成ByteArrayInputStream
	 * @param blob
	 * @return
	 */
	public static InputStream blobToStream(Blob blob) {
		InputStream is = null;
		try {
			is = new BufferedInputStream(blob.getBinaryStream());
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		return is;
	}
	
	/**
	 * Clob转换成String
	 * @param clob
	 * @return
	 */
	public static String clobToString(Clob clob) {
		char[] buffer = new char[bufferSize];
		StringBuilder builder = new StringBuilder(builderSize);
		try {
			Reader reader = clobToStream(clob);
			while (reader.read(buffer) != -1) {
				builder.append(buffer);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return builder.toString().trim();
	}
	
	/**
	 * Clob转换成CharArrayReader
	 * @param clob
	 * @return
	 */
	public static Reader clobToStream(Clob clob) {
		Reader reader = null;
		try {
			reader = clob.getCharacterStream();		
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return reader;
	}
	
	/**
	 * Clob写入文件
	 * @param clob
	 * @param file
	 */
	public static boolean clobToFile(Clob clob, File file) {
		char[] buffer = new char[bufferSize];
		FileWriter writer = null;
		Reader reader = new BufferedReader(clobToStream(clob));
		try {
			int readChars = 0;
			writer = new FileWriter(file);
			while ((readChars = reader.read(buffer)) != -1) {
				writer.write(buffer, 0, readChars);
			}
			writer.flush();
			writer.close();
			return true;
		} catch (IOException e) {		
			e.printStackTrace();
			return false;
		}		
	}
	
}
