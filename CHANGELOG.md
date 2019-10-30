# Changelog

## 0.3.2
- Allow excluding requests from metrics by providing a list of regex to be matched against the request URI

## 0.3.1
- Eager initialization of parser by default
- Additional performance improvements

## 0.3.0
- Switch to Yauaa library for user agent parsing

## 0.2.0
- Use micrometer framework for metrics collection (requires spring boot 2.1+)

## 0.1.1
- Allow autoconfig of filter via url-patterns property (fixes [#3](https://github.com/lukashinsch/spring-boot-actuator-user-agent-metrics/issues/3))
- Cache parsing of user agent string (fixes [#6](https://github.com/lukashinsch/spring-boot-actuator-user-agent-metrics/issues/6))

## 0.1.0
- First release