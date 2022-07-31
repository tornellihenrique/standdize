package br.ufu.standdize.controller;

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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.util.Collections;
import java.util.List;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TestController {

    private final WeatherService weatherService;
    private final MapsService mapsService;

    @RequestMapping("/test")
    public ResponseEntity<InputStreamResource> test(@RequestParam("zoom") int zoom, @RequestParam("d") int d) throws IOException, InterruptedException {
        MediaType contentType = MediaType.IMAGE_PNG;

        List<InputStream> is = mapsService.getTrafficFlowTiles("Uberlandia", zoom, d);

        return ResponseEntity.ok().contentType(contentType).body(new InputStreamResource(new SequenceInputStream(Collections.enumeration(is))));
    }

}
