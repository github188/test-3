package nari.MongoQuery.MapService.Service.interfaces;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller("MongoMapServiceController")
@RequestMapping("/MongoMapService")
public interface MapService {
	/**/
	@RequestMapping(value = "/getVectorMap", method = {RequestMethod.GET, RequestMethod.POST})
	@ResponseBody
	public String getVectorMap(@RequestParam("input")String input, HttpServletRequest request, HttpServletResponse response);
}
