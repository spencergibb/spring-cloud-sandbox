Spring IO Platform Sandbox
=========

A place to rapidly develop code.  

Code should migrate to a separate module and then to another project.

- [x] Example front end app
- [x] Example back end service
- [x] Use platform config
- [x] Hystrix integration (hystrix-javanica)
- [x] Feign use spring message converters
- [x] Ribbon (static server list)
- [ ] Eureka boot app (Service registration)
- [x] Eureka (apache -> tomcat) see https://github.com/cfregly/fluxcapacitor/wiki/NetflixOSS-FAQ#eureka-service-discovery-load-balancer and https://groups.google.com/forum/?fromgroups#!topic/eureka_netflix/g3p2r7gHnN0
- [x] Archaius bridge to spring environment
- [x] Ribbon (Client side load balancing) (Eureka integration)
  - [ ] Synchronous removal of service from eureka on shutdown
- [ ] Router (Zuul) integrated using hystrix/ribbon/eureka
- [ ] Refresh log levels dynamically
- [ ] Better observable example
- [ ] Distributed refresh environment via platform bus
- [ ] Metrics aggregation (turbine)

