package net.srt.service;

import net.srt.entity.DataServiceApiGroupEntity;
import net.srt.framework.common.utils.TreeNodeVo;
import net.srt.framework.mybatis.service.BaseService;
import net.srt.vo.DataServiceApiGroupVO;

import java.util.List;

/**
 * 数据服务-api分组
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-01-28
 */
public interface DataServiceApiGroupService extends BaseService<DataServiceApiGroupEntity> {

    void save(DataServiceApiGroupVO vo);

    void update(DataServiceApiGroupVO vo);

    void delete(Long id);

	List<TreeNodeVo> listTree();

	List<TreeNodeVo> listTreeWithApi();
}
