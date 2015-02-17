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
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;

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
                                @Named("length") float length, @Named("subLevel") String subLevel){

        Entity lvTry = new Entity("Level1ATry");
        lvTry.setProperty("id",playerID);
        lvTry.setProperty("score", score);
        lvTry.setProperty("StartingPoint", startingPoint);
        lvTry.setProperty("Final Point_X",finalPointX);
        lvTry.setProperty("Final Point_Y",finalPointY);
        lvTry.setProperty("length", length);
        lvTry.setProperty("subLevel", subLevel);
        datastore.put(lvTry);

        Game response = new Game();
        response.setData("Saved score for player " + "CVeva");
        return response;
    }

    @ApiMethod(name = "getData")
    public Printer getData(){
        Printer a = new Printer();
        Query.Filter filter = new Query.FilterPredicate("subLevel", Query.FilterOperator.EQUAL,"A");
        Query q = new Query("Level1ATry").setFilter(filter);
        PreparedQuery pq = datastore.prepare(q);
        for(Entity e : pq.asIterable())
        {
            a.addData((String)e.getProperty("subLevel") +" "+ (Long)e.getProperty("score"));
        }
        a.addData("123");
        a.addData("1234");

        return a;
    }



}
