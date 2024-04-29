package net.srt.lineage.repository;

import net.srt.lineage.relation.DatabaseRelation;
import net.srt.lineage.relation.DatabaseTableRelation;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DatabaseTableRelationRepository extends Neo4jRepository<DatabaseTableRelation, Long> {

	@Query("match(a:DATABASE) WHERE ID(a)=$targetId match(b:TABLE) WHERE ID(b)=$sourceId match p=(b)-[r:BELONG_TO]->(a) return {relId:ID(r)} LIMIT 1")
	DatabaseTableRelation getBySourceAndTargetId(Long sourceId, Long targetId);

	@Query("match(a:DATABASE) WHERE ID(a)=$targetId match(b:TABLE) WHERE ID(b)=$sourceId " +
			"create (b)-[r:BELONG_TO" +
			"{relationType:$relationType}" +
			"]->(a)  ")
	void create(Long sourceId, Long targetId, String relationType);
}
