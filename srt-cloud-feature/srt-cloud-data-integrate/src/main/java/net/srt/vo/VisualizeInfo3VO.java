package net.srt.vo;

import lombok.Data;

import java.util.List;

@Data
public class VisualizeInfo3VO implements java.io.Serializable{
    private static final long serialVersionUID = 1L;

    private List<String> property;
    Integer value;
    Integer year;
}
