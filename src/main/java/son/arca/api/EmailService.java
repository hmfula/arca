package son.arca.api;

import son.arca.model.Cause;

import java.util.concurrent.Future;

/**
 * EmailService application programming interface
 * @author Harrison Mfula
 * @since 21.2.2016.
 */
public interface EmailService {

    Boolean send(Cause cause);

    void sendAsync(Cause cause);

    Future<Boolean> sendAsyncWithResult(Cause cause);

}
