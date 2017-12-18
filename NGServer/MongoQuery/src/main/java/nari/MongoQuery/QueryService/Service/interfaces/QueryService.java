package nari.MongoQuery.QueryService.Service.interfaces;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller("/MongoQueryServiceController")
@RequestMapping("/MongoQueryService")
public interface QueryService {
	/**
	 * query device attributes by condition
	 */
	@RequestMapping(value = "/queryByCondition", method = { RequestMethod.GET, RequestMethod.POST } )
	public String queryByCondition(@RequestParam("input")String input, HttpServletRequest request, HttpServletResponse response);
	
	/**
	 * query all device in the station by station-id
	 */
	@RequestMapping(value = "/queryByStationId", method = RequestMethod.GET)
	public String queryByStationId(@RequestParam("input")String input, HttpServletRequest request, HttpServletResponse response);
	
	/**
	 * location query
	 */
	@RequestMapping(value ="/queryLandMark", method = RequestMethod.GET)
	public String queryLandMark(String input, HttpServletRequest request, HttpServletResponse response);
	
	/**
	 * 空间查询
	 */
	@RequestMapping(value = "/spatialQuery", method = RequestMethod.GET)
	public String spatialQuery(@RequestParam("input")String input,HttpServletRequest request,HttpServletResponse response);
	
	/**
	 * 专题图图实例下设备查询
	 */
	@RequestMapping(value = "/thematicDev", method = {RequestMethod.GET,RequestMethod.POST})
	public String thematicDev(@RequestParam("input")String input,HttpServletRequest request,HttpServletResponse response);
	
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
	
	/**
	 * 7. 查询大馈线（含线路）
	 */
	@RequestMapping(value = "/queryFeadrLine", method = { RequestMethod.GET, RequestMethod.POST })
	public String queryFeadrLine(@RequestParam("input")String input, HttpServletRequest request, HttpServletResponse response);
	
}

