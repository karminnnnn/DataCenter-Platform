package net.srt.system.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.AllArgsConstructor;
import net.srt.framework.common.page.PageResult;
import net.srt.framework.mybatis.service.impl.BaseServiceImpl;
import net.srt.system.convert.SysLogUserConvert;
import net.srt.system.dao.SysLogSysActivityDao;
import net.srt.system.dao.SysLogUserActivityDao;
import net.srt.system.entity.SysLogSysActiveEntity;
import net.srt.system.entity.SysLogUserActiveEntity;
import net.srt.system.query.SysOtherLogQuery;
import net.srt.system.service.SysLogSysService;
import net.srt.system.service.SysLogUserService;
import net.srt.system.vo.SysLogVo;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SysLogSysActivityServiceImpl extends BaseServiceImpl<SysLogSysActivityDao, SysLogSysActiveEntity> implements SysLogSysService {
    @Override
    public PageResult<SysLogVo> pageSys(SysOtherLogQuery query) {
        IPage<SysLogSysActiveEntity> page = baseMapper.selectPage(getPage(query), getOtherWrapper(query));
        return new PageResult<>(SysLogUserConvert.INSTANCE.convertList2(page.getRecords()), page.getTotal());
    }

    private LambdaQueryWrapper<SysLogSysActiveEntity> getOtherWrapper(SysOtherLogQuery query) {
        LambdaQueryWrapper<SysLogSysActiveEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.like(StrUtil.isNotBlank(query.getUserName()), SysLogSysActiveEntity::getUserName, query.getUserName());
        return wrapper;
    }

}
