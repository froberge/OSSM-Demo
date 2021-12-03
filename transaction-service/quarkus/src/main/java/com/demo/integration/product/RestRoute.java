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
                    .to("direct:findAllTransactions")
                .endRest()    
                .post("/transaction").route()
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
                .setBody().simple("{\"error\": \"Debit Service 403-unauthorize\"}")
            .end();

        from("direct:creditAllTransaction")
            .log("calling the credit service  get all transaction")
            .to("http:creditservice:8080/rest/credit?httpMethod=GET");

        from("direct:findAllTransactions")
            .log("findAllTransactions")
            .multicast( new MyAggregationStrategy())
            .parallelProcessing().timeout(1000).to("direct:debitAllTransaction", "direct:creditAllTransaction")
            .end()
            .unmarshal().json(JsonLibrary.Jackson);
    }

    private class MyAggregationStrategy implements AggregationStrategy {
        @Override
        public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
            if (oldExchange == null) {
                return newExchange;
            }
            String oldBody = oldExchange.getIn().getBody(String.class);
            String newBody = newExchange.getIn().getBody(String.class);

            if (oldBody==null)
                oldBody=""; 
            else {
                oldBody = oldBody.replaceAll("[\\[\\]]", "" );
                oldBody = oldBody.concat(",");
            }

            if (newBody==null)
                newBody="";
            else
                newBody = newBody.replaceAll("[\\[\\]]", "" );

            StringBuffer sb = new StringBuffer();
            sb.append("[")
            .append(oldBody)
            .append(newBody)
            .append("]");

            newExchange.getIn().setBody(sb.toString());            
            return newExchange;
        }
    }
}
