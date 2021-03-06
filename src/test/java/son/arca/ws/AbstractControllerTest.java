package son.arca.ws;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import son.arca.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;

/**
 * This class extends the functionality of AbstractTest. AbstractControllerTest
 * is the parent of all web controller unit test classes. The class ensures that
 * a type of WebApplicationContext is built and prepares a MockMvc instance for
 * use in test methods.
 *
 * @author Harrison Mfula
 * @since 11.3.2016
 */
@WebAppConfiguration
public abstract class AbstractControllerTest extends AbstractTest {

    protected MockMvc mvc;

    @Autowired
    protected WebApplicationContext webApplicationContext;

    /**
     * Prepares the test class for execution of web tests. Builds a MockMvc
     * instance. Call this method from the concrete JUnit test class in the
     * <code>@Before</code> setup method.
     */
    protected void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    /**
     * Prepares the test class for execution of web tests. Builds a MockMvc
     * instance using standalone configuration facilitating the injection of
     * Mockito resources into the controller class.
     *
     * @param controller A controller object to be tested.
     */
    protected void setUp(BaseController controller) {
        mvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    /**
     * Maps an Object into a JSON String. Uses a Jackson ObjectMapper.
     *
     * @param obj The Object to map.
     * @return A String of JSON.
     * @throws com.fasterxml.jackson.core.JsonProcessingException Thrown if an error occurs while mapping.
     */
    protected String mapToJson(Object obj) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(obj);
    }

    /**
     * Maps a String of JSON into an instance of a Class of type T. Uses a
     * Jackson ObjectMapper.
     *
     * @param json  A String of JSON.
     * @param clazz A Class of type T. The mapper will attempt to convert the
     *              JSON into an Object of this Class type.
     * @return An Object of type T.
     * @throws java.io.IOException Thrown if an error occurs while mapping.
     */
    protected <T> T mapFromJson(String json, Class<T> clazz)
            throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, clazz);
    }

}