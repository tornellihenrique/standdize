package br.ufu.standdize.repository;

import br.ufu.standdize.model.TrafficFlowSegment;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TrafficFlowSegmentRepository extends MongoRepository<TrafficFlowSegment, String> {
}