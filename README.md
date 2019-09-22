[![Coverage Status](https://coveralls.io/repos/lukashinsch/spring-boot-actuator-user-agent-metrics/badge.svg?branch=master)](https://coveralls.io/r/lukashinsch/spring-boot-actuator-user-agent-metrics?branch=master)
[![Build Status](https://travis-ci.org/lukashinsch/spring-boot-actuator-user-agent-metrics.svg?branch=master)](https://travis-ci.org/lukashinsch/spring-boot-actuator-user-agent-metrics)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/eu.hinsch/spring-boot-actuator-user-agent-metrics/badge.svg)](https://maven-badges.herokuapp.com/maven-central/eu.hinsch/spring-boot-actuator-user-agent-metrics/)

# spring-boot-actuator-user-agent-metrics
Filter to log user agent statistics as spring boot actuator metrics. Uses Yauaa library (https://github.com/nielsbasjes/yauaa) under the hood.

## Upgrade notice
With version 0.3 the baseline has been updated to java 11 and spring boot 2.1 and the underlying user agent parser library has been replaced with a more recent project.
To match the new metrics framework (micrometer), the metrics concept has been changed from individual metrics keys per value (e.g. browser version) to a single metric with (configurable) tags. Therefore, the `keys` namespace in the configuration has been renamed to `tags`.

Note: do not use version 0.2.0, while it was updated to work with current framework versions, the library used for user agent parsing was outdated and could not correctly identify a number of current browsers.

## Howto use

### Gradle
```
runtime("eu.hinsch:spring-boot-actuator-user-agent-metrics:0.3.0")
```

### Maven
```
<dependency>
  <groupId>eu.hinsch</groupId>
  <artifactId>spring-boot-actuator-user-agent-metrics</artifactId>
  <version>0.3.0</version>
</dependency>
```

### Configuration

All config properties are located beneath the prefix `user-agent-metric`

| Key          | Default    | Description                |
|--------------|------------|----------------------------|
| enabled      | false      | Turn metrics filter on/off | 
| url-patterns | empty list | List of patterns to match the servlet filter on |
| tags         | empty list | list of fields to be added as micrometer tags. For a list of available fields see [https://github.com/nielsbasjes/yauaa/blob/master/analyzer/src/main/java/nl/basjes/parse/useragent/UserAgent.java](https://github.com/nielsbasjes/yauaa/blob/master/analyzer/src/main/java/nl/basjes/parse/useragent/UserAgent.java) |

For an example see [SpringBootActuatorUserAgentMetricsTestApplication](https://github.com/lukashinsch/spring-boot-actuator-user-agent-metrics/blob/master/src/test/java/eu/hinsch/spring/boot/actuator/useragent/SpringBootActuatorUserAgentMetricsTestApplication.java)
and [application.yml](https://github.com/lukashinsch/spring-boot-actuator-user-agent-metrics/blob/master/src/test/resources/application.yml)
