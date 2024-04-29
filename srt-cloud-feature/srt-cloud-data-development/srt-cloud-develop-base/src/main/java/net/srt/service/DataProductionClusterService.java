package net.srt.service;

import net.srt.entity.DataProductionClusterEntity;
import net.srt.flink.core.cluster.FlinkClusterInfo;
import net.srt.framework.common.page.PageResult;
import net.srt.framework.mybatis.service.BaseService;
import net.srt.query.DataProductionClusterQuery;
import net.srt.vo.DataProductionClusterVO;

import java.util.List;

/**
 * 数据生产-集群实例
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2022-12-01
 */
public interface DataProductionClusterService extends BaseService<DataProductionClusterEntity> {

	FlinkClusterInfo checkHeartBeat(String hosts, String host);

    PageResult<DataProductionClusterVO> page(DataProductionClusterQuery query);

    void save(DataProductionClusterVO vo);

    void update(DataProductionClusterVO vo);

    void delete(List<Long> idList);

	void heartbeat(List<Long> idList);

	List<DataProductionClusterEntity> listAll();

	String buildEnvironmentAddress(boolean useRemote, Integer clusterId);

	String buildRemoteEnvironmentAddress(Integer id);

	String getJobManagerAddress(DataProductionClusterEntity cluster);

	String buildLocalEnvironmentAddress();

	DataProductionClusterEntity registersCluster(DataProductionClusterEntity autoRegistersCluster);

	Integer clearCluster();

	List<DataProductionClusterEntity> listAuto();

	Integer buildJobManagePort(String address);
}
