package nari.ThematicMapService.Service.interfaces;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller("ThematicServiceController")
@RequestMapping("/ThematicService")
public interface ThematicService {

	/**
	 * 电缆埋设剖面图
	 */
	@RequestMapping(value = "/getCableSectionMap", method = {RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String getCableSectionMap(@RequestParam("input")String input,HttpServletRequest request,HttpServletResponse response);
	
	/**
	 * 电缆井站看图
	 */
	public String getCableWellSectionMap(String input);
	
	/**
	 * 单线图
	 */
	public String getSingleLineMap(String input);
	
	/**
	 * 站内图
	 */
	public String getStationMap(String input);
	
	/**
	 * 供电范围图
	 */
	public String getSupplyRangeMap(String input);
	
	/**
	 * 系统图
	 */
	public String getSystemMap(String input);
	
	
	/**
	 * 专题图空间查询
	 */
	public String spatialQuery(String input);
	
	/**
	 * 获取专题图配置信息
	 */
	@RequestMapping(value = "/getThematicConfig", method = {RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String getThematicConfig(@RequestParam("input")String input,HttpServletRequest request,HttpServletResponse response);
	
	/**
	 * 专题图图实例下设备查询
	 */
	@RequestMapping(value = "/thematicDev", method = {RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String thematicDev(@RequestParam("input")String input,HttpServletRequest request,HttpServletResponse response);

}
