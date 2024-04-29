package net.srt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import net.srt.api.module.data.integrate.DataProjectApi;
import net.srt.config.RedisMessageConfig;
import net.srt.convert.DataProductionSysConfigConvert;
import net.srt.dao.DataProductionSysConfigDao;
import net.srt.entity.DataProductionSysConfigEntity;
import net.srt.flink.common.assertion.Asserts;
import net.srt.flink.common.model.ProjectSystemConfiguration;
import net.srt.flink.common.model.SystemConfiguration;
import net.srt.framework.common.cache.RedisCache;
import net.srt.framework.common.cache.bean.DataProjectCacheBean;
import net.srt.framework.mybatis.service.impl.BaseServiceImpl;
import net.srt.service.DataProductionSysConfigService;
import net.srt.vo.DataProductionSysConfigVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import srt.cloud.framework.dbswitch.common.util.SingletonObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 数据生产-配置中心
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2022-12-26
 */
@Service
@AllArgsConstructor
public class DataProductionSysConfigServiceImpl extends BaseServiceImpl<DataProductionSysConfigDao, DataProductionSysConfigEntity> implements DataProductionSysConfigService {

	private final DataProjectApi dataProjectApi;
	private final RedisCache redisCache;

	@Override
	public void save(DataProductionSysConfigVO vo) {
		DataProductionSysConfigEntity entity = DataProductionSysConfigConvert.INSTANCE.convert(vo);
		entity.setProjectId(getProjectId());
		baseMapper.insert(entity);
	}

	@Override
	public void update(DataProductionSysConfigVO vo) {
		DataProductionSysConfigEntity entity = DataProductionSysConfigConvert.INSTANCE.convert(vo);
		entity.setProjectId(getProjectId());
		updateById(entity);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delete(List<Long> idList) {
		removeByIds(idList);
	}

	@Override
	public Map<String, Object> getAll() {
		Map<String, Object> map = getByProjectId(getProjectId());
		SystemConfiguration systemConfiguration = ProjectSystemConfiguration.getByProjectId(getProjectId());
		systemConfiguration.addConfiguration(map);
		return map;
	}

	@Override
	public Map<String, Object> getAll(Long projectId) {
		Map<String, Object> map = getByProjectId(projectId);
		SystemConfiguration systemConfiguration = ProjectSystemConfiguration.getByProjectId(projectId);
		systemConfiguration.addConfiguration(map);
		return map;
	}

	private Map<String, Object> getByProjectId(Long projectId) {
		Map<String, Object> map = new HashMap<>();
		LambdaQueryWrapper<DataProductionSysConfigEntity> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(DataProductionSysConfigEntity::getProjectId, projectId);
		List<DataProductionSysConfigEntity> sysConfigs = list(wrapper);
		for (DataProductionSysConfigEntity item : sysConfigs) {
			map.put(item.getName(), item.getValue());
		}
		return map;
	}

	@Override
	public void updateSysConfigByJson(JsonNode node) {
		Long projectId = getProjectId();
		if (node != null && node.isObject()) {
			Iterator<Map.Entry<String, JsonNode>> it = node.fields();
			while (it.hasNext()) {
				Map.Entry<String, JsonNode> entry = it.next();
				String name = entry.getKey();
				String value = entry.getValue().asText();
				DataProductionSysConfigEntity config = getOne(new QueryWrapper<DataProductionSysConfigEntity>().eq("name", name).eq("project_id", projectId));
				DataProductionSysConfigEntity newConfig = new DataProductionSysConfigEntity();
				newConfig.setValue(value);
				if (Asserts.isNull(config)) {
					newConfig.setName(name);
					newConfig.setProjectId(projectId);
					save(newConfig);
				} else {
					newConfig.setId(config.getId());
					newConfig.setProjectId(projectId);
					updateById(newConfig);
				}
			}
		}
		SystemConfiguration systemConfiguration = ProjectSystemConfiguration.getByProjectId(projectId);
		systemConfiguration.setConfiguration(node);
		//通知各节点更新
		redisCache.convertAndSend(RedisMessageConfig.DATA_PRO_SYS_CONFIG_TOPIC, RedisMessageConfig.DATA_PRO_SYS_CONFIG_TOPIC);
	}

	@Override
	public void initSysConfig() {
		//初始化每个项目（租户）的配置中心
		List<DataProjectCacheBean> projects = dataProjectApi.getProjectList().getData();
		for (DataProjectCacheBean project : projects) {
			ProjectSystemConfiguration.getByProjectId(project.getId()).setConfiguration(SingletonObject.OBJECT_MAPPER.valueToTree(getAll(project.getId())));
		}
	}

}
