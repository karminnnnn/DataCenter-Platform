package net.srt.dao;

import net.srt.entity.VisualizeInfo2Entity;
import net.srt.framework.mybatis.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface VisualizeInfo2Dao extends BaseDao<VisualizeInfo2Entity>{
    String getAvgByClassIdStr = "SELECT \n" +
            "    -1 AS id,\n" +
            "    '10000' AS NetId,\n" +
            "    AVG(pub_course_perf) AS PubCoursePerf,\n" +
            "    AVG(ele_course_perf) AS EleCoursePerf,\n" +
            "    AVG(mand_spec_course_perf) AS MandSpecCoursePerf,\n" +
            "    AVG(jd_idea_pol_awards) AS JdIdeaPolAwards,\n" +
            "    AVG(disc_comp_awards) AS DiscCompAwards,\n" +
            "    AVG(art_comp_awards) AS ArtCompAwards,\n" +
            "    AVG(sport_comp_awards) AS SportCompAwards,\n" +
            "    AVG(entrep_comp_awards) AS EntrepCompAwards,\n" +
            "    AVG(academic_awards) AS AcademicAwards,\n" +
            "    AVG(high_level_pubs) AS HighLevelPubs,\n" +
            "    AVG(vol_serv_hours) AS VolServHours,\n" +
            "    AVG(patents) AS Patents,\n" +
            "    AVG(soft_copy_inventions) AS SoftCopyInventions,\n" +
            "    AVG(monographs_pub) AS MonographsPub,\n" +
            "    -1 AS ClassId,\n" +
            "    -1 AS GradeId\n" +
            "FROM \n" +
            "    visualize_info2\n" +
            "WHERE \n" +
            "    class_id = #{classId}";

    String getAvgByGradeIdStr = "SELECT \n" +
            "    -2 AS id,\n" +
            "    '10000' AS NetId,\n" +
            "    AVG(pub_course_perf) AS PubCoursePerf,\n" +
            "    AVG(ele_course_perf) AS EleCoursePerf,\n" +
            "    AVG(mand_spec_course_perf) AS MandSpecCoursePerf,\n" +
            "    AVG(jd_idea_pol_awards) AS JdIdeaPolAwards,\n" +
            "    AVG(disc_comp_awards) AS DiscCompAwards,\n" +
            "    AVG(art_comp_awards) AS ArtCompAwards,\n" +
            "    AVG(sport_comp_awards) AS SportCompAwards,\n" +
            "    AVG(entrep_comp_awards) AS EntrepCompAwards,\n" +
            "    AVG(academic_awards) AS AcademicAwards,\n" +
            "    AVG(high_level_pubs) AS HighLevelPubs,\n" +
            "    AVG(vol_serv_hours) AS VolServHours,\n" +
            "    AVG(patents) AS Patents,\n" +
            "    AVG(soft_copy_inventions) AS SoftCopyInventions,\n" +
            "    AVG(monographs_pub) AS MonographsPub,\n" +
            "    -2 AS ClassId,\n" +
            "    -1 AS GradeId\n" +
            "FROM \n" +
            "    visualize_info2\n" +
            "WHERE \n" +
            "    grade_id = #{gradeId}";

    String getMaxByClassIdStr = "SELECT \n" +
            "    -2 AS id,\n" +
            "    '10000' AS NetId,\n" +
            "    MAX(pub_course_perf) AS PubCoursePerf,\n" +
            "    MAX(ele_course_perf) AS EleCoursePerf,\n" +
            "    MAX(mand_spec_course_perf) AS MandSpecCoursePerf,\n" +
            "    MAX(jd_idea_pol_awards) AS JdIdeaPolAwards,\n" +
            "    MAX(disc_comp_awards) AS DiscCompAwards,\n" +
            "    MAX(art_comp_awards) AS ArtCompAwards,\n" +
            "    MAX(sport_comp_awards) AS SportCompAwards,\n" +
            "    MAX(entrep_comp_awards) AS EntrepCompAwards,\n" +
            "    MAX(academic_awards) AS AcademicAwards,\n" +
            "    MAX(high_level_pubs) AS HighLevelPubs,\n" +
            "    MAX(vol_serv_hours) AS VolServHours,\n" +
            "    MAX(patents) AS Patents,\n" +
            "    MAX(soft_copy_inventions) AS SoftCopyInventions,\n" +
            "    MAX(monographs_pub) AS MonographsPub,\n" +
            "    -3 AS ClassId,\n" +
            "    -1 AS GradeId\n" +
            "FROM \n" +
            "    visualize_info2\n" +
            "WHERE \n" +
            "    class_id = #{classId}";

    String getAvgByClassIdAndGradeIdStr = "SELECT \n" +
            "    -2 AS id,\n" +
            "    '10000' AS NetId,\n" +
            "    AVG(pub_course_perf) AS PubCoursePerf,\n" +
            "    AVG(ele_course_perf) AS EleCoursePerf,\n" +
            "    AVG(mand_spec_course_perf) AS MandSpecCoursePerf,\n" +
            "    AVG(jd_idea_pol_awards) AS JdIdeaPolAwards,\n" +
            "    AVG(disc_comp_awards) AS DiscCompAwards,\n" +
            "    AVG(art_comp_awards) AS ArtCompAwards,\n" +
            "    AVG(sport_comp_awards) AS SportCompAwards,\n" +
            "    AVG(entrep_comp_awards) AS EntrepCompAwards,\n" +
            "    AVG(academic_awards) AS AcademicAwards,\n" +
            "    AVG(high_level_pubs) AS HighLevelPubs,\n" +
            "    AVG(vol_serv_hours) AS VolServHours,\n" +
            "    AVG(patents) AS Patents,\n" +
            "    AVG(soft_copy_inventions) AS SoftCopyInventions,\n" +
            "    AVG(monographs_pub) AS MonographsPub,\n" +
            "    -2 AS ClassId,\n" +
            "    -1 AS GradeId\n" +
            "FROM \n" +
            "    visualize_info2\n" +
            "WHERE \n" +
            "    grade_id = #{gradeId} AND class_id = #{classId}";
    String getByNetIdStr = "SELECT * FROM visualize_info2 WHERE net_id = #{netId}";
    @Select(getAvgByClassIdStr)
    VisualizeInfo2Entity getAvgByClassId(int classId);

    @Select(getAvgByGradeIdStr)
    VisualizeInfo2Entity getAvgByGradeId(int gradeId);

    @Select(getMaxByClassIdStr)
    VisualizeInfo2Entity getMaxByClassId(int classId);

    @Select(getByNetIdStr)
    VisualizeInfo2Entity selectByNetId(int netId);

    @Select(getAvgByClassIdAndGradeIdStr)
    VisualizeInfo2Entity getAvgByClassIdAndGradeId(int classId, int gradeId);
}
