package test.project1;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class Server extends AbstractVerticle {

    private Router router;
    private HttpServer server;
    private List<String> words = new ArrayList<>();

    @Override
    public void start(Promise<Void> start) throws Exception {
        router = Router.router(vertx);

        // Body handler for parsing JSON requests
        router.route().handler(BodyHandler.create());

        // Define analyze route
        router.post("/analyze").handler(this::analyze);

        // Serve static files from webroot directory
        router.route("/*").handler(StaticHandler.create("webroot"));

        // Read words from file
        readWordsFromFile();

        // Create HTTP server and start listening for requests
        server = vertx.createHttpServer();
        server.requestHandler(router).listen(8080, ar -> {
            if (ar.succeeded()) {
                System.out.println("Server started on port " + server.actualPort());
                start.complete();
            } else {
                System.out.println("Could not start server: " + ar.cause().getMessage());
                start.fail(ar.cause());
            }
        });
    }

    private void analyze(RoutingContext context) {
        @SuppressWarnings("deprecation")
        JsonObject request = context.getBodyAsJson();
        String input = request.getString("text");

        if (input == null || input.isEmpty()) {
            context.response().setStatusCode(400).end("Invalid input");
            return;
        }

        if (words.isEmpty()) {
            // No words to compare against, return null for both fields
            JsonObject result = new JsonObject();
            result.putNull("value");
            result.putNull("lexical");
            context.response().end(Json.encodeToBuffer(result));
            // Add new word to list and write to file
            words.add(input);
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/java/test/project1/words.txt", true));
                writer.write(input + "\n");
                writer.close();
            } catch (IOException e) {
                System.out.println("Error writing to file: " + e.getMessage());
                context.response().setStatusCode(500).end("Error writing to file");
                return;
            }
            return;
        }

        // Check if input already exists in list
        if (words.contains(input)) {
            // Find closest word based on character value and lexical order
            List<String> closestChar = words.stream()
                    .filter(w -> !w.equals(input))
                    .sorted(Comparator.comparingInt(w -> Math.abs(getTotalCharValue(w) - getTotalCharValue(input))))
                    .collect(Collectors.toList());

            List<String> closestLexical = words.stream()
                    .filter(w -> !w.equals(input))
                    .sorted(String.CASE_INSENSITIVE_ORDER)
                    .collect(Collectors.toList());

            JsonObject result = new JsonObject();
            result.put("value", closestChar.isEmpty() ? null : closestChar.get(0));
            result.put("lexical", closestLexical.isEmpty() ? null : closestLexical.get(0));

            context.response().end(Json.encodeToBuffer(result));
        } else {
            // Add new word to list and write to file
            words.add(input);
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/java/test/project1/words.txt", true));
                writer.write(input + "\n");
                writer.close();
            } catch (IOException e) {
                System.out.println("Error writing to file: " + e.getMessage());
                context.response().setStatusCode(500).end("Error writing to file");
                return;
            }

            // Find closest word based on character value and lexical order
            List<String> closestChar = words.stream()
                    .filter(w -> !w.equals(input))
                    .sorted(Comparator.comparingInt(w -> Math.abs(getTotalCharValue(w) - getTotalCharValue(input))))
                    .collect(Collectors.toList());

            List<String> closestLexical = words.stream()
                    .filter(w -> !w.equals(input))
                    .sorted(String.CASE_INSENSITIVE_ORDER)
                    .collect(Collectors.toList());

            JsonObject result = new JsonObject();
            result.put("value", closestChar.isEmpty() ? null : closestChar.get(0));
            result.put("lexical", closestLexical.isEmpty() ? null : closestLexical.get(0));

            context.response().end(Json.encodeToBuffer(result));
        }
    }




    private void readWordsFromFile() throws IOException {
    	File file = new File("src/main/java/test/project1/words.txt");;
        if (!file.exists()) {
            // Generar archivo si no existe
            file.createNewFile();
            
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.close();
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
        String line;
        while ((line = reader.readLine()) != null) {
            words.add(line.trim());
        }
        reader.close();
    }


    private int getTotalCharValue(String word) {
        if (word == null || word.isEmpty()) {
            return 0;
        }
        int totalCharValue = 0;
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            if (Character.isAlphabetic(c)) {
                totalCharValue += Character.toLowerCase(c) - 'a' + 1;
            }
        }
        return totalCharValue;
    }

}
