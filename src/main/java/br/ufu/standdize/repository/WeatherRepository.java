package br.ufu.standdize.repository;

import br.ufu.standdize.model.Weather;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface WeatherRepository extends MongoRepository<Weather, String> {

    List<Weather> findTop100ByOrderByDateDesc();

}