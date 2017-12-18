package nari.Obsevation.service.interfaces;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller("ObsevationServiceController")
@RequestMapping("/ObsevationService")
public interface ObsevationService {

	/**
	 * 动态矢量出图(缓存查询)
	 */
	@RequestMapping(value = "/getVectorMap", method = RequestMethod.POST)
	public String getVectorMap(@RequestParam("input")String input,HttpServletRequest request,HttpServletResponse response);
	
}
