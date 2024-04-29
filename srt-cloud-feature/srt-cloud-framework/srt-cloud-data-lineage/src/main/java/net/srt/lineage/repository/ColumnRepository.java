package net.srt.lineage.repository;

import net.srt.lineage.node.Column;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ColumnRepository extends Neo4jRepository<Column, Long> {

	@Query("MATCH (n:COLUMN) WHERE n.databaseId=$databaseId AND n.code=$code AND n.parentId=$parentId return n LIMIT 1")
	Column get(Long databaseId, String code, Long parentId);

	@Query("MATCH (n:COLUMN) WHERE ID(n)=:#{#column.id} SET n.name=:#{#column.name},n.code=:#{#column.code},n.parentId=:#{#column.parentId},n.databaseId=:#{#column.databaseId}")
	void update(@Param("column") Column column);
}
