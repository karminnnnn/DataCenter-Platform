package net.srt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.AllArgsConstructor;
import net.srt.convert.DataServiceApiAuthConvert;
import net.srt.convert.DataServiceAppConvert;
import net.srt.dao.DataServiceApiAuthDao;
import net.srt.dao.DataServiceAppDao;
import net.srt.dto.AppToken;
import net.srt.entity.DataServiceApiAuthEntity;
import net.srt.entity.DataServiceAppEntity;
import net.srt.flink.common.utils.JSONUtil;
import net.srt.framework.common.cache.RedisCache;
import net.srt.framework.common.cache.RedisKeys;
import net.srt.framework.common.exception.ServerException;
import net.srt.framework.common.page.PageResult;
import net.srt.framework.mybatis.service.impl.BaseServiceImpl;
import net.srt.framework.security.user.SecurityUser;
import net.srt.query.DataServiceAppQuery;
import net.srt.service.DataServiceAppService;
import net.srt.vo.DataServiceApiAuthVO;
import net.srt.vo.DataServiceAppVO;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import srt.cloud.framework.dbswitch.common.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据服务-app应用
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-02-16
 */
@Service
@AllArgsConstructor
public class DataServiceAppServiceImpl extends BaseServiceImpl<DataServiceAppDao, DataServiceAppEntity> implements DataServiceAppService {

	private final DataServiceApiAuthDao apiAuthDao;
	private final RedisCache redisCache;

	@Override
	public PageResult<DataServiceAppVO> page(DataServiceAppQuery query) {
		if (query.getApplyId() != null) {
			DataServiceAppEntity dataServiceAppEntity = baseMapper.selectByApplyId(query.getApplyId());
			List<DataServiceAppEntity> list = new ArrayList<>(1);
			if (dataServiceAppEntity != null) {
				list.add(dataServiceAppEntity);
				return new PageResult<>(DataServiceAppConvert.INSTANCE.convertList(list), 1);
			}
			return new PageResult<>(new ArrayList<>(), 0);
		}
		IPage<DataServiceAppEntity> page = baseMapper.selectPage(getPage(query), getWrapper(query));

		return new PageResult<>(DataServiceAppConvert.INSTANCE.convertList(page.getRecords()), page.getTotal());
	}

	private LambdaQueryWrapper<DataServiceAppEntity> getWrapper(DataServiceAppQuery query) {
		LambdaQueryWrapper<DataServiceAppEntity> wrapper = Wrappers.lambdaQuery();
		wrapper.like(StringUtil.isNotBlank(query.getName()), DataServiceAppEntity::getName, query.getName())
				.eq(DataServiceAppEntity::getIfMarket, query.getIfMarket() != null ? query.getIfMarket() : 0)
				.eq(query.getIfMarket() != null, DataServiceAppEntity::getCreator, SecurityUser.getUserId())
				.eq(StringUtil.isNotBlank(query.getAppKey()), DataServiceAppEntity::getAppKey, query.getAppKey())
				.orderByDesc(DataServiceAppEntity::getCreateTime).orderByDesc(DataServiceAppEntity::getId);
		if (query.getIfMarket() == null) {
			dataScopeWithOrgId(wrapper);
		} else {
			//个人应用没有部门id
			dataScopeWithoutOrgId(wrapper);
		}
		return wrapper;
	}

	@Override
	public void save(DataServiceAppVO vo) {
		DataServiceAppEntity app = DataServiceAppConvert.INSTANCE.convert(vo);
		buildApp(app);
		baseMapper.insert(app);
	}


	@Override
	public void update(DataServiceAppVO vo) {
		DataServiceAppEntity app = DataServiceAppConvert.INSTANCE.convert(vo);
		buildApp(app);
		updateById(app);
	}

	private void buildApp(DataServiceAppEntity app) {
		app.setProjectId(getProjectId());
		if (app.getId() == null) {
			app.setAppKey(RandomStringUtils.random(16, true, true));
			app.setAppSecret(RandomStringUtils.random(32, true, true));
		}
		switch (app.getExpireDesc()) {
			case "10min":
				app.setExpireDuration(10 * 60L);
				break;
			case "1hour":
				app.setExpireDuration(60 * 60L);
				break;
			case "1day":
				app.setExpireDuration(60 * 60 * 24L);
				break;
			case "30day":
				app.setExpireDuration(60 * 60 * 24 * 30L);
				break;
			case "once":
				app.setExpireDuration(0L);
				break;
			case "forever":
				app.setExpireDuration(-1L);
				break;
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delete(List<Long> idList) {
		removeByIds(idList);
		//判断是否有app授权与之关联
		for (Long appId : idList) {
			LambdaQueryWrapper<DataServiceApiAuthEntity> wrapper = new LambdaQueryWrapper<>();
			wrapper.eq(DataServiceApiAuthEntity::getAppId, appId).last(" limit 1");
			DataServiceApiAuthEntity authEntity = apiAuthDao.selectOne(wrapper);
			if (authEntity != null) {
				throw new ServerException("该应用下有与之关联授权的 api，不可删除！");
			}
		}
	}

	@Override
	public void addAuth(DataServiceApiAuthVO authVO) {
		authVO.setProjectId(getProjectId());
		apiAuthDao.insert(DataServiceApiAuthConvert.INSTANCE.convert(authVO));
	}

	@Override
	public void cancelAuth(Long authId) {
		apiAuthDao.deleteById(authId);
	}

	@Override
	public String tokenGenerate(String appKey, String appSecret) {
		LambdaQueryWrapper<DataServiceAppEntity> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(DataServiceAppEntity::getAppKey, appKey).last(" limit 1");
		DataServiceAppEntity appEntity = baseMapper.selectOne(wrapper);
		if (appEntity == null) {
			throw new ServerException("appKey 不存在！");
		}
		if (!appSecret.equals(appEntity.getAppSecret())) {
			throw new ServerException("密钥有误，请检查！");
		}
		//生成token
		String token = RandomStringUtils.random(32, true, true);
		AppToken appToken = new AppToken();
		appToken.setToken(token);
		appToken.setAppKey(appKey);
		appToken.setAppId(appEntity.getId());
		//单次失效
		if (appEntity.getExpireDuration() == 0) {
			appToken.setExpireAt(0L);
		}
		// 永久有效
		else if (appEntity.getExpireDuration() == -1) {
			appToken.setExpireAt(-1L);
		}
		// 设置了有效的失效时间
		else if (appEntity.getExpireDuration() > 0) {
			long expireAt = System.currentTimeMillis() + appEntity.getExpireDuration() * 1000;
			appToken.setExpireAt(expireAt);
		}

		redisCache.set(RedisKeys.getAppTokenKey(token), JSONUtil.toJsonString(appToken), RedisCache.NOT_EXPIRE);

		//clean old token
		String oldToken = (String) redisCache.get(RedisKeys.getAppIdKey(appEntity.getId()));
		if (StringUtil.isNotBlank(oldToken)) {
			redisCache.delete(RedisKeys.getAppTokenKey(oldToken));
		}
		//appid和最新token关系记录下来,便于下次可以找到旧token可以删除，否则缓存中token越来越多
		redisCache.set(RedisKeys.getAppIdKey(appEntity.getId()), token, RedisCache.NOT_EXPIRE);

		return appToken.getToken();
	}

	@Override
	public void upAuth(DataServiceApiAuthVO authVO) {
		apiAuthDao.updateById(DataServiceApiAuthConvert.INSTANCE.convert(authVO));
	}

}
