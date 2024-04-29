package net.srt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.AllArgsConstructor;
import net.srt.convert.DataGovernanceMasterDataCatalogConvert;
import net.srt.dao.DataGovernanceMasterDataCatalogDao;
import net.srt.dao.DataGovernanceMasterModelDao;
import net.srt.entity.DataGovernanceMasterDataCatalogEntity;
import net.srt.entity.DataGovernanceMasterModelEntity;
import net.srt.framework.common.exception.ServerException;
import net.srt.framework.common.page.PageResult;
import net.srt.framework.common.utils.BeanUtil;
import net.srt.framework.common.utils.BuildTreeUtils;
import net.srt.framework.common.utils.TreeNodeVo;
import net.srt.framework.mybatis.service.impl.BaseServiceImpl;
import net.srt.query.DataGovernanceMasterDataCatalogQuery;
import net.srt.service.DataGovernanceMasterDataCatalogService;
import net.srt.vo.DataGovernanceMasterDataCatalogVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import srt.cloud.framework.dbswitch.common.util.StringUtil;

import java.util.List;

/**
 * 数据治理-主数据目录
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-09-27
 */
@Service
@AllArgsConstructor
public class DataGovernanceMasterDataCatalogServiceImpl extends BaseServiceImpl<DataGovernanceMasterDataCatalogDao, DataGovernanceMasterDataCatalogEntity> implements DataGovernanceMasterDataCatalogService {


	private final DataGovernanceMasterModelDao masterModelDao;

	@Override
	public List<TreeNodeVo> listMasterModelTree() {
		LambdaQueryWrapper<DataGovernanceMasterDataCatalogEntity> wrapper = new LambdaQueryWrapper<>();
		dataScopeWithOrgId(wrapper);
		wrapper.orderByAsc(DataGovernanceMasterDataCatalogEntity::getOrderNo);
		List<DataGovernanceMasterDataCatalogEntity> catalogEntities = baseMapper.selectList(wrapper);
		List<TreeNodeVo> treeNodeVos = BeanUtil.copyListProperties(catalogEntities, TreeNodeVo::new, (oldItem, newItem) -> {
			newItem.setLabel(oldItem.getName());
			if (oldItem.getType() == 1) {
				//获取主数据id
				LambdaQueryWrapper<DataGovernanceMasterModelEntity> modelWrapper = Wrappers.lambdaQuery();
				modelWrapper.eq(DataGovernanceMasterModelEntity::getCatalogId, oldItem.getId()).eq(DataGovernanceMasterModelEntity::getStatus, 1)
						.last("limit 1");
				DataGovernanceMasterModelEntity modelEntity = masterModelDao.selectOne(modelWrapper);
				if (modelEntity != null) {
					//设置为主数据的id
					newItem.setValue(modelEntity.getId());
					newItem.setName(oldItem.getName() + "(" + modelEntity.getTableName() + ")");
				} else {
					newItem.setDisabled(true);
					newItem.setValue(oldItem.getId());
				}
			} else {
				newItem.setDisabled(true);
				newItem.setValue(oldItem.getId());
			}
			if (newItem.getPath().contains("/")) {
				newItem.setParentPath(newItem.getPath().substring(0, newItem.getPath().lastIndexOf("/")));
			}
		});
		return BuildTreeUtils.buildTree(treeNodeVos);
	}

	@Override
	public List<TreeNodeVo> listTree() {
		LambdaQueryWrapper<DataGovernanceMasterDataCatalogEntity> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(DataGovernanceMasterDataCatalogEntity::getProjectId, getProjectId()).orderByAsc(DataGovernanceMasterDataCatalogEntity::getOrderNo);
		List<DataGovernanceMasterDataCatalogEntity> catalogEntities = baseMapper.selectList(wrapper);
		List<TreeNodeVo> treeNodeVos = BeanUtil.copyListProperties(catalogEntities, TreeNodeVo::new, (oldItem, newItem) -> {
			newItem.setLabel(oldItem.getName());
			newItem.setValue(oldItem.getId());
			if (newItem.getPath().contains("/")) {
				newItem.setParentPath(newItem.getPath().substring(0, newItem.getPath().lastIndexOf("/")));
			}
		});
		return BuildTreeUtils.buildTree(treeNodeVos);
	}

	@Override
	public PageResult<DataGovernanceMasterDataCatalogVO> page(DataGovernanceMasterDataCatalogQuery query) {
		IPage<DataGovernanceMasterDataCatalogEntity> page = baseMapper.selectPage(getPage(query), getWrapper(query));

		return new PageResult<>(DataGovernanceMasterDataCatalogConvert.INSTANCE.convertList(page.getRecords()), page.getTotal());
	}

	private LambdaQueryWrapper<DataGovernanceMasterDataCatalogEntity> getWrapper(DataGovernanceMasterDataCatalogQuery query) {
		LambdaQueryWrapper<DataGovernanceMasterDataCatalogEntity> wrapper = Wrappers.lambdaQuery();

		return wrapper;
	}

	@Override
	public void save(DataGovernanceMasterDataCatalogVO vo) {
		DataGovernanceMasterDataCatalogEntity entity = DataGovernanceMasterDataCatalogConvert.INSTANCE.convert(vo);
		entity.setProjectId(getProjectId());
		entity.setPath(recursionPath(entity, null));
		baseMapper.insert(entity);
	}

	@Override
	public void update(DataGovernanceMasterDataCatalogVO vo) {
		DataGovernanceMasterDataCatalogEntity entity = DataGovernanceMasterDataCatalogConvert.INSTANCE.convert(vo);
		entity.setProjectId(getProjectId());
		entity.setPath(recursionPath(entity, null));
		updateById(entity);
		//修改主数据的orgId
		LambdaUpdateWrapper<DataGovernanceMasterModelEntity> wrapper = Wrappers.lambdaUpdate();
		wrapper.eq(DataGovernanceMasterModelEntity::getCatalogId, vo.getId());
		wrapper.set(DataGovernanceMasterModelEntity::getOrgId, vo.getOrgId());
		masterModelDao.update(null, wrapper);
	}

	private String recursionPath(DataGovernanceMasterDataCatalogEntity catalogEntity, String path) {
		if (StringUtil.isBlank(path)) {
			path = catalogEntity.getName();
		}
		if (catalogEntity.getParentId() != 0) {
			DataGovernanceMasterDataCatalogEntity parent = getById(catalogEntity.getParentId());
			path = parent.getName() + "/" + path;
			return recursionPath(parent, path);
		}
		return path;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delete(Long id) {
		DataGovernanceMasterDataCatalogEntity catalogEntity = baseMapper.selectById(id);
		if (catalogEntity.getType() == 0) {
			//判断是否有子项目
			LambdaQueryWrapper<DataGovernanceMasterDataCatalogEntity> wrapper = Wrappers.lambdaQuery();
			wrapper.eq(DataGovernanceMasterDataCatalogEntity::getParentId, id).last("limit 1");
			if (baseMapper.selectOne(wrapper) != null) {
				throw new ServerException("存在子节点，不可删除！");
			}
		} else {
			LambdaQueryWrapper<DataGovernanceMasterModelEntity> wrapper = Wrappers.lambdaQuery();
			wrapper.eq(DataGovernanceMasterModelEntity::getCatalogId, id).last("limit 1");
			DataGovernanceMasterModelEntity modelEntity = masterModelDao.selectOne(wrapper);
			if (modelEntity != null) {
				throw new ServerException("该目录下存在主数据模型，不可删除！");
			}
		}
		removeById(id);
	}

}
