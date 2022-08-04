package br.ufu.standdize.controller;

import br.ufu.standdize.model.TrafficFlowSegmentAddress;
import br.ufu.standdize.model.TrafficIncidentAddress;
import br.ufu.standdize.repository.TrafficFlowSegmentAddressRepository;
import br.ufu.standdize.repository.TrafficIncidentAddressRepository;
import br.ufu.standdize.services.WeatherService;
import br.ufu.standdize.util.MapsService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TestController {

    private final WeatherService weatherService;
    private final MapsService mapsService;

    private final TrafficFlowSegmentAddressRepository trafficFlowSegmentAddressRepository;

    private final TrafficIncidentAddressRepository trafficIncidentAddressRepository;

    @RequestMapping("/test")
    public ResponseEntity<InputStreamResource> testFlowTiles(@RequestParam("zoom") int zoom, @RequestParam("d") int d) throws IOException, InterruptedException {
        MediaType contentType = MediaType.IMAGE_PNG;

        List<InputStream> is = mapsService.getTrafficFlowTiles("Uberlandia", zoom, d);

        return ResponseEntity.ok().contentType(contentType).body(new InputStreamResource(new SequenceInputStream(Collections.enumeration(is))));
    }

    @RequestMapping("/test2")
    public void testAddresses() {
        trafficFlowSegmentAddressRepository.save(TrafficFlowSegmentAddress.builder()
                .creationDate(new Date())
                .modificationDate(new Date())
                .address("Rondon Pacheco")
                .build());


        trafficIncidentAddressRepository.save(TrafficIncidentAddress.builder()
                .creationDate(new Date())
                .modificationDate(new Date())
                .address("Rondon Pacheco")
                .build());
    }

}
