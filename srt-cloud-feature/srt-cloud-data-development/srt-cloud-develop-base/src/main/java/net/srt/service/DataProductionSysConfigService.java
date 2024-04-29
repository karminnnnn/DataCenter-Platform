package net.srt.service;

import com.fasterxml.jackson.databind.JsonNode;
import net.srt.entity.DataProductionSysConfigEntity;
import net.srt.framework.mybatis.service.BaseService;
import net.srt.vo.DataProductionSysConfigVO;

import java.util.List;
import java.util.Map;

/**
 * 数据生产-配置中心
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2022-12-26
 */
public interface DataProductionSysConfigService extends BaseService<DataProductionSysConfigEntity> {

    void save(DataProductionSysConfigVO vo);

    void update(DataProductionSysConfigVO vo);

    void delete(List<Long> idList);

	Map<String, Object> getAll();

	Map<String, Object> getAll(Long projectId);

	void updateSysConfigByJson(JsonNode para);

	void initSysConfig();
}
