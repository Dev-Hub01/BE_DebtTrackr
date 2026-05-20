package com.debttrackr;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.net.InetAddress;

@Component
public class StartupLogger {

    private final Environment env;

    public StartupLogger(Environment env) {
        this.env = env;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void logStartup() {
        try {
            String appName = env.getProperty("spring.application.name");
            String port = env.getProperty("server.port");
            String profile = String.join(",", env.getActiveProfiles());
            String hostAddress = InetAddress.getLocalHost().getHostAddress();

            System.out.println("\n----------------------------------------------------------");
            System.out.println("🚀 Application Started Successfully!");
            System.out.println("📦 Application : " + appName);
            System.out.println("🌐 Local URL   : http://localhost:" + port);
            System.out.println("🌍 External URL: http://" + hostAddress + ":" + port);
            System.out.println("⚙️ Profile     : " + (profile.isEmpty() ? "default" : profile));
            System.out.println("----------------------------------------------------------\n");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
