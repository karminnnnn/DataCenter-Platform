package net.srt.system.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.AllArgsConstructor;
import net.srt.framework.common.page.PageResult;
import net.srt.framework.mybatis.service.impl.BaseServiceImpl;
import net.srt.system.convert.SysLogLoginConvert;
import net.srt.system.convert.SysLogUserConvert;
import net.srt.system.dao.SysLogLoginDao;
import net.srt.system.dao.SysLogUserActivityDao;
import net.srt.system.entity.SysLogLoginEntity;
import net.srt.system.entity.SysLogUserActiveEntity;
import net.srt.system.query.SysOtherLogQuery;
import net.srt.system.service.SysLogService;
import net.srt.system.service.SysLogUserService;
import net.srt.system.vo.SysLogVo;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SysLogUserActivityServiceImpl extends BaseServiceImpl<SysLogUserActivityDao, SysLogUserActiveEntity> implements SysLogUserService {
    @Override
    public PageResult<SysLogVo> pageUser(SysOtherLogQuery query) {
        IPage<SysLogUserActiveEntity> page = baseMapper.selectPage(getPage(query), getOtherWrapper(query));
        return new PageResult<>(SysLogUserConvert.INSTANCE.convertList(page.getRecords()), page.getTotal());
    }

    private LambdaQueryWrapper<SysLogUserActiveEntity> getOtherWrapper(SysOtherLogQuery query) {
        LambdaQueryWrapper<SysLogUserActiveEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.like(StrUtil.isNotBlank(query.getUserName()), SysLogUserActiveEntity::getUserName, query.getUserName());
        return wrapper;
    }

}
