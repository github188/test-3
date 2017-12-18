package nari.web;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.application.plugin.StandardFramework;
import com.application.plugin.State;
import com.application.plugin.bundle.BundleException;
import com.application.plugin.framework.Framework;

public class WebAppStartupListener implements ServletContextListener{

	private Framework framework = null;
	
	@Override
	public void contextInitialized(ServletContextEvent context) {
		framework = new StandardFramework();
		try {
			framework.init(null);
			framework.start();
		} catch (BundleException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent context) {
		if(framework!=null && framework.getState()==State.STARTED){
			try {
				framework.stop();
			} catch (BundleException e) {
				e.printStackTrace();
			}
		}
	}
}
