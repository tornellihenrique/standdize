package br.ufu.standdize.util;

import br.ufu.standdize.model.dto.*;
import br.ufu.standdize.util.JsonBodyHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MapsService {

    private static final String geocodingApiUri = "https://api.tomtom.com/search/2/geocode/%s.json?storeResult=false&view=Unified&key=%s";
    private static final String trafficFlowSegmentApiUri = "https://api.tomtom.com/traffic/services/4/flowSegmentData/relative0/%s/json?point=%s,%s&unit=KMPH&openLr=false&key=%s";
    private static final String trafficFlowTilesApiUri = "https://api.tomtom.com/traffic/map/4/tile/flow/relative0/%s/%s/%s.png?tileSize=512&key=%s";

    @Value("${tomtom.key}")
    String key;

    public List<InputStream> getTrafficFlowTiles(GeocodeResultAPIResponse geocode, int zoom, int d) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        Double lat = geocode.getPosition().getLat();
        Double lon = geocode.getPosition().getLon();

        String[] zxy = MapsUtils.latLonToTileZXY(lat, lon, zoom).split("/");
        int x = Integer.parseInt(zxy[1]);
        int y = Integer.parseInt(zxy[2]);

        List<InputStream> list = new ArrayList<>();

        for (int i = x-d; i <= x+d; i++) {
            for (int j = y-d; j <= y+d; j++) {
                String uri = String.format(trafficFlowTilesApiUri, String.valueOf(zoom), i, j, key);

                HttpRequest request = HttpRequest.newBuilder(URI.create(uri)).build();

                var response = client.send(request, HttpResponse.BodyHandlers.ofInputStream());

                list.add(response.body());
            }
        }

        return list;
    }

    public List<InputStream> getTrafficFlowTiles(String query, int zoom, int d) throws IOException, InterruptedException {
        GeocodeResultAPIResponse geocode = getGeocode(query, GeocodeResultTypeAPIResponse.Geography);

        return getTrafficFlowTiles(geocode, zoom, d);
    }

    public InputStream getTrafficFlowTile(GeocodeResultAPIResponse geocode, int zoom) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        Double lat = geocode.getPosition().getLat();
        Double lon = geocode.getPosition().getLon();

        String[] zxy = MapsUtils.latLonToTileZXY(lat, lon, zoom).split("/");
        String x = zxy[1];
        String y = zxy[2];
        String uri = String.format(trafficFlowTilesApiUri, String.valueOf(zoom), x, y, key);

        HttpRequest request = HttpRequest.newBuilder(URI.create(uri)).build();

        var response = client.send(request, HttpResponse.BodyHandlers.ofInputStream());

        return response.body();
    }

    public InputStream getTrafficFlowTile(String query, int zoom) throws IOException, InterruptedException {
        GeocodeResultAPIResponse geocode = getGeocode(query, GeocodeResultTypeAPIResponse.Geography);

        return getTrafficFlowTile(geocode, zoom);
    }

    public TrafficFlowSegmentAPIResponse getTrafficFlowSegmentData(GeocodeResultAPIResponse geocode) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        String zoom = "10";

        String lat = geocode.getPosition().getLat().toString();
        String lon = geocode.getPosition().getLon().toString();
        String uri = String.format(trafficFlowSegmentApiUri, zoom, lat, lon, key);

        HttpRequest request = HttpRequest.newBuilder(URI.create(uri)).build();

        var response = client.send(request, new JsonBodyHandler<>(TrafficFlowAPIResponse.class));

        return response.body().get().getFlowSegment();
    }

    public TrafficFlowSegmentAPIResponse getTrafficFlowSegmentData(String query) throws IOException, InterruptedException {
        GeocodeResultAPIResponse geocode = getGeocode(query, GeocodeResultTypeAPIResponse.Geography);

        return getTrafficFlowSegmentData(geocode);
    }

    public GeocodeResultAPIResponse getGeocode(String query, GeocodeResultTypeAPIResponse type) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        String uri = String.format(geocodingApiUri, query, key);

        HttpRequest request = HttpRequest.newBuilder(URI.create(uri)).build();

        var response = client.send(request, new JsonBodyHandler<>(GeocodeAPIResponse.class));

        return extractResult(response.body().get(), type);
    }

    private GeocodeResultAPIResponse extractResult(GeocodeAPIResponse response, GeocodeResultTypeAPIResponse type) {
        return response.getResults().stream().filter(r -> r.getType().equals(type)).findFirst().orElse(null);
    }

}
