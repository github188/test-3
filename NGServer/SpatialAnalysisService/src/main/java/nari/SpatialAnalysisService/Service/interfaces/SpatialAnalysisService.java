package nari.SpatialAnalysisService.Service.interfaces;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller("SpatialAnalysisServiceController")
@RequestMapping("/SpatialAnalysisService")
public interface SpatialAnalysisService {

	/**
	 * 空间对象缓冲区分析
	 */
	public String spatialBuffer(String input,HttpServletRequest request,HttpServletResponse response);
	
	/**
	 * 设备缓冲区分析
	 */
	public String deviceBuffer(String input,HttpServletRequest request,HttpServletResponse response);
	
	/**
	 * 最短路径分析
	 */
	public String shortPath(String input,HttpServletRequest request,HttpServletResponse response);
}
