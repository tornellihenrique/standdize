package br.ufu.standdize.repository;

import br.ufu.standdize.model.Sync;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface SyncRepository extends MongoRepository<Sync, String> {

    Sync findFirstByTypeOrderByDateDesc(String type);

    List<Sync> findTop100ByOrderByDateDesc();

}