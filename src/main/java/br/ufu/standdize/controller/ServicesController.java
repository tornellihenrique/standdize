package br.ufu.standdize.controller;

import br.ufu.standdize.exceptions.ServiceNotFoundException;
import br.ufu.standdize.model.dto.response.ServiceDetailsResponse;
import br.ufu.standdize.model.dto.response.ServiceResponse;
import br.ufu.standdize.services.SyncService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;

@RestController
@RequestMapping("/service")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ServicesController {

    @Autowired
    private List<SyncService> serviceList;

    @GetMapping("/overview")
    public List<ServiceResponse> getOverview() {
        return serviceList.stream().map(SyncService::getOverview).toList();
    }

    @GetMapping("/{id}")
    public ServiceResponse getDetails(@PathVariable("id") String id) {
        SyncService service = serviceList.stream()
                .filter(s -> s.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ServiceNotFoundException("Service: " + id));

        return service.getDetails();
    }

}
