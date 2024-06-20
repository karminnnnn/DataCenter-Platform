package net.srt.vo;

import lombok.Data;
import net.srt.entity.VisualizeInfo2Entity;

import java.util.List;
@Data
public class VisualizeInfo2ListVO implements java.io.Serializable{
    private static final long serialVersionUID = 1L;

    private List<VisualizeInfo2Entity> list;
}
