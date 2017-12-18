package org.Invoker.rpc.protocol;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceView;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@Configuration
@EnableWebMvc
public class SpringMVC extends WebMvcConfigurerAdapter {

//	private nari.Logger.Logger logger = LoggerManager.getLogger(this.getClass());
	
  /**
   * 非必须
   */
	@Override
	public void configureDefaultServletHandling(final DefaultServletHandlerConfigurer configurer) {
		configurer.enable("mvcServlet");
	}

	//配置html等的方法
	@Override
	public void configureViewResolvers(ViewResolverRegistry registry){
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setPrefix("/WEB-INF/");
		viewResolver.setSuffix(".html");
		viewResolver.setViewClass(InternalResourceView.class);
		registry.viewResolver(viewResolver);
	}
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
//		registry.addWebRequestInterceptor(new WebRequestInterceptor() {
//			
//			@Override
//			public void preHandle(WebRequest arg0) throws Exception {
//				logger.info("1");
//			}
//			
//			@Override
//			public void postHandle(WebRequest arg0, ModelMap arg1) throws Exception {
//				logger.info("2");
//			}
//			
//			@Override
//			public void afterCompletion(WebRequest arg0, Exception arg1) throws Exception {
//				logger.info("3");
//			}
//		});
	}
	
  /**
   * 如果项目的一些资源文件放在/WEB-INF/resources/下面
   * 在浏览器访问的地址就是类似：http://host:port/projectName/WEB-INF/resources/xxx.css
   * 但是加了如下定义之后就可以这样访问：
   * http://host:port/projectName/resources/xxx.css
   * 非必须
   */
  	@Override
	public void addResourceHandlers(final ResourceHandlerRegistry registry) {
//		  registry.addResourceHandler("/resources").addResourceLocations("classpath:/resources/");
	}
}
