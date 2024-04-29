package net.srt.lineage.repository;

import net.srt.lineage.relation.DatabaseRelation;
import net.srt.lineage.relation.TableRelation;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TableRelationRepository extends Neo4jRepository<TableRelation, Long> {

	@Query("match(a:TABLE) WHERE ID(a)=$sourceId match(b:TABLE) WHERE ID(b)=$targetId match p=(a)-[r:FLOW_TO]->(b) return {relId:ID(r)} LIMIT 1")
	TableRelation getBySourceAndTargetId(Long sourceId, Long targetId);

	@Query("match(a:TABLE) WHERE ID(a)=$sourceId match(b:TABLE) WHERE ID(b)=$targetId " +
			"create (a)-[r:FLOW_TO" +
			"{relationType::#{#tableRelation.relationType},dataAccessId::#{#tableRelation.dataAccessId},dataAccessName::#{#tableRelation.dataAccessName}}" +
			"]->(b)  ")
	void create(Long sourceId, Long targetId, @Param("tableRelation") TableRelation tableRelation);

	@Query("MATCH (n:TABLE)-[r:FLOW_TO]->(m:TABLE) WHERE ID(r)=:#{#tableRelation.id} SET r.relationType=:#{#tableRelation.relationType},r.dataAccessId=:#{#tableRelation.dataAccessId},r.dataAccessName=:#{#tableRelation.dataAccessName}")
	void update(@Param("tableRelation") TableRelation tableRelation);
}
