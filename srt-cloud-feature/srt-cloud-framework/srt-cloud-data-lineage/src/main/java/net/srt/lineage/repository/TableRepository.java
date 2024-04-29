package net.srt.lineage.repository;

import net.srt.lineage.node.Table;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TableRepository extends Neo4jRepository<Table, Long> {

	@Query("MATCH (n:TABLE) WHERE n.databaseId=$databaseId AND n.code=$code AND n.parentId=$parentId return n LIMIT 1")
	Table get(Long databaseId, String code, Long parentId);

	@Query("MATCH (n:TABLE) WHERE ID(n)=:#{#table.id} SET n.name=:#{#table.name},n.code=:#{#table.code},n.parentId=:#{#table.parentId},n.databaseId=:#{#table.databaseId}")
	void update(@Param("table") Table table);
}
