package net.srt.service;

import net.srt.framework.common.page.PageResult;
import net.srt.framework.common.utils.TreeNodeVo;
import net.srt.framework.mybatis.service.BaseService;
import net.srt.vo.DataGovernanceMasterDataCatalogVO;
import net.srt.query.DataGovernanceMasterDataCatalogQuery;
import net.srt.entity.DataGovernanceMasterDataCatalogEntity;

import java.util.List;

/**
 * 数据治理-主数据目录
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-09-27
 */
public interface DataGovernanceMasterDataCatalogService extends BaseService<DataGovernanceMasterDataCatalogEntity> {

	List<TreeNodeVo> listMasterModelTree();

	List<TreeNodeVo> listTree();

	PageResult<DataGovernanceMasterDataCatalogVO> page(DataGovernanceMasterDataCatalogQuery query);

	void save(DataGovernanceMasterDataCatalogVO vo);

	void update(DataGovernanceMasterDataCatalogVO vo);

	void delete(Long id);

}
