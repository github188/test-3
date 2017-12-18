package org.Invoker.rpc.protocol;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nari.httpServer.ServerAttribute;

import org.springframework.web.filter.OncePerRequestFilter;

public class CharsetEncodeFilter extends OncePerRequestFilter {

	public CharsetEncodeFilter() {
		
	}
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
//		response.setHeader("Access-Control-Allow-Origin", "*");
//		response.setHeader("Access-Control-Allow-Methods", "GET,POST");
////		response.setContentType("text/html;charset=gbk");
//		response.setHeader("content-type", "text/html;charset=UTF-8");
//		String character = ServerAttribute.getCharacterEncoding("gbk");
//		System.out.println("-------------------------"+character+"---------------------");
//		request.setCharacterEncoding(character);
//		response.setCharacterEncoding(character);
//		chain.doFilter(request, response);
		
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "GET,POST");
//		response.setContentType("text/html;charset=gbk");
		String character = ServerAttribute.getCharacterEncoding("gbk");
		//System.out.println("character is "+character);
		request.setCharacterEncoding(character);
		
//		response.setCharacterEncoding(character);
		
		String contentType = "text/html;charset="+character;
		response.setContentType(contentType);
		chain.doFilter(request, response);
	}
	
}
