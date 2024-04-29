package net.srt.service;

import net.srt.api.module.data.governance.constant.DbType;
import net.srt.dto.SqlCheckResultDto;
import net.srt.dto.SqlExecuteDto;
import net.srt.entity.DataAssetsResourceMountEntity;
import net.srt.framework.common.page.PageResult;
import net.srt.framework.common.utils.TreeNodeVo;
import net.srt.framework.mybatis.service.BaseService;
import net.srt.query.DataAssetsResourceMountQuery;
import net.srt.vo.DataAssetsResourceMountVO;

import java.util.List;
import java.util.Map;

/**
 * 数据资产-资源挂载表
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-07-06
 */
public interface DataAssetsResourceMountService extends BaseService<DataAssetsResourceMountEntity> {

	PageResult<DataAssetsResourceMountVO> page(DataAssetsResourceMountQuery query);

	void save(DataAssetsResourceMountVO vo);

	void update(DataAssetsResourceMountVO vo);

	void delete(Long id);

	Map<DbType, List<TreeNodeVo>> getDbInfo(Long id, Integer queryApply);

	SqlCheckResultDto sqlCheck(SqlExecuteDto sqlExecuteDto);

	void check(Long resourceId);
}
