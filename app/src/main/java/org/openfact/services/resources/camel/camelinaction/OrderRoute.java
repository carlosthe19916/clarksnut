package org.openfact.services.resources.camel.camelinaction;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import org.apache.camel.builder.RouteBuilder;

import javax.enterprise.inject.Produces;
import javax.inject.Named;

public class OrderRoute extends RouteBuilder {

    @Produces
    @Named("jsonProvider")
    public JacksonJsonProvider jsonProvider() {
        return new JacksonJsonProvider();
    }

    @Override
    public void configure() throws Exception {

        // use CXF-RS to setup the REST web service using the resource class
        // and use the simple binding style which is recommended to use
        from("cxfrs:http://0.0.0.0:8080/camel?resourceClasses="
                + RestOrderService.class.getName() + "&bindingStyle=SimpleConsumer&providers=#jsonProvider")
                // call the route based on the operation invoked on the REST web service
                .toD("direct:${header.operationName}");

        // routes that implement the REST services
        from("direct:createOrder")
                .bean("orderService", "createOrder");

        from("direct:getOrder")
                .bean("orderService", "getOrder(${header.id})");

        from("direct:updateOrder")
                .bean("orderService", "updateOrder");

        from("direct:cancelOrder")
                .bean("orderService", "cancelOrder(${header.id})");
    }
}
