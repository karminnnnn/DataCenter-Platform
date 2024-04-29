package net.srt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import net.srt.convert.DataProductionClusterConvert;
import net.srt.dao.DataProductionClusterDao;
import net.srt.dao.DataProductionTaskDao;
import net.srt.entity.DataProductionClusterEntity;
import net.srt.entity.DataProductionTaskEntity;
import net.srt.flink.common.constant.NetConstant;
import net.srt.flink.core.api.FlinkAPI;
import net.srt.flink.core.cluster.FlinkCluster;
import net.srt.flink.core.cluster.FlinkClusterInfo;
import net.srt.flink.executor.constant.FlinkConstant;
import net.srt.framework.common.exception.ServerException;
import net.srt.framework.common.page.PageResult;
import net.srt.framework.mybatis.service.impl.BaseServiceImpl;
import net.srt.query.DataProductionClusterQuery;
import net.srt.service.DataProductionClusterService;
import net.srt.vo.DataProductionClusterVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import srt.cloud.framework.dbswitch.common.util.StringUtil;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.List;

/**
 * 数据生产-集群实例
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2022-12-01
 */
@Service
@AllArgsConstructor
public class DataProductionClusterServiceImpl extends BaseServiceImpl<DataProductionClusterDao, DataProductionClusterEntity> implements DataProductionClusterService {

	private final DataProductionTaskDao taskDao;

	@Override
	public FlinkClusterInfo checkHeartBeat(String hosts, String host) {
		return FlinkCluster.testFlinkJobManagerIP(hosts, host);
	}

	@Override
	public PageResult<DataProductionClusterVO> page(DataProductionClusterQuery query) {
		IPage<DataProductionClusterEntity> page = baseMapper.selectPage(getPage(query), getWrapper(query));

		return new PageResult<>(DataProductionClusterConvert.INSTANCE.convertList(page.getRecords()), page.getTotal());
	}

	private LambdaQueryWrapper<DataProductionClusterEntity> getWrapper(DataProductionClusterQuery query) {
		LambdaQueryWrapper<DataProductionClusterEntity> wrapper = Wrappers.lambdaQuery();
		wrapper.like(StringUtil.isNotBlank(query.getName()), DataProductionClusterEntity::getName, query.getName());
		wrapper.like(StringUtil.isNotBlank(query.getAlias()), DataProductionClusterEntity::getAlias, query.getAlias());
		dataScopeWithOrgId(wrapper);
		wrapper.orderByDesc(DataProductionClusterEntity::getCreateTime);
		wrapper.orderByDesc(DataProductionClusterEntity::getId);
		return wrapper;
	}

	@Override
	public void save(DataProductionClusterVO vo) {
		DataProductionClusterEntity entity = DataProductionClusterConvert.INSTANCE.convert(vo);
		entity.setProjectId(getProjectId());
		entity.setAutoRegisters(false);
		checkHealth(entity);
		baseMapper.insert(entity);
	}

