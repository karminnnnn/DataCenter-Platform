package net.srt.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import net.srt.framework.common.cache.bean.Neo4jInfo;
import net.srt.framework.common.utils.Result;
import net.srt.framework.common.utils.TreeNodeVo;
import net.srt.service.DataGovernanceMetadataService;
import net.srt.vo.DataGovernanceMetadataVO;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * 数据治理-元数据
 *
 * @author zrx 985134801@qq.com
 * @since 1.0.0 2023-03-29
 */
@RestController
@RequestMapping("metadata")
@Tag(name = "数据治理-元数据")
@AllArgsConstructor
public class DataGovernanceMetadataController {
	private final DataGovernanceMetadataService dataGovernanceMetadataService;

	@GetMapping("/list-child")
	@Operation(summary = "根据父级id获取信息")
	public Result<List<TreeNodeVo>> listByPatentId(@RequestParam Long parentId) {
		List<TreeNodeVo> treeNodeVos = dataGovernanceMetadataService.listByPatentId(parentId);
		return Result.ok(treeNodeVos);
	}

	@GetMapping("/list-floder")
	@Operation(summary = "获取目录树")
	public Result<List<TreeNodeVo>> listFloder() {
		List<TreeNodeVo> treeNodeVos = dataGovernanceMetadataService.listFloder();
		return Result.ok(treeNodeVos);
	}

	@GetMapping("/list-db")
	@Operation(summary = "获取库表目录树")
	public Result<List<TreeNodeVo>> listDb() {
		List<TreeNodeVo> treeNodeVos = dataGovernanceMetadataService.listDb();
		return Result.ok(treeNodeVos);
	}

	@GetMapping("/list-keyword")
	@Operation(summary = "模糊查询")
	public Result<List<TreeNodeVo>> listByKeyword(String keyword) {
		List<TreeNodeVo> treeNodeVos = dataGovernanceMetadataService.listByKeyword(keyword);
		return Result.ok(treeNodeVos);
	}


	@GetMapping("{id}")
	@Operation(summary = "信息")
	public Result<DataGovernanceMetadataVO> get(@PathVariable("id") Long id) {
		return Result.ok(dataGovernanceMetadataService.get(id));
	}

	@PostMapping
	@Operation(summary = "保存")
	public Result<String> save(@RequestBody DataGovernanceMetadataVO vo) {
		dataGovernanceMetadataService.save(vo);

		return Result.ok();
	}

	@PutMapping
	@Operation(summary = "修改")
	public Result<String> update(@RequestBody @Valid DataGovernanceMetadataVO vo) {
		dataGovernanceMetadataService.update(vo);

		return Result.ok();
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "删除")
	public Result<String> delete(@PathVariable Long id) {
		dataGovernanceMetadataService.delete(id);
		return Result.ok();
	}

	@PostMapping("/neo4j")
	@Operation(summary = "更新neo4j的url")
	public Result<String> upNeo4jInfo(@RequestBody Neo4jInfo neo4jInfo) {
		dataGovernanceMetadataService.upNeo4jInfo(neo4jInfo);
		return Result.ok();
	}

	@GetMapping("/neo4j")
	@Operation(summary = "获取neo4j的url")
	public Result<Neo4jInfo> getNeo4jInfo() {
		return Result.ok(dataGovernanceMetadataService.getNeo4jInfo());
	}
}
