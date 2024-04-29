package net.srt.service;

import net.srt.entity.DataProductionJarEntity;
import net.srt.framework.common.page.PageResult;
import net.srt.framework.mybatis.service.BaseService;
import net.srt.query.DataProductionJarQuery;
import net.srt.vo.DataProductionJarVO;

import java.util.List;

/**
 * 数据生产-jar管理
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-11-13
 */
public interface DataProductionJarService extends BaseService<DataProductionJarEntity> {

    PageResult<DataProductionJarVO> page(DataProductionJarQuery query);

    void save(DataProductionJarVO vo);

    void update(DataProductionJarVO vo);

    void delete(List<Long> idList);

	List<DataProductionJarVO> listAllByRunType(Integer jarRunType);
}
