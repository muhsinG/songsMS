package htwb.muhsinG;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.http.codec.ServerCodecConfigurer;

@SpringBootApplication
@EnableDiscoveryClient
public class SongLyricsSpringBootApp {
    public static void main(String[] args) {
        SpringApplication.run(SongLyricsSpringBootApp.class, args);
    }

    @Bean
    public ServerCodecConfigurer serverCodecConfigurer() {
        return ServerCodecConfigurer.create();
    }

}
