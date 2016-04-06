package son.arca.service;


import son.arca.api.ArcaService;
import son.arca.model.Cause;
import son.arca.repository.ArcaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import son.arca.util.ArcaConstants;

import javax.persistence.EntityExistsException;
import javax.persistence.NoResultException;
import java.util.Collection;

/**
 * The ArcaServiceBean encapsulates all business behaviors operating on the
 * Cause entity model object.
 *
 * @author Harrison Mfula
 * @since 10.2.2016
 */
@Service
@Transactional(
        propagation = Propagation.SUPPORTS,
        readOnly = true)
public class ArcaServiceBean implements ArcaService {



    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * The <code>CounterService</code> captures metrics for Spring Actuator.
     */
    @Autowired
    private CounterService counterService;

    /**
     * The Spring Data repository for Cause entities.
     */
    @Autowired
    private ArcaRepository arcaServiceRepository;

    @Override
    public Collection<Cause> findAll() {
        logger.info("> findAll");

        counterService.increment("method.invoked.ArcaServiceBean.findAll");

        Collection<Cause> causes = arcaServiceRepository.findAll();

        logger.info("< findAll");
        return causes;
    }

    @Override
    @Cacheable(
            value = ArcaConstants.ARCA_CACHE_NAME,
            key = "#id")
    public Cause findOne(Long id) {
        logger.info("> findOne id:{}", id);

        counterService.increment("method.invoked.ArcaServiceBean.findOne");

        Cause cause = arcaServiceRepository.findOne(id);

        logger.info("< findOne id:{}", id);
        return cause;
    }

    @Override
    @Transactional(
            propagation = Propagation.REQUIRED,
            readOnly = false)
    @CachePut(
            value = ArcaConstants.ARCA_CACHE_NAME,
            key = "#result.id")
    public Cause create(Cause cause) {
        logger.info("> create");

        counterService.increment("method.invoked.ArcaServiceBean.create");

        if (cause.getId() != null) {
            logger.error(
                    "Attempted to create a Cause, but id attribute was not null.");
            throw new EntityExistsException(
                    "The id attribute must be null to persist a new entity.");
        }

        Cause savedCause = arcaServiceRepository.save(cause);

        logger.info("< create");
        return savedCause;
    }

    @Override
    @Transactional(
            propagation = Propagation.REQUIRED,
            readOnly = false)
    @CachePut(
            value = ArcaConstants.ARCA_CACHE_NAME,
            key = "#cause.id")
    public Cause update(Cause cause) {
        logger.info("> update id:{}", cause.getId());

        counterService.increment("method.invoked.ArcaServiceBean.update");

        Cause causeToUpdate = findOne(cause.getId());
        if (causeToUpdate == null) {
            logger.error(
                    "Attempted to update a Cause, but the entity does not exist.");
            throw new NoResultException("Requested entity not found.");
        }

        causeToUpdate.setName(cause.getName());
        causeToUpdate.setFrequency(cause.getFrequency());
        causeToUpdate.setTotal(cause.getTotal());
        causeToUpdate.setDescription(cause.getDescription());
        Cause updatedCause = arcaServiceRepository.save(causeToUpdate);

        logger.info("< update id:{}", cause.getId());
        return updatedCause;
    }

    @Override
    @Transactional(
            propagation = Propagation.REQUIRED,
            readOnly = false)
    @CacheEvict(
            value = ArcaConstants.ARCA_CACHE_NAME,
            key = "#id")
    public void delete(Long id) {
        logger.info("> delete id:{}", id);

        counterService.increment("method.invoked.ArcaServiceBean.delete");

        arcaServiceRepository.delete(id);

        logger.info("< delete id:{}", id);
    }

    @Override
    @CacheEvict(
            value = ArcaConstants.ARCA_CACHE_NAME,
            allEntries = true)
    public void evictCache() {
        logger.info("> evictCache");
        logger.info("< evictCache");
    }
}