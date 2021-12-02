package com.demo.integration.product;

import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.Exchange;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.core.MediaType;


import org.apache.camel.builder.RouteBuilder;

@ApplicationScoped
public class RestRoute extends RouteBuilder {

    public void configure() throws Exception {

        restConfiguration()
                .bindingMode(RestBindingMode.auto)
                .dataFormatProperty("disableFeatures", "FAIL_ON_EMPTY_BEANS");

        rest("/rest")
                .consumes(MediaType.APPLICATION_JSON)
                .produces(MediaType.APPLICATION_JSON)
                .get("/transaction/health").route()
                .to("direct:health")
                .endRest()
                .post("/transaction")
                .route()
                .removeHeader(Exchange.HTTP_URI)
                .removeHeader(Exchange.HTTP_PATH)    
                .log("request received: ${body}")
                .choice()
                    .when().simple("${body[type]} == 'debit'")
                        .to("direct:debitTransaction")
                    .when().simple("${body[type]} == 'credit'")
                        .to("direct:creditTransaction") 
                    .otherwise()
                        .log( "invalid path : ${body[type]}" )
                .end();

        from("direct:debitTransaction")
            .log("calling the debit service")
            .marshal().json(JsonLibrary.Jackson)
            .log("BODY: ${body}")  
            .to("http:debitservice:8080/rest/debit?httpMethod=POST"); 
            
        from("direct:creditTransaction")    
            .log("calling the credit service")
            .marshal().json(JsonLibrary.Jackson)
            .log("BODY: ${body}")
            .to("http:creditservice:8080/rest/credit?httpMethod=POST"); 

        from("direct:health")
            .log("--------------------")
            .log("service healthy")
            .setBody(simple("healthy"))
            .log("--------------------");

    }
}
