package org.Invoker.rpc.invoker;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.Invoker.HttpClientIdentity;
import org.Invoker.Identity;
import org.Invoker.rpc.exception.InvokerException;
import org.Invoker.rpc.result.Result;
import org.Invoker.rpc.result.RpcResult;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

public class HttpInvoker<T> extends AbstractInvoker<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -282335791340262623L;

	private Map<Method,RequestMapping> methodMap;
	
	private String uri;
	
	private HttpClientIdentity identity;
	
	public HttpInvoker(String uri,Map<Method,RequestMapping> methodMap,Class<T> interfaceType,HttpClientIdentity identity){
		super(interfaceType);
		this.uri = uri;
		this.methodMap = methodMap;
		this.identity = identity;
	}
	
	@Override
	public void destroy() throws InvokerException {
		
	}

	@Override
	public Identity getIdentity() {
		return identity;
	}

	@Override
	protected Result doInvoke(Invocation inv) throws InvokerException {
		Object val = null;
		Result res = null;
		
		String methodName = inv.getMethdName();
		Class<?>[] types = inv.getParamterTypes();
		
		RequestMapping request = null;
		Method method = null;
		for(Map.Entry<Method, RequestMapping> entry:methodMap.entrySet()){
			method = entry.getKey();
			if(!method.getName().equals(methodName)){
				method = null;
				continue;
			}
			if(types==null || types.length==0){
				request = entry.getValue();
				continue;
			}
			
			if(Arrays.equals(types, method.getParameterTypes())){
				request = entry.getValue();
			}
		}
		
		if(request==null){
			return new RpcResult("no remote method found");
		}
		
		RequestMethod[] m = request.method();
		
		RequestMethod requestMethod = null;
		if(m==null || m.length==0){
			requestMethod = RequestMethod.POST;
		}else{
			requestMethod = m[0];
		}
		
		Object[] values = inv.getArguments();
		
		if(requestMethod==RequestMethod.GET){
			String uri = this.uri;
			uri = uri + "/" + request.value()[0];
			Class<?>[] parameters = method.getParameterTypes();
			String query = "";
			int i = 0;
			
			Annotation[][] anos = method.getParameterAnnotations();
			for(int j=0;i<parameters.length;j++){
				Annotation[] an=anos[j];
            	if(an!=null && an.length>0){
            		Annotation a=an[0];
            		if(a.annotationType()==RequestParam.class){
            			RequestParam r = (RequestParam)a;
            			String value = r.value();
            			query = query + value+"="+values[i++]+"&";
            		}
            	}
//				ArgVariable arg = parameter.getAnnotation(ArgVariable.class);
			}
			
			if(query.endsWith("&")){
				query = query.substring(0, query.length()-1);
			}
			
			if(!"".equals(query)){
				uri = uri + "?" + query;
			}
			
			try {
				val = invokeGET(uri);
			} catch (Exception e) {
				e.printStackTrace();
				res = new RpcResult(e);
				return res;
			}
		}else if(requestMethod==RequestMethod.POST){
			String uri = this.uri;
			uri = uri + "/" + request.value()[0];
			
			Class<?>[] parameters = method.getParameterTypes();
			List<NameValuePair> pairs = new ArrayList<NameValuePair>();
			int i = 0;
			
			Annotation[][] anos = method.getParameterAnnotations();
			for(int j=0;i<parameters.length;j++){
				Annotation[] an=anos[j];
            	if(an!=null && an.length>0){
            		Annotation a=an[0];
            		if(a.annotationType()==RequestParam.class){
            			RequestParam r = (RequestParam)a;
            			String name = r.value();
        				Object value = values[i++];
        				pairs.add(new NameValuePair(name, value==null?"":String.valueOf(value)));
            		}
            	}
//				ArgVariable arg = parameter.getAnnotation(ArgVariable.class);
			}
			
//			for(Class<?> parameter:parameters){
//				ArgVariable arg = parameter.getAnnotation(ArgVariable.class);
//				if(arg==null){
//					i++;
//					continue;
//				}
//				Object value = values[i++];
//				pairs.add(new NameValuePair(arg.name(), value==null?"":String.valueOf(value)));
//			}
			
			NameValuePair[] data = new NameValuePair[pairs.size()];
			data = pairs.toArray(data);
			
			try {
				val = invokePOST(uri,data);
			} catch (Exception e) {
				e.printStackTrace();
				res = new RpcResult(e);
				return res;
			}
		}else{
			res = new RpcResult(new InvokerException("not support method "+requestMethod));
			return res;
		}
		
		try {
			res = new RpcResult(val);
		} catch (Exception e) {
			e.printStackTrace();
			res = new RpcResult(e);
		}
		return res;
	}
	
	private Object invokePOST(String uri,NameValuePair[] data) throws Exception{
		HttpClient client = new HttpClient();
		PostMethod post = new PostMethod(uri);
		post.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
		
		post.setRequestBody(data);
		byte[] resp = null;
		try {
			int code = client.executeMethod(post);
			if(code == HttpStatus.SC_OK){
				resp = post.getResponseBody();
			}
		} finally{
			post.releaseConnection();
		}
		
		return resp==null?"":new String(resp);
	}
	
	private Object invokeGET(String uri) throws Exception{
		HttpClient client = new HttpClient();
		
		GetMethod get = new GetMethod(uri);
		get.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
		
		byte[] resp = null;
		
		int code = 0;
		try {
			code = client.executeMethod(get);
			if(code == HttpStatus.SC_OK){
				resp = get.getResponseBody();
			}
		} finally{
			get.releaseConnection();
		}
		
		return resp==null?"":new String(resp);
	}
	
}
