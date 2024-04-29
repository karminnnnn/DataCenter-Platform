package net.srt.convert;

import net.srt.entity.DataMarketResourceCommentEntity;
import net.srt.vo.DataMarketResourceCommentVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
* 数据集市-资源评价表
*
* @author zrx 985134801@qq.com
* @since 1.0.0 2023-08-27
*/
@Mapper
public interface DataMarketResourceCommentConvert {
    DataMarketResourceCommentConvert INSTANCE = Mappers.getMapper(DataMarketResourceCommentConvert.class);

    DataMarketResourceCommentEntity convert(DataMarketResourceCommentVO vo);

    DataMarketResourceCommentVO convert(DataMarketResourceCommentEntity entity);

    List<DataMarketResourceCommentVO> convertList(List<DataMarketResourceCommentEntity> list);

}