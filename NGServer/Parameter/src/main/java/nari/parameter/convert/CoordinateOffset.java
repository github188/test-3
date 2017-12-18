package nari.parameter.convert;

import nari.Xml.bundle.annotation.NodeType;
import nari.Xml.bundle.annotation.XmlNode;

public class CoordinateOffset {

	public static final CoordinateOffset NONE = new CoordinateOffset();
	
	@XmlNode(name="xoffset",type=NodeType.Normal, clazz=Double.class)
	private Double xoffset = 0.0;
	
	@XmlNode(name="yoffset",type=NodeType.Normal, clazz=Double.class)
	private Double yoffset = 0.0;
	
	public Double getXoffset() {
		return xoffset;
	}

	public void setXoffset(Double xoffset) {
		this.xoffset = xoffset;
	}

	public Double getYoffset() {
		return yoffset;
	}

	public void setYoffset(Double yoffset) {
		this.yoffset = yoffset;
	}
}
