package nari.httpServer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import nari.Xml.impl.ConfigSearchService;
import nari.Xml.interfaces.ConfigSearch;

public class ServerAttribute {

	private static ServerBean bean = null;
	
	private static boolean isInit = false;
	
	public static void init(){
		if(!isInit){
			InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream("config/server.xml");
			
			if(stream==null){
				String root = System.getProperty("user.dir");
				File file = new File(root);
				file = new File(file.getParentFile(),"config/server.xml");
				
				try {
					URL url = file.toURI().toURL();
					if(url!=null){
						stream = url.openStream();
					}
					
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			ConfigSearch searcher = new ConfigSearchService();
			bean = searcher.loadConfigCache("ServerAttribute",stream,"xml",ServerBean.class);
			isInit = true;
		}
	}
	
	public static String getCharacterEncoding(String def){
		if(bean==null){
			return def;
		}
		return (bean.getCharter()==null || bean.getCharter().equals(""))?def:bean.getCharter();
	}
}
