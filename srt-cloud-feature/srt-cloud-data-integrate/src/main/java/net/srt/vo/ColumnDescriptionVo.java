// Copyright tang.  All rights reserved.
// https://gitee.com/inrgihc/dbswitch
//
// Use of this source code is governed by a BSD-style license
//
// Author: tang (inrgihc@126.com)
// Date : 2020/1/2
// Location: beijing , china
/////////////////////////////////////////////////////////////
package net.srt.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import srt.cloud.framework.dbswitch.common.type.ProductTypeEnum;

/**
 * 数据库列描述符信息定义(Column Description)
 *
 * @author jrl
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ColumnDescriptionVo {

	private String fieldName;
	private String remarks;
	private String labelName;
	private String fieldTypeName;
	private Integer displaySize;
//	private String filedTypeClassName;
//  private int fieldType;
    private Integer scaleSize;
	//默认值
    private String defaultValue;
	private boolean isNullable;
	//是否是主键
	private boolean isPk;
	private boolean isAutoIncrement;
	private Long datatableId;
	private String datatableName;
	//private int precisionSize;



	//private boolean signed = false;
	//private ProductTypeEnum dbtype;
	//索引是否可以不唯一
	//private boolean nonIndexUnique;
	//索引类别
	//private String indexQualifier;
	//索引名称
	//private String indexName;
	//private short indexType;
	//private String ascOrDesc;




}
