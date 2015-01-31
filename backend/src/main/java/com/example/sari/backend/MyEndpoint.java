/*
   For step-by-step instructions on connecting your Android application to this backend module,
   see "App Engine Java Endpoints Module" template documentation at
   https://github.com/GoogleCloudPlatform/gradle-appengine-templates/tree/master/HelloEndpoints
*/

package com.example.sari.backend;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
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
    public Game sayHi(@Named("name") String name) {
        Game response = new Game();
        response.setData("Hi, " + name);

        return response;
    }


    @ApiMethod(name = "saveDataLevel1A")
    public Game saveDataLevel1A(@Named("playerID") String playerID, @Named("score") int score, @Named("startingPoint") float startingPoint,
                                @Named("finalPointX") float finalPointX, @Named("finalPointY") float finalPointY,
                                @Named("length") float length){

        Entity lvTry = new Entity("Level1ATry");
        lvTry.setProperty("id",playerID);
        lvTry.setProperty("score", score);
        lvTry.setProperty("StartingPoint", startingPoint);
        lvTry.setProperty("Final Point_X",finalPointX);
        lvTry.setProperty("Final Point_Y",finalPointY);
        lvTry.setProperty("length", length);
        datastore.put(lvTry);

        Game response = new Game();
        response.setData("Saved score for player " + "CVeva");

        return response;
    }

    @ApiMethod(name = "saveDataLevel1B")
    public void saveDataLevel1B(){
        Entity game = new Entity("Level1BTry");
    }



}
