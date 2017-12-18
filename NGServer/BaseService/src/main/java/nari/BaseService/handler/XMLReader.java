package nari.BaseService.handler;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.helpers.DefaultHandler;

public class XMLReader extends DefaultHandler {
//	java.util.Stack tags = new java.util.Stack();

	public XMLReader() {
		super();
	}

	//
//	public void startElement(String uri, String localName, String qName,
//			Attributes attrs) {
//		tags.push(qName);
//	}
	
//	public static void main(String args[]) {
//		long lasting = System.currentTimeMillis();
//		try {
//			SAXParserFactory sf = SAXParserFactory.newInstance();
//			SAXParser sp = sf.newSAXParser();
//			XMLReader reader = new XMLReader();
//			sp.parse(new InputSource("D:\\hahaha.xml"), reader);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		System.out.println("运行时间：" + (System.currentTimeMillis() - lasting)
//				+ "毫秒");
//	}
//	
//	@Override
//	public void characters(char ch[], int start, int length)
//			throws SAXException {
//		String tag = (String) tags.peek();
//		if (tag.equals("NO")) {
//			System.out.print("车牌号码：" + new String(ch, start, length));
//		}
//		if (tag.equals("ADDR")) {
//			System.out.println("地址:" + new String(ch, start, length));
//		}
//}
		

//	public void huhuan(Object[] arr,int a,int b){
//		Object huan = new Object();
//		huan = arr[a];
//		arr[a] = arr[b];
//		arr[b] = huan;
//	}
		
//	public static void main(String[] args) {
//		DocumentBuilder a;
//		try {
//			a = DocumentBuilderFactory.newInstance().newDocumentBuilder();
//			Document b = a.parse("a");
//			org.dom4j.Document d = sp.p
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//	}
}