package br.ufu.standdize;

import br.ufu.standdize.services.SyncService;
import br.ufu.standdize.services.WeatherService;
import br.ufu.standdize.util.MapsService;
import br.ufu.standdize.util.MapsUtils;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest(properties = {"spring.profiles.active=dev"})
class ExternalServicesTests {

	@Autowired
	private WeatherService weatherService;

	@Autowired
	private MapsService mapsService;

	@SneakyThrows
	@Test
	void checkWeather() {
		// Weather
		Assertions.assertNotNull(weatherService.getCurrentWeather());
	}

	@SneakyThrows
	@Test
	void checkTraffic() {
		// Traffic
		Assertions.assertNotNull(mapsService.getGeocode("Brazil"));
	}

	@SneakyThrows
	@Test
	void checkMapsUtils() {
		val res = mapsService.getGeocode("Brazil");

		Assertions.assertNotNull(res);

		String tile = MapsUtils.latLonToTileZXY(res.getPosition().getLat(), res.getPosition().getLon(), 1);

		Assertions.assertNotNull(tile);
		Assertions.assertFalse(tile.isEmpty());

		String[] tiles = tile.split("/");
		String latlon = MapsUtils.tileZXYToLatLon(Integer.parseInt(tiles[0]), Integer.parseInt(tiles[1]), Integer.parseInt(tiles[2]));

		Assertions.assertNotNull(latlon);
		Assertions.assertFalse(latlon.isEmpty());
	}
}
