package net.srt.convert;

import net.srt.entity.DataMarketResourceApplyEntity;
import net.srt.vo.DataMarketResourceApplyVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 数据集市-资源申请表
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-07-26
 */
@Mapper
public interface DataMarketResourceApplyConvert {
	DataMarketResourceApplyConvert INSTANCE = Mappers.getMapper(DataMarketResourceApplyConvert.class);

	DataMarketResourceApplyEntity convert(DataMarketResourceApplyVO vo);

	DataMarketResourceApplyVO convert(DataMarketResourceApplyEntity entity);

	List<DataMarketResourceApplyVO> convertList(List<DataMarketResourceApplyEntity> list);
}
