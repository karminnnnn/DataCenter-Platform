package net.srt.service.impl;

import lombok.AllArgsConstructor;
import net.srt.dao.VisualizeInfoDao;
import net.srt.entity.VisualizeInfoEntity;
import net.srt.framework.mybatis.service.impl.BaseServiceImpl;
import net.srt.service.VisualizeInfoService;
import net.srt.vo.VisualizeInfoListVO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class VisualizeInfoServiceImpl extends BaseServiceImpl<VisualizeInfoDao, VisualizeInfoEntity> implements VisualizeInfoService {
    private final VisualizeInfoDao visualizeInfoDao;
    public VisualizeInfoListVO getVisualizeInfoList() {
        VisualizeInfoListVO visualizeInfoListVO = new VisualizeInfoListVO();
        List<List<VisualizeInfoEntity>> list = new ArrayList<>();
        //visualizeInfoListVO.setList(VisualizeInfoConvert.INSTANCE.convertList(visualizeInfoDao.selectAll()));
        List<VisualizeInfoEntity> visualizeInfoEntityList = visualizeInfoDao.selectAll();
        for(int i = 0;i < 5;i++){
            List<VisualizeInfoEntity>entityList = new ArrayList<>();
            list.add(entityList);
        }
        for(VisualizeInfoEntity entity : visualizeInfoEntityList){
            list.get(entity.getKind()-1).add(entity);
        }
        visualizeInfoListVO.setList(list);
        return visualizeInfoListVO;
    }
}
