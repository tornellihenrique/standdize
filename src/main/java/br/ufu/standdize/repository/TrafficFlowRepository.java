package br.ufu.standdize.repository;

import br.ufu.standdize.model.TrafficFlow;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TrafficFlowRepository extends MongoRepository<TrafficFlow, String> {
}