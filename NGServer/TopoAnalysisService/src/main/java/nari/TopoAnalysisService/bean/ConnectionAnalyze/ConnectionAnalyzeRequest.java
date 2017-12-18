package nari.TopoAnalysisService.bean.ConnectionAnalyze;

import java.io.Serializable;

import nari.parameter.code.PsrTypeSystem;
import nari.parameter.convert.AbstractRequest;

/**
 * 连通性分析请求参数
 * @author birderyu
 *
 */
public class ConnectionAnalyzeRequest 
	extends AbstractRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4775435593503961815L;
	
	/**
	 * 第一个设备的设备类型
	 */
	private String psrTypeA = null;
	
	/**
	 * 第一个设备的设备ID
	 */
	private String psrIdA = null;
	
	/**
	 * 第二个设备的设备类型
	 */
	private String psrTypeB = null;
	
	/**
	 * 第二个设备的设备ID
	 */
	private String psrIdB = null;
	
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
	
	public String getPsrTypeA() {
		return psrTypeA;
	}

	public void setPsrTypeA(String psrTypeA) {
		this.psrTypeA = psrTypeA;
	}

	public String getPsrIdA() {
		return psrIdA;
	}

	public void setPsrIdA(String psrIdA) {
		this.psrIdA = psrIdA;
	}

	public String getPsrTypeB() {
		return psrTypeB;
	}

	public void setPsrTypeB(String psrTypeB) {
		this.psrTypeB = psrTypeB;
	}

	public String getPsrIdB() {
		return psrIdB;
	}

	public void setPsrIdB(String psrIdB) {
		this.psrIdB = psrIdB;
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
		return psrTypeA != null && psrIdA != null
				&& psrTypeB != null && psrIdB != null;
	}

}
