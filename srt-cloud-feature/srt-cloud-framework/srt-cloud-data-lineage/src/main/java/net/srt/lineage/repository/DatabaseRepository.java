package net.srt.lineage.repository;

import net.srt.lineage.node.Database;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DatabaseRepository extends Neo4jRepository<Database, Long> {

	@Query("MATCH (n:DATABASE) WHERE n.databaseId=$databaseId return n LIMIT 1")
	Database getByDatabaseId(Long databaseId);

	@Query("MATCH (n:DATABASE) WHERE ID(n)=:#{#database.id} SET n.name=:#{#database.name},n.code=:#{#database.code},n.jdbcUrl=:#{#database.jdbcUrl},n.username=:#{#database.username},n.password=:#{#database.password}")
	void update(@Param("database") Database database);

	/*@Query("MATCH p=()-[r:FLOW_TO]->() RETURN p LIMIT 25")
	List<Database> selectAll();*/
}
