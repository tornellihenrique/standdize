package br.ufu.standdize.services;

import br.ufu.standdize.model.Sync;
import br.ufu.standdize.model.TrafficFlow;
import br.ufu.standdize.model.TrafficFlowSegment;
import br.ufu.standdize.model.TrafficIncident;
import br.ufu.standdize.model.dto.api.geocode.GeocodeResultAPIResponse;
import br.ufu.standdize.model.dto.api.geocode.GeocodeResultTypeAPIResponse;
import br.ufu.standdize.model.dto.response.ServiceResponse;
import br.ufu.standdize.repository.*;
import br.ufu.standdize.util.MapsService;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
class TrafficResponse extends ServiceResponse {
    List<ServiceResponse> trafficFlows;
    List<ServiceResponse> trafficFlowSegments;
    List<ServiceResponse> trafficIncidents;
}

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

    private final SyncRepository syncRepository;

    @Override
    public void sync() throws Exception {
        val geocode = mapsService.getGeocode(city, GeocodeResultTypeAPIResponse.GEOGRAPHY);

        syncTrafficFlow(geocode);
        syncTrafficFlowSegment(geocode);
        syncTrafficIncidents(geocode);
    }

    @Override
    public String getId() {
        return "traffic";
    }

    @Override
    public ServiceResponse getOverview() {
        Sync sync = syncRepository.findFirstByTypeOrderByDateDesc(getClass().getName());

        return ServiceResponse.builder()
                .id(getId())
                .name("Trânsito")
                .description("Informações sobre trânsito")
                .date(sync != null ? sync.getDate() : null)
                .lastUpdate(sync != null ? sync.getDate() : null)
                .build();
    }

    @Override
    public ServiceResponse getDetails() {
        ServiceResponse overview = getOverview();

        return TrafficResponse.builder()
                .id(overview.getId())
                .name(overview.getName())
                .description(overview.getDescription())
                .date(overview.getDate())
                .lastUpdate(overview.getLastUpdate())
                .trafficFlows(trafficFlowRepository.findTop100ByOrderByDateDesc().stream().map(TrafficFlow::toResponse).toList())
                .trafficFlowSegments(trafficFlowSegmentRepository.findTop100ByOrderByDateDesc().stream().map(TrafficFlowSegment::toResponse).toList())
                .trafficIncidents(trafficIncidentRepository.findTop100ByOrderByDateDesc().stream().map(TrafficIncident::toResponse).toList())
                .build();
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
