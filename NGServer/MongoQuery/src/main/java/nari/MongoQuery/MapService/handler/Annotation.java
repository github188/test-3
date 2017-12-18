package nari.MongoQuery.MapService.handler;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bson.BsonArray;
import org.bson.BsonDocument;
import org.bson.BsonDouble;
import org.bson.BsonInt32;
import org.bson.BsonString;
import org.bson.BsonValue;

/**
 * 解析Annotation字段
 * 
 * @author Birderyu
 *
 */
public class Annotation {
	
	private static class Point {
		
		private double x;
		private double y;
		
		public Point(double x, double y) {
			super();
			this.x = x;
			this.y = y;
		}
		
		public double getX() {
			return x;
		}
		
		public void setX(double x) {
			this.x = x;
		}
		
		public double getY() {
			return y;
		}
		
		public void setY(double y) {
			this.y = y;
		}
		
		@Override
		public String toString() {
			return "[" + x + ", " + y + "]";
		}
	}
	
	//int:4 double:8 short:2 long:8 char:2
	
	private int color;
	private double height;
	private double weidth;
	private double angle;
	private int style;
	private double hExtra;
	private double vExtra;
	private int hAlign;
	private int vAlign;
	private double styleAngle;
	private double offsetX;
	private double offsetY;
	private int bColor;
	private short hollow;
	private short transparent;
	private short underLine;
	private short strikeOut;
	private short shadow;
	private int shadeColor;
	private double shadowOffsetX;
	private double shadowOffsetY;
	private int arrange;
	private int direction;
//	private int pointNums; //点数目
	Point[] coords;
	String fontName;//字体
	String text;    //文本
	
