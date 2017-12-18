package nari.MongoDBUpdate.service.interfaces;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller("/MongoUpdateServiceController")
@RequestMapping("/MongoUpdateService")
public interface MongoUpdateService {

	/**
	 * 专题图设备更新
	 */
	@RequestMapping(value = "/updateThematic", method = {RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String updateThematic(@RequestParam("input")String input,HttpServletRequest request,HttpServletResponse response);
	
	/**
	 * 大馈线设备更新
	 */
	@RequestMapping(value = "/udateFeederDev", method = {RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String udateFeederDev(@RequestParam("input")String input,HttpServletRequest request,HttpServletResponse response);
}
