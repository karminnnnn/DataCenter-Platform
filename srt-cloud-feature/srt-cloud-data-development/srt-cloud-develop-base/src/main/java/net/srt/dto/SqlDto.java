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

package net.srt.dto;

import lombok.Getter;
import lombok.Setter;
import net.srt.entity.DataProductionScheduleNodeRecordEntity;

/**
 * SqlDto
 *
 * @author zrx
 * @since 2021/12/29 19:42
 */
@Getter
@Setter
public class SqlDto {
	private Integer taskId;
	private Integer sqlDbType;
	private String statement;
	private Integer databaseId;
	private Integer openTrans;
	private Integer maxRowNum;
	private DataProductionScheduleNodeRecordEntity nodeRecord;

	public SqlDto(String statement, Integer databaseId, Integer maxRowNum) {
		this.statement = statement;
		this.databaseId = databaseId;
		this.maxRowNum = maxRowNum;
	}

	public SqlDto(Integer taskId, String statement, Integer sqlDbType, Integer databaseId, Integer openTrans, Integer maxRowNum) {
		this.taskId = taskId;
		this.statement = statement;
		this.sqlDbType = sqlDbType;
		this.databaseId = databaseId;
		this.openTrans = openTrans;
		this.maxRowNum = maxRowNum;
	}

	public SqlDto(Integer taskId, String statement, Integer sqlDbType, Integer databaseId, Integer openTrans, Integer maxRowNum, DataProductionScheduleNodeRecordEntity nodeRecord) {
		this.taskId = taskId;
		this.statement = statement;
		this.sqlDbType = sqlDbType;
		this.databaseId = databaseId;
		this.openTrans = openTrans;
		this.maxRowNum = maxRowNum;
		this.nodeRecord = nodeRecord;
	}

	public static SqlDto build(Integer taskId, String statement, Integer sqlDbType, Integer databaseId, Integer openTrans, Integer maxRowNum) {
		return new SqlDto(taskId, statement, sqlDbType, databaseId, openTrans, maxRowNum);
	}

	public static SqlDto build(Integer taskId, String statement, Integer sqlDbType, Integer databaseId, Integer openTrans, Integer maxRowNum, DataProductionScheduleNodeRecordEntity nodeRecord) {
		return new SqlDto(taskId, statement, sqlDbType, databaseId, openTrans, maxRowNum, nodeRecord);
	}
}
