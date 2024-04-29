package net.srt.service;

import net.srt.entity.DataGovernanceMasterColumnEntity;
import net.srt.framework.mybatis.service.BaseService;
import net.srt.vo.DataGovernanceMasterColumnVO;

import java.util.List;

/**
 * 数据治理-主数据模型字段
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-09-27
 */
public interface DataGovernanceMasterColumnService extends BaseService<DataGovernanceMasterColumnEntity> {

	void save(DataGovernanceMasterColumnVO vo);

	void update(DataGovernanceMasterColumnVO vo);

	void delete(List<Long> idList);

	List<DataGovernanceMasterColumnVO> middleDbClumnInfo(String tableName);
}
