package io.openliberty.beer.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.HashMap;
import jakarta.json.JsonObject;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.Form;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import io.openliberty.beer.models.Beer;

import org.junit.jupiter.api.Test;

public class BeerEntityIT extends BeerTest {
  


    private static final String BREWERY_NAME = "some brewery";
    private static final String BEER_NAME = "some beer";

    private static final String UPDATED_BREWERY = "updated brewery";
    private static final String UPDATED_BEER = "updated beer";

    private static final int NO_CONTENT_CODE = Status.NO_CONTENT.getStatusCode();
    private static final int NOT_FOUND_CODE = Status.NOT_FOUND.getStatusCode();

    @BeforeAll
    public static void oneTimeSetup() {
        port = System.getProperty("backend.http.port");
        baseUrl = "http://localhost:" + port + "/";
    }

    @BeforeEach
    public void setup() {
        form = new Form();
        client = ClientBuilder.newClient();

        beerForm = new HashMap<String, String>();

        beerForm.put(JSONFIELD_NAME, BEER_NAME);
        beerForm.put(JSONFIELD_BREWERY, BREWERY_NAME);      

        //cleanup
        Beer b = new Beer(BREWERY_NAME, BEER_NAME);
        JsonObject beer = findBeer(b);
        if ( beer != null ) {
            Response deleteResponse = deleteBeer(beer.getInt("id"));
            assertEquals(NO_CONTENT_CODE, deleteResponse.getStatus(), "error in test setup. delete returned: " + deleteResponse.readEntity(String.class));
        }
    }

    @Test
    public void testInvalidRead() {
        assertEquals(true, getIndividualBeer(-1).isEmpty(),
          "Reading a beer that does not exist should return an empty list");
    }

    @Test
    public void testInvalidDelete() {
        Response deleteResponse = deleteBeer(-1);
        assertEquals(NOT_FOUND_CODE, deleteResponse.getStatus(),
          "Trying to delete a beer that does not exist should return the "
          + "HTTP response code " + NOT_FOUND_CODE);
    }

    @Test
    public void testInvalidUpdate() {
        int updateResponse = updateRequest(beerForm, -1);
        assertEquals(NOT_FOUND_CODE, updateResponse,
          "Trying to update a beer that does not exist should return the "
          + "HTTP response code " + NOT_FOUND_CODE);
    }

    @Test
    public void testReadIndividualBeer() {
      
        Beer b = new Beer(BREWERY_NAME, BEER_NAME);
        JsonObject beer = findBeer(b);
       
        // create beer
        Response postResponse = createBeers(beerForm);
        assertEquals(NO_CONTENT_CODE, postResponse.getStatus(),
          "Creating a Beer should return the HTTP reponse code " + NO_CONTENT_CODE + ". err: " + postResponse.readEntity(String.class));

       
        beer = findBeer(b);
        beer = getIndividualBeer(beer.getInt("id"));
        assertData(beer, BREWERY_NAME, BEER_NAME);

        Response deleteResponse = deleteBeer(beer.getInt("id"));
        assertEquals(NO_CONTENT_CODE, deleteResponse.getStatus(),
          "Deleting a beer should return the HTTP response code " + NO_CONTENT_CODE);
    }

    @Test
    public void testCRUD() {
        int beerCount = getBeers().size();
        Response postResponse = createBeers(beerForm);
        assertEquals(NO_CONTENT_CODE, postResponse.getStatus(),
          "Creating a beer should return the HTTP reponse code " + NO_CONTENT_CODE + ". err " + postResponse.readEntity(String.class));

        Beer e = new Beer(BREWERY_NAME, BEER_NAME);
        JsonObject beer = findBeer(e);
        assertData(beer, BREWERY_NAME, BEER_NAME);

        beerForm.put(JSONFIELD_NAME, UPDATED_BEER);
        beerForm.put(JSONFIELD_BREWERY, UPDATED_BREWERY);
       
        int updateResponse = updateRequest(beerForm, beer.getInt("id"));
        assertEquals(NO_CONTENT_CODE, updateResponse,
          "Updating a beer should return the HTTP response code " + NO_CONTENT_CODE);

        e = new Beer(UPDATED_BREWERY, UPDATED_BEER);
        beer = findBeer(e);
        assertData(beer, UPDATED_BREWERY, UPDATED_BEER);

        Response deleteResponse = deleteBeer(beer.getInt("id"));
        assertEquals(NO_CONTENT_CODE, deleteResponse.getStatus(),
          "Deleting a beer should return the HTTP response code " + NO_CONTENT_CODE);
        assertEquals(beerCount, getBeers().size(),
          "Total number of beers stored should be the same after testing "
          + "CRUD operations.");
    }

        /**
     *  Asserts beer fields (name, brewery name) equal the provided fields
     */
    protected void assertData(JsonObject beer, String brewery, String name) {
        assertNotNull(beer, "Beer not found");
        assertNotNull(beer.getString(JSONFIELD_NAME), "Name should be available");
        assertNotNull(beer.getString(JSONFIELD_BREWERY), "brewery name should be available");
        assertEquals(beer.getString(JSONFIELD_NAME), name);
        assertEquals(beer.getString(JSONFIELD_BREWERY), brewery);      
    }
    @AfterEach
    public void teardown() {
        response.close();
        client.close();
    }

}
