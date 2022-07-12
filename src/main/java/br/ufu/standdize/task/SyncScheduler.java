package br.ufu.standdize.task;

import br.ufu.standdize.model.Sync;
import br.ufu.standdize.repository.SyncRepository;
import br.ufu.standdize.services.SyncService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.List;

@Component
public class SyncScheduler {

    @Autowired
    private List<SyncService> serviceList;

    @Autowired
    private SyncRepository syncRepository;

    @Scheduled(fixedRate = 1000)
    public void run() {
        serviceList.forEach(s -> {
            Sync sync = Sync.builder()
                    .date(OffsetDateTime.now())
                    .type(s.getClass().getName())
                    .hasError(false)
                    .build();

            try {
                s.sync();

                syncRepository.save(sync);
            } catch (Exception e) {
                syncRepository.save(sync.toBuilder().hasError(true).errorMessage(e.getMessage()).build());

                e.printStackTrace();
            }
        });
    }
}
