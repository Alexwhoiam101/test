package com.trytocopyit.config;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class HttpSecurityConfig implements WebMvcConfigurer {

    private final Map<String, Bucket> cache;

    public HttpSecurityConfig() {
        cache = new ConcurrentHashMap<>();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new HandlerInterceptor() {
            @Override
            public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
                String IPAddress = request.getRemoteAddr();
                System.out.println("Remote Address: " + IPAddress);

                updateCache(IPAddress);

                if (!isRequestRateOk(IPAddress)) {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    return false;
                }
                return true;
            }
        });
    }

    private void updateCache(String IPAddress) {
        cache.putIfAbsent(IPAddress,
                Bucket.builder()
                        .addLimit(Bandwidth.classic(50, Refill.intervally(50, Duration.ofMinutes(1)))) //request rate limit
                        .build());
    }

    private boolean isRequestRateOk(String IPAddress) {
        return cache.get(IPAddress).tryConsume(1);
    }
}