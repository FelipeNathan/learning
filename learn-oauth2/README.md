# learn-oauth2
Project designed to learn about oauth 2

A project to learn how to implement oauth2 client signing in Github

Code copied at [Spring guide](https://spring.io/guides/tutorials/spring-boot-oauth2/), but doesn't have some features:
* The guide of Spring.io uses Spring Webflux and I use Spring Web which don't have WebClient
* In the middle of code have this code `handler.onAuthenticationFailure(request, response, exception);` that doesn't have any references about `handler`