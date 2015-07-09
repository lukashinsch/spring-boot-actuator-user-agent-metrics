[![Coverage Status](https://coveralls.io/repos/lukashinsch/spring-boot-actuator-user-agent-metrics/badge.svg?branch=master)](https://coveralls.io/r/lukashinsch/spring-boot-actuator-user-agent-metrics?branch=master)
[![Build Status](https://travis-ci.org/lukashinsch/spring-boot-actuator-user-agent-metrics.svg?branch=master)](https://travis-ci.org/lukashinsch/spring-boot-actuator-user-agent-metrics)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/eu.hinsch/spring-boot-actuator-user-agent-metrics/badge.svg)](https://maven-badges.herokuapp.com/maven-central/spring-boot-actuator-user-agent-metrics/)

# spring-boot-actuator-user-agent-metrics
Filter to log user agent statistics as spring boot actuator metrics

## Howto use

### Maven
```
<dependency>
  <groupId>eu.hinsch</groupId>
  <artifactId>spring-boot-actuator-user-agent-metrics</artifactId>
  <version>0.1.0</version>
</dependency>
```

### Gradle
```
runtime("eu.hinsch:spring-boot-actuator-user-agent-metrics:0.1.8")
```

### Configuration

For an example see [SpringBootActuatorUserAgentMetricsTestApplication](https://github.com/lukashinsch/spring-boot-actuator-user-agent-metrics/blob/master/src/test/java/eu/hinsch/spring/boot/actuator/useragent/SpringBootActuatorUserAgentMetricsTestApplication.java)
and [application.yml](https://github.com/lukashinsch/spring-boot-actuator-user-agent-metrics/blob/master/src/test/resources/application.yml)
