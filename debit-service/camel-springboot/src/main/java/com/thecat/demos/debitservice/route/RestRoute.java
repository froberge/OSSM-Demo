package com.thecat.demos.debitservice.route;

import com.thecat.demos.debitservice.entities.TransactionEntity;
import com.thecat.demos.debitservice.services.DebitService;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

@Component
public class RestRoute extends RouteBuilder {

    private final Environment env;

    public RestRoute(Environment env) {
        this.env = env;
    }

    public void configure() throws Exception {
        restConfiguration()
                .contextPath(env.getProperty("camel.component.servlet.mapping.contextPath", "/rest/*"))
                .apiContextPath("/api-doc")
                .apiProperty("api.title", "Spring Boot Camel Postgres Debit Service.")
                .apiProperty("api.version", "1.0")
                .apiProperty("cors", "true")
                .apiContextRouteId("doc-api")
                .port(env.getProperty("server.port", "8080"))
                .bindingMode(RestBindingMode.auto);

        rest("/debit")
                .consumes(MediaType.APPLICATION_JSON_VALUE)
                .produces(MediaType.APPLICATION_JSON_VALUE)
                .get("/{clientId}").route()
                .to("{{route.findTransactionByClientId}}")
                .endRest()
                .get("/").route()
                .to("{{route.findAllTransactions}}")
                .endRest()
                .post("/").type(TransactionEntity.class).route()
                .to("{{route.saveTransaction}}") 
                .end();

        from("{{route.saveTransaction}}")
                .log("Received Body : ${body}")
                .bean(DebitService.class, "addTransaction(${body})");

        from("{{route.findTransactionByClientId}}")
                .log( "Received header : ${header.clientId}")
                .bean(DebitService.class, "findTransactionByClientId(${header.clientId})");

        from("{{route.findAllTransactions}}")
                .log( "Retrieve all transactions")
                .bean(DebitService.class, "findAllTransactions");
    }
}
