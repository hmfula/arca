package son.arca.api;

import son.arca.ws.AbstractControllerTest;
import son.arca.model.Cause;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

/**
 * Unit tests for the ArcaController using Spring MVC Mocks.
 * <p/>
 * These tests utilize the Spring MVC Mock objects to simulate sending actual
 * HTTP requests to the Controller component. This test ensures that the
 * RequestMappings are configured correctly. Also, these tests ensure that the
 * request and response bodies are serialized as expected.
 *
 * @author Harrison Mfula
 * @since 11.3.2016
 */
@Transactional
public class ArcaControllerTest extends AbstractControllerTest {

    @Autowired
    private ArcaService arcaService;

    @Before
    public void setUp() {
        super.setUp();
        arcaService.evictCache();
    }

    @Test
    public void testGetCauses() throws Exception {

        String uri = "/api/causes";

        MvcResult result = mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON)).andReturn();

        String content = result.getResponse().getContentAsString();
        int status = result.getResponse().getStatus();

        Assert.assertEquals("failure - expected HTTP status", 200, status);
        Assert.assertTrue(
                "failure - expected HTTP response body to have a value",
                content.trim().length() > 0);
    }

    @Test
    public void testGetCause() throws Exception {

        String uri = "/api/causes/{id}";
        Long id = new Long(1);

        MvcResult result = mvc.perform(MockMvcRequestBuilders.get(uri, id)
                .accept(MediaType.APPLICATION_JSON)).andReturn();

        String content = result.getResponse().getContentAsString();
        int status = result.getResponse().getStatus();

        Assert.assertEquals("failure - expected HTTP status 200", 200, status);
        Assert.assertTrue(
                "failure - expected HTTP response body to have a value",
                content.trim().length() > 0);

    }

    @Test
    public void testGetCauseNotFound() throws Exception {

        String uri = "/api/causes/{id}";
        Long id = Long.MAX_VALUE;

        MvcResult result = mvc.perform(MockMvcRequestBuilders.get(uri, id)
                .accept(MediaType.APPLICATION_JSON)).andReturn();

        String content = result.getResponse().getContentAsString();
        int status = result.getResponse().getStatus();

       Assert.assertEquals("failure - expected HTTP status 404", 404, status);
        Assert.assertTrue("failure - expected HTTP response body to be empty",
                content.trim().length() == 0);

    }

    @Test
    public void testCreateCause() throws Exception {

        String uri = "/api/causes";
        Cause cause = new Cause();
        cause.setName("test");
        String inputJson = super.mapToJson(cause);

        MvcResult result = mvc
                .perform(MockMvcRequestBuilders.post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON).content(inputJson))
                .andReturn();

        String content = result.getResponse().getContentAsString();
        int status = result.getResponse().getStatus();

        Assert.assertEquals("failure - expected HTTP status 201", 201, status);
        Assert.assertTrue(
                "failure - expected HTTP response body to have a value",
                content.trim().length() > 0);

        Cause createdCause = super.mapFromJson(content, Cause.class);

        Assert.assertNotNull("failure - expected cause not null",
                createdCause);
        Assert.assertNotNull("failure - expected cause.id not null",
                createdCause.getId());
        Assert.assertEquals("failure - expected cause.text match", "test",
                createdCause.getName());

    }

    @Test
    public void testUpdateCause() throws Exception {

        String uri = "/api/causes/{id}";
        Long id = new Long(1);
        Cause cause = arcaService.findOne(id);
        String updatedName = cause.getName() + " test";
        cause.setName(updatedName);
        String inputJson = super.mapToJson(cause);

        MvcResult result = mvc
                .perform(MockMvcRequestBuilders.put(uri, id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON).content(inputJson))
                .andReturn();

        String content = result.getResponse().getContentAsString();
        int status = result.getResponse().getStatus();

        Assert.assertEquals("failure - expected HTTP status 200", 200, status);
        Assert.assertTrue(
                "failure - expected HTTP response body to have a value",
                content.trim().length() > 0);

        Cause updatedCause = super.mapFromJson(content, Cause.class);

        Assert.assertNotNull("failure - expected cause not null",
                updatedCause);
        Assert.assertEquals("failure - expected cause.id unchanged",
                cause.getId(), updatedCause.getId());
        Assert.assertEquals("failure - expected updated cause text match",
                updatedName, updatedCause.getName());

    }

    @Test
    public void testDeleteCause() throws Exception {

        String uri = "/api/causes/{id}";
        Long id = new Long(1);

        MvcResult result = mvc.perform(MockMvcRequestBuilders.delete(uri, id)
                .contentType(MediaType.APPLICATION_JSON)).andReturn();

        String content = result.getResponse().getContentAsString();
        int status = result.getResponse().getStatus();

        Assert.assertEquals("failure - expected HTTP status 204", 204, status);
        Assert.assertTrue("failure - expected HTTP response body to be empty",
                content.trim().length() == 0);

        Cause deletedCause = arcaService.findOne(id);

        Assert.assertNull("failure - expected causes to be null",
                deletedCause);

    }

}