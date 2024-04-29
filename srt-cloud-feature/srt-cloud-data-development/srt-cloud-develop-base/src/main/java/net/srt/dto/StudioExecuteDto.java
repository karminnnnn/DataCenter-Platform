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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.srt.api.FlinkVersion;
import net.srt.flink.common.assertion.Asserts;
import net.srt.flink.core.job.JobConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * StudioExecuteDTO
 *
 * @author wenmo
 * @since 2021/5/30 11:09
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class StudioExecuteDto extends AbstractStatementDto {
    // RUN_MODE
    private String type;
    private String dialect;
    private Integer sqlDbType;
    private Integer openTrans;
    private boolean useResult;
    private boolean useChangeLog;
    private boolean useAutoCancel;
    private boolean statementSet;
    private boolean batchModel;
    private boolean useSession;
    private String session;
    private Integer clusterId;
    private Integer clusterConfigurationId;
    private Integer databaseId;
    private Integer jarId;
    private String jobName;
    private Integer taskId;
    private Integer id;
    private Integer maxRowNum;
    private Integer checkPoint;
    private Integer parallelism;
    private Integer savePointStrategy;
    private String savePointPath;
    private String configJson;
	/**
	 * 是否添加进程结束标识
	 */
	private Boolean processEnd;
    private static final ObjectMapper mapper = new ObjectMapper();
    private String accessToken;
    private Long projectId;
    private String flinkVersion;

    public JobConfig getJobConfig() {
        Map<String, String> config = new HashMap<>();
        if (Asserts.isNotNullString(configJson)) {
            try {
                JsonNode paras = mapper.readTree(configJson);
                paras.forEach((JsonNode node) -> {
                        if (!node.isNull()) {
                            config.put(node.get("key").asText(), node.get("value").asText());
                        }
                    }
                );
            } catch (JsonProcessingException e) {
                log.error(e.getMessage());
            }
        }
        return new JobConfig(
            type, useResult, useChangeLog, useAutoCancel, useSession, session, clusterId,
            clusterConfigurationId, jarId, taskId, jobName, isFragment(), statementSet, batchModel,
            maxRowNum, checkPoint, parallelism, savePointStrategy, savePointPath, getVariables(), config);
    }

    public Integer getTaskId() {
        return taskId == null ? getId() : taskId;
    }
}
