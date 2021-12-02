package com.demo.integration.product;


import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.Path;
import io.quarkus.security.identity.SecurityIdentity;
import javax.inject.Inject;
import javax.ws.rs.core.MediaType;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.builder.RouteBuilder;

@ApplicationScoped
public class RestRoute extends RouteBuilder {

    public void configure() throws Exception {

        from("timer:init?repeatCount=1").log("INIT");

        from("direct:writedebit")
            .id("writedebit")
            .log("add debit service")
            .log("BODY: ${body}")
            .setBody(simple("insert into transaction (CLIENT_ID, TYPE, LOCATION, AMOUNT) values ('${body[clientId]}', '${body[type]}','${body[location]}','${body[amount]}' );"))
            .to("jdbc:camel")
            .setBody(simple("done!"));
  
        from("direct:getById")
        .log("get by Id")
        .log("clientid: ${header.clientID}")
        .setBody(simple("select CLIENT_ID, TYPE, LOCATION, AMOUNT from transaction where client_id='${header.clientId}'"))
        .to("jdbc:camel");


        from("direct:getTransactions")
        .log("getAll")
        .setBody(simple("select CLIENT_ID, TYPE, LOCATION, AMOUNT from transaction"))
        .to("jdbc:camel");


        from("direct:health")
            .log("--------------------")    
            .log("service healthy") 
            .setBody(simple("healthy"))

            .log("--------------------");

    }
}
