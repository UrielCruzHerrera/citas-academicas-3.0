package mx.uv.dsw.citas.controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class HealthControllerTest {

    private final HealthController controller = new HealthController();

    @Test
    void health_retorna200ConStatusUp() {
        ResponseEntity<Map<String, Object>> respuesta = controller.health();

        assertEquals(200, respuesta.getStatusCode().value());
        assertNotNull(respuesta.getBody());
        assertEquals("UP", respuesta.getBody().get("status"));
        assertEquals("backend-citas", respuesta.getBody().get("service"));
        assertEquals("1.0.0", respuesta.getBody().get("version"));
    }
}