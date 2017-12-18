package nari.TopoAnalysisService.bean.PowerSourceAnalyze;

import java.io.Serializable;

import nari.parameter.bean.QueryResult;
import nari.parameter.code.ReturnCode;

/**
 * 电源追溯分析返回结果
 * @author birderyu
 *
 */
public class PowerSourceAnalyzeResponse 
	implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9177033410444941600L;

	/**
	 * 返回码
	 */
	private ReturnCode code = null;
	
	/**
	 * 查询结果
	 * 查询结果为包括起始设备与电源点以及二者之间的设备的轨迹集合
	 */
	private QueryResult[] result = null;
	
	/**
	 * 电源点
	 */
	private QueryResult source = null;

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

	public QueryResult getSource() {
		return source;
	}

	public void setSource(QueryResult source) {
		this.source = source;
	}
}
