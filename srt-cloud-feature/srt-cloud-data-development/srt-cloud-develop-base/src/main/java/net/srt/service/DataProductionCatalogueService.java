package net.srt.service;

import net.srt.entity.DataProductionCatalogueEntity;
import net.srt.framework.common.utils.TreeNodeVo;
import net.srt.framework.mybatis.service.BaseService;
import net.srt.vo.DataProductionCatalogueVO;

import java.util.List;

/**
 * 数据生产目录
 *
 * @author zrx 985134801@qq.com
 * @since 2.0.0 2022-11-27
 */
public interface DataProductionCatalogueService extends BaseService<DataProductionCatalogueEntity> {

    void save(DataProductionCatalogueVO vo);

    void update(DataProductionCatalogueVO vo);

    void delete(Long id);

	List<TreeNodeVo> listTree();
}
