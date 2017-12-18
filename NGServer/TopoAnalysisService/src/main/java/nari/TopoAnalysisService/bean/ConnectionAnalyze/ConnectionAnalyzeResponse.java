package nari.TopoAnalysisService.bean.ConnectionAnalyze;

import java.io.Serializable;

import nari.parameter.bean.QueryResult;
import nari.parameter.code.ReturnCode;

/**
 * 连通性分析返回结果
 * @author birderyu
 *
 */
public class ConnectionAnalyzeResponse 
	implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1386998946886889631L;
	
	/**
	 * 是否是连通的
	 */
	private boolean isConnectional = false;
	
	/**
	 * 返回码
	 */
	private ReturnCode code = null;
	
	/**
	 * 查询结果
	 * 查询结果为包括起始设备与电源点以及二者之间的设备的轨迹集合
	 */
	private QueryResult[] result = null;

	public boolean isConnectional() {
		return isConnectional;
	}

	public void setConnectional(boolean isConnectional) {
		this.isConnectional = isConnectional;
	}

	public ReturnCode getCode() {
		return code;
	}

	public void setCode(ReturnCode code) {
		this.code = code;
	}

	public QueryResult[] getResult() {
		return result;
	}

	public void setResult(QueryResult[] result) {
		this.result = result;
	}
}
