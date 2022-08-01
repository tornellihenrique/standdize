package br.ufu.standdize.services;

import br.ufu.standdize.model.TrafficFlow;
import br.ufu.standdize.model.TrafficFlowSegment;
import br.ufu.standdize.model.Weather;
import br.ufu.standdize.model.dto.GeocodeResultAPIResponse;
import br.ufu.standdize.model.dto.GeocodeResultTypeAPIResponse;
import br.ufu.standdize.repository.TrafficFlowRepository;
import br.ufu.standdize.repository.TrafficFlowSegmentAddressRepository;
import br.ufu.standdize.repository.TrafficFlowSegmentRepository;
import br.ufu.standdize.util.MapsService;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    private final TrafficFlowSegmentAddressRepository trafficFlowSegmentAddressRepository;

    private final TrafficFlowSegmentRepository trafficFlowSegmentRepository;

    private final MapsService mapsService;

    @Override
    public void sync() throws Exception {
        val geocode = mapsService.getGeocode(city, GeocodeResultTypeAPIResponse.Geography);

        syncTrafficFlow(geocode);
        syncTrafficFlowSegment(geocode);
    }

    @Transactional
    public void syncTrafficFlow(GeocodeResultAPIResponse geocode) throws IOException, InterruptedException {
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

    @Transactional
    public void syncTrafficFlowSegment(GeocodeResultAPIResponse geocode) throws IOException, InterruptedException {
        val AddressList = trafficFlowSegmentAddressRepository.findByActiveTrue();

        AddressList.stream().forEach(a -> {
            try {
                val addressGeocode = mapsService.getGeocode(a.getAddress() + " " + city, GeocodeResultTypeAPIResponse.Street);

                val data = mapsService.getTrafficFlowSegmentData(addressGeocode);

                trafficFlowSegmentRepository.save(TrafficFlowSegment.builder()
                    .date(new Date())
                    .lastUpdate(new Date())
                    .address(a.getAddress())
                    .frc(data.getFrc())
                    .currentSpeed(data.getCurrentSpeed())
                    .freeFlowSpeed(data.getFreeFlowSpeed())
                    .currentTravelTime(data.getCurrentTravelTime())
                    .freeFlowTravelTime(data.getFreeFlowTravelTime())
                    .coordinates(data.getCoordinates().getItems())
                    .city(geocode.getAddress().getMunicipality())
                    .region(geocode.getAddress().getCountrySubdivision())
                    .country(geocode.getAddress().getCountry())
                    .build());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
}
