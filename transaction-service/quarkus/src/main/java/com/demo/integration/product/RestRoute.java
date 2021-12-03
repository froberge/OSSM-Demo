package com.demo.integration.product;

import org.apache.camel.AggregationStrategy;
import org.apache.camel.Exchange;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.model.rest.RestBindingMode;

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
                .get("/transaction").route()
                .removeHeader(Exchange.HTTP_URI)
                .removeHeader(Exchange.HTTP_PATH)
                .setHeader(Exchange.CONTENT_TYPE, simple("application/json"))
                .log("findAllTransactions")
                .multicast( new MyAggregationStrategy())
                .parallelProcessing().timeout(1000).to("direct:debitAllTransaction", "direct:creditAllTransaction")
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
            .log("service healthy")
            .setBody(simple("healthy"));

        from("direct:debitAllTransaction")
            .log("calling the debit service  get all transaction")
            .doTry()
                .to("http:debitservice:8080/rest/debit?httpMethod=GET")
            .doCatch(Exception.class)
                .setBody().simple("{\"error\": \"Debit Service\"}")
            .end()
            .unmarshal().json(JsonLibrary.Jackson);

        from("direct:creditAllTransaction")
            .log("calling the credit service  get all transaction")
            .to("http:creditservice:8080/rest/credit?httpMethod=GET")
            .unmarshal().json(JsonLibrary.Jackson);
    }

    private class MyAggregationStrategy implements AggregationStrategy {
        @Override
        public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
            if (oldExchange == null) {
                return newExchange;
            }
            String newBody = newExchange.getIn().getBody(String.class);
            String oldBody = oldExchange.getIn().getBody(String.class);
            if(oldBody==null)oldBody="";
            if(newBody==null)newBody="";

            newBody = oldBody.concat("\n").concat(newBody);
            newExchange.getIn().setBody(newBody);
            return newExchange;
        }
    }
}
