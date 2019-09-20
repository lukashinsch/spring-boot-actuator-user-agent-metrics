[![Coverage Status](https://coveralls.io/repos/lukashinsch/spring-boot-actuator-user-agent-metrics/badge.svg?branch=master)](https://coveralls.io/r/lukashinsch/spring-boot-actuator-user-agent-metrics?branch=master)
[![Build Status](https://travis-ci.org/lukashinsch/spring-boot-actuator-user-agent-metrics.svg?branch=master)](https://travis-ci.org/lukashinsch/spring-boot-actuator-user-agent-metrics)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/eu.hinsch/spring-boot-actuator-user-agent-metrics/badge.svg)](https://maven-badges.herokuapp.com/maven-central/eu.hinsch/spring-boot-actuator-user-agent-metrics/)

# spring-boot-actuator-user-agent-metrics
Filter to log user agent statistics as spring boot actuator metrics

## Upgrade notice
With version 0.2 the baseline has been updated to java 11 and spring boot 2.1.
To match the new metrics framework (micrometer), the metrics concept has been changed from individual metrics keys per value (e.g. browser version) to a single metric with (configurable) tags. Therefore, the `keys` namespace in the configuration has been replaced with a `tags` configuration map.

## Howto use

### Gradle
```
runtime("eu.hinsch:spring-boot-actuator-user-agent-metrics:0.2.0")
```

### Maven
```
<dependency>
  <groupId>eu.hinsch</groupId>
  <artifactId>spring-boot-actuator-user-agent-metrics</artifactId>
  <version>0.2.0</version>
</dependency>
```

### Configuration

All config properties are located beneath the prefix `user-agent-metric`

| Key          | Default    | Description                |
|--------------|------------|----------------------------|
| enabled      | false      | Turn metrics filter on/off | 
| url-patterns | empty list | List of patterns to match the servlet filter on |
| tags         | empty map  | key/value maps of tag name to expressions matching on net.sf.uadetector.ReadableUserAgent |

For an example see [SpringBootActuatorUserAgentMetricsTestApplication](https://github.com/lukashinsch/spring-boot-actuator-user-agent-metrics/blob/master/src/test/java/eu/hinsch/spring/boot/actuator/useragent/SpringBootActuatorUserAgentMetricsTestApplication.java)
and [application.yml](https://github.com/lukashinsch/spring-boot-actuator-user-agent-metrics/blob/master/src/test/resources/application.yml)
