package nari.MapService.Service.interfaces;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller("MapServiceController")
@RequestMapping("/MapService")
public interface MapService {

	/**
	 * 添加书签
	 */
	@RequestMapping(value = "/addBookMark", method = RequestMethod.GET)
	public String addBookMark(@RequestParam("input")String input,HttpServletRequest request,HttpServletResponse response);
	
	/**
	 * 修改书签
	 */
	@RequestMapping(value = "/modifyBookMark", method = RequestMethod.GET)
	public String modifyBookMark(@RequestParam("input")String input,HttpServletRequest request,HttpServletResponse response);
	
	/**
	 * 删除书签
	 */
	@RequestMapping(value = "/removeBookMark", method = RequestMethod.GET)
	public String removeBookMark(@RequestParam("input")String input,HttpServletRequest request,HttpServletResponse response);
	
	/**
	 * 地图浏览，获取磁盘缓存地图切片
	 */
	@RequestMapping(value = "/getMap", method = RequestMethod.GET)
	public @ResponseBody String getMap(HttpServletRequest request,HttpServletResponse response);
	
	/**
	 * 动态矢量出图(数据库查询)
	 */
	@RequestMapping(value = "/getVectorMap", method = RequestMethod.POST)
	public @ResponseBody String getVectorMap(@RequestParam("input")String input,HttpServletRequest request,HttpServletResponse response);
	
	/**
	 * 动态矢量出图(缓存查询)
	 */
	@RequestMapping(value = "/getVectorMapByCache", method = RequestMethod.POST)
	public String getVectorMapByCache(@RequestParam("input")String input,HttpServletRequest request,HttpServletResponse response);
	
//	/**
//	 * 获取动态指定图片
//	 */
//	public String getOtherMap(String input,HttpServletRequest request,HttpServletResponse response);
	
	/**
	 * 获取地图切片配置
	 */
	@RequestMapping(value = "/getMapConfig", method = RequestMethod.GET)
	public String getMapConfig(@RequestParam("input")String input,HttpServletRequest request,HttpServletResponse response);
}
