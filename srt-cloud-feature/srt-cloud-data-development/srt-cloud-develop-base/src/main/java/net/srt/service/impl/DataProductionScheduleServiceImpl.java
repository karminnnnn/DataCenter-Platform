package net.srt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.AllArgsConstructor;
import net.srt.api.module.data.development.constant.ExecuteType;
import net.srt.api.module.data.integrate.constant.CommonRunStatus;
import net.srt.api.module.quartz.QuartzDataProductionScheduleApi;
import net.srt.convert.DataProductionScheduleConvert;
import net.srt.convert.DataProductionScheduleNodeRecordConvert;
import net.srt.dao.DataProductionScheduleDao;
import net.srt.dao.DataProductionScheduleNodeDao;
import net.srt.dao.DataProductionScheduleNodeRecordDao;
import net.srt.dao.DataProductionScheduleRecordDao;
import net.srt.dto.Flow;
import net.srt.dto.FlowEdge;
import net.srt.dto.FlowNode;
import net.srt.dto.FlowNodeProperties;
import net.srt.entity.DataProductionScheduleEntity;
import net.srt.entity.DataProductionScheduleNodeEntity;
import net.srt.entity.DataProductionScheduleNodeRecordEntity;
import net.srt.entity.DataProductionScheduleRecordEntity;
import net.srt.flink.common.config.Dialect;
import net.srt.flink.common.utils.JSONUtil;
import net.srt.flink.common.utils.LogUtil;
import net.srt.flink.common.utils.ThreadUtil;
import net.srt.flink.core.job.JobResult;
import net.srt.flink.process.context.ProcessContextHolder;
import net.srt.flink.process.model.ProcessEntity;
import net.srt.flink.process.model.ProcessType;
import net.srt.flink.process.pool.ConsolePool;
import net.srt.framework.common.exception.ServerException;
import net.srt.framework.common.page.PageResult;
import net.srt.framework.mybatis.service.impl.BaseServiceImpl;
import net.srt.framework.security.user.SecurityUser;
import net.srt.model.ConsoleLog;
import net.srt.query.DataProductionScheduleQuery;
import net.srt.query.DataProductionScheduleRecordQuery;
import net.srt.service.DataProductionScheduleRecordService;
import net.srt.service.DataProductionScheduleService;
import net.srt.service.DataProductionTaskService;
import net.srt.vo.DataProductionScheduleNodeRecordVO;
import net.srt.vo.DataProductionScheduleRecordVO;
import net.srt.vo.DataProductionScheduleVO;
import org.quartz.CronExpression;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import srt.cloud.framework.dbswitch.common.util.StringUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.stream.Collectors;

/**
 * 数据生产-作业调度
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-01-12
 */
@Service
@AllArgsConstructor
public class DataProductionScheduleServiceImpl extends BaseServiceImpl<DataProductionScheduleDao, DataProductionScheduleEntity> implements DataProductionScheduleService {

	private final DataProductionScheduleNodeDao nodeDao;
	private final DataProductionScheduleRecordDao recordDao;
	private final DataProductionScheduleNodeRecordDao nodeRecordDao;
	private final DataProductionTaskService taskService;
	private final DataProductionScheduleRecordService recordService;
	private final QuartzDataProductionScheduleApi scheduleApi;


	@Override
	public PageResult<DataProductionScheduleVO> page(DataProductionScheduleQuery query) {
		IPage<DataProductionScheduleEntity> page = baseMapper.selectPage(getPage(query), getWrapper(query));

		return new PageResult<>(DataProductionScheduleConvert.INSTANCE.convertList(page.getRecords()), page.getTotal());
	}

