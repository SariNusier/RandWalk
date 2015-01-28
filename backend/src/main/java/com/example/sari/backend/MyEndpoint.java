/*
   For step-by-step instructions on connecting your Android application to this backend module,
   see "App Engine Java Endpoints Module" template documentation at
   https://github.com/GoogleCloudPlatform/gradle-appengine-templates/tree/master/HelloEndpoints
*/

package com.example.sari.backend;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import java.util.Date;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import javax.inject.Named;

/**
 * An endpoint class we are exposing
 */
@Api(name = "myApi", version = "v1", namespace = @ApiNamespace(ownerDomain = "backend.sari.example.com", ownerName = "backend.sari.example.com", packagePath = ""))
public class MyEndpoint {
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    /**
     * A simple endpoint method that takes a name and says Hi back
     */
    @ApiMethod(name = "sayHi")
    public MyBean sayHi(@Named("name") String name) {
        MyBean response = new MyBean();
        response.setData("Hi, " + name);

        return response;
    }


    @ApiMethod(name = "saveData")
    public MyBean saveData(@Named("data") String data, @Named("score") int score){
        Entity player = new Entity("Employee");
        player.setProperty("firstName", data);
        player.setProperty("score", score);

        datastore.put(player);

        MyBean response = new MyBean();
        response.setData("Saved score for player " + data);

        return response;
    }



}
