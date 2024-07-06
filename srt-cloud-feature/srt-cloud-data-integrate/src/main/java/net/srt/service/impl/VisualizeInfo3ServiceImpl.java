package net.srt.service.impl;

import lombok.AllArgsConstructor;
import net.srt.dao.VisualizeInfo3Dao;
import net.srt.entity.VisualizeInfo3Entity;
import net.srt.framework.mybatis.service.impl.BaseServiceImpl;
import net.srt.service.VisualizeInfo3Service;
import net.srt.vo.VisualizeInfo3ListVO;
import net.srt.vo.VisualizeInfo3VO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class VisualizeInfo3ServiceImpl extends BaseServiceImpl<VisualizeInfo3Dao, VisualizeInfo3Entity> implements VisualizeInfo3Service {
    private final VisualizeInfo3Dao visualizeInfo3Dao;
    public VisualizeInfo3ListVO getVisualizeInfo3List() {
        List<List<VisualizeInfo3Entity>> list_lists = new ArrayList<>();
        List<VisualizeInfo3Entity> list = visualizeInfo3Dao.selectAll();
        List<List<String>> result = new ArrayList<>();

        //找出list数组中的entity的入学年份一共有哪些年份
        List<Integer> years = new ArrayList<>();
        for(VisualizeInfo3Entity entity : list){
            Integer year = entity.getEnterYear();
            if(!years.contains(year)){
                years.add(year);
            }
        }

        for(Integer year : years){
            /*
                一共有八个大类，分别是：学生类别、性别、民族、出生日期、教育程度、政治面貌、籍贯、高考总分,要统计每一年的这些信息
                所以在result中，0-7是第一个年份，8-15是第二个年份，以此类推
             */
            for(int i = 0;i < 8;i++){
                List<String> property_lists = new ArrayList<>();
                result.add(property_lists);
            }
        }

        for(VisualizeInfo3Entity entity : list){
            // 首先根据entity的入学年份找到其对应years数组里面的哪个下标
            int index = years.indexOf(entity.getEnterYear());

            // 再根据Index找到在result中对应数组的起始位置
            int start = index * 8;

            // 提取出对应的属性
            String category = entity.getCategory();
            String gender = entity.getGender();
            String ethnicity = entity.getEthnicity();
            String birthDate = entity.getBirthDate();
            String educationLevel = entity.getEducationLevel();
            String politicalStatus = entity.getPoliticalStatus();
            String hometown = entity.getHometown();
            String highScore = entity.getHighScore();


            // 保存到对应的数组,暂存每个年份的属性值
            result.get(start).add(category);
            result.get(start+1).add(gender);
            result.get(start+2).add(ethnicity);
            result.get(start+3).add(birthDate);
            result.get(start+4).add(educationLevel);
            result.get(start+5).add(politicalStatus);
            result.get(start+6).add(hometown);
            result.get(start+7).add(highScore);
        }

        // result_lists用以保存最终返回给前端的结果，Check用以检查是否有重复的属性
        List<List<VisualizeInfo3VO>> result_lists = new ArrayList<>();
        List<List<Map<String,Integer>>> Check = new ArrayList<>();
        for(int i = 0;i < 8;i++){
            List<VisualizeInfo3VO> result_list = new ArrayList<>();
            List<Map<String,Integer>> check_list = new ArrayList<>();
            result_lists.add(result_list);
            Check.add(check_list);
        }

        // 每个数组加入对应的人数、年份信息，返回给前端

        for(Integer year : years){
            int index = years.indexOf(year);
            int start = index * 8;

            for(int i = 0;i < 8;i++){
                List<String> property_list = result.get(start + i);

                for(String property : property_list){
                    // flag用以标记是否有这个属性,false表示没有
                    boolean flag = false;
                    for(Map<String,Integer> map : Check.get(i)){
                        // 如果有这个属性，则值加一
                        if(map.containsKey(property)){
                            map.put(property,map.get(property)+1);
                            flag = true;
                        }
                    }
                    // 如果没有这个属性，则新建一个map
                    if(!flag){
                        Map<String,Integer> tmp_map = new HashMap<>();
                        tmp_map.put(property,1);
                        Check.get(i).add(tmp_map);
                    }
                }

                for(Map<String,Integer> map : Check.get(i)){
                    VisualizeInfo3VO visualizeInfo3VO = new VisualizeInfo3VO();
                    visualizeInfo3VO.setType(map.keySet().iterator().next());
                    visualizeInfo3VO.setValue(map.values().iterator().next());
                    visualizeInfo3VO.setYear(year);
                    result_lists.get(i).add(visualizeInfo3VO);
                }

            }

        }
            VisualizeInfo3ListVO visualizeInfo3ListVO = new VisualizeInfo3ListVO();
            visualizeInfo3ListVO.setList(result_lists);

        return visualizeInfo3ListVO;
    }
}
