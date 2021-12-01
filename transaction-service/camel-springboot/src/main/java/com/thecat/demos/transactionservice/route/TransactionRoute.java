package com.thecat.demos.transactionservice.route;

import com.thecat.demos.transactionservice.entities.TransactionDTO;

import org.apache.camel.processor.aggregate.AggregationStrategy;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;



@Component
public class TransactionRoute extends RouteBuilder {

    private final Environment env;

    public TransactionRoute(Environment env) {
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
            .bindingMode(RestBindingMode.auto)
            .dataFormatProperty("disableFeatures", "FAIL_ON_EMPTY_BEANS");

        rest("/transaction")
            .consumes(MediaType.APPLICATION_JSON_VALUE)
            .produces(MediaType.APPLICATION_JSON_VALUE)
            .get("/health").route()
            .to("direct:health")
            .endRest()
            .get("/").route()
            .to("{{route.findAllTransactions}}")
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

        from("direct:health")
            .log("service healthy")
            .setBody().simple( "healthy" );
                
        from("{{route.findAllTransactions}}")
            .log("findAllTransactions")
            .multicast( new MyAggregationStrategy())
            .parallelProcessing().timeout(1000).to("{{route.debitAllTransaction}}", "{{route.creditAllTransaction}}")
            .end();
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
