package edu.psu.se411.lab15;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class Lab15ApplicationTests {

	@Autowired
	Environment env;

	private final HttpClient http = HttpClient.newHttpClient();

	private HttpResponse<String> send(String method, String path, String json) throws Exception {
		String base = "http://localhost:" + env.getProperty("local.server.port");
		HttpRequest.Builder b = HttpRequest.newBuilder(URI.create(base + path)).header("Content-Type", "application/json");
		b.method(method, json == null ? HttpRequest.BodyPublishers.noBody() : HttpRequest.BodyPublishers.ofString(json));
		return http.send(b.build(), HttpResponse.BodyHandlers.ofString());
	}

	@Test
	void readAllAndOne() throws Exception {
		HttpResponse<String> all = send("GET", "/api/v1/courses", null);
		assertEquals(200, all.statusCode());
		assertTrue(all.body().contains("SE201") && all.body().contains("CS101"));
		assertEquals(200, send("GET", "/api/v1/courses/1", null).statusCode());
		assertEquals(404, send("GET", "/api/v1/courses/999", null).statusCode());
	}

	@Test
	void createUpdateDelete() throws Exception {
		HttpResponse<String> created = send("POST", "/api/v1/courses",
				"{\"code\":\"SE411\",\"name\":\"Software Construction\",\"description\":\"Labs\",\"credits\":3}");
		assertEquals(201, created.statusCode());
		String location = created.headers().firstValue("Location").orElseThrow();
		assertTrue(location.endsWith("/api/v1/courses/3"), location);

		assertEquals(204, send("PUT", "/api/v1/courses/3",
				"{\"code\":\"SE411\",\"name\":\"Software Construction II\",\"description\":\"Labs\",\"credits\":4}").statusCode());
		assertTrue(send("GET", "/api/v1/courses/3", null).body().contains("Software Construction II"));

		assertEquals(204, send("DELETE", "/api/v1/courses/3", null).statusCode());
		assertEquals(404, send("GET", "/api/v1/courses/3", null).statusCode());
	}

	@Test
	void validationRejectsBadCourse() throws Exception {
		assertEquals(400, send("POST", "/api/v1/courses",
				"{\"code\":\"\",\"name\":\"X\",\"credits\":20}").statusCode());
	}
}
