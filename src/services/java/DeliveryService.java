import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.Executors;

/**
 * Ein kleiner Lieferdienst für Kapitel 150: Er schätzt die Lieferzeit für eine Postleitzahl.
 *
 * <p>Start ohne Build, nur mit dem JDK:
 * <pre>
 * java src/services/java/DeliveryService.java        (Port 8081)
 * java src/services/java/DeliveryService.java 9090   (anderer Port)
 * </pre>
 *
 * <p>{@code GET /estimates?postalCode=40302} liefert {@code {"postalCode":"40302","minutes":25}}.
 * Der Dienst kennt nur einige Liefergebiete (die ersten beiden Ziffern der Postleitzahl), für alle
 * anderen antwortet er mit 404. Das Gebiet 99 ist überlastet: Die Antwort kommt erst nach
 * 5 Sekunden.
 */
public class DeliveryService {

    // Liefergebiet (erste zwei Ziffern) -> Grundlieferzeit in Minuten
    static final Map<String, Integer> AREAS = Map.of(
            "40", 25,
            "88", 40,
            "97", 20,
            "99", 30);

    static final String SLOW_AREA = "99";

    static final int SLOW_DELAY_MILLIS = 5_000;

    public static void main(String[] args) throws IOException {
        int port = args.length > 0 ? Integer.parseInt(args[0]) : 8081;
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/estimates", DeliveryService::handleEstimate);
        server.setExecutor(Executors.newCachedThreadPool()); // der langsame Fall blockiert sonst alle anderen
        server.start();
        System.out.println("Lieferdienst läuft auf http://localhost:" + port + "/estimates?postalCode=40302");
    }

    static void handleEstimate(HttpExchange exchange) throws IOException {
        try (exchange) {
            if (!"GET".equals(exchange.getRequestMethod())) {
                send(exchange, 405, problem(405, "Method Not Allowed", "Nur GET"));
                return;
            }
            String postalCode = queryParam(exchange, "postalCode");
            System.out.println("GET /estimates?postalCode=" + postalCode);
            if (postalCode == null || !postalCode.matches("\\d{5}")) {
                send(exchange, 400, problem(400, "Bad Request", "postalCode muss fünfstellig sein"));
                return;
            }
            String area = postalCode.substring(0, 2);
            Integer baseMinutes = AREAS.get(area);
            if (baseMinutes == null) {
                send(exchange, 404, problem(404, "Not Found", "Kein Liefergebiet: " + postalCode));
                return;
            }
            if (SLOW_AREA.equals(area)) {
                sleep(SLOW_DELAY_MILLIS);
            }
            // etwas Streuung innerhalb des Gebiets, aber für dieselbe Postleitzahl immer gleich
            int minutes = baseMinutes + Integer.parseInt(postalCode.substring(3)) % 10;
            send(exchange, 200, """
                    {"postalCode":"%s","minutes":%d}""".formatted(postalCode, minutes));
        }
    }

    static String queryParam(HttpExchange exchange, String name) {
        String query = exchange.getRequestURI().getRawQuery();
        if (query == null) {
            return null;
        }
        for (String pair : query.split("&")) {
            String[] keyValue = pair.split("=", 2);
            if (keyValue[0].equals(name) && keyValue.length == 2) {
                return URLDecoder.decode(keyValue[1], StandardCharsets.UTF_8);
            }
        }
        return null;
    }

    static String problem(int status, String title, String detail) {
        return """
                {"status":%d,"title":"%s","detail":"%s"}""".formatted(status, title, detail);
    }

    static void send(HttpExchange exchange, int status, String json) throws IOException {
        byte[] body = json.getBytes(StandardCharsets.UTF_8);
        String contentType = status == 200 ? "application/json" : "application/problem+json";
        exchange.getResponseHeaders().set("Content-Type", contentType);
        exchange.sendResponseHeaders(status, body.length);
        exchange.getResponseBody().write(body);
        System.out.println("  -> " + status + " " + json);
    }

    static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
