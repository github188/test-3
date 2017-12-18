package nari.SymbolService.Service.interfaces;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller("SymbolServiceController")
@RequestMapping("/SymbolService")
public interface SymbolService {

	/**
	 * 1.获取绘制图形参数
	 */
	@RequestMapping(value = "/getRenderRuleObj", method = RequestMethod.GET)
	public @ResponseBody
	String getRenderRuleObj(HttpServletRequest request,
			HttpServletResponse response);

	/**
	 * 2.获取相关绘图样式
	 */
	@RequestMapping(value = "/getRenderStyleObj", method = RequestMethod.GET)
	public @ResponseBody
	String getRenderStyleObj(HttpServletRequest request,
			HttpServletResponse response);

	// /**
	// * 3.获取各种图形配置表(表内数据转为JSON形式字符串)
	// */
	// @RequestMapping(value = "/dataToJSON", method = RequestMethod.GET)
	// public @ResponseBody String dataToJSON(@RequestParam("input")String
	// input,HttpServletRequest request,HttpServletResponse response);

	/**
	 * 4.获取符号信息
	 */
	@RequestMapping(value = "/getSymbolMember", method = RequestMethod.GET)
	public @ResponseBody
	String getSymbolMember(HttpServletRequest request,HttpServletResponse response);

	
	/**
	 * 5.获取标注信息
	 */
	@RequestMapping(value = "/getLabelRender", method = RequestMethod.GET)
	public @ResponseBody
	String getLabelRender(HttpServletRequest request,HttpServletResponse response);

	/**
	 * 6.获取模型符号状态
	 */
	@RequestMapping(value = "/getSymbolIdStatus", method = RequestMethod.GET)
	public @ResponseBody
	String getSymbolIdStatus(@RequestParam("input")String input,HttpServletRequest request,HttpServletResponse response);
}
