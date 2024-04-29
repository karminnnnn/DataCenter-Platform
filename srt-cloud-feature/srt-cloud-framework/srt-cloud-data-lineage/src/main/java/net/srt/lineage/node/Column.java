package net.srt.lineage.node;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.srt.lineage.relation.ColumnRelation;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName Table
 * @Author zrx
 * @Date 2023/4/10 14:24
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Node("COLUMN")
public class Column {
	@Id
	@GeneratedValue
	private Long id;
	@Property
	private String name;
	@Property
	private String code;
	@Property
	private Long parentId;
	@Property
	private Long databaseId;

	public Column(String name, String code, Long databaseId, Long parentId) {
		this.name = name;
		this.code = code;
		this.databaseId = databaseId;
		this.parentId = parentId;
	}

	@Relationship(type = "FLOW_TO", direction = Relationship.Direction.OUTGOING)
	private List<ColumnRelation> columnRelations = new ArrayList<>();
}
