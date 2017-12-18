package nari.TopoAnalysisService.bean.PowerSourceAnalyze;

import java.io.Serializable;

import nari.parameter.code.PsrTypeSystem;
import nari.parameter.convert.AbstractRequest;

/**
 * 电源追溯分析请求参数
 * @author birderyu
 *
 */
public class PowerSourceAnalyzeRequest 
	extends AbstractRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2711788267987376073L;
	
	/**
	 * 设备类型
	 */
	private String psrType = null;
	
	/**
	 * 设备ID
	 */
	private String psrId = null;
	
	/**
	 * 电源设备类型
	 * 默认为主变压器
	 */
	private String psrSourceType = null;
	
	/**
	 * 是否考虑开关状态，默认情况下不考虑
	 */
	private boolean useSwitch = false;
	
	/**
	 * 返回的设备类型列表，为空则表示返回所有设备类型
	 */
	private String[] resultPsrTypes = null;
	
	/**
	 * 最大返回设备数量，默认200，为0则表示不限制
	 */
	private int maxResultNum = 200;
	
	/**
	 * 设备类型体系
	 */
	private String psrTypeSys = PsrTypeSystem.EQUIPMENT_ID;

	public String getPsrType() {
		return psrType;
	}

	public void setPsrType(String psrType) {
		this.psrType = psrType;
	}

	public String getPsrId() {
		return psrId;
	}

	public void setPsrId(String psrId) {
		this.psrId = psrId;
	}
	
	public String getPsrSourceType() {
		return psrSourceType;
	}

	public void setPsrSourceType(String psrSourceType) {
		this.psrSourceType = psrSourceType;
	}

	public boolean isUseSwitch() {
		return useSwitch;
	}

	public void setUseSwitch(boolean useSwitch) {
		this.useSwitch = useSwitch;
	}

	public String[] getResultPsrTypes() {
		return resultPsrTypes;
	}

	public void setResultPsrTypes(String[] resultPsrTypes) {
		this.resultPsrTypes = resultPsrTypes;
	}
	
	public int getMaxResultNum() {
		return maxResultNum;
	}

	public void setMaxResultNum(int maxResultNum) {
		this.maxResultNum = maxResultNum;
	}

	public String getPsrTypeSys() {
		return psrTypeSys;
	}

	public void setPsrTypeSys(String psrTypeSys) {
		this.psrTypeSys = psrTypeSys;
	}

	@Override
	public boolean validate() {
		return psrType != null && psrId != null;
	}
}
