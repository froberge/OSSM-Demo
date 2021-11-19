package com.thecat.demos.transactionservice.route;

import com.thecat.demos.transactionservice.entities.TransactionDTO;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
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
 //               .component("servlet")
                .contextPath(env.getProperty("camel.component.servlet.mapping.contextPath", "/rest/*"))
                .apiContextPath("/api-doc")
                .apiProperty("api.title", "Transaction Service")
                .apiProperty("api.version", "1.0")
                .apiProperty("cors", "true")
                .apiContextRouteId("doc-api")
                .port(env.getProperty("server.port", "8080"))
                .bindingMode(RestBindingMode.auto)
                .dataFormatProperty("disableFeatures", "FAIL_ON_EMPTY_BEANS");

        rest("/transaction")
                .consumes(MediaType.APPLICATION_JSON_VALUE)
                .produces(MediaType.APPLICATION_JSON_VALUE)
                .get("/health").route()
                .to("direct:health")
                .endRest()
                .post("/").type(TransactionDTO.class).route()
                .choice()
                    .when().simple("${body.type} == 'debit'")
                        .to("{{route.debitTransaction}}")
                    .when().simple("${body.type} == 'credit'")
                        .to("{{route.creditTransaction}}") 
                    .otherwise()
                        .log( "invalid path : ${body.type}" )
                .end();

        from("{{route.debitTransaction}}")
            .log("calling the debit service")
            .marshal().json(JsonLibrary.Jackson)
            .removeHeader(Exchange.HTTP_URI)
            .log("BODY: ${body}")
            .to("{{service.debitservice.url}}?httpMethod=POST"); 
            
        from( "{{route.creditTransaction}}")    
            .log("calling the credit service")
            .marshal().json(JsonLibrary.Jackson)
            .removeHeader(Exchange.HTTP_URI)
            .log("BODY: ${body}")
            .to("{{service.creditservice.url}}?httpMethod=POST"); 

        from("direct:health")
            .log("--------------------")
            .log("service healthy")
            .log("--------------------");

    }
}
