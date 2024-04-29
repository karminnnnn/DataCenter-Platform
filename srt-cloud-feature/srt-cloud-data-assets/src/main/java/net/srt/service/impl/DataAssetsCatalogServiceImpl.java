package net.srt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.AllArgsConstructor;
import net.srt.convert.DataAssetsCatalogConvert;
import net.srt.dao.DataAssetsCatalogDao;
import net.srt.dao.DataAssetsResourceDao;
import net.srt.entity.DataAssetsCatalogEntity;
import net.srt.entity.DataAssetsResourceEntity;
import net.srt.framework.common.exception.ServerException;
import net.srt.framework.common.utils.BeanUtil;
import net.srt.framework.common.utils.BuildTreeUtils;
import net.srt.framework.common.utils.TreeNodeVo;
import net.srt.framework.mybatis.service.impl.BaseServiceImpl;
import net.srt.service.DataAssetsCatalogService;
import net.srt.vo.DataAssetsCatalogVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import srt.cloud.framework.dbswitch.common.util.StringUtil;

import java.util.List;

/**
 * 数据资产-资产目录
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-07-04
 */
@Service
@AllArgsConstructor
public class DataAssetsCatalogServiceImpl extends BaseServiceImpl<DataAssetsCatalogDao, DataAssetsCatalogEntity> implements DataAssetsCatalogService {

	private final DataAssetsResourceDao resourceDao;

	@Override
	public List<TreeNodeVo> listTree() {
		LambdaQueryWrapper<DataAssetsCatalogEntity> wrapper = new LambdaQueryWrapper<>();
		wrapper.orderByAsc(DataAssetsCatalogEntity::getOrderNo);
		dataScopeWithOrgId(wrapper);
		List<DataAssetsCatalogEntity> dataAssetsCatalogEntities = baseMapper.selectList(wrapper);
		List<TreeNodeVo> treeNodeVos = BeanUtil.copyListProperties(dataAssetsCatalogEntities, TreeNodeVo::new, (oldItem, newItem) -> {
			newItem.setLabel(oldItem.getName());
			newItem.setValue(oldItem.getId());
			if (newItem.getPath().contains("/")) {
				newItem.setParentPath(newItem.getPath().substring(0, newItem.getPath().lastIndexOf("/")));
			}
		});
		return BuildTreeUtils.buildTree(treeNodeVos);
	}

	@Override
	public void save(DataAssetsCatalogVO vo) {
		DataAssetsCatalogEntity entity = DataAssetsCatalogConvert.INSTANCE.convert(vo);
		entity.setPath(recursionPath(entity, null));
		entity.setProjectId(getProjectId());
		baseMapper.insert(entity);
	}

	@Override
	public void update(DataAssetsCatalogVO vo) {
		DataAssetsCatalogEntity entity = DataAssetsCatalogConvert.INSTANCE.convert(vo);
		entity.setPath(recursionPath(entity, null));
		entity.setProjectId(getProjectId());
		updateById(entity);
		//更新资源的orgId
		LambdaUpdateWrapper<DataAssetsResourceEntity> wrapper = Wrappers.lambdaUpdate();
		wrapper.eq(DataAssetsResourceEntity::getCatalogId, vo.getId());
		wrapper.set(DataAssetsResourceEntity::getOrgId, vo.getOrgId());
		resourceDao.update(null, wrapper);
	}

	private String recursionPath(DataAssetsCatalogEntity catalogEntity, String path) {
		if (StringUtil.isBlank(path)) {
			path = catalogEntity.getName();
		}
		if (catalogEntity.getParentId() != 0) {
			DataAssetsCatalogEntity parent = getById(catalogEntity.getParentId());
			path = parent.getName() + "/" + path;
			return recursionPath(parent, path);
		}
		return path;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delete(Long id) {
		//判断是否有子项目
		LambdaQueryWrapper<DataAssetsCatalogEntity> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(DataAssetsCatalogEntity::getParentId, id).last("limit 1");
		if (baseMapper.selectOne(wrapper) != null) {
			throw new ServerException("存在子节点，不可删除！");
		}
		LambdaQueryWrapper<DataAssetsResourceEntity> resourceWrapper = new LambdaQueryWrapper<>();
		resourceWrapper.eq(DataAssetsResourceEntity::getCatalogId, id).last("limit 1");
		if (resourceDao.selectOne(resourceWrapper) != null) {
			throw new ServerException("目录下存在数据资产，不可删除！");
		}
		removeById(id);
	}

}
