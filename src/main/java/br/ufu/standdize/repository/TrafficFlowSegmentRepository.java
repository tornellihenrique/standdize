package br.ufu.standdize.repository;

import br.ufu.standdize.model.TrafficFlowSegment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TrafficFlowSegmentRepository extends MongoRepository<TrafficFlowSegment, String> {

    List<TrafficFlowSegment> findTop100ByOrderByDateDesc();

}