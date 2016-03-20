package son.arca.api;

import son.arca.ws.AbstractControllerTest;
import son.arca.model.Cause;
import son.arca.controller.ArcaController;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the ArcaController using Mockito mocks and spies.
 * <p/>
 * These tests utilize the Mockito framework objects to simulate interaction
 * with back-end components. The controller methods are invoked directly
 * bypassing the Spring MVC mappings. Back-end components are mocked and
 * injected into the controller. Mockito spies and verifications are performed
 * ensuring controller behaviors.
 *
 * @author Harrison Mfula
 * @since 11.3.2016
 */
@Transactional
public class CauseControllerMocksTest extends AbstractControllerTest {

    /**
     * A mocked ArcaService
     */
    @Mock
    private ArcaService arcaService;

    /**
     * A mocked EmailService
     */
    @Mock
    private EmailService emailService;

    /**
     * A ArcaController instance with <code>@Mock</code> components injected
     * into it.
     */
    @InjectMocks
    private ArcaController arcaController;

    /**
     * Setup each test method. Initialize Mockito mock and spy objects. Scan for
     * Mockito annotations.
     */
    @Before
    public void setUp() {
        // Initialize Mockito annotated components
        MockitoAnnotations.initMocks(this);
        // Prepare the Spring MVC Mock components for standalone testing
        setUp(arcaController);
    }

    @Test
    public void testGetCauses() throws Exception {

        // Create some test data
        Collection<Cause> list = getEntityListStubData();

        // Stub the ArcaService.findAll method return value
        when(arcaService.findAll()).thenReturn(list);

        // Perform the behavior being tested
        String uri = "/api/causes";

        MvcResult result = mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON)).andReturn();

        // Extract the response status and body
        String content = result.getResponse().getContentAsString();
        int status = result.getResponse().getStatus();

        // Verify the CauseService.findAll method was invoked once
        verify(arcaService, times(1)).findAll();

        // Perform standard JUnit assertions on the response
        Assert.assertEquals("failure - expected HTTP status 200", 200, status);
        Assert.assertTrue(
                "failure - expected HTTP response body to have a value",
                content.trim().length() > 0);

    }

    @Test
    public void testGetCause() throws Exception {

        // Create some test data
        Long id = new Long(1);
        Cause entity = getEntityStubData();

        // Stub the causeservice.findOne method return value
        when(arcaService.findOne(id)).thenReturn(entity);

        // Perform the behavior being tested
        String uri = "/api/causes/{id}";

        MvcResult result = mvc.perform(MockMvcRequestBuilders.get(uri, id)
                .accept(MediaType.APPLICATION_JSON)).andReturn();

        // Extract the response status and body
        String content = result.getResponse().getContentAsString();
        int status = result.getResponse().getStatus();

        // Verify the CauseService.findOne method was invoked once
        verify(arcaService, times(1)).findOne(id);

        // Perform standard JUnit assertions on the test results
        Assert.assertEquals("failure - expected HTTP status 200", 200, status);
        Assert.assertTrue(
                "failure - expected HTTP response body to have a value",
                content.trim().length() > 0);
    }

    @Test
    public void testGetCauseNotFound() throws Exception {

        // Create some test data
        Long id = Long.MAX_VALUE;

        // Stub the CauseService.findOne method return value
        when(arcaService.findOne(id)).thenReturn(null);

        // Perform the behavior being tested
        String uri = "/api/causes/{id}";

        MvcResult result = mvc.perform(MockMvcRequestBuilders.get(uri, id)
                .accept(MediaType.APPLICATION_JSON)).andReturn();

        // Extract the response status and body
        String content = result.getResponse().getContentAsString();
        int status = result.getResponse().getStatus();

        // Verify the CauseService.findOne method was invoked once
        verify(arcaService, times(1)).findOne(id);

        // Perform standard JUnit assertions on the test results
        Assert.assertEquals("failure - expected HTTP status 404", 404, status);
        Assert.assertTrue("failure - expected HTTP response body to be empty",
                content.trim().length() == 0);

    }

    @Test
    public void testCreateCause() throws Exception {

        // Create some test data
        Cause entity = getEntityStubData();

        // Stub the CauseService.create method return value
        when(arcaService.create(any(Cause.class))).thenReturn(entity);

        // Perform the behavior being tested
        String uri = "/api/causes";
        String inputJson = super.mapToJson(entity);

        MvcResult result = mvc
                .perform(MockMvcRequestBuilders.post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON).content(inputJson))
                .andReturn();

        // Extract the response status and body
        String content = result.getResponse().getContentAsString();
        int status = result.getResponse().getStatus();

        // Verify the CauseService.create method was invoked once
        verify(arcaService, times(1)).create(any(Cause.class));

        // Perform standard JUnit assertions on the test results
        Assert.assertEquals("failure - expected HTTP status 201", 201, status);
        Assert.assertTrue(
                "failure - expected HTTP response body to have a value",
                content.trim().length() > 0);

        Cause createdEntity = super.mapFromJson(content, Cause.class);

        Assert.assertNotNull("failure - expected entity not null",
                createdEntity);
        Assert.assertNotNull("failure - expected id attribute not null",
                createdEntity.getId());
        Assert.assertEquals("failure - expected text attribute match",
                entity.getText(), createdEntity.getText());
    }

    @Test
    public void testUpdateCause() throws Exception {

        // Create some test data
        Cause entity = getEntityStubData();
        entity.setText(entity.getText() + " test");
        Long id = new Long(1);

        // Stub the CauseService.update method return value
        when(arcaService.update(any(Cause.class))).thenReturn(entity);

        // Perform the behavior being tested
        String uri = "/api/causes/{id}";
        String inputJson = super.mapToJson(entity);

        MvcResult result = mvc
                .perform(MockMvcRequestBuilders.put(uri, id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON).content(inputJson))
                .andReturn();

        // Extract the response status and body
        String content = result.getResponse().getContentAsString();
        int status = result.getResponse().getStatus();

        // Verify the CauseService.update method was invoked once
        verify(arcaService, times(1)).update(any(Cause.class));

        // Perform standard JUnit assertions on the test results
        Assert.assertEquals("failure - expected HTTP status 200", 200, status);
        Assert.assertTrue(
                "failure - expected HTTP response body to have a value",
                content.trim().length() > 0);

        Cause updatedEntity = super.mapFromJson(content, Cause.class);

        Assert.assertNotNull("failure - expected entity not null",
                updatedEntity);
        Assert.assertEquals("failure - expected id attribute unchanged",
                entity.getId(), updatedEntity.getId());
        Assert.assertEquals("failure - expected text attribute match",
                entity.getText(), updatedEntity.getText());

    }

    @Test
    public void testDeleteCause() throws Exception {

        // Create some test data
        Long id = new Long(1);

        // Perform the behavior being tested
        String uri = "/api/causes/{id}";

        MvcResult result = mvc.perform(MockMvcRequestBuilders.delete(uri, id))
                .andReturn();

        // Extract the response status and body
        String content = result.getResponse().getContentAsString();
        int status = result.getResponse().getStatus();

        // Verify the CauseService.delete method was invoked once
        verify(arcaService, times(1)).delete(id);

        // Perform standard JUnit assertions on the test results
        Assert.assertEquals("failure - expected HTTP status 204", 204, status);
        Assert.assertTrue("failure - expected HTTP response body to be empty",
                content.trim().length() == 0);

    }

    private Collection<Cause> getEntityListStubData() {
        Collection<Cause> list = new ArrayList<Cause>();
        list.add(getEntityStubData());
        return list;
    }

    private Cause getEntityStubData() {
        Cause entity = new Cause();
        entity.setId(1L);
        entity.setText("hello");
        return entity;
    }

}