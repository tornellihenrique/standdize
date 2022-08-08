package br.ufu.standdize.util;

import br.ufu.standdize.model.dto.api.geocode.GeocodeAPIResponse;
import br.ufu.standdize.model.dto.api.geocode.GeocodeResultAPIResponse;
import br.ufu.standdize.model.dto.api.geocode.GeocodeResultTypeAPIResponse;
import br.ufu.standdize.model.dto.api.traffic.TrafficFlowAPIResponse;
import br.ufu.standdize.model.dto.api.traffic.TrafficFlowSegmentAPIResponse;
import br.ufu.standdize.model.dto.api.traffic.TrafficIncidentAPIResponse;
import br.ufu.standdize.model.dto.api.traffic.TrafficIncidentItemAPIResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MapsService {

    private static final String geocodingApiUri = "https://api.tomtom.com/search/2/geocode/%s.json?storeResult=false&view=Unified&key=%s";
    private static final String trafficFlowSegmentApiUri = "https://api.tomtom.com/traffic/services/4/flowSegmentData/relative0/%s/json?point=%s,%s&unit=KMPH&openLr=false&key=%s";
    private static final String trafficFlowTilesApiUri = "https://api.tomtom.com/traffic/map/4/tile/flow/relative0/%s/%s/%s.png?tileSize=512&key=%s";

    private static final String trafficIncidentApiUri = "https://api.tomtom.com/traffic/services/5/incidentDetails?bbox=%s&fields=%s&language=en-US&categoryFilter=%s&timeValidityFilter=present&key=%s";

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
        GeocodeResultAPIResponse geocode = getGeocode(query, GeocodeResultTypeAPIResponse.GEOGRAPHY);

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
        GeocodeResultAPIResponse geocode = getGeocode(query, GeocodeResultTypeAPIResponse.GEOGRAPHY);

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
        GeocodeResultAPIResponse geocode = getGeocode(query, GeocodeResultTypeAPIResponse.GEOGRAPHY);

        return getTrafficFlowSegmentData(geocode);
    }

    public List<TrafficIncidentItemAPIResponse> getTrafficIncidents(GeocodeResultAPIResponse geocode, int zoom, int d) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        Double lat = geocode.getPosition().getLat();
        Double lon = geocode.getPosition().getLon();

        String[] zxy = MapsUtils.latLonToTileZXY(lat, lon, zoom).split("/");
        int x = Integer.parseInt(zxy[1]);
        int y = Integer.parseInt(zxy[2]);

        StringBuilder box = new StringBuilder();

        String[] start = MapsUtils.tileZXYToLatLon(zoom, x-d, y+d).split("/");
        String[] end = MapsUtils.tileZXYToLatLon(zoom, x+d, y-d).split("/");

        box
            .append(start[0]).append(",").append(start[1])
            .append(",")
            .append(end[0]).append(",").append(end[1]);

        String fields = URLEncoder.encode("{incidents{type,geometry{type,coordinates},properties{iconCategory}}}", StandardCharsets.UTF_8);
        String categoryFilter = URLEncoder.encode("0,1,2,3,4,5,6,7,8,9,10,11,14", StandardCharsets.UTF_8);

        String uri = String.format(trafficIncidentApiUri, box, fields, categoryFilter, key);

        HttpRequest request = HttpRequest.newBuilder(URI.create(uri)).build();

        var response = client.send(request, new JsonBodyHandler<>(TrafficIncidentAPIResponse.class));

        return response.body().get().getIncidents();
    }

    public GeocodeResultAPIResponse getGeocode(String query, GeocodeResultTypeAPIResponse type) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        String uri = String.format(geocodingApiUri, URLEncoder.encode(query, StandardCharsets.UTF_8), key);

        HttpRequest request = HttpRequest.newBuilder(URI.create(uri)).build();

        var response = client.send(request, new JsonBodyHandler<>(GeocodeAPIResponse.class));

        return extractResult(response.body().get(), type);
    }

    public GeocodeResultAPIResponse getGeocode(String query) throws IOException, InterruptedException {
        return getGeocode(query, null);
    }

    private GeocodeResultAPIResponse extractResult(GeocodeAPIResponse response, GeocodeResultTypeAPIResponse type) {
        if (CollectionUtils.isEmpty(response.getResults())) return null;

        if (type != null) return response.getResults().stream().filter(r -> r.getType().equals(type)).findFirst().orElse(null);

        return response.getResults().get(0);
    }

}
