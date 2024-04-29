package net.srt.lineage.repository;

import net.srt.lineage.relation.ColumnRelation;
import net.srt.lineage.relation.DatabaseRelation;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ColumnRelationRepository extends Neo4jRepository<ColumnRelation, Long> {

	@Query("match(a:COLUMN) WHERE ID(a)=$sourceId match(b:COLUMN) WHERE ID(b)=$targetId match p=(a)-[r:FLOW_TO]->(b) return {relId:ID(r)} LIMIT 1")
	ColumnRelation getBySourceAndTargetId(Long sourceId, Long targetId);

	@Query("match(a:COLUMN) WHERE ID(a)=$sourceId match(b:COLUMN) WHERE ID(b)=$targetId " +
			"create (a)-[r:FLOW_TO" +
			"{relationType::#{#columnRelation.relationType},dataAccessId::#{#columnRelation.dataAccessId},dataAccessName::#{#columnRelation.dataAccessName}}" +
			"]->(b)  ")
	void create(@Param("sourceId") Long sourceId, @Param("targetId") Long targetId, @Param("columnRelation") ColumnRelation columnRelation);

	@Query("MATCH (n:COLUMN)-[r:FLOW_TO]->(m:COLUMN) WHERE ID(r)=:#{#columnRelation.id} SET r.relationType=:#{#columnRelation.relationType},r.dataAccessId=:#{#columnRelation.dataAccessId},r.dataAccessName=:#{#columnRelation.dataAccessName}")
	void update(@Param("columnRelation") ColumnRelation columnRelation);
}
