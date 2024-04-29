package net.srt.lineage.node;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.srt.lineage.relation.DatabaseRelation;
import net.srt.lineage.relation.DatabaseTableRelation;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据库节点
 *
 * @ClassName Database
 * @Author zrx
 * @Date 2023/4/10 14:13
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Node("DATABASE")
public class Database {
	@Id
	@GeneratedValue
	private Long id;
	@Property
	private String name;
	@Property
	private String code;
	@Property
	private String jdbcUrl;
	@Property
	private String username;
	@Property
	private String password;
	//库中的id
	@Property
	private Long databaseId;

	public Database(String name, String code, String jdbcUrl, String username, String password, Long databaseId) {
		this.name = name;
		this.code = code;
		this.jdbcUrl = jdbcUrl;
		this.username = username;
		this.password = password;
		this.databaseId = databaseId;
	}

	@Relationship(type = "FLOW_TO", direction = Relationship.Direction.OUTGOING)
	private List<DatabaseRelation> databaseRelations = new ArrayList<>();

	@Relationship(type = "BELONG_TO", direction = Relationship.Direction.INCOMING)
	private List<DatabaseTableRelation> databaseTableRelations = new ArrayList<>();

}
