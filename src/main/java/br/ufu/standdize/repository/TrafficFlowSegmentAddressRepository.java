package br.ufu.standdize.repository;

import br.ufu.standdize.model.TrafficFlowSegmentAddress;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TrafficFlowSegmentAddressRepository extends MongoRepository<TrafficFlowSegmentAddress, String> {

    List<TrafficFlowSegmentAddress> findByActiveTrue();

}