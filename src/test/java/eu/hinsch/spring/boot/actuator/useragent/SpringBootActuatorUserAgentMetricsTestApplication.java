package eu.hinsch.spring.boot.actuator.useragent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@EnableCaching
public class SpringBootActuatorUserAgentMetricsTestApplication {

    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager("user-agent-parser");
    }

    @RequestMapping("/test")
    @ResponseBody
    public String test() {
        return "ok";
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringBootActuatorUserAgentMetricsTestApplication.class, args);
    }
}
