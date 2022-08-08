package br.ufu.standdize;

import br.ufu.standdize.services.SyncService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest(properties = {"spring.profiles.active=dev"})
class StanddizeApplicationTests {

	@Autowired
	private List<SyncService> serviceList;

	@Test
	void checkServices() {
		Assertions.assertTrue(serviceList.size() > 0);

		for (SyncService service : serviceList) {
			Assertions.assertNotNull(service);
			Assertions.assertNotNull(service.getId());
			Assertions.assertNotNull(service.getOverview());
		}
	}

	@Test
	void checkServicesGetId() {
		Assertions.assertTrue(serviceList.size() > 0);

		for (SyncService service : serviceList) {
			Assertions.assertNotNull(service);
			Assertions.assertNotNull(service.getId());
		}
	}

	@Test
	void checkServicesGetOverview() {
		Assertions.assertTrue(serviceList.size() > 0);

		for (SyncService service : serviceList) {
			Assertions.assertNotNull(service);
			Assertions.assertNotNull(service.getOverview());
		}
	}
}
