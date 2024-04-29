package net.srt.lineage.repository;

import net.srt.lineage.relation.TableColumnRelation;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TableColumnRelationRepository extends Neo4jRepository<TableColumnRelation, Long> {

	@Query("match(a:TABLE) WHERE ID(a)=$targetId match(b:COLUMN) WHERE ID(b)=$sourceId match p=(b)-[r:BELONG_TO]->(a) return {relId:ID(r)} LIMIT 1")
	TableColumnRelation getBySourceAndTargetId(Long sourceId, Long targetId);

	@Query("match(a:TABLE) WHERE ID(a)=$targetId match(b:COLUMN) WHERE ID(b)=$sourceId " +
			"create (b)-[r:BELONG_TO" +
			"{relationType:$relationType}" +
			"]->(a)  ")
	void create(Long sourceId, Long targetId, String relationType);
}
