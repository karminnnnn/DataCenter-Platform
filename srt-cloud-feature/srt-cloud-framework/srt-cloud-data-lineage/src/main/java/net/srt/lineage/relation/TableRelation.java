package net.srt.lineage.relation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.srt.lineage.node.Table;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.RelationshipId;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

/**
 * @ClassName DatabaseRelation
 * @Author zrx
 * @Date 2023/4/10 14:35
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@RelationshipProperties
public class TableRelation {

	@RelationshipId
	private Long id;
	private Long relId;
	@TargetNode
	private Table table;
	@Property
	private String relationType;
	@Property
	private Long dataAccessId;
	@Property
	private String dataAccessName;
	@Property
	private Long dataProductionTaskId;
	@Property
	private String dataProductionTaskName;

	public TableRelation(Table table, String relationType, Long dataAccessId, String dataAccessName) {
		this.table = table;
		this.relationType = relationType;
		this.dataAccessId = dataAccessId;
		this.dataAccessName = dataAccessName;
	}

}
