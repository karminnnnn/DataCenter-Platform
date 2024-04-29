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
@RelationshipProperties
@NoArgsConstructor
@AllArgsConstructor
public class DatabaseTableRelation {

	@RelationshipId
	private Long id;
	private Long relId;
	@TargetNode
	private Table table;
	@Property
	private String relationType;

	public DatabaseTableRelation(Table table, String relationType) {
		this.table = table;
		this.relationType = relationType;
	}

}
