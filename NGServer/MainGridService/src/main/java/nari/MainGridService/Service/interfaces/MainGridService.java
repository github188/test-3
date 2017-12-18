package nari.MainGridService.Service.interfaces;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller("MainGridServiceController")
@RequestMapping("/MainGridService")
public interface MainGridService {

	/**
	 * 1.动态获取屏幕设备
	 */
	@RequestMapping(value = "/getViewGeometry", method = RequestMethod.GET)
	@ResponseBody
	public String getViewGeometry(@RequestParam("input")String input,HttpServletRequest request,HttpServletResponse response);

	/**
	 * 2.具体设备按条件查询
	 */
	@RequestMapping(value = "/conditionLocatQuery", method = RequestMethod.POST)
	@ResponseBody
	public String conditionLocatQuery(@RequestParam("input")String input,HttpServletRequest request,HttpServletResponse response);

	/**
	 * 3.具体设备按坐标查询
	 */
	@RequestMapping(value = "/geoLocatQuery", method = RequestMethod.GET)
	@ResponseBody
	public String geoLocatQuery(@RequestParam("input")String input,HttpServletRequest request,HttpServletResponse response);

	/**
	 * 4.获取文件夹下特殊指定文件
	 */
	@RequestMapping(value = "/getFileName", method = RequestMethod.GET)
	@ResponseBody
	public String getFileName(@RequestParam("input")String input,HttpServletRequest request,HttpServletResponse response);

	/**
	 * 5.由虚拟设备线路得到导线OID
	 */
	@RequestMapping(value = "/getXLOID", method = RequestMethod.GET)
	@ResponseBody
	public String getXLOID(@RequestParam("input")String input,HttpServletRequest request,HttpServletResponse response);
	
	/**
	 * 6.合并完后(框架外)线路查询
	 */
	@RequestMapping(value = "/XLGeoQuery", method = RequestMethod.POST)
	@ResponseBody
	public String XLGeoQuery(@RequestParam("input")String input,HttpServletRequest request,HttpServletResponse response);
	
//	/**
//	 * 7.分页转shp
//	 */
//	@RequestMapping(value = "/rownumQuery", method = RequestMethod.GET)
//	@ResponseBody
//	public String rownumQuery(@RequestParam("input")String input,HttpServletRequest request,HttpServletResponse response);
	
	/**
	 * 8.模糊查询(得到设备名字)
	 */
	@RequestMapping(value = "/fuzzyQuery", method = RequestMethod.GET)
	@ResponseBody
	public String fuzzyQuery(@RequestParam("input")String input,HttpServletRequest request,HttpServletResponse response);

	/**
	 * 9.虚拟设备查询(根据sbId)
	 */
	@RequestMapping(value = "/virtualDevLocat", method = RequestMethod.GET)
	@ResponseBody
	public String virtualDevLocat(@RequestParam("input")String input,HttpServletRequest request,HttpServletResponse response);
	
	/**
	 * 10.获取气象台风预测信息
	 */
	@RequestMapping(value = "/getForecastMessage", method = RequestMethod.GET)
	@ResponseBody
	public String getForecastMessage(@RequestParam("input")String input,HttpServletRequest request,HttpServletResponse response);
	
	/**
	 * 11.获取气象台风历史信息
	 */
	@RequestMapping(value = "/getHistoryMessage", method = RequestMethod.GET)
	@ResponseBody
	public String getHistoryMessage(@RequestParam("input")String input,HttpServletRequest request,HttpServletResponse response);
	
	/**
	 * 12.通过杆塔查导线段
	 */
	@RequestMapping(value = "/queryDXDByYXGT", method = RequestMethod.POST)
	@ResponseBody
	public String queryDXDByYXGT(@RequestParam("input")String input,HttpServletRequest request,HttpServletResponse response);
	
	/**
	 * 13.地理信息纠偏
	 */
	@RequestMapping(value = "/updateRealGeoHandler", method = RequestMethod.GET)
	@ResponseBody
	public String updateRealGeoHandler(@RequestParam("input")String input,HttpServletRequest request,HttpServletResponse response);
	
	/**
	 * 14.获取ip信息
	 */
	@RequestMapping(value = "/getIpAdress", method = RequestMethod.GET)
	@ResponseBody
	public String getIpAdress(@RequestParam("input")String input,HttpServletRequest request,HttpServletResponse response);
	
}
