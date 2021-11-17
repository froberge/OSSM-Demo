package com.thecat.demos.transactionservice.route;

import com.thecat.demos.transactionservice.entities.RestMessage;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
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
                .component("servlet")
                .contextPath(env.getProperty("camel.component.servlet.mapping.contextPath", "/rest/*"))
                .apiContextPath("/api-doc")
                .apiProperty("api.title", "Transaction Service")
                .apiProperty("api.version", "1.0")
                .apiProperty("cors", "true")
                .apiContextRouteId("doc-api")
                .port(env.getProperty("server.port", "8080"))
                .bindingMode(RestBindingMode.auto);

        rest("/transaction")
                .consumes(MediaType.APPLICATION_JSON_VALUE)
                .produces(MediaType.APPLICATION_JSON_VALUE)
                .get("/health").route()
                .to("direct:health")
                .endRest()
                .post("/").route()
                .unmarshal(getJacksonDataFormat(RestMessage.class))
                .to("{{route.debitTransaction}}")
                .end();

        from("{{route.debitTransaction}}")
            .log("--------------------")
            .log("service called: + ${body}")
            .log("--------------------");

        from("direct:health")
            .log("--------------------")
            .log("service healthy")
            .log("--------------------");

    }

    private JacksonDataFormat getJacksonDataFormat(Class<?> unmarshalType) {
        JacksonDataFormat format = new JacksonDataFormat();
        format.setEnableJacksonTypeConverter(true);
        format.setUnmarshalType(unmarshalType);
        return format;
    }
}
