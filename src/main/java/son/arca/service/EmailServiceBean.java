package son.arca.service;

import son.arca.api.EmailService;
import son.arca.model.Cause;
import son.arca.util.AsyncResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.Future;

/**
 * @author Harrison Mfula
 * @since 21.2.2016.
 */
@Service
public class EmailServiceBean implements EmailService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());



    @Override
    public Boolean send(Cause cause) {

        logger.info("> send");

        Boolean success = Boolean.FALSE;
        long pause = 5000;
        try {
            Thread.sleep(pause);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        logger.info("Processing time was  {} seconds.", pause / 1000);

        success = Boolean.TRUE;

        logger.info("< send");

        return success;
    }

    @Async
    @Override
    public void sendAsync(Cause cause) {

        logger.info("> sendAsync");

        try{
            send(cause);
        }catch (Exception ex){
            logger.warn("Exception caught during sending of asynchronous email", ex);
        }
        logger.info("< sendAsync");
    }

    @Async
    @Override
    public Future<Boolean> sendAsyncWithResult(Cause cause) {
        logger.info("> sendAsyncWithResult");
        AsyncResponse<Boolean> asyncResponse = new AsyncResponse<Boolean>();
        try{
            Boolean success =  send(cause);
            asyncResponse.completed(success);
        }catch (Exception ex){
            logger.warn("Exception caught during sending of asynchronous email", ex);
            asyncResponse.completedExceptionally(ex);
        }
        logger.info("< sendAsyncWithResult");
        return asyncResponse;
    }
}
