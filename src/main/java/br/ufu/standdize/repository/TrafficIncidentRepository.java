package br.ufu.standdize.repository;

import br.ufu.standdize.model.TrafficIncident;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TrafficIncidentRepository extends MongoRepository<TrafficIncident, String> {

    List<TrafficIncident> findTop100ByOrderByDateDesc();

}