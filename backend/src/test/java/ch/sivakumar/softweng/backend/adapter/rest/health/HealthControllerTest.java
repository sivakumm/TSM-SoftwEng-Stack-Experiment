package ch.sivakumar.softweng.backend.adapter.rest.health;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HealthControllerTest {

    @Test
    void shouldReturnHealthyState() {
        HealthController controller = new HealthController();

        ResponseEntity<String> result = controller.returnHealthCheck();

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("Healthy", result.getBody());
    }
}
