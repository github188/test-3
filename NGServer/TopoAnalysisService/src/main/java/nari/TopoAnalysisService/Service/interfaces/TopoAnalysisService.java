package nari.TopoAnalysisService.Service.interfaces;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller("TopoAnalysisServiceController")
@RequestMapping("/TopoAnalysisService")
public interface TopoAnalysisService {
	
	/**
	 * 连通性分析
	 */
	@RequestMapping(value = "/connectionAnalyze", method = {RequestMethod.GET, RequestMethod.POST})
	public String connectionAnalyze(@RequestParam("input")String input, HttpServletRequest request, HttpServletResponse response);
	
	/**
	 * 电源追溯分析
	 */
	@RequestMapping(value = "/powerSourceAnalyze", method = {RequestMethod.GET, RequestMethod.POST})
	public String powerSourceAnalyze(@RequestParam("input")String input, HttpServletRequest request, HttpServletResponse response);
	
	/**
	 * 停电范围分析
	 */
	@RequestMapping(value = "/outageRangeAnalyze", method = {RequestMethod.GET, RequestMethod.POST})
	public String outageRangeAnalyze(@RequestParam("input")String input, HttpServletRequest request, HttpServletResponse response);
	
	/**
	 * 模拟停电分析
	 */
	@RequestMapping(value = "/simOutageRangeAnalyze", method = {RequestMethod.GET, RequestMethod.POST})
	public String simOutageRangeAnalyze(@RequestParam("input")String input, HttpServletRequest request, HttpServletResponse response);
	
	/**
	 * 供电范围分析
	 */
	@RequestMapping(value = "/supplyRangeAnalyze", method = {RequestMethod.GET, RequestMethod.POST})
	public String supplyRangeAnalyze(@RequestParam("input")String input, HttpServletRequest request, HttpServletResponse response);
	
	/**
	 * 供电半径分析
	 */
	@RequestMapping(value = "/supplyRadiusAnalyze", method = {RequestMethod.GET, RequestMethod.POST})
	public String supplyRadiusAnalyze(@RequestParam("input")String input, HttpServletRequest request, HttpServletResponse response);
	
}