	public Annotation(byte[] value) {
		try {
			parse(value);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	private void parse(byte[] value) throws UnsupportedEncodingException {
		
		int offset = 0;
		int sizeofShort = 2;
		int sizeofInteger = 4;
		int sizeofDouble = 8;
//		String charSet = "UTF-8";
		String charSet = "GBK";
		
		color = bytesToInteger(value, offset);
		offset += sizeofInteger;
		
		height = bytesToDouble(value, offset);
		offset += sizeofDouble;
		
		weidth = bytesToDouble(value, offset);
		offset += sizeofDouble;
		
		angle = bytesToDouble(value, offset);
		offset += sizeofDouble;
		
		style = bytesToInteger(value, offset);
		offset += sizeofInteger;
		
		hExtra = bytesToDouble(value, offset);
		offset += sizeofDouble;
		
		vExtra = bytesToDouble(value, offset);
		offset += sizeofDouble;
		
		hAlign = bytesToInteger(value, offset);
		offset += sizeofInteger;
		
		vAlign = bytesToInteger(value, offset);
		offset += sizeofInteger;
		
		styleAngle = bytesToDouble(value, offset);
		offset += sizeofDouble;
		
		offsetX = bytesToDouble(value, offset);
		offset += sizeofDouble;
		
		offsetY = bytesToDouble(value, offset);
		offset += sizeofDouble;
		
		bColor = bytesToInteger(value, offset);
		offset += sizeofInteger;
		
		hollow = bytesToShort(value, offset);
		offset += sizeofShort;
		
		transparent = bytesToShort(value, offset);
		offset += sizeofShort;
		
		underLine = bytesToShort(value, offset);
		offset += sizeofShort;
		
		strikeOut = bytesToShort(value, offset);
		offset += sizeofShort;
		
		shadow = bytesToShort(value, offset);
		offset += sizeofShort;
		
		shadeColor = bytesToInteger(value, offset);
		offset += sizeofInteger;
		
		shadowOffsetX = bytesToDouble(value, offset);
		offset += sizeofDouble;
		
		shadowOffsetY = bytesToDouble(value, offset);
		offset += sizeofDouble;
		
		arrange = bytesToInteger(value, offset);
		offset += sizeofInteger;
		
		direction = bytesToInteger(value, offset);
		offset += sizeofInteger;
		
		int pointNums = bytesToInteger(value, offset);
		offset += sizeofInteger;
		coords = new Point[pointNums];
		for (int i = 0; i < pointNums; i++) {
			double x = bytesToDouble(value, offset);
			offset += sizeofDouble;
			double y = bytesToDouble(value, offset);
			offset += sizeofDouble;
			coords[i] = new Point(x, y);
		}
		
		//解析String:前四位代表该String的byte长度(int型)，后面为内容
		int fontSize = bytesToInteger(value, offset);
		offset += sizeofInteger;
		fontName = bytesToString(value,offset,fontSize,charSet);
		offset += fontSize;
		
		System.out.println(fontName);
		System.out.println(fontName.length());
		
		int textSize = bytesToInteger(value, offset);
		offset += sizeofInteger;
		text = bytesToString(value,offset,textSize,charSet);
		offset += textSize;
		
		System.out.println(text);
		System.out.println(text.length());
	}
	
	public BsonValue toBsonValue() {
		BsonDocument annotationBson = new BsonDocument();
		annotationBson.append("COLOR", new BsonInt32(color));
		annotationBson.append("HEIGHT", new BsonDouble(height));
		annotationBson.append("WEIDTH", new BsonDouble(weidth));
		annotationBson.append("ANGLE", new BsonDouble(angle));
		annotationBson.append("STYLE", new BsonInt32(style));
		annotationBson.append("HORIZONTAL_EXTRA", new BsonDouble(hExtra)); // horizontal
		annotationBson.append("VERTICAL_EXTRA", new BsonDouble(vExtra)); // vertical
		annotationBson.append("HORIZONTAL_ALIGN", new BsonInt32(hAlign)); // horizontal
		annotationBson.append("VERTICAL_ALIGN", new BsonInt32(vAlign)); // vertical
		annotationBson.append("STYLEANGLE", new BsonDouble(styleAngle)); 
		annotationBson.append("OFFSETX", new BsonDouble(offsetX)); 
		annotationBson.append("OFFSETY", new BsonDouble(offsetY)); 
		annotationBson.append("BCOLOR", new BsonInt32(bColor));
		annotationBson.append("HOLLOW", new BsonInt32(hollow));	// short hollow;
		annotationBson.append("TRANSPARENT", new BsonInt32(transparent));	// short
		annotationBson.append("UNDERLINE", new BsonInt32(underLine));	// short
		annotationBson.append("STRIKEOUT", new BsonInt32(strikeOut));	// short
		annotationBson.append("SHADOW", new BsonInt32(shadow));		// short
		annotationBson.append("SHADOWCOLOR", new BsonInt32(shadeColor));
		annotationBson.append("SHADOWOFFSETX", new BsonDouble(offsetX)); 
		annotationBson.append("SHADOWOFFSETY", new BsonDouble(offsetY)); 
		annotationBson.append("ARRANGE", new BsonInt32(arrange));
		annotationBson.append("DIRECTION", new BsonInt32(direction));
//		private int pointNums; //瀹氫綅鐐规暟
		
		//coords Load To Bson
		BsonArray coordsBsonArray = new BsonArray();
		for(int i=0;i<coords.length;i++){
			coordsBsonArray.add(new BsonString(coords[i].toString()));
		}
		annotationBson.append("COORDS", coordsBsonArray);
		annotationBson.append("FONTNAME", new BsonString(fontName));
		annotationBson.append("TEXT", new BsonString(text));
		return annotationBson;
	}
	
	private static short bytesToShort(byte[] bytes, int off) {
		int b0 = bytes[off + 0] & 0xFF;  
	    int b1 = bytes[off + 1] & 0xFF;  
	    return (short) ((b1 << 8) | b0);  
	}
	
	private static int bytesToInteger(byte[] bytes, int off) {
		int b0 = bytes[off + 0] & 0xFF;  
	    int b1 = bytes[off + 1] & 0xFF;  
	    int b2 = bytes[off + 2] & 0xFF;  
	    int b3 = bytes[off + 3] & 0xFF; 
	    return (b3 << 24) | (b2 << 16) | (b1 << 8) | b0;  
	}
	
	private static long bytesToLong(byte[] bytes, int off) {
		int b0 = bytes[off + 0] & 0xFF;  
	    int b1 = bytes[off + 1] & 0xFF;  
	    int b2 = bytes[off + 2] & 0xFF;  
	    int b3 = bytes[off + 3] & 0xFF; 
	    int b4 = bytes[off + 4] & 0xFF;  
	    int b5 = bytes[off + 5] & 0xFF; 
	    int b6 = bytes[off + 6] & 0xFF; 
	    int b7 = bytes[off + 7] & 0xFF; 
	    return (long) ((b7 << 56) | (b6 << 48) | (b5 << 40) | (b4 << 32) |
	    		(b3 << 24) | (b2 << 16) | (b1 << 8) | b0);  
	}
	
	private double bytesToDouble(byte[] bytes, int off) {
		int b0 = bytes[off + 0] & 0xFF;  
	    int b1 = bytes[off + 1] & 0xFF;  
	    int b2 = bytes[off + 2] & 0xFF;  
	    int b3 = bytes[off + 3] & 0xFF; 
	    int b4 = bytes[off + 4] & 0xFF;  
	    int b5 = bytes[off + 5] & 0xFF; 
	    int b6 = bytes[off + 6] & 0xFF; 
	    int b7 = bytes[off + 7] & 0xFF; 
	    return 	(double)((b7 << 56) | (b6 << 48) | (b5 << 40) | (b4 << 32) |
	    		(b3 << 24) | (b2 << 16) | (b1 << 8) | b0);
	}
	
//	private char bytesToChar(byte[] bytes, int off) {
//		int b0 = bytes[off + 1] & 0xFF;
//		int b1 = bytes[off + 2] & 0xFF;
//		return 	(char)((b1 << 8) | b0);
//	}
	
	private String bytesToString(byte[] bytes, int off, int length, String charSet) throws UnsupportedEncodingException {
//		char[] chars = new char[charNum];
//		for(int i=0;i<charNum;i++){
//			chars[i] = bytesToChar(bytes,off + 2*charNum);
//		}
//		return new String(chars);
		
		byte[] StringBytes = new byte[length - 1];
		for(int i=0;i<length-1;i++){
			StringBytes[i] = bytes[off+i];
		}
		return new String(StringBytes,charSet);
	}
	
	public static void main(String[] args) {
		String OracleDriver = "oracle.jdbc.driver.OracleDriver";
		String URL = "jdbc:oracle:thin:@172.16.223.186:1521/orcl";
		String NAME = "dwzy";
		String PASSWORD = "xtcsdwzy";
		
		
		try {
			Class.forName(OracleDriver);
			try {
				Connection conn = DriverManager.getConnection(URL,NAME,PASSWORD);
				String sql = "select * from dwzy.t_ztt_dxt_ggss_fzbj ";
				PreparedStatement ps = conn.prepareStatement(sql);
				ResultSet rs = ps.executeQuery();
				while(rs.next()){
					Blob annotationBolb = rs.getBlob("ANNOTATION");
					int BolbLength =  Long.valueOf(annotationBolb.length()).intValue();
					byte[] annotationValue = annotationBolb.getBytes((long)1, BolbLength);
					Annotation annotation = new Annotation(annotationValue);
					BsonValue annotationBson = annotation.toBsonValue();
					String a = annotationBson.asDocument().toJson();
					System.out.println(a);
				}
				
				
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
