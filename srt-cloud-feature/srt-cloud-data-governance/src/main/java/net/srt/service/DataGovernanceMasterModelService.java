package net.srt.service;

import net.srt.entity.DataGovernanceMasterModelEntity;
import net.srt.framework.mybatis.service.BaseService;
import net.srt.vo.DataGovernanceMasterModelVO;

import java.util.List;

/**
 * 数据治理-主数据模型
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-09-27
 */
public interface DataGovernanceMasterModelService extends BaseService<DataGovernanceMasterModelEntity> {

	DataGovernanceMasterModelEntity save(DataGovernanceMasterModelVO vo);

	DataGovernanceMasterModelEntity update(DataGovernanceMasterModelVO vo);

	void delete(Long id);

	DataGovernanceMasterModelEntity getByCatalogId(Long catalogId);

	DataGovernanceMasterModelEntity release(Long id);

	DataGovernanceMasterModelEntity cancelRelease(Long id);
}
