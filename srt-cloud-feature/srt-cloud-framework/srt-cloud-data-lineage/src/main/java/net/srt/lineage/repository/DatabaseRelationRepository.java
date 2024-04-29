package net.srt.lineage.repository;

import net.srt.lineage.relation.DatabaseRelation;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DatabaseRelationRepository extends Neo4jRepository<DatabaseRelation, Long> {

	@Query("match(a:DATABASE) WHERE ID(a)=$sourceId match(b:DATABASE) WHERE ID(b)=$targetId match p=(a)-[r:FLOW_TO]->(b) return {relId:ID(r)} LIMIT 1")
	DatabaseRelation getBySourceAndTargetId(Long sourceId, Long targetId);

	@Query("match(a:DATABASE) WHERE ID(a)=$sourceId match(b:DATABASE) WHERE ID(b)=$targetId " +
			"create (a)-[r:FLOW_TO" +
			"{relationType::#{#databaseRelation.relationType},dataAccessId::#{#databaseRelation.dataAccessId},dataAccessName::#{#databaseRelation.dataAccessName}}" +
			"]->(b)  ")
	void create(Long sourceId, Long targetId, @Param("databaseRelation") DatabaseRelation databaseRelation);

	@Query("MATCH (n:DATABASE)-[r:FLOW_TO]->(m:DATABASE) WHERE ID(r)=:#{#databaseRelation.id} SET r.relationType=:#{#databaseRelation.relationType},r.dataAccessId=:#{#databaseRelation.dataAccessId},r.dataAccessName=:#{#databaseRelation.dataAccessName}")
	void update(@Param("databaseRelation") DatabaseRelation databaseRelation);
}
