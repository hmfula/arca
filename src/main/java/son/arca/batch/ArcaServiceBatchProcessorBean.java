package son.arca.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import son.arca.api.ArcaService;
import son.arca.model.Cause;

import java.util.Collection;

/**
 * run it as: java -jar target/spring-boot-fundamentals-1.0-SNAPSHOT.jar --spring.profiles.active=batch
 * @author Harrison Mfula
 * @since 21.2.2016.
 */
@Profile("batch")
@Component
public class ArcaServiceBatchProcessorBean {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private ArcaService arcaService;

    @Scheduled(
            cron = "0,30 * * * * *" //every 30sec , every min,hour,day   cron = "${batch.cause.cron}"
    )
    public void cronJob() {
        logger.info("> cronJob");

        Collection<Cause> causes = arcaService.findAll();
        logger.info("there are {} causes in the data store.", causes.size());
        logger.info("< cronJob");
    }


    @Scheduled(//uses start-start concept eg start 10:00:00 next job starts at 10:00:15

            //time between executions is measure from the beginning of the previous job

            //  Does not wait for  current job to complete
            initialDelayString = "${batch.cause.initial-delay}",
            fixedRateString = "${batch.cause.fixed-rate}"
    )
    public void fixedRateJobWithInitialDelay() {
        logger.info("> fixedRateJobWithInitialDelay");
        long pause = 5000;
        long start = System.currentTimeMillis();
        do {
            if (start + pause < System.currentTimeMillis()) {
                break;
            }

        } while (true);
        logger.info("Processing time was  {} seconds.", pause / 1000);
        logger.info("< fixedRateJobWithInitialDelay");
    }


    /**
     * This method is used to schedule SON functions runs such that the impact time is
     * respected. In this demo it is set to 15 seconds.
     */
    @Scheduled(//waits a given amount of time after completion of current job
            // Ensures at least one instance of the job is running at a time

            //time between executions is measure from the end of the previous job

            //In this example. run every 15sec after the end of previous jobs

            initialDelayString = "${batch.cause.initial-delay}",
            fixedDelayString = "${batch.cause.fixed-delay}"
    )
    public void fixedDelayJobWithInitialDelay() {
        logger.info("> fixedDelayJobWithInitialDelay");
        long pause = 5000;
        long start = System.currentTimeMillis();
        do {
            if (start + pause < System.currentTimeMillis()) {
                break;
            }

        } while (true);
        logger.info("Processing time was  {} seconds.", pause / 1000);
        logger.info("< fixedDelayJobWithInitialDelay");
    }
}
