package com.thecat.demos.transactionservice.route;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;


@Component
public class DebitRoute extends RouteBuilder {

    private final Environment env;

    public DebitRoute(Environment env) {
        this.env = env;
    }

    public void configure() throws Exception {

        restConfiguration()
            .contextPath(env.getProperty("camel.component.servlet.mapping.contextPath", "/rest/*"))
            .apiContextPath("/api-doc")
            .apiProperty("api.title", "Transaction Service")
            .apiProperty("api.version", "1.0")
            .apiProperty("cors", "true")
            .apiContextRouteId("doc-api")
            .port(env.getProperty("server.port", "8080"))
            .bindingMode(RestBindingMode.json)
            .dataFormatProperty("disableFeatures", "FAIL_ON_EMPTY_BEANS");

        rest("/debit")
            .get("/").produces("application/json").route().to("{{route.debitAllTransaction}}");

        from("{{route.debitTransaction}}")
            .log("calling the debit service")
            .marshal().json(JsonLibrary.Jackson)
            .removeHeader(Exchange.HTTP_URI)
            .removeHeader(Exchange.HTTP_PATH)
            .log("BODY: ${body}")
            .to("{{service.debitservice.url}}?httpMethod=POST"); 
    
        from("{{route.debitAllTransaction}}")
            .log("calling the debit service  get all transaction")
            .removeHeader(Exchange.HTTP_URI)
            .removeHeader(Exchange.HTTP_PATH)
            .doTry()
                .to("{{service.debitservice.url}}?httpMethod=GET")
            .doCatch(Exception.class)
                .setBody().simple("{\"error\": \"Debit Service 403-unauthorize\"}")
            .end();
    }
}
