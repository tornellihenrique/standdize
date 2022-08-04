package br.ufu.standdize.repository;

import br.ufu.standdize.model.TrafficIncidentAddress;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TrafficIncidentAddressRepository extends MongoRepository<TrafficIncidentAddress, String> {

    List<TrafficIncidentAddress> findByActiveTrue();

}