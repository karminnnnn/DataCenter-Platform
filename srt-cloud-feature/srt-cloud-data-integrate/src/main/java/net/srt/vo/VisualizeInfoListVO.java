package net.srt.vo;

import lombok.Data;
import net.srt.entity.VisualizeInfoEntity;

import java.util.List;
@Data
public class VisualizeInfoListVO implements java.io.Serializable{
    private static final long serialVersionUID = 1L;

    private List<List<VisualizeInfoEntity>> list;

}
