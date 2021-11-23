package com.demo.integration.product;


import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import org.jboss.resteasy.annotations.cache.NoCache;
import io.quarkus.security.identity.SecurityIdentity;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.Handler;

import org.apache.camel.CamelContext;
@ApplicationScoped
@Path("/debit")
public class TransactionResources  {


    @Inject
    CamelContext context;

 
    @POST
    @RolesAllowed("debit-user")
    @Handler
    public String getResource(Object body) {
        context.createProducerTemplate().sendBody("direct:writedebit", body);
        return "done";
        }

        
}
