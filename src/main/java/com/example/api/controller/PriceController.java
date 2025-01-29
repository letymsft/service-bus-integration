
package com.example.api.controller;

import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;

@Path("/price")
public class PriceController {
    
    @GET
    @Path("/get")
    public String getPrice() {
        return "Price is 100";
    }
    
    @POST
    @Path("/set")
    public String setPrice() {
        return "Price is set";
    }
    
    @PUT
    @Path("/update")
    public String updatePrice() {
        return "Price is updated";
    }
    
    @DELETE
    @Path("/delete")
    public String deletePrice() {
        return "Price is deleted";
    }
    
    @GET
    @Path("/get/{id}")
    public String getPriceById(@PathParam("id") int id) {
        return "Price is 100";
    }
    
    @GET
    @Path("/get/{id}/{name}")
    public String getPriceByIdAndName(@PathParam("id") int id, @PathParam("name") String name) {
        return "Price is 100";
    }
    
    @GET
    @Path("/get/{id}/{name}/{age}")
    public String getPriceByIdAndNameAndAge(@PathParam("id") int id, @PathParam("name") String name, @PathParam("age") int age) {
        return "Price is 100";
    }

}
