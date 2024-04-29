package net.srt.lineage.relation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.srt.lineage.node.Column;
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
public class TableColumnRelation {

	@RelationshipId
	private Long id;
	private Long relId;
	@TargetNode
	private Column column;
	@Property
	private String relationType;

	public TableColumnRelation(Column column, String relationType) {
		this.column = column;
		this.relationType = relationType;
	}

}
