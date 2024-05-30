package net.srt.convert;

import net.srt.api.module.data.integrate.dto.DataTableDto;
import net.srt.entity.DataTableEntity;
import net.srt.vo.DataTableVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 数据集成-贴源数据
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2022-11-07
 */
@Mapper
public interface DataOdsConvert {
	DataOdsConvert INSTANCE = Mappers.getMapper(DataOdsConvert.class);

	@Mapping(source = "datatableId", target = "id")
	@Mapping(source = "datatableName", target = "tableName")
	@Mapping(source = "remarks", target = "remarks")
	@Mapping(source = "recentlySyncTime", target = "recentlySyncTime")
	@Mapping(source = "databaseId", target = "databaseId")
	@Mapping(source = "dataAccessId", target = "dataAccessId")
	@Mapping(source = "projectId", target = "projectId")
	DataTableEntity convert(DataTableVO vo);

	@Mapping(source = "datatableId", target = "id")
	@Mapping(source = "datatableName", target = "tableName")
	@Mapping(source = "remarks", target = "remarks")
	@Mapping(source = "recentlySyncTime", target = "recentlySyncTime")
	@Mapping(source = "databaseId", target = "databaseId")
	@Mapping(source = "dataAccessId", target = "dataAccessId")
	@Mapping(source = "projectId", target = "projectId")
	DataTableEntity convertByDto(DataTableDto dto);

	@Mapping(source = "id", target = "datatableId")
	@Mapping(source = "tableName", target = "datatableName")
	@Mapping(source = "remarks", target = "remarks")
	@Mapping(source = "recentlySyncTime", target = "recentlySyncTime")
	@Mapping(source = "databaseId", target = "databaseId")
	@Mapping(source = "dataAccessId", target = "dataAccessId")
	@Mapping(source = "projectId", target = "projectId")
	DataTableVO convert(DataTableEntity entity);

	@Mapping(source = "id", target = "datatableId")
	@Mapping(source = "tableName", target = "datatableName")
	@Mapping(source = "remarks", target = "remarks")
	@Mapping(source = "recentlySyncTime", target = "recentlySyncTime")
	@Mapping(source = "databaseId", target = "databaseId")
	@Mapping(source = "dataAccessId", target = "dataAccessId")
	@Mapping(source = "projectId", target = "projectId")
	List<DataTableVO> convertList(List<DataTableEntity> list);

	@Mapping(source = "id", target = "datatableId")
	@Mapping(source = "tableName", target = "datatableName")
	@Mapping(source = "remarks", target = "remarks")
	@Mapping(source = "recentlySyncTime", target = "recentlySyncTime")
	@Mapping(source = "databaseId", target = "databaseId")
	@Mapping(source = "dataAccessId", target = "dataAccessId")
	@Mapping(source = "projectId", target = "projectId")
	DataTableDto entityToDto(DataTableEntity entity);

}
