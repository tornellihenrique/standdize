package br.ufu.standdize.services;

import br.ufu.standdize.model.dto.response.ServiceDetailsResponse;
import br.ufu.standdize.model.dto.response.ServiceResponse;

public interface SyncService {

    void sync() throws Exception;

    String getId();

    ServiceResponse getOverview();

    ServiceResponse getDetails();

}
