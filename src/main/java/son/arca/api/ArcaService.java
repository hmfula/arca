package son.arca.api;

import son.arca.model.Cause;

import java.util.Collection;

/**
 * ArcaService application programming interface
 * @author Harrison Mfula
 * @since 10.2.2016.
 */
public interface ArcaService {

    Collection<Cause> findAll();

    Cause findOne(Long id);

    Cause create(Cause cause);

    Cause update(Cause cause);

    void delete(Long id);

    void evictCache();
}
