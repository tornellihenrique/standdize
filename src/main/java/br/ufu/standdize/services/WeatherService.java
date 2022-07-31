package br.ufu.standdize.services;

import br.ufu.standdize.model.Weather;
import br.ufu.standdize.model.dto.WeatherAPIResponse;
import br.ufu.standdize.repository.WeatherRepository;
import br.ufu.standdize.util.JsonBodyHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WeatherService implements SyncService {

    private static final String apiUri = "http://api.weatherapi.com/v1/current.json?key=%s&q=%s&aqi=no";

    @Value("${weather.key}")
    String key;

    @Value("${city}")
    String city;

    private final WeatherRepository weatherRepository;

    @Override
    public void sync() throws Exception {
        WeatherAPIResponse response = callApi();

        weatherRepository.save(Weather.builder()
            .date(new Date())
            .lastUpdate(new Date(OffsetDateTime.ofInstant(response.getCurrent().getLastUpdate().toInstant(), ZoneId.systemDefault()).toInstant().toEpochMilli()))
            .condition(response.getCurrent().getCondition().getText())
            .conditionIcon(response.getCurrent().getCondition().getIcon())
            .conditionCode(response.getCurrent().getCondition().getCode())
            .windKph(response.getCurrent().getWindKph())
            .windDegree(response.getCurrent().getWindDegree())
            .windDir(response.getCurrent().getWindDir())
            .pressureMb(response.getCurrent().getPressureMb())
            .pressureIn(response.getCurrent().getPressureIn())
            .humidity(response.getCurrent().getHumidity())
            .cloud(response.getCurrent().getCloud())
            .city(response.getLocation().getName())
            .region(response.getLocation().getRegion())
            .country(response.getLocation().getCountry())
            .build());
    }

    private WeatherAPIResponse callApi() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        String uri = String.format(apiUri, key, city);

        HttpRequest request = HttpRequest.newBuilder(URI.create(uri)).build();

        var response = client.send(request, new JsonBodyHandler<>(WeatherAPIResponse.class));

        return response.body().get();
    }
}
