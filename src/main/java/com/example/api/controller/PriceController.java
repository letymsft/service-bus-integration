
package com.example.api.controller;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.core.MediaType;

@Path("/price")
public class PriceController {

    private static final ExecutorService executorService = Executors.newFixedThreadPool(10);

    @GET
    @Path("/get")
    public String getPrice() {
        return "Price is 100";
    }

    @POST
    @Path("/set")
    @Consumes(MediaType.APPLICATION_JSON)
    //Async method to set the price of a product in the database using a JSON message from Service Bus.
    public String setPrice(String jsonMessage) {
        CompletableFuture.runAsync(() -> {
            try {
                String sqlQuery = buildSqlQuery(jsonMessage);
               // executeSqlQuery(sqlQuery);
               // notifyServiceBusMessageProcessed();
                System.out.println("SQL Query processed: " + sqlQuery);
            } catch (IOException /*| SQLException*/ e) {
                e.printStackTrace();
            }
        }, executorService);

        return "Request to set price is being processed from API...";
    }

    /* 
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
    }*/
    
    //Method to build the SQL query from a JSON Array message from Service Bus.
    private String buildSqlQuery(String jsonMessage) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonArray = objectMapper.readTree(jsonMessage);
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("INSERT INTO productos (id, producto, precio, descripcion) VALUES ");

        for (JsonNode jsonNode : jsonArray) {
            int id = jsonNode.get("id").asInt();
            String producto = jsonNode.get("producto").asText();
            double precio = jsonNode.get("precio").asDouble();
            String descripcion = jsonNode.get("descripcion").asText();

            sqlBuilder.append("(")
                    .append(id).append(", ")
                    .append("'").append(producto).append("', ")
                    .append(precio).append(", ")
                    .append("'").append(descripcion).append("'), ");
        }

        // Remove the last comma and space
        sqlBuilder.setLength(sqlBuilder.length() - 2);
        sqlBuilder.append(";");

        return sqlBuilder.toString();
    }

    //Method to execute the SQL query to insert the data into the database.
    private void executeSqlQuery(String sqlQuery) throws SQLException {
        String url = "";
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = DriverManager.getConnection(url);
            preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.executeUpdate();
        } finally {
            // Close resources to avoid memory leaks
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //Method to notify the Service Bus that the message has been processed.
    private void notifyServiceBusMessageProcessed() {
        // Implement the logic to notify the Service Bus that the message has been processed
        // This could involve sending an HTTP request, calling an SDK method, etc.
    }
}
