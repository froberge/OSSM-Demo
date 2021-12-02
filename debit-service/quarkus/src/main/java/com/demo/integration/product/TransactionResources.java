package com.demo.integration.product;


import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.Handler;


import org.apache.camel.CamelContext;
@ApplicationScoped
@Path("/rest")
public class TransactionResources  {


    @Inject
    CamelContext context;

    @POST
    @Path("debit")
    @Handler
    public String getResource(Object body) {
        context.createProducerTemplate().sendBody("direct:writedebit", body);
        return "done";
    }

    @GET
    @Path("debit/")
    @Produces(MediaType.APPLICATION_JSON)
    public Object getAllTransactions() {
        return context.createProducerTemplate().requestBody("direct:getTransactions", new Object() );
    }

    @GET
    @Path("debit/{clientid}")
    @Produces(MediaType.APPLICATION_JSON)
    public Object getTransactionById(@PathParam( "clientid" ) String clientId) {
        return context.createProducerTemplate().requestBodyAndHeader("direct:getById", new Object(), "clientID", clientId );
    }

    @GET
    @Path("debit/health")
    @Produces(MediaType.APPLICATION_JSON)
    public Object getHealth() {
        return context.createProducerTemplate().requestBody("direct:health", new Object() );
    }
}
