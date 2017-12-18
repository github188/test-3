package nari.QueryService.Service.interfaces;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller("QueryServiceController")
@RequestMapping("/QueryService")
public interface QueryService {

	/**
	 * 1.按条件查询设备属性
	 */
	@RequestMapping(value = "/queryByCondition", method = {RequestMethod.GET, RequestMethod.POST})
	@ResponseBody
	public String queryByCondition(@RequestParam("input")String input,HttpServletRequest request,HttpServletResponse response);
	
	/**
	 * 2.空间查询
	 */
	@RequestMapping(value = "/spatialQuery", method = RequestMethod.GET)
	@ResponseBody
	public String spatialQuery(@RequestParam("input")String input,HttpServletRequest request,HttpServletResponse response);
	
	/**
	 * 5.空间虚拟设备包含设备查询
	 */
	@RequestMapping(value = "/spatialqueryVirtual", method = RequestMethod.GET)
	public String spatialqueryVirtual(@RequestParam("input")String input,HttpServletRequest request,HttpServletResponse response);
	
	/**
	 * 3.虚拟设备下所有设备查询
	 */
	@RequestMapping(value = "/getAllSubSet", method = RequestMethod.GET)
	@ResponseBody
	public String getAllSubSet(@RequestParam("input")String input,HttpServletRequest request,HttpServletResponse response);
	
	
	/**
	 * 4.站内所有设备查询
	 */
	@RequestMapping(value = "/queryByStationId", method = RequestMethod.GET)
	public String queryByStationId(@RequestParam("input")String input,HttpServletRequest request,HttpServletResponse response);
	
	/**
	 * 5. 查询关系设备
	 */
	@RequestMapping(value = "/queryRelations", method = { RequestMethod.GET, RequestMethod.POST })
	public String queryRelations(@RequestParam("input")String input, HttpServletRequest request, HttpServletResponse response);
	
	/**
	 * 5. 查询子设备
	 */
	@RequestMapping(value = "/queryContains", method = { RequestMethod.GET, RequestMethod.POST })
	public String queryContains(@RequestParam("input")String input, HttpServletRequest request, HttpServletResponse response);
	
	/**
	 * 6. 查询父设备
	 */
	@RequestMapping(value = "/queryParents", method = { RequestMethod.GET, RequestMethod.POST })
	public String queryParents(@RequestParam("input")String input, HttpServletRequest request, HttpServletResponse response);
	
	
//	/**
//	 * 3.基础地位查询定位
//	 */
//	@RequestMapping(value = "/queryLandMark", method = RequestMethod.GET)
//	public String queryLandMark(String input,HttpServletRequest request,HttpServletResponse response);
	
//	/**
//	 * 4.道路查询定位
//	 */
//	@RequestMapping(value = "/queryRoad", method = RequestMethod.GET)
//	public String queryRoad(@RequestParam("input")String input,HttpServletRequest request,HttpServletResponse response);
//	
//	/**
//	 * 5.设备定位
//	 */
//	@RequestMapping(value = "/locate", method = RequestMethod.GET)
//	public String locate(@RequestParam("input")String input,HttpServletRequest request,HttpServletResponse response);
	
	
//	/**
//	 * 7.虚拟设备包含设备查询
//	 */
//	@RequestMapping(value = "/queryVirtual", method = RequestMethod.GET)
//	public String queryVirtual(@RequestParam("input")String input,HttpServletRequest request,HttpServletResponse response);
	
	
//	
//	/**
//	 * 8.虚拟设备定位
//	 */
//	@RequestMapping(value = "/locateVirtual", method = RequestMethod.GET)
//	public String locateVirtual(@RequestParam("input")String input,HttpServletRequest request,HttpServletResponse response);
	
	
	
}
