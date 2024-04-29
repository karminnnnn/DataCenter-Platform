package net.srt.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.AllArgsConstructor;
import net.srt.api.FlinkApiFactory;
import net.srt.api.FlinkVersion;
import net.srt.convert.DataProductionClusterConfigurationConvert;
import net.srt.dao.DataProductionClusterConfigurationDao;
import net.srt.dao.DataProductionTaskDao;
import net.srt.entity.DataProductionClusterConfigurationEntity;
import net.srt.entity.DataProductionTaskEntity;
import net.srt.flink.common.assertion.Asserts;
import net.srt.flink.core.job.JobManager;
import net.srt.flink.gateway.GatewayType;
import net.srt.flink.gateway.config.ClusterConfig;
import net.srt.flink.gateway.config.FlinkConfig;
import net.srt.flink.gateway.config.GatewayConfig;
import net.srt.flink.gateway.result.TestResult;
import net.srt.framework.common.exception.ServerException;
import net.srt.framework.common.page.PageResult;
import net.srt.framework.mybatis.service.impl.BaseServiceImpl;
import net.srt.query.DataProductionClusterConfigurationQuery;
import net.srt.service.DataProductionClusterConfigurationService;
import net.srt.vo.DataProductionClusterConfigurationVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 数据生产-集群配置
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2022-12-20
 */
@Service
@AllArgsConstructor
public class DataProductionClusterConfigurationServiceImpl extends BaseServiceImpl<DataProductionClusterConfigurationDao, DataProductionClusterConfigurationEntity> implements DataProductionClusterConfigurationService {

	private final DataProductionTaskDao taskDao;

	@Override
	public PageResult<DataProductionClusterConfigurationVO> page(DataProductionClusterConfigurationQuery query) {
		IPage<DataProductionClusterConfigurationEntity> page = baseMapper.selectPage(getPage(query), getWrapper(query));

		return new PageResult<>(DataProductionClusterConfigurationConvert.INSTANCE.convertList(page.getRecords()), page.getTotal());
	}

	private LambdaQueryWrapper<DataProductionClusterConfigurationEntity> getWrapper(DataProductionClusterConfigurationQuery query) {
		LambdaQueryWrapper<DataProductionClusterConfigurationEntity> wrapper = Wrappers.lambdaQuery();
		wrapper.like(StrUtil.isNotBlank(query.getName()), DataProductionClusterConfigurationEntity::getName, query.getName())
				.like(StrUtil.isNotBlank(query.getAlias()), DataProductionClusterConfigurationEntity::getAlias, query.getAlias());
		dataScopeWithOrgId(wrapper);
		wrapper.orderByDesc(DataProductionClusterConfigurationEntity::getCreateTime);
		wrapper.orderByDesc(DataProductionClusterConfigurationEntity::getId);
		return wrapper;
	}

	@Override
	public void save(DataProductionClusterConfigurationVO vo) {
		DataProductionClusterConfigurationEntity entity = DataProductionClusterConfigurationConvert.INSTANCE.convert(vo);
		entity.setProjectId(getProjectId());
		//统一使用114测试就行
		TestResult testResult = FlinkApiFactory.getByVersion(FlinkVersion.FLINK114).testGateway(vo);
		entity.setIsAvailable(testResult.isAvailable());
		baseMapper.insert(entity);
	}

	@Override
	public void update(DataProductionClusterConfigurationVO vo) {
		DataProductionClusterConfigurationEntity entity = DataProductionClusterConfigurationConvert.INSTANCE.convert(vo);
		entity.setProjectId(getProjectId());
		TestResult testResult = FlinkApiFactory.getByVersion(FlinkVersion.FLINK114).testGateway(vo);
		entity.setIsAvailable(testResult.isAvailable());
		updateById(entity);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delete(List<Long> idList) {
		//判断有没有数据生产任务与之关联
		for (Long id : idList) {
			LambdaQueryWrapper<DataProductionTaskEntity> wrapper = new LambdaQueryWrapper<>();
			wrapper.eq(DataProductionTaskEntity::getClusterConfigurationId, id).last(" limit 1");
			DataProductionTaskEntity taskEntity = taskDao.selectOne(wrapper);
			if (taskEntity != null) {
				throw new ServerException(String.format("存在数据生产任务【%s】与之关联，不可删除！", taskEntity.getName()));
			}
		}
		removeByIds(idList);
	}

	@Override
	public DataProductionClusterConfigurationEntity getClusterConfigById(Integer clusterConfigurationId) {
		DataProductionClusterConfigurationEntity clusterConfiguration = baseMapper.selectById(clusterConfigurationId);
		clusterConfiguration.parseConfig();
		return clusterConfiguration;
	}

	@Override
	public Map getGatewayConfig(Long id) {
		DataProductionClusterConfigurationEntity clusterConfiguration = this.getClusterConfigById(id.intValue());
		return clusterConfiguration.getConfig();
	}

	@Override
	public TestResult testGateway(DataProductionClusterConfigurationVO clusterConfiguration) {
		return FlinkApiFactory.getByVersion(FlinkVersion.FLINK114).testGateway(clusterConfiguration);
	}

	@Override
	public TestResult testGatewayCommon(DataProductionClusterConfigurationVO clusterConfiguration) {
		clusterConfiguration.parseConfig();
		Map<String, Object> config = clusterConfiguration.getConfig();
		GatewayConfig gatewayConfig = new GatewayConfig();
		if (config.containsKey("hadoopConfigPath")) {
			gatewayConfig.setClusterConfig(ClusterConfig.build(config.get("flinkConfigPath").toString(),
					config.get("flinkLibPath").toString(),
					config.get("hadoopConfigPath").toString()));
		} else {
			gatewayConfig.setClusterConfig(ClusterConfig.build(config.get("flinkConfigPath").toString()));
		}
		if (config.containsKey("flinkConfig")) {
			gatewayConfig.setFlinkConfig(FlinkConfig.build((Map<String, String>) config.get("flinkConfig")));
		}
		if (Asserts.isEqualsIgnoreCase(clusterConfiguration.getType(), "Yarn")) {
			gatewayConfig.setType(GatewayType.YARN_APPLICATION);
		}
		// TODO k8s后续考虑支持
		/*else if (Asserts.isEqualsIgnoreCase(clusterConfiguration.getType(), "Kubernetes")) {
			gatewayConfig.setType(GatewayType.KUBERNETES_APPLICATION);
			Map kubernetesConfig = (Map) config.get("kubernetesConfig");
			if (kubernetesConfig.containsKey("kubernetes.namespace")) {
				gatewayConfig.getFlinkConfig().getConfiguration().put("kubernetes.namespace", kubernetesConfig.get("kubernetes.namespace").toString());
			}
			if (kubernetesConfig.containsKey("kubernetes.cluster-id")) {
				gatewayConfig.getFlinkConfig().getConfiguration().put("kubernetes.cluster-id", kubernetesConfig.get("kubernetes.cluster-id").toString());
			} else {
				//初始化FlinkKubeClient需要CLUSTER_ID,先用UUID代替，后面使用job名称来作为CLUSTER_ID
				gatewayConfig.getFlinkConfig().getConfiguration().put("kubernetes.cluster-id", UUID.randomUUID().toString());
			}
			if (kubernetesConfig.containsKey("kubernetes.container.image")) {
				gatewayConfig.getFlinkConfig().getConfiguration().put("kubernetes.container.image", kubernetesConfig.get("kubernetes.container.image").toString());
			}
			String fileDir = FileUtil.isDirectory(PathConstant.WORK_DIR + "/dlink-doc") ? PathConstant.WORK_DIR + "/dlink-doc" : PathConstant.WORK_DIR;
			File dockerFile = null;
			try {
				dockerFile = FileUtil.writeUtf8String(FileUtil.readUtf8String(dockerfileResource.getFile()), fileDir + "/DinkyFlinkDockerfile");
				Docker docker = Docker.build((Map) getClusterConfigById(clusterConfiguration.getId()).getConfig().get("dockerConfig"));
				if (docker != null && StringUtils.isNotBlank(docker.getInstance())) {
					new DockerClientUtils(docker,dockerFile).initImage();
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}*/
		return JobManager.testGateway(gatewayConfig);
	}

	@Override
	public List<DataProductionClusterConfigurationVO> listAll() {
		LambdaQueryWrapper<DataProductionClusterConfigurationEntity> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(DataProductionClusterConfigurationEntity::getEnabled, true);
		dataScopeWithOrgId(wrapper);
		wrapper.orderByDesc(DataProductionClusterConfigurationEntity::getCreateTime);
		wrapper.orderByDesc(DataProductionClusterConfigurationEntity::getId);
		return DataProductionClusterConfigurationConvert.INSTANCE.convertList(baseMapper.selectList(wrapper));
	}

}
