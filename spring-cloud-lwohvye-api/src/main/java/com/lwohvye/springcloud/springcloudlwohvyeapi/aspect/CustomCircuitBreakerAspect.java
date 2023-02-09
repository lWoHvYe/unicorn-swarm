package com.lwohvye.springcloud.springcloudlwohvyeapi.aspect;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientResponseException;

import java.net.URI;
import java.net.URL;

@Slf4j
@Aspect
@Component
public class CustomCircuitBreakerAspect {

    @Autowired
    private CircuitBreakerRegistry circuitBreakerRegistry;

    /**
     * This is for restTemplate，for Openfeign maybe can consider `client.execute(request, options)` in `feign.SynchronousMethodHandler#executeAndDecode(feign.RequestTemplate, feign.Request.Options)`
     */
    @Pointcut("execution(public * org.springframework.web.client.RestTemplate.exchange(..))")
    public void restTemExchange() {
    }

    @Around("restTemExchange()")
    public Object circuitBreakerAroundAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
        var args = joinPoint.getArgs();

        var param1 = args[0]; // contains url
        var host = "";
        if (param1 instanceof String url) {
            host = new URL(url).getHost();
        } else if (param1 instanceof URI uri) {
            host = uri.getHost();
        } else if (param1 instanceof RequestEntity<?> requestEntity) {
            host = requestEntity.getUrl().getHost();
        }

        var optionalCircuitBreaker = circuitBreakerRegistry.find("swarm");
        if (optionalCircuitBreaker.isPresent()) {
            var decorateSupplier =
                    CircuitBreaker.decorateCheckedSupplier(optionalCircuitBreaker.get(), joinPoint::proceed);
            var result = Try.of(decorateSupplier);
            if (result.isSuccess())
                return result.get();
            else
                return fallback(result);
        } else
            return joinPoint.proceed();
    }

    private ResponseEntity fallback(Try<Object> result) {
        var cause = result.getCause();
        log.error(cause.getMessage());
        if (cause instanceof RestClientResponseException responseException)
            return new ResponseEntity<>(responseException.getStatusText(), responseException.getResponseHeaders(), responseException.getRawStatusCode());
        return new ResponseEntity<>(cause.getMessage(), HttpStatus.BAD_REQUEST);
    }

}