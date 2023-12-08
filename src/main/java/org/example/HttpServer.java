package org.example;

import io.javalin.Javalin;
import io.javalin.core.JavalinConfig;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class HttpServer {

    public static void startServer() {
        new Thread(() -> {
            Javalin.create(getConfig())
                    .get("/getConfigInfo", ctx -> {
                        Map<String, String> map = new HashMap<>();
                        map.put("deviceKey", "LAPTOP-7H4RS15T");
                        map.put("deviceName", "LAPTOP-7H4RS15T");
                        ctx.json(map);
                    });
        }).start();
    }

    private static Consumer<JavalinConfig> getConfig() {
        return JavalinConfig::enableCorsForAllOrigins;
    }
}
