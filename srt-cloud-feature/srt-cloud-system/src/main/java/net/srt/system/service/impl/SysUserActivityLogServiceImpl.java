package net.srt.system.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.AllArgsConstructor;
import net.srt.framework.common.page.PageResult;
import net.srt.framework.mybatis.service.impl.BaseServiceImpl;
import net.srt.system.convert.SysLogLoginConvert;
import net.srt.system.convert.SysUserActivityLogConvert;
import net.srt.system.dao.SysUserActivityLogDao;
import net.srt.system.entity.SysLogLoginEntity;
import net.srt.system.entity.SysUserActivityLogEntity;
import net.srt.system.query.SysLogLoginQuery;
import net.srt.system.query.SysUserActivityLogQuery;
import net.srt.system.service.SysUserActivityLogService;
import net.srt.system.vo.SysLogLoginVO;
import net.srt.system.vo.SysUserActivityLogVO;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SysUserActivityLogServiceImpl extends BaseServiceImpl<SysUserActivityLogDao, SysUserActivityLogEntity> implements SysUserActivityLogService {
    @Override
    public PageResult<SysUserActivityLogVO> page(SysUserActivityLogQuery query) {
        IPage<SysUserActivityLogEntity> page = baseMapper.selectPage(getPage(query), getWrapper(query));

        return new PageResult<>(SysUserActivityLogConvert.INSTANCE.convertList(page.getRecords()), page.getTotal());
    }

    private LambdaQueryWrapper<SysUserActivityLogEntity> getWrapper(SysUserActivityLogQuery query) {
        LambdaQueryWrapper<SysUserActivityLogEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.like(StrUtil.isNotBlank(query.getUsername()), SysUserActivityLogEntity::getUsername, query.getUsername());
        wrapper.orderByDesc(SysUserActivityLogEntity::getId);
        return wrapper;
    }

    @Override
    public void save(String username,  Integer action){
        SysUserActivityLogEntity entity = new SysUserActivityLogEntity();
        entity.setUsername(username);
        entity.setAction(action);
        baseMapper.insert(entity);
    }
}
