package me.dragonappear.domain.main;

import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class WebServerCustomizer implements WebServerFactoryCustomizer<ConfigurableWebServerFactory> {

    @Override
    public void customize(ConfigurableWebServerFactory factory) {
        ErrorPage error404 = new ErrorPage(HttpStatus.NOT_FOUND, "/api/error/404");
        ErrorPage errorEx = new ErrorPage(RuntimeException.class, "/api/error/500");
        ErrorPage error500 = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/api/error/500");
        factory.addErrorPages(error404, errorEx, error500);
    }
}