	private LambdaQueryWrapper<DataProductionScheduleEntity> getWrapper(DataProductionScheduleQuery query) {
		LambdaQueryWrapper<DataProductionScheduleEntity> wrapper = Wrappers.lambdaQuery();
		wrapper.like(StringUtil.isNotBlank(query.getName()), DataProductionScheduleEntity::getName, query.getName())
				.eq(query.getStatus() != null, DataProductionScheduleEntity::getStatus, query.getStatus())
				.orderByDesc(DataProductionScheduleEntity::getUpdateTime).orderByDesc(DataProductionScheduleEntity::getId);
		dataScopeWithOrgId(wrapper);
		return wrapper;
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void save(Flow flow) {
		insertOrUpdate(flow);
	}

	@Override
	public Flow get(Long id) {
		DataProductionScheduleEntity scheduleEntity = baseMapper.selectById(id);
		Flow flow = new Flow();
		flow.setId(scheduleEntity.getId());
		flow.setOrgId(scheduleEntity.getOrgId());
		flow.setName(scheduleEntity.getName());
		flow.setCron(scheduleEntity.getCron());
		flow.setStatus(scheduleEntity.getStatus());
		flow.setReleaseUserId(scheduleEntity.getReleaseUserId());
		flow.setReleaseTime(scheduleEntity.getReleaseTime());
		flow.setIfCycle(scheduleEntity.getIfCycle());
		flow.setNote(scheduleEntity.getNote());
		flow.setEdges(JSONUtil.parseObject(scheduleEntity.getEdges(), new TypeReference<List<FlowEdge>>() {
		}));
		List<FlowNode> nodes = new ArrayList<>(6);
		flow.setNodes(nodes);
		//获取节点
		Map<String, Object> queryMap = new HashMap<>();
		queryMap.put("task_schedule_id", id);
		List<DataProductionScheduleNodeEntity> dbNodes = nodeDao.selectByMap(queryMap);
		for (DataProductionScheduleNodeEntity dbNode : dbNodes) {
			FlowNode flowNode = getFlowNode(dbNode);
			nodes.add(flowNode);
		}
		return flow;
	}

	/**
	 * 根据数据库的node信息获取flowNode
	 *
	 * @param dbNode
	 * @return
	 */
	private FlowNode getFlowNode(DataProductionScheduleNodeEntity dbNode) {
		FlowNode flowNode = new FlowNode();
		flowNode.setId(dbNode.getNo());
		flowNode.setType(dbNode.getType());
		flowNode.setX(dbNode.getX());
		flowNode.setY(dbNode.getY());
		FlowNodeProperties properties = new FlowNodeProperties();
		properties.setId(dbNode.getId());
		properties.setName(dbNode.getName());
		properties.setTaskId(dbNode.getTaskId());
		properties.setWeight(dbNode.getWeight());
		properties.setTaskType(dbNode.getTaskType());
		properties.setTaskTypeVal(Dialect.getByCode(dbNode.getTaskType().toString()).getValue());
		properties.setNote(dbNode.getNote());
		properties.setFailGoOn(dbNode.getFailGoOn());
		flowNode.setProperties(properties);
		return flowNode;
	}

	private void insertOrUpdate(Flow flow) {
		if (flow.getIfCycle() == 1 && !CronExpression.isValidExpression(flow.getCron())) {
			throw new ServerException("cron 表达式有误，请检查后重新填写！");
		}

		DataProductionScheduleEntity schedule = DataProductionScheduleEntity.builder().id(flow.getId())
				.projectId(getProjectId()).orgId(flow.getOrgId()).ifCycle(flow.getIfCycle())
				.name(flow.getName()).cron(flow.getCron()).note(flow.getNote()).status(flow.getStatus()).releaseTime(flow.getReleaseTime()).releaseUserId(flow.getReleaseUserId()).build();
		List<FlowNode> nodes = flow.getNodes();
		List<FlowEdge> edges = flow.getEdges();
		//寻找入度为0的节点
		List<FlowNode> startNodes = getStartNodes(nodes, edges);
		//检查闭环
		checkClosedLoop(edges);
		//获取节点执行list
		Set<FlowNode> runNodeSet = new LinkedHashSet<>(6);
		buildRunNodes(runNodeSet, startNodes, nodes, edges);
		if (schedule.getId() == null) {
			schedule.setEdges(JSONUtil.toJsonString(edges));
			baseMapper.insert(schedule);
			//转换前端传过来的节点为entity类型
			List<DataProductionScheduleNodeEntity> clientNodes = getNodesByNodeSet(schedule, runNodeSet);
			//新增节点
			clientNodes.forEach(nodeDao::insert);
		} else {
			List<DataProductionScheduleNodeEntity> clientNodes = getNodesByNodeSet(schedule, runNodeSet);
			schedule.setEdges(JSONUtil.toJsonString(edges));
			baseMapper.updateById(schedule);
			//获取库中的节点
			Map<String, Object> queryMap = new HashMap<>();
			queryMap.put("task_schedule_id", schedule.getId());
			List<DataProductionScheduleNodeEntity> dbNodes = nodeDao.selectByMap(queryMap);
			//查询clientNodes的properties的id为空的
			List<DataProductionScheduleNodeEntity> insertNodes = clientNodes.stream().filter(item -> item.getId() == null).collect(Collectors.toList());
			insertNodes.forEach(nodeDao::insert);
			//查询clientNodes的properties的id不为空的
			clientNodes = getNodesByNodeSet(schedule, runNodeSet);
			List<DataProductionScheduleNodeEntity> updateNodes = clientNodes.stream().filter(item -> item.getId() != null).collect(Collectors.toList());
			updateNodes.forEach(nodeDao::updateById);
			//查询库里有，nodeSet里没有的，则是需要删除的
			for (DataProductionScheduleNodeEntity dbNode : dbNodes) {
				if (clientNodes.stream().noneMatch(item -> dbNode.getNo().equals(item.getNo()))) {
					nodeDao.deleteById(dbNode.getId());
				}
			}
		}
	}

	private List<DataProductionScheduleNodeEntity> getNodesByNodeSet(DataProductionScheduleEntity schedule, Set<FlowNode> runNodeSet) {
		List<DataProductionScheduleNodeEntity> clientNodes = new ArrayList<>(10);
		int i = 0;
		for (FlowNode flowNode : runNodeSet) {
			i++;
			DataProductionScheduleNodeEntity nodeEntity = new DataProductionScheduleNodeEntity();
			nodeEntity.setId(flowNode.getProperties().getId());
			nodeEntity.setOrgId(schedule.getOrgId());
			nodeEntity.setTaskScheduleId(schedule.getId());
			nodeEntity.setProjectId(schedule.getProjectId());
			nodeEntity.setNo(flowNode.getId());
			nodeEntity.setSort(i);
			nodeEntity.setName(flowNode.getProperties().getName());
			nodeEntity.setType(flowNode.getType());
			nodeEntity.setX(flowNode.getX());
			nodeEntity.setY(flowNode.getY());
			nodeEntity.setNote(flowNode.getProperties().getNote());
			nodeEntity.setTaskId(flowNode.getProperties().getTaskId());
			nodeEntity.setTaskType(flowNode.getProperties().getTaskType());
			nodeEntity.setFailGoOn(flowNode.getProperties().getFailGoOn());
			nodeEntity.setWeight(flowNode.getProperties().getWeight());
			clientNodes.add(nodeEntity);
		}
		return clientNodes;
	}

	private List<FlowNode> getStartNodes(List<FlowNode> nodes, List<FlowEdge> edges) {
		if (nodes.isEmpty()) {
			throw new ServerException("流程不能为空！");
		}
		List<FlowNode> startNodes = new ArrayList<>(1);
		for (FlowNode node : nodes) {
			//如果没有找到targetNodeId为该节点的边，说明该节点为起始节点
			if (edges.stream().noneMatch(item -> node.getId().equals(item.getTargetNodeId()))) {
				startNodes.add(node);
			}
		}
		return startNodes;
	}

	/**
	 * 构建 有向图 邻接表
	 *
	 * @return
	 */
	private Map<String, List<String>> buildDag(List<FlowEdge> edges) {

		Map<String, List<String>> adj = new HashMap<>();
		if (CollectionUtils.isEmpty(edges)) {
			return adj;
		}
		for (FlowEdge edg : edges) {
			String node1 = edg.getSourceNodeId();
			String node2 = edg.getTargetNodeId();
			adj.computeIfAbsent(node1, k -> new ArrayList<>());
			adj.computeIfAbsent(node2, k -> new ArrayList<>());
			adj.get(node1).add(node2);
		}
		return adj;
	}

	/**
	 * @param adj     图的临接表
	 * @param current 当前节点
	 * @param visited 判断是否访问
	 */
	private static boolean dgDfsCycle(Map<String, List<String>> adj, String current, Map<String, Boolean> visited, Stack<String> visitedStack) {
		// 首先 访问当前节点 并进行标记
		visited.put(current, true);
		visitedStack.push(current);

		// 获取到当前节点能够到达的所有节点
		List<String> list = adj.get(current);
		for (String can : list) {
			// 如果节点没有被访问过
			if (!visited.get(can)) {
				// 当前节点就是父节点，循环的节点就是子节点
				return dgDfsCycle(adj, can, visited, visitedStack);
			}
			// 在节点被访问过的情况下 说明有环
			else {
				return visitedStack.contains(can);
			}
		}
		return false;
	}


	/**
	 * 有向图中判断是否有环
	 *
	 */
	private void checkClosedLoop(List<FlowEdge> edges) {
		// 习惯上转换成临接表的形式
		Map<String, List<String>> adj = buildDag(edges);
		// 定义一个节点状态数组 判断是否访问过
		Map<String, Boolean> visited = new HashMap<>();
		Stack<String> visitedStack;
		Set<String> keySet = adj.keySet();
		for (String key : keySet) {
			visited.put(key, false);
		}
		// 引用传递 函数内部修改值后退出函数可见
		for (String key : keySet) {
			visitedStack = new Stack<>();
			// 如果没有进行访问 则进行深度优先搜索回溯
			if (!visited.get(key)) {
				boolean dfs = dgDfsCycle(adj, key, visited, visitedStack);
				if (dfs) {
					throw new ServerException("流程图存在闭环，请检查！");
				} else {
					visited.put(key, false);
					visitedStack.pop();
				}
			}
		}
	}


	private void buildRunNodes(Set<FlowNode> nodeSet, List<FlowNode> startNodes, List<FlowNode> nodes, List<FlowEdge> edges) {
		if (startNodes.isEmpty()) {
			return;
		}
		//按权重逆序，权重越高越在前面
		startNodes.sort((item1, item2) -> item2.getProperties().getWeight().compareTo(item1.getProperties().getWeight()));
		for (FlowNode startNode : startNodes) {
			if (nodes.contains(startNode)) {
				nodeSet.remove(startNode);
			}
			nodeSet.add(startNode);
		}
		//获取子节点，添加到set中
		List<FlowNode> childNodes = new ArrayList<>(2);
		for (FlowNode startNode : startNodes) {
			//获取以node为父亲的子节点
			List<FlowEdge> collect = edges.stream().filter(item -> startNode.getId().equals(item.getSourceNodeId())).collect(Collectors.toList());
			for (FlowEdge flowEdge : collect) {
				FlowNode flowNode = nodes.stream().filter(item -> item.getId().equals(flowEdge.getTargetNodeId())).findFirst().get();
				childNodes.add(flowNode);
			}
		}
		//递归子节点的子节点
		buildRunNodes(nodeSet, childNodes, nodes, edges);
	}

	@Override
	public String run(Integer id) {
		return scheduleRun(id, ExecuteType.HAND);
	}

	@Override
	public ConsoleLog getLog(Integer recordId) {
		ConsoleLog consoleLog = new ConsoleLog();
		String processId = recordId + DataProductionScheduleRecordEntity.SCHEDULE_RECORD;
		if (ConsolePool.getInstance().exist(processId)) {
			String msg = ConsolePool.getInstance().get(processId).toString();
			consoleLog.setLog(msg);
			consoleLog.setEnd(msg.endsWith(ProcessEntity.INFO_END));
			return consoleLog;
		} else {
			//说明被清了，从库里取
			DataProductionScheduleRecordEntity recordEntity = recordDao.selectById(recordId);
			String log = recordEntity.getLog();
			if (log != null) {
				consoleLog.setLog(log);
				consoleLog.setEnd(log.endsWith(ProcessEntity.INFO_END));
			}
			return consoleLog;
		}
	}

	@Override
	public List<DataProductionScheduleNodeRecordVO> listNodeRecord(Integer recordId) {
		LambdaQueryWrapper<DataProductionScheduleNodeRecordEntity> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(DataProductionScheduleNodeRecordEntity::getScheduleRecordId, recordId).orderByAsc(DataProductionScheduleNodeRecordEntity::getId);
		List<DataProductionScheduleNodeRecordEntity> nodeRecordEntityList = nodeRecordDao.selectList(wrapper);
		if (nodeRecordEntityList.isEmpty()) {
			return new ArrayList<>();
		}
		//获取节点之前的关系
		String edges = baseMapper.selectById(nodeRecordEntityList.get(0).getTaskScheduleId()).getEdges();
		List<FlowEdge> flowEdges = JSONUtil.parseObject(edges, new TypeReference<List<FlowEdge>>() {
		});
		assert flowEdges != null;
		List<DataProductionScheduleNodeRecordVO> nodeRecords = DataProductionScheduleNodeRecordConvert.INSTANCE.convertList(nodeRecordEntityList);
		for (DataProductionScheduleNodeRecordVO nodeRecord : nodeRecords) {
			nodeRecord.setEdgeId(flowEdges.stream().filter(item -> nodeRecord.getScheduleNodeNo().equals(item.getTargetNodeId())).findFirst().orElse(new FlowEdge()).getId());
		}
		return nodeRecords;
	}

	@Override
	public PageResult<DataProductionScheduleRecordVO> pageRecord(DataProductionScheduleRecordQuery query) {
		return recordService.page(query);
	}

	@Override
	public void delRecord(List<Long> idList) {
		recordService.delete(idList);
	}

	@Override
	public DataProductionScheduleNodeRecordVO getNodeRecord(Integer nodeRecordId) {
		return DataProductionScheduleNodeRecordConvert.INSTANCE.convert(nodeRecordDao.selectById(nodeRecordId));
	}

	@Override
	public void release(Integer id) {
		scheduleApi.release(id.longValue());
		//更新状态，发布人和发布时间
		DataProductionScheduleEntity dbEntity = new DataProductionScheduleEntity();
		dbEntity.setId(id.longValue());
		dbEntity.setStatus(1);
		dbEntity.setReleaseUserId(SecurityUser.getUserId().intValue());
		dbEntity.setReleaseTime(new Date());
		baseMapper.changeStutus(dbEntity);
	}

	@Override
	public void cancle(Integer id) {
		scheduleApi.cancle(id.longValue());
		//更新状态，发布人和发布时间
		DataProductionScheduleEntity dbEntity = new DataProductionScheduleEntity();
		dbEntity.setId(id.longValue());
		dbEntity.setStatus(0);
		baseMapper.changeStutus(dbEntity);
	}

	@Override
	@Async("scheduledPoolTaskExecutor")
	public void runActive() {
		LambdaQueryWrapper<DataProductionScheduleRecordEntity> wrapper = new LambdaQueryWrapper<>();
		wrapper.in(DataProductionScheduleRecordEntity::getRunStatus, CommonRunStatus.WAITING.getCode(), CommonRunStatus.RUNNING.getCode());
		List<DataProductionScheduleRecordEntity> activeList = recordDao.selectList(wrapper);
		for (DataProductionScheduleRecordEntity activeRecord : activeList) {
			String processId = activeRecord.getId() + DataProductionScheduleRecordEntity.SCHEDULE_RESTART_RECORD;
			ProcessEntity flowProcess = ProcessContextHolder.registerFlowProcess(ProcessEntity.init(ProcessType.FLOWEXECUTE, processId));
			//获取记录所对应的任务的节点
			LambdaQueryWrapper<DataProductionScheduleNodeEntity> nodeWrapper = new LambdaQueryWrapper<>();
			nodeWrapper.eq(DataProductionScheduleNodeEntity::getTaskScheduleId, activeRecord.getTaskScheduleId())
					.orderByAsc(DataProductionScheduleNodeEntity::getSort);
			List<DataProductionScheduleNodeEntity> nodes = nodeDao.selectList(nodeWrapper);
			//构造历史流程
			List<FlowNode> flowNodes = nodes.stream().map(this::getFlowNode).collect(Collectors.toList());
			//获取记录对应的节点记录
			LambdaQueryWrapper<DataProductionScheduleNodeRecordEntity> nodeRecordWrapper = new LambdaQueryWrapper<>();
			nodeRecordWrapper.eq(DataProductionScheduleNodeRecordEntity::getScheduleRecordId, activeRecord.getId());
			List<DataProductionScheduleNodeRecordEntity> nodeRecords = nodeRecordDao.selectList(nodeRecordWrapper);
			flowProcess.info("Restart run flow...");
			boolean recordSuccess = true;
			for (DataProductionScheduleNodeEntity node : nodes) {
				//构建流程节点
				FlowNode flowNode = flowNodes.stream().filter(item -> item.getId().equals(node.getNo())).findFirst().get();
				flowProcess.info(String.format("Start run node %s-%s", node.getName(), Dialect.getByCode(node.getTaskType().toString()).getValue()));
				//如果该节点没有被执行，添加到执行节点列表
				DataProductionScheduleNodeRecordEntity nodeRecord = nodeRecords.stream().filter(item -> node.getNo().equals(item.getScheduleNodeNo())).findFirst().orElse(null);
				if (nodeRecord == null ||
						CommonRunStatus.WAITING.getCode().equals(nodeRecord.getRunStatus()) ||
						CommonRunStatus.RUNNING.getCode().equals(nodeRecord.getRunStatus())) {
					if (nodeRecord == null) {
						nodeRecord = new DataProductionScheduleNodeRecordEntity();
						nodeRecord.setTaskScheduleId(node.getTaskScheduleId().intValue());
						nodeRecord.setScheduleNodeId(node.getId().intValue());
						nodeRecord.setScheduleNodeNo(node.getNo());
						nodeRecord.setScheduleRecordId(activeRecord.getId().intValue());
						nodeRecord.setTaskId(node.getTaskId());
						nodeRecord.setOrgId(node.getOrgId());
						nodeRecord.setCreator(node.getCreator());
						nodeRecord.setProjectId(node.getProjectId());
						nodeRecord.setRunStatus(CommonRunStatus.RUNNING.getCode());
						nodeRecord.setStartTime(new Date());
						nodeRecord.setExecuteType(activeRecord.getExecuteType());
						nodeRecordDao.insert(nodeRecord);
					}
					flowNode.getProperties().setNodeRecordId(nodeRecord.getId().intValue());
					JobResult jobResult;
					try {
						jobResult = taskService.scheduleTask(nodeRecord);
						nodeRecord.setEndTime(new Date());
						nodeRecord.setLog(jobResult.getLog());
						if (!jobResult.isSuccess()) {
							recordSuccess = false;
							flowProcess.error(jobResult.getLog());
							nodeRecord.setRunStatus(CommonRunStatus.FAILED.getCode());
							//更新节点历史记录
							nodeRecordDao.updateById(nodeRecord);
						} else {
							flowProcess.info(jobResult.getLog());
							flowProcess.info(String.format("Node %s-%s run end", node.getName(), Dialect.getByCode(node.getTaskType().toString()).getValue()));
							nodeRecord.setRunStatus(CommonRunStatus.SUCCESS.getCode());
							//更新节点历史记录
							nodeRecordDao.updateById(nodeRecord);
						}
					} catch (Exception e) {
						recordSuccess = false;
						nodeRecord.setEndTime(new Date());
						nodeRecord.setLog(LogUtil.getError(e));
						flowProcess.error(nodeRecord.getLog());
						nodeRecord.setRunStatus(CommonRunStatus.FAILED.getCode());
						//更新节点历史记录
						nodeRecordDao.updateById(nodeRecord);
					}
				} else {
					flowProcess.info(String.format("Node %s-%s has run before, skip", node.getName(), Dialect.getByCode(node.getTaskType().toString()).getValue()));
				}
				flowNode.getProperties().setRunStatus(nodeRecord.getRunStatus());
				flowNode.getProperties().setStyle(CommonRunStatus.SUCCESS.getCode().equals(nodeRecord.getRunStatus()) ? FlowNodeProperties.SUCCESS_STYLE : FlowNodeProperties.FALIE_STYLE);
				if (node.getFailGoOn() == 0 && CommonRunStatus.FAILED.getCode().equals(nodeRecord.getRunStatus())) {
					break;
				}
			}
			flowProcess.infoEnd();
			//更新调度记录
			activeRecord.setEndTime(new Date());
			activeRecord.setRunStatus(recordSuccess ? CommonRunStatus.SUCCESS.getCode() : CommonRunStatus.FAILED.getCode());
			activeRecord.setLog(ConsolePool.getInstance().get(processId).toString());
			//保存历史json
			Flow flow = new Flow();
			DataProductionScheduleEntity scheduleEntity = baseMapper.selectById(activeRecord.getTaskScheduleId());
			flow.setId(scheduleEntity.getId());
			flow.setIfCycle(scheduleEntity.getIfCycle());
			flow.setRecordId(activeRecord.getId().intValue());
			flow.setName(scheduleEntity.getName());
			flow.setNote(scheduleEntity.getNote());
			flow.setCron(scheduleEntity.getCron());
			flow.setNodes(flowNodes);
			flow.setEdges(JSONUtil.parseObject(scheduleEntity.getEdges(), new TypeReference<List<FlowEdge>>() {
			}));
			activeRecord.setConfigJson(JSONUtil.toJsonString(flow));
			recordDao.updateById(activeRecord);
			//移除日志
			ProcessContextHolder.clearFlow();
			ConsolePool.getInstance().remove(processId);
		}
	}

	@Override
	public String scheduleRun(Integer id, ExecuteType executeType) {
		DataProductionScheduleEntity scheduleEntity = baseMapper.selectById(id);
		LambdaQueryWrapper<DataProductionScheduleNodeEntity> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(DataProductionScheduleNodeEntity::getTaskScheduleId, id)
				.orderByAsc(DataProductionScheduleNodeEntity::getSort);
		List<DataProductionScheduleNodeEntity> dbNodes = nodeDao.selectList(wrapper);
		//新增调度日志
		DataProductionScheduleRecordEntity scheduleRecordEntity = new DataProductionScheduleRecordEntity();
		scheduleRecordEntity.setCreator(scheduleEntity.getCreator());
		scheduleRecordEntity.setOrgId(scheduleEntity.getOrgId());
		scheduleRecordEntity.setProjectId(scheduleEntity.getProjectId());
		scheduleRecordEntity.setName(scheduleEntity.getName());
		scheduleRecordEntity.setTaskScheduleId(id);
		scheduleRecordEntity.setRunStatus(CommonRunStatus.RUNNING.getCode());
		scheduleRecordEntity.setStartTime(new Date());
		scheduleRecordEntity.setExecuteType(executeType.getValue());
		recordDao.insert(scheduleRecordEntity);
		ThreadUtil.threadPool.execute(() -> runNode(scheduleEntity, executeType, dbNodes, scheduleRecordEntity));
		return scheduleRecordEntity.getId().toString();
	}

	private void runNode(DataProductionScheduleEntity scheduleEntity, ExecuteType executeType, List<DataProductionScheduleNodeEntity> dbNodes, DataProductionScheduleRecordEntity scheduleRecordEntity) {
		String processId = scheduleRecordEntity.getId() + DataProductionScheduleRecordEntity.SCHEDULE_RECORD;
		ProcessEntity flowProcess = ProcessContextHolder.registerFlowProcess(ProcessEntity.init(ProcessType.FLOWEXECUTE, processId));
		flowProcess.info("Start run flow...");
		boolean recordSuccess = true;
		List<FlowNode> flowNodes = dbNodes.stream().map(this::getFlowNode).collect(Collectors.toList());
		for (DataProductionScheduleNodeEntity dbNode : dbNodes) {
			//返回给前台的节点
			FlowNode flowNode = flowNodes.stream().filter(item -> item.getId().equals(dbNode.getNo())).findFirst().get();
			flowNode.getProperties().setRunStatus(CommonRunStatus.SUCCESS.getCode());
			flowNode.getProperties().setStyle(FlowNodeProperties.SUCCESS_STYLE);
			flowProcess.info(String.format("Start run node %s-%s", dbNode.getName(), Dialect.getByCode(dbNode.getTaskType().toString()).getValue()));
			DataProductionScheduleNodeRecordEntity nodeRecordEntity = new DataProductionScheduleNodeRecordEntity();
			nodeRecordEntity.setCreator(scheduleEntity.getCreator());
			nodeRecordEntity.setOrgId(scheduleEntity.getOrgId());
			nodeRecordEntity.setProjectId(scheduleRecordEntity.getProjectId());
			nodeRecordEntity.setTaskScheduleId(scheduleEntity.getId().intValue());
			nodeRecordEntity.setScheduleNodeId(dbNode.getId().intValue());
			nodeRecordEntity.setScheduleNodeNo(dbNode.getNo());
			nodeRecordEntity.setScheduleRecordId(scheduleRecordEntity.getId().intValue());
			nodeRecordEntity.setTaskId(dbNode.getTaskId());
			nodeRecordEntity.setRunStatus(CommonRunStatus.RUNNING.getCode());
			nodeRecordEntity.setStartTime(new Date());
			nodeRecordEntity.setExecuteType(executeType.getValue());
			nodeRecordDao.insert(nodeRecordEntity);
			flowNode.getProperties().setNodeRecordId(nodeRecordEntity.getId().intValue());
			JobResult jobResult = null;
			try {
				jobResult = taskService.scheduleTask(nodeRecordEntity);
				//调试看效果用
				//Thread.sleep(5000);
				nodeRecordEntity.setEndTime(new Date());
				nodeRecordEntity.setLog(jobResult.getLog());
				if (!jobResult.isSuccess()) {
					recordSuccess = false;
					flowNode.getProperties().setRunStatus(CommonRunStatus.FAILED.getCode());
					flowNode.getProperties().setStyle(FlowNodeProperties.FALIE_STYLE);
					nodeRecordEntity.setRunStatus(CommonRunStatus.FAILED.getCode());
					//更新节点历史记录
					nodeRecordDao.updateById(nodeRecordEntity);
					if (dbNode.getFailGoOn() == 0) {
						flowProcess.info(jobResult.getLog());
						flowProcess.info(String.format("Node %s-%s run end", dbNode.getName(), Dialect.getByCode(dbNode.getTaskType().toString()).getValue()));
						break;
					}
				} else {
					nodeRecordEntity.setRunStatus(CommonRunStatus.SUCCESS.getCode());
					//更新节点历史记录
					nodeRecordDao.updateById(nodeRecordEntity);
				}
			} catch (Exception e) {
				recordSuccess = false;
				flowNode.getProperties().setRunStatus(CommonRunStatus.FAILED.getCode());
				flowNode.getProperties().setStyle(FlowNodeProperties.FALIE_STYLE);
				nodeRecordEntity.setEndTime(new Date());
				nodeRecordEntity.setLog(LogUtil.getError(e));
				nodeRecordEntity.setRunStatus(CommonRunStatus.FAILED.getCode());
				//更新节点历史记录
				nodeRecordDao.updateById(nodeRecordEntity);
				flowProcess.error(LogUtil.getError(e));
				if (dbNode.getFailGoOn() == 0) {
					flowProcess.info(String.format("Node %s-%s run end", dbNode.getName(), Dialect.getByCode(dbNode.getTaskType().toString()).getValue()));
					break;
				}
			}
			if (jobResult != null) {
				flowProcess.info(jobResult.getLog());
			}
			flowProcess.info(String.format("Node %s-%s run end", dbNode.getName(), Dialect.getByCode(dbNode.getTaskType().toString()).getValue()));
		}
		flowProcess.infoEnd();
		//更新调度记录
		scheduleRecordEntity.setEndTime(new Date());
		scheduleRecordEntity.setRunStatus(recordSuccess ? CommonRunStatus.SUCCESS.getCode() : CommonRunStatus.FAILED.getCode());
		scheduleRecordEntity.setLog(ConsolePool.getInstance().get(processId).toString());
		//保存历史json
		Flow flow = new Flow();
		flow.setId(scheduleEntity.getId());
		flow.setIfCycle(scheduleEntity.getIfCycle());
		flow.setRecordId(scheduleRecordEntity.getId().intValue());
		flow.setName(scheduleEntity.getName());
		flow.setNote(scheduleEntity.getNote());
		flow.setCron(scheduleEntity.getCron());
		flow.setNodes(flowNodes);
		flow.setEdges(JSONUtil.parseObject(scheduleEntity.getEdges(), new TypeReference<List<FlowEdge>>() {
		}));
		scheduleRecordEntity.setConfigJson(JSONUtil.toJsonString(flow));
		recordDao.updateById(scheduleRecordEntity);
		//移除日志
		ProcessContextHolder.clearFlow();
		ConsolePool.getInstance().remove(processId);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delete(List<Long> idList) {
		removeByIds(idList);
		for (Long id : idList) {
			//同步删除节点
			Map<String, Object> delMap = new HashMap<>();
			delMap.put("task_schedule_id", id);
			nodeDao.deleteByMap(delMap);
		}
	}
}
