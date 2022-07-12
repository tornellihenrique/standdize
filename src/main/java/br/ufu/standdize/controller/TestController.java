package br.ufu.standdize.controller;

import br.ufu.standdize.services.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TestController {

    private final WeatherService weatherService;

    @RequestMapping("/test")
    String test() {
        return "Hello World!";
    }

}
