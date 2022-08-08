package br.ufu.standdize.controller;

import br.ufu.standdize.exceptions.ServiceNotFoundException;
import br.ufu.standdize.model.Sync;
import br.ufu.standdize.model.dto.response.ServiceResponse;
import br.ufu.standdize.repository.SyncRepository;
import br.ufu.standdize.services.SyncService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/sync")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SyncController {

    @Autowired
    private List<SyncService> serviceList;

    private final SyncRepository syncRepository;

    @GetMapping
    public List<ServiceResponse> getSyncs() {
        return syncRepository.findTop100ByOrderByDateDesc().stream().map(Sync::toResponse).toList();
    }

}
