package br.ufu.standdize.repository;

import br.ufu.standdize.model.TrafficIncident;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TrafficIncidentRepository extends MongoRepository<TrafficIncident, String> {
}