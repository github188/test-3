package nari.network.device;

/**
 * 大馈线
 *
 * Created by Birderyu on 2017/6/16.
 */
public interface BigFeeder
    extends Device {

    /**
     * 获取出线开关类型
     * @return
     */
    int getOutletSwitchType();

    /**
     * 获取出线开关ID
     * @return
     */
    int getOutletSwitchId();

    /**
     * 获取起始电站
     *
     * @return
     */
    Station getStartStation();
}
