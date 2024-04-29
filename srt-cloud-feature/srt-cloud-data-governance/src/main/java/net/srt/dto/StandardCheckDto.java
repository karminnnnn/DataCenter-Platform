package net.srt.dto;

import lombok.Data;

import java.util.List;

/**
 * @ClassName StandardCheckDto
 * @Author zrx
 * @Date 2023/5/26 11:46
 */
@Data
public class StandardCheckDto {
	//数据属性比对结果
	private List<CompareResult> compareResults;
	//是否关联了码表
	private Boolean relStandardCode;
	//是否有码表数据
	private Boolean hasStandardCode;
	private String fillNumSql;
	//符合标准数量
	private Object fillNum;
	private String notFillNumSql;
	//不符合标准数量
	private Object notFullNum;
}
