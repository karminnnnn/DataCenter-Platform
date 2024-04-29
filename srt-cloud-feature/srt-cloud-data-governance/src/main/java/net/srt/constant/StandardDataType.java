/*
 *
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package net.srt.constant;


/**
 * FlinkType
 *
 * @author zrx
 **/
public enum StandardDataType {

	/**
	 * 数字
	 */
	NUMBER(1, "数字","INT,LONG.NUMBER,BIGINT"),
	/**
	 * 字符串
	 */
	STRING(2, "字符串","CHAR,VARCHAR,NVARCHAR,TEXT,LONGTEXT"),
	/**
	 * 日期
	 */
	DATE(3, "日期","DATE,DATETIME.TIMESTAMP"),
	/**
	 * 小数
	 */
	NUMBER_SACLE(4, "小数","DOUBLE,NUMBER"),
	;


	private final Integer value;
	private final String longValue;
	private final String dbDataTypes;

	StandardDataType(Integer value, String longValue,String dbDataTypes) {
		this.value = value;
		this.longValue = longValue;
		this.dbDataTypes = dbDataTypes;
	}

	public Integer getValue() {
		return value;
	}

	public String getLongValue() {
		return longValue;
	}

	public String getDbDataTypes() {
		return dbDataTypes;
	}

	public static StandardDataType getByCode(String value) {
		for (StandardDataType standardDataType : StandardDataType.values()) {
			if (standardDataType.getValue().equals(Integer.parseInt(value))) {
				return standardDataType;
			}
		}
		return StandardDataType.STRING;
	}
}
