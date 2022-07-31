package br.ufu.standdize.task;

import br.ufu.standdize.model.Sync;
import br.ufu.standdize.repository.SyncRepository;
import br.ufu.standdize.services.SyncService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
@Slf4j
public class SyncScheduler {

    @Autowired
    private List<SyncService> serviceList;

    @Autowired
    private SyncRepository syncRepository;

    @Scheduled(fixedRate = 10000)
    public void run() {
        serviceList.forEach(s -> {
            Sync sync = Sync.builder()
                    .date(new Date())
                    .type(s.getClass().getName())
                    .hasError(false)
                    .build();

            try {
                log.info("Syncing " + sync.getType());

                s.sync();

                syncRepository.save(sync);
                log.info("Success!");
            } catch (Exception e) {
                syncRepository.save(sync.toBuilder().hasError(true).errorMessage(e.getMessage()).build());
                log.info("Error!");

                e.printStackTrace();
            }
        });
    }
}