	@Override
	public void update(DataProductionClusterVO vo) {
		DataProductionClusterEntity entity = DataProductionClusterConvert.INSTANCE.convert(vo);
		entity.setProjectId(getProjectId());
		entity.setAutoRegisters(false);
		checkHealth(entity);
		updateById(entity);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delete(List<Long> idList) {
		for (Long id : idList) {
			LambdaQueryWrapper<DataProductionTaskEntity> wrapper = new LambdaQueryWrapper<>();
			wrapper.eq(DataProductionTaskEntity::getClusterId, id).last(" limit 1");
			DataProductionTaskEntity task = taskDao.selectOne(wrapper);
			if (task != null) {
				throw new ServerException(String.format("要删除的集群实例中有数据生产任务【%s】与之关联，不可删除！", task.getName()));
			}
		}
		removeByIds(idList);
	}

	@Override
	public void heartbeat(List<Long> idList) {
		idList.forEach(id -> {
			DataProductionClusterEntity entity = baseMapper.selectById(id);
			checkHealth(entity);
			updateById(entity);
		});
	}

	@Override
	public List<DataProductionClusterEntity> listAll() {
		LambdaQueryWrapper<DataProductionClusterEntity> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(DataProductionClusterEntity::getEnabled, true).eq(DataProductionClusterEntity::getAutoRegisters, 0);
		dataScopeWithOrgId(wrapper);
		wrapper.orderByDesc(DataProductionClusterEntity::getCreateTime);
		wrapper.orderByDesc(DataProductionClusterEntity::getId);
		return baseMapper.selectList(wrapper);
	}

	@Override
	public String buildEnvironmentAddress(boolean useRemote, Integer clusterId) {
		if (useRemote && clusterId != null && clusterId != 0) {
			return buildRemoteEnvironmentAddress(clusterId);
		} else {
			return buildLocalEnvironmentAddress();
		}
	}

	@Override
	public String buildRemoteEnvironmentAddress(Integer id) {
		return getJobManagerAddress(getById(id));
	}

	@Override
	public String getJobManagerAddress(DataProductionClusterEntity cluster) {
		FlinkClusterInfo info = FlinkCluster.testFlinkJobManagerIP(cluster.getHosts(), cluster.getJobManagerHost());
		String host = null;
		if (info.isEffective()) {
			host = info.getJobManagerAddress();
		}
		if (host == null) {
			throw new ServerException("Get JobManagerAddress failed,please check if the flink cluster is running normally!");
		}
		if (!host.equals(cluster.getJobManagerHost())) {
			cluster.setJobManagerHost(host);
			updateById(cluster);
		}
		return host;
	}

	@Override
	public String buildLocalEnvironmentAddress() {
		try {
			InetAddress inetAddress = InetAddress.getLocalHost();
			if (inetAddress != null) {
				return inetAddress.getHostAddress() + NetConstant.COLON + FlinkConstant.FLINK_REST_DEFAULT_PORT;
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return FlinkConstant.LOCAL_HOST;
	}

	@Override
	public DataProductionClusterEntity registersCluster(DataProductionClusterEntity cluster) {
		checkHealth(cluster);
		cluster.setProjectId(cluster.getProjectId() == null ? getProjectId() : cluster.getProjectId());
		saveOrUpdate(cluster);
		return cluster;
	}

	@Override
	public Integer clearCluster() {
		List<DataProductionClusterEntity> clusters = listAuto();
		int count = 0;
		for (DataProductionClusterEntity item : clusters) {
			if ((!checkHealth(item)) && removeById(item)) {
				count++;
			}
		}
		return count;
	}

	@Override
	public List<DataProductionClusterEntity> listAuto() {
		return list(new LambdaQueryWrapper<DataProductionClusterEntity>().eq(DataProductionClusterEntity::getAutoRegisters, 1));
	}

	@Override
	public Integer buildJobManagePort(String address) {
		//获取JobManage端口
		JsonNode jobManagerConfig = FlinkAPI.build(address).getJobManagerConfig();
		Iterator<JsonNode> elements = jobManagerConfig.elements();
		while (elements.hasNext()) {
			JsonNode jsonNode = elements.next();
			if ("jobmanager.rpc.port".equals(jsonNode.get("key").asText())) {
				return jsonNode.get("value").asInt();
			}
		}
		throw new RuntimeException("build jobmanager port error，please check flink config if exists jobmanager.rpc.port！");
	}


	private boolean checkHealth(DataProductionClusterEntity cluster) {
		FlinkClusterInfo info = checkHeartBeat(cluster.getHosts(), cluster.getJobManagerHost());
		if (!info.isEffective()) {
			cluster.setJobManagerHost("");
			cluster.setStatus(0);
			return false;
		} else {
			cluster.setJobManagerHost(info.getJobManagerAddress());
			cluster.setStatus(1);
			cluster.setFlinkVersion(info.getVersion());
			return true;
		}
	}
}
