package br.ufu.standdize.repository;

import br.ufu.standdize.model.TrafficFlow;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TrafficFlowRepository extends MongoRepository<TrafficFlow, String> {

    List<TrafficFlow> findTop100ByOrderByDateDesc();

}