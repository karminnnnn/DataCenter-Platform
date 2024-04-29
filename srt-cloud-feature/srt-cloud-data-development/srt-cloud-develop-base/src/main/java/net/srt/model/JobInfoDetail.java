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

package net.srt.model;

import net.srt.entity.DataProductionClusterConfigurationEntity;
import net.srt.entity.DataProductionClusterEntity;
import net.srt.entity.DataProductionTaskHistoryEntity;
import net.srt.entity.DataProductionTaskInstanceEntity;
import net.srt.entity.DataProductionTaskInstanceHistoryEntity;
import org.apache.flink.runtime.taskexecutor.TaskManagerConfiguration;

import java.util.Set;

/**
 * JobInfoDetail
 *
 * @author wenmo
 * @since 2022/3/1 19:31
 **/
public class JobInfoDetail {

    private Integer id;
    private DataProductionTaskInstanceEntity instance;
    private DataProductionClusterEntity cluster;
    private DataProductionClusterConfigurationEntity clusterConfiguration;
    private DataProductionTaskHistoryEntity history;
    private DataProductionTaskInstanceHistoryEntity jobHistory;
    private JobManagerConfiguration jobManagerConfiguration;
    private Set<TaskManagerConfiguration> taskManagerConfiguration;
    private Integer refreshCount;

    public JobInfoDetail(Integer id) {
        this.id = id;
        this.refreshCount = 0;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public DataProductionTaskInstanceEntity getInstance() {
        return instance;
    }

    public void setInstance(DataProductionTaskInstanceEntity instance) {
        this.instance = instance;
    }

    public DataProductionClusterEntity getCluster() {
        return cluster;
    }

    public void setCluster(DataProductionClusterEntity cluster) {
        this.cluster = cluster;
    }

    public DataProductionClusterConfigurationEntity getClusterConfiguration() {
        return clusterConfiguration;
    }

    public void setClusterConfiguration(DataProductionClusterConfigurationEntity clusterConfiguration) {
        this.clusterConfiguration = clusterConfiguration;
    }

    public void setJobManagerConfiguration(JobManagerConfiguration jobMangerConfiguration) {
        this.jobManagerConfiguration = jobMangerConfiguration;
    }

    public JobManagerConfiguration getJobManagerConfiguration() {
        return jobManagerConfiguration;
    }

    public void setTaskManagerConfiguration(Set<TaskManagerConfiguration> taskManagerConfiguration) {
        this.taskManagerConfiguration = taskManagerConfiguration;
    }

    public Set<TaskManagerConfiguration> getTaskManagerConfiguration() {
        return taskManagerConfiguration;
    }

    public DataProductionTaskHistoryEntity getHistory() {
        return history;
    }

    public void setHistory(DataProductionTaskHistoryEntity history) {
        this.history = history;
    }

    public DataProductionTaskInstanceHistoryEntity getJobHistory() {
        return jobHistory;
    }

    public void setJobHistory(DataProductionTaskInstanceHistoryEntity jobHistory) {
        this.jobHistory = jobHistory;
    }

    public void refresh() {
        refreshCount = refreshCount + 1;
        if (isNeedSave()) {
            refreshCount = 0;
        }
    }

    public boolean isNeedSave() {
    	//改为10s一刷
        return refreshCount % 10 == 0;
    }
}
