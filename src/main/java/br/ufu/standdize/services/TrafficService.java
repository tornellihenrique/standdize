package br.ufu.standdize.services;

import br.ufu.standdize.model.TrafficFlow;
import br.ufu.standdize.model.TrafficFlowSegment;
import br.ufu.standdize.model.TrafficIncident;
import br.ufu.standdize.model.Weather;
import br.ufu.standdize.model.dto.GeocodeResultAPIResponse;
import br.ufu.standdize.model.dto.GeocodeResultTypeAPIResponse;
import br.ufu.standdize.repository.*;
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
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TrafficService implements SyncService {

    @Value("${city}")
    String city;

    @Value("${traffic.flow.zoom}")
    Integer zoomFlow;

    @Value("${traffic.flow.range}")
    Integer rangeFlow;

    @Value("${traffic.incidents.zoom}")
    Integer zoomIncidents;

    @Value("${traffic.incidents.range}")
    Integer rangeIncidents;

    private final TrafficFlowRepository trafficFlowRepository;

    private final TrafficFlowSegmentAddressRepository trafficFlowSegmentAddressRepository;

    private final TrafficFlowSegmentRepository trafficFlowSegmentRepository;

    private final TrafficIncidentAddressRepository trafficIncidentAddressRepository;

    private final TrafficIncidentRepository trafficIncidentRepository;

    private final MapsService mapsService;

    @Override
    public void sync() throws Exception {
        val geocode = mapsService.getGeocode(city, GeocodeResultTypeAPIResponse.GEOGRAPHY);

        syncTrafficFlow(geocode);
        syncTrafficFlowSegment(geocode);
        syncTrafficIncidents(geocode);
    }

    @Transactional
    public void syncTrafficFlow(GeocodeResultAPIResponse geocode) throws IOException, InterruptedException {
        List<String> tiles = mapsService.getTrafficFlowTiles(geocode, zoomFlow, rangeFlow).stream().map(t -> {
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
    public void syncTrafficFlowSegment(GeocodeResultAPIResponse geocode) {
        val addressList = trafficFlowSegmentAddressRepository.findByActiveTrue();

        val entities = addressList.stream().map(a -> {
            try {
                val addressGeocode = mapsService.getGeocode(a.getAddress() + " " + city);

                val data = mapsService.getTrafficFlowSegmentData(addressGeocode);

                return TrafficFlowSegment.builder()
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
                    .build();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).toList();

        trafficFlowSegmentRepository.saveAll(entities);
    }

    @Transactional
    public void syncTrafficIncidents(GeocodeResultAPIResponse geocode) {
        val addressList = trafficIncidentAddressRepository.findByActiveTrue();

        val entities = addressList.stream().map(a -> {
            try {
                val addressGeocode = mapsService.getGeocode(a.getAddress() + " " + city);

                val data = mapsService.getTrafficIncidents(addressGeocode, zoomIncidents, rangeIncidents);

                return data.stream().map(d -> TrafficIncident.builder()
                        .date(new Date())
                        .lastUpdate(new Date())
                        .address(a.getAddress())
                        .externalId(d.getProperties().getId())
                        .type(d.getType())
                        .iconCategory(d.getProperties().getIconCategory())
                        .magnitudeOfDelay(d.getProperties().getMagnitudeOfDelay())
                        .geometryType(d.getGeometry().getType())
                        .coordinates(d.getGeometry().getCoordinates())
                        .city(geocode.getAddress().getMunicipality())
                        .region(geocode.getAddress().getCountrySubdivision())
                        .country(geocode.getAddress().getCountry())
                        .build()).toList();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).flatMap(Collection::stream).toList();

        trafficIncidentRepository.saveAll(entities);
    }
}
