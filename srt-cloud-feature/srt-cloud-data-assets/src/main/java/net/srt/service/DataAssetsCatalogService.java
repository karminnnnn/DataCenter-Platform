package net.srt.service;

import net.srt.framework.common.utils.TreeNodeVo;
import net.srt.framework.mybatis.service.BaseService;
import net.srt.vo.DataAssetsCatalogVO;
import net.srt.entity.DataAssetsCatalogEntity;

import java.util.List;

/**
 * 数据资产-资产目录
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-07-04
 */
public interface DataAssetsCatalogService extends BaseService<DataAssetsCatalogEntity> {

	List<TreeNodeVo> listTree();

    void save(DataAssetsCatalogVO vo);

    void update(DataAssetsCatalogVO vo);

    void delete(Long id);

}
