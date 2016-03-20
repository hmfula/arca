package son.arca.repository;

import son.arca.model.Cause;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * JPA repository
 * @author Harrison Mfula
 * @since 9.2.2016.
 */
@Repository
public interface ArcaRepository extends JpaRepository<Cause, Long> {
}
