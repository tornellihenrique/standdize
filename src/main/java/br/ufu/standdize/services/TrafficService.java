package br.ufu.standdize.services;

import br.ufu.standdize.model.TrafficFlow;
import br.ufu.standdize.model.Weather;
import br.ufu.standdize.model.dto.GeocodeResultAPIResponse;
import br.ufu.standdize.model.dto.GeocodeResultTypeAPIResponse;
import br.ufu.standdize.repository.TrafficFlowRepository;
import br.ufu.standdize.util.MapsService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TrafficService implements SyncService {

    @Value("${city}")
    String city;

    @Value("${traffic.zoom}")
    Integer zoom;

    @Value("${traffic.range}")
    Integer range;

    private final TrafficFlowRepository trafficFlowRepository;

    private final MapsService mapsService;

    @Override
    public void sync() throws Exception {
        GeocodeResultAPIResponse geocode = mapsService.getGeocode(city, GeocodeResultTypeAPIResponse.Geography);

        syncTrafficFlow(geocode);
    }

    private void syncTrafficFlow(GeocodeResultAPIResponse geocode) throws IOException, InterruptedException {
        List<String> tiles = mapsService.getTrafficFlowTiles(geocode, zoom, range).stream().map(t -> {
            try {
                return Base64.getEncoder().encodeToString(IOUtils.toByteArray(t));
            } catch (IOException e) {
                return "";
            }
        }).filter(t -> !t.isEmpty()).collect(Collectors.toList());

        trafficFlowRepository.save(TrafficFlow.builder()
            .date(new Date())
            .lastUpdate(new Date())
            .tiles(tiles)
            .city(geocode.getAddress().getMunicipality())
            .region(geocode.getAddress().getCountrySubdivision())
            .country(geocode.getAddress().getCountry())
            .build());
    }
}
