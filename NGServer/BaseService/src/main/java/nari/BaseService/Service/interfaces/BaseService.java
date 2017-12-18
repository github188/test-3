package nari.BaseService.Service.interfaces;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller("BaseServiceController")
@RequestMapping("/BaseService")
public interface BaseService {

	/**
	 * 1.建立连接
	 */
	@RequestMapping(value = "/getConnection", method = RequestMethod.GET)
	@ResponseBody
	public String getConnection(@RequestParam("input")String input,HttpServletRequest request,HttpServletResponse response);
	
	/**
	 * 2.断开连接
	 */
	@RequestMapping(value = "/disConnection", method = RequestMethod.GET)
	@ResponseBody
	public String disConnection(@RequestParam("input")String input,HttpServletRequest request,HttpServletResponse response);
	
	/**
	 * 3.获取设备类型(由6种大类获取其对应classID数组)
	 */
	@RequestMapping(value = "/getPSRDef", method = RequestMethod.GET)
	@ResponseBody
	public String getPSRDef(@RequestParam("input")String input,HttpServletRequest request,HttpServletResponse response);
	
	/**
	 * 4.根据类型得到该表所属字段
	 */
	@RequestMapping(value = "/getField", method = RequestMethod.GET)
	@ResponseBody
	public String getField(@RequestParam("input")String input,HttpServletRequest request,HttpServletResponse response);

	/**
	 * 5.判断指定字段所有的表的关系
	 */
	@RequestMapping(value = "/judgeFeildName", method = RequestMethod.GET)
	@ResponseBody
	public String judgeFeildName(@RequestParam("input")String input,HttpServletRequest request,HttpServletResponse response);

	/**
	 * 6.更新线路地理信息表(合并线路)
	 */
	@RequestMapping(value = "/updXLGeoMessage", method = RequestMethod.GET)
	@ResponseBody
	public String updXLGeoMessage(@RequestParam("input")String input,HttpServletRequest request,HttpServletResponse response);
	
	/**
	 * 7.得到modelid相关信息
	 */
	@RequestMapping(value = "/getModelMeta", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public String getModelMeta(@RequestParam("input")String input,HttpServletRequest request,HttpServletResponse response);
	
	/**
	 * 8.得到classid相关信息
	 */
	@RequestMapping(value = "/getClassMeta", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public String getClassMeta(@RequestParam("input")String input,HttpServletRequest request,HttpServletResponse response);
	
	/**
	 * 9.得到模型种类相关信息(mapId)
	 */
	@RequestMapping(value = "/getDocumentModel",method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public String getDocumentModel(@RequestParam("input")String input,HttpServletRequest request,HttpServletResponse response);
	
	/**
	 * 10.得到转义表码值
	 */
	@RequestMapping(value = "/getTransCode", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public String getTransCode(@RequestParam("input")String input,HttpServletRequest request,HttpServletResponse response);
	
	/**
	 * 11.获取电压等级列表
	 */
	@RequestMapping(value = "/getVoltageLevelList", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public String getVoltageLevelList(HttpServletRequest request,HttpServletResponse response);
	
	/**
	 * 12.获取图类型对应的设备类型
	 */
	@RequestMapping(value = "/getMapClassList", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public String getMapClassList(@RequestParam("input")String input,HttpServletRequest request,HttpServletResponse response);
	
	/**
	 * 13.获取geo的相关地理信息(范围，图形等)
	 */
	@RequestMapping(value = "/getAreaGeoMessage", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public String getAreaGeoMessage(@RequestParam("input")String input,HttpServletRequest request,HttpServletResponse response);
	
	/**
	 * 14.获取设备的最小凸包多边形
	 */
	@RequestMapping(value = "/getConvexHull", method = { RequestMethod.GET, RequestMethod.POST } )
	@ResponseBody
	public String getConvexHull(@RequestParam("input")String input, HttpServletRequest request, HttpServletResponse response);
	
	/**
	 * 15.下载文档服务
	 */
	@RequestMapping(value = "/getFileService", method = { RequestMethod.GET, RequestMethod.POST } )
	@ResponseBody
	public String getFileService(@RequestParam("input")String input, HttpServletRequest request, HttpServletResponse response);
	
	/**
	 * 15.获取工具箱配置
	 */
	@RequestMapping(value = "/getToolBoxconfig", method = { RequestMethod.GET, RequestMethod.POST } )
	@ResponseBody
	public String getToolBoxconfig(@RequestParam("input")String input, HttpServletRequest request, HttpServletResponse response);
}
