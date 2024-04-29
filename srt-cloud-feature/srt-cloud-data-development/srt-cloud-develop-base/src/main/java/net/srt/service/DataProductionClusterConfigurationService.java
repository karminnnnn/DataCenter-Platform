package net.srt.service;

import net.srt.entity.DataProductionClusterConfigurationEntity;
import net.srt.flink.gateway.result.TestResult;
import net.srt.framework.common.page.PageResult;
import net.srt.framework.mybatis.service.BaseService;
import net.srt.query.DataProductionClusterConfigurationQuery;
import net.srt.vo.DataProductionClusterConfigurationVO;

import java.util.List;
import java.util.Map;

/**
 * 数据生产-集群配置
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2022-12-20
 */
public interface DataProductionClusterConfigurationService extends BaseService<DataProductionClusterConfigurationEntity> {

    PageResult<DataProductionClusterConfigurationVO> page(DataProductionClusterConfigurationQuery query);

    void save(DataProductionClusterConfigurationVO vo);

    void update(DataProductionClusterConfigurationVO vo);

    void delete(List<Long> idList);

	DataProductionClusterConfigurationEntity getClusterConfigById(Integer clusterConfigurationId);

	Map<String, Object> getGatewayConfig(Long id);

	TestResult testGateway(DataProductionClusterConfigurationVO clusterConfiguration);

	TestResult testGatewayCommon(DataProductionClusterConfigurationVO clusterConfiguration);

	List<DataProductionClusterConfigurationVO> listAll();
}
