package net.srt.lineage.relation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.srt.lineage.node.Database;
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
@NoArgsConstructor
@AllArgsConstructor
@RelationshipProperties
public class DatabaseRelation {

	@RelationshipId
	private Long id;
	private Long relId;
	@TargetNode
	private Database database;
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

	public DatabaseRelation(Database database, String relationType, Long dataAccessId, String dataAccessName, Long dataProductionTaskId, String dataProductionTaskName) {
		this.database = database;
		this.relationType = relationType;
		this.dataAccessId = dataAccessId;
		this.dataAccessName = dataAccessName;
		this.dataProductionTaskId = dataProductionTaskId;
		this.dataProductionTaskName = dataProductionTaskName;
	}

	public DatabaseRelation(Database database, String relationType, Long dataAccessId, String dataAccessName) {
		this.database = database;
		this.relationType = relationType;
		this.dataAccessId = dataAccessId;
		this.dataAccessName = dataAccessName;
	}
}
