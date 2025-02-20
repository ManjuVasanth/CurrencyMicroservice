package com.manju.microservices.api_gateway;

import java.util.function.Function;

import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.Buildable;
import org.springframework.cloud.gateway.route.builder.PredicateSpec;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiGatewayConfiguration {
//RouteLocator is a component used to define and manage API routes
	@Bean
	public RouteLocator gatewayRouter(RouteLocatorBuilder builder) {
		return builder.routes()
				 // First Route: Mapping "/get"
						.route(p->p.path("/get")
								.filters(f->f.addRequestHeader("MyHeader","MyURI") // Add a custom header
											.addRequestParameter("Param","MyValue")) // Add a query parameter
								.uri("http://httpbin.org:80"))// Forward to httpbin.org
						  // Second Route: Load-balanced currency-exchange service
				.route(p->p.path("/currency-exchange/**")
				.uri("lb://currency-exchange")) //Forward using Eureka service discovery
				.route(p->p.path("/currency-conversion/**")
						.uri("lb://currency-conversion")) //Forward using Eureka service discovery
				.route(p->p.path("/currency-conversion-feign/**")
						.uri("lb://currency-conversion")) //Forward using Eureka service discovery
				.route(p->p.path("/currency-conversion-new/**")
						.filters(f->f.rewritePath("/currency-conversion-new/(?<segment>.*","/currency-conversion-feign/${segment}"))
						.uri("lb://currency-conversion"))		
				.build();
				
	}
}
