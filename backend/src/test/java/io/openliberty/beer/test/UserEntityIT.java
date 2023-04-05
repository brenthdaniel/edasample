package io.openliberty.beer.test;

import java.util.HashMap;
import jakarta.json.JsonObject;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.Form;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import io.openliberty.beer.models.User;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


public class UserEntityIT extends BeerTest {

    private static final String USER_NAME = "some username";
    private static final String PASSWORD = "secret";
    private static final String UPDATED_USER = "updated user";
    private static final String UPDATED_PASSWORD = "updated";

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
        beerForm.put(JSONFIELD_USERNAME, USER_NAME);    
        beerForm.put(JSONFIELD_PASSWORD, PASSWORD);        
    }

    @AfterEach
    public void tearDown() {
         //cleanup
         User u = new User(USER_NAME, PASSWORD);
  
         JsonObject user = findUser(u);
         if ( user != null ) {
             Response deleteResponse = deleteUser(user.getInt("id"));
             assertEquals(NO_CONTENT_CODE, deleteResponse.getStatus(), "error in test setup. delete returned: " + deleteResponse.readEntity(String.class));
         }
    }


    @Test
    public void testReadIndividualUser() {
            
        // create user
        Response postResponse = createUsers(beerForm);
        assertEquals(NO_CONTENT_CODE, postResponse.getStatus(),
          "Creating a user should return the HTTP reponse code " + NO_CONTENT_CODE + ". err: " + postResponse.readEntity(String.class));

        User u = new User(USER_NAME, PASSWORD);
        JsonObject user = findUser(u);    

        user = getIndividualUser(user.getInt("id"));
        assertData(user, USER_NAME);

        Response deleteResponse = deleteUser(user.getInt("id"));
        assertEquals(NO_CONTENT_CODE, deleteResponse.getStatus(),
          "Deleting a user should return the HTTP response code " + NO_CONTENT_CODE);
    }

  
    protected void assertData(JsonObject user, String name) {
      assertNotNull(user, "User should be found");    
      assertEquals(user.getString(JSONFIELD_USERNAME), name);    
      assertEquals(user.getInt(JSONFIELD_CHECKIN_NUMBER), 0);
    }

    @AfterEach
    public void teardown() {
        response.close();
        client.close();
    }

  }


