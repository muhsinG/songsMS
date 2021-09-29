package SBApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication //(exclude={DataSourceAutoConfiguration.class})
@EnableDiscoveryClient
public class AuthSpringBootApp {

    public static void main(String[] args) {
        SpringApplication.run(AuthSpringBootApp.class, args);
    }

    @Bean
    @LoadBalanced
    public RestTemplate getRestTemplate(){ return new RestTemplate();}

    @Bean
    public ServerCodecConfigurer serverCodecConfigurer() {
        return ServerCodecConfigurer.create();
    }
}
