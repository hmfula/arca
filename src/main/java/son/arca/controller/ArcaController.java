package son.arca.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import son.arca.api.ArcaService;
import son.arca.api.EmailService;
import son.arca.model.Cause;

import java.util.Collection;
import java.util.concurrent.Future;

/**
 * The ArcaController class is a RESTful web service controller. The
 * <code>@RestController</code> annotation informs Spring that each
 * <code>@RequestMapping</code> method returns a <code>@ResponseBody</code>
 * which, by default, contains a ResponseEntity converted into JSON with an
 * associated HTTP status code.
 *
 * @author Harrison Mfula
 */
@RestController
public class ArcaController extends BaseController {

    @Autowired
    private ArcaService arcaService;

    @Autowired
    private EmailService emailService;

    @RequestMapping("/")
    public String index() {
        return "index";
    }

    /**
     * Web service endpoint to fetch all Cause entities. The service returns
     * the collection of Cause entities as JSON.
     *
     * @return A ResponseEntity containing a Collection of Cause objects.
     */
    @RequestMapping(
            value = "/api/causes",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<Cause>> getCauses() {
        logger.info("> getCauses");

        Collection<Cause> causes = arcaService.findAll();

        logger.info("< getCauses");
        return new ResponseEntity<Collection<Cause>>(causes,
                HttpStatus.OK);
    }

    /**
     * Web service endpoint to fetch a single Cause entity by primary key
     * identifier.
     *
     * If found, the Cause is returned as JSON with HTTP status 200.
     *
     * If not found, the service returns an empty response body with HTTP status
     * 404.
     *
     * @param id A Long URL path variable containing the Cause primary key
     *        identifier.
     * @return A ResponseEntity containing a single Cause object, if found,
     *         and a HTTP status code as described in the method comment.
     */
    @RequestMapping(
            value = "/api/causes/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Cause> getCause(@PathVariable("id") Long id) {
        logger.info("> getCause id:{}", id);

        Cause cause = arcaService.findOne(id);
        if (cause == null) {
            return new ResponseEntity<Cause>(HttpStatus.NOT_FOUND);
        }

        logger.info("< getCause id:{}", id);
        return new ResponseEntity<Cause>(cause, HttpStatus.OK);
    }

    /**
     * Web service endpoint to create a single Cause entity. The HTTP request
     * body is expected to contain a Cause object in JSON format. The
     * Cause is persisted in the data repository.
     *
     * If created successfully, the persisted Cause is returned as JSON with
     * HTTP status 201.
     *
     * If not created successfully, the service returns an empty response body
     * with HTTP status 500.
     *
     * @param cause The Cause object to be created.
     * @return A ResponseEntity containing a single Cause object, if created
     *         successfully, and a HTTP status code as described in the method
     *         comment.
     */
    @RequestMapping(
            value = "/api/causes",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Cause> createCause(
            @RequestBody Cause cause) {
        logger.info("> createCause");

        Cause savedCause = arcaService.create(cause);

        logger.info("< createCause");
        return new ResponseEntity<Cause>(savedCause, HttpStatus.CREATED);
    }

    /**
     * Web service endpoint to update a single Cause entity. The HTTP request
     * body is expected to contain a Cause object in JSON format. The
     * Cause is updated in the data repository.
     *
     * If updated successfully, the persisted Cause is returned as JSON with
     * HTTP status 200.
     *
     * If not found, the service returns an empty response body and HTTP status
     * 404.
     *
     * If not updated successfully, the service returns an empty response body
     * with HTTP status 500.
     *
     * @param cause The Cause object to be updated.
     * @return A ResponseEntity containing a single Cause object, if updated
     *         successfully, and a HTTP status code as described in the method
     *         comment.
     */
    @RequestMapping(
            value = "/api/causes/{id}",
            method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Cause> updateCause(
            @RequestBody Cause cause) {
        logger.info("> updateCause:{}", cause.getId());

        Cause updatedCause = arcaService.update(cause);
        if (updatedCause == null) {
            return new ResponseEntity<Cause>(
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

        logger.info("< updateCause:{}", cause.getId());
        return new ResponseEntity<Cause>(updatedCause, HttpStatus.OK);
    }

    /**
     * Web service endpoint to delete a single Cause entity. The HTTP request
     * body is empty. The primary key identifier of the Cause to be deleted
     * is supplied in the URL as a path variable.
     *
     * If deleted successfully, the service returns an empty response body with
     * HTTP status 204.
     *
     * If not deleted successfully, the service returns an empty response body
     * with HTTP status 500.
     *
     * @param id A Long URL path variable containing the Cause primary key
     *        identifier.
     * @return A ResponseEntity with an empty response body and a HTTP status
     *         code as described in the method comment.
     */
    @RequestMapping(
            value = "/api/causes/{id}",
            method = RequestMethod.DELETE)
    public ResponseEntity<Cause> deleteCause(
            @PathVariable("id") Long id) {
        logger.info("> deleteCause:{}", id);

        arcaService.delete(id);

        logger.info("< deleteCause:{}", id);
        return new ResponseEntity<Cause>(HttpStatus.NO_CONTENT);
    }

    /**
     * Web service endpoint to fetch a single Cause entity by primary key
     * identifier and send it as an email.
     *
     * If found, the Cause is returned as JSON with HTTP status 200 and sent
     * via Email.
     *
     * If not found, the service returns an empty response body with HTTP status
     * 404.
     *
     * @param id A Long URL path variable containing the Cause primary key
     *        identifier.
     * @param waitForAsyncResult A boolean indicating if the web service should
     *        wait for the asynchronous email transmission.
     * @return A ResponseEntity containing a single Cause object, if found,
     *         and a HTTP status code as described in the method comment.
     */
    @RequestMapping(
            value = "/api/causes/{id}/send",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Cause> sendCause(@PathVariable("id") Long id,
                                                 @RequestParam(
                                                         value = "wait",
                                                         defaultValue = "false") boolean waitForAsyncResult) {

        logger.info("> sendCause id:{}", id);

        Cause cause = null;

        try {
            cause = arcaService.findOne(id);
            if (cause == null) {
                logger.info("< sendCause id:{}", id);
                return new ResponseEntity<Cause>(HttpStatus.NOT_FOUND);
            }

            if (waitForAsyncResult) {
                Future<Boolean> asyncResponse = emailService
                        .sendAsyncWithResult(cause);
                boolean emailSent = asyncResponse.get();
                logger.info("- cause email sent? {}", emailSent);
            } else {
                emailService.sendAsync(cause);
            }
        } catch (Exception e) {
            logger.error("A problem occurred sending the Cause.", e);
            return new ResponseEntity<Cause>(
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

        logger.info("< sendCause id:{}", id);
        return new ResponseEntity<Cause>(cause, HttpStatus.OK);
    }

}