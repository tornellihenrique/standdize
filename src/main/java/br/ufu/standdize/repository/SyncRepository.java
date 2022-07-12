package br.ufu.standdize.repository;

import br.ufu.standdize.model.Sync;
import br.ufu.standdize.model.Weather;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SyncRepository extends MongoRepository<Sync, String> {
}