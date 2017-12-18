package nari.distribution.TopoAnalysis.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller("DistributionTopoAnalysisServiceController")
@RequestMapping("/DistributionTopoAnalysisService")
public interface DistributionTopoAnalysisService {
	
	/**
	 * 测试分析
	 */
	@RequestMapping(value = "/testAnalyze", method = {RequestMethod.GET, RequestMethod.POST})
	public String testAnalyze(@RequestParam("input")String input, HttpServletRequest request, HttpServletResponse response);
	
	/**
	 * 模拟停电分析
	 */
	@RequestMapping(value = "/simOutageRangeAnalyze", method = {RequestMethod.GET, RequestMethod.POST})
	public String mockOutageRangeAnalyze(@RequestParam("input")String input, HttpServletRequest request, HttpServletResponse response);
	
}
