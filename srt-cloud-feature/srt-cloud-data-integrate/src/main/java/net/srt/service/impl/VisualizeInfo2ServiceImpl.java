package net.srt.service.impl;

import lombok.AllArgsConstructor;
import net.srt.dao.VisualizeInfo2Dao;
import net.srt.dao.VisualizeInfoDao;
import net.srt.entity.VisualizeInfo2Entity;
import net.srt.entity.VisualizeInfoEntity;
import net.srt.framework.mybatis.service.impl.BaseServiceImpl;
import net.srt.service.VisualizeInfo2Service;
import net.srt.vo.VisualizeInfo2ListVO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class VisualizeInfo2ServiceImpl extends BaseServiceImpl<VisualizeInfo2Dao, VisualizeInfo2Entity> implements VisualizeInfo2Service {
    private final VisualizeInfo2Dao visualizeInfo2Dao;
    public VisualizeInfo2ListVO getInfoByNetId(int NetId) {
        VisualizeInfo2ListVO visualizeInfo2ListVO = new VisualizeInfo2ListVO();
        VisualizeInfo2Entity entity1 = visualizeInfo2Dao.selectByNetId(NetId);
        VisualizeInfo2Entity entity2 = visualizeInfo2Dao.getAvgByClassId(entity1.getClassId());
        VisualizeInfo2Entity entity3 = visualizeInfo2Dao.getAvgByGradeId(entity1.getGradeId());
        VisualizeInfo2Entity entity4 = visualizeInfo2Dao.getMaxByClassId(entity1.getClassId());

        System.out.println("entity2: " + entity2.getPubCoursePerf());

        List<VisualizeInfo2Entity>list = new ArrayList<>();
        list.add(entity1);
        list.add(entity2);
        list.add(entity3);
        list.add(entity4);
        visualizeInfo2ListVO.setList(list);
        return visualizeInfo2ListVO;
    }

    public VisualizeInfo2ListVO getAll(){
        VisualizeInfo2ListVO visualizeInfo2ListVO = new VisualizeInfo2ListVO();
        List<VisualizeInfo2Entity>list = new ArrayList<>();
        // grade:1-4为本科生，4-7为研究生
        for(int i = 1;i <= 7;i++){
            for(int j = 1;j <= 3;j++){
                VisualizeInfo2Entity entity = visualizeInfo2Dao.getAvgByClassIdAndGradeId(j,i);
                list.add(entity);
            }
            VisualizeInfo2Entity entity = visualizeInfo2Dao.getAvgByGradeId(i);
            list.add(entity);
        }
        visualizeInfo2ListVO.setList(list);
        return visualizeInfo2ListVO;
    }
}
