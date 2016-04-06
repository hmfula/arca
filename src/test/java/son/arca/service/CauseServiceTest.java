package son.arca.service;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import son.arca.api.ArcaService;
import son.arca.model.Cause;
import son.arca.ws.AbstractTest;

import javax.persistence.EntityExistsException;
import javax.persistence.NoResultException;
import java.util.Collection;

/**
 * Unit test methods for the ArcaService and ArcaServiceBean.
 *
 * @author Harrison Mfula
 */
@Transactional
public class CauseServiceTest extends AbstractTest {

    @Autowired
    private ArcaService service;

    @Before
    public void setUp() {
        service.evictCache();
    }

    @After
    public void tearDown() {
        // clean up after each test method
    }

    @Test
    public void testFindAll() {

        Collection<Cause> list = service.findAll();

        Assert.assertNotNull("failure - expected not null", list);
        Assert.assertEquals("failure - expected list size", 15, list.size());

    }

    @Test
    public void testFindOne() {

        Long id = new Long(1);

        Cause entity = service.findOne(id);

        Assert.assertNotNull("failure - expected not null", entity);
        Assert.assertEquals("failure - expected id attribute match", id,
                entity.getId());

    }

    @Test
    public void testFindOneNotFound() {

        Long id = Long.MAX_VALUE;

        Cause entity = service.findOne(id);

        Assert.assertNull("failure - expected null", entity);

    }

    @Test
    public void testCreate() {

        Cause entity = new Cause();
        entity.setName("test");

        Cause createdEntity = service.create(entity);

        Assert.assertNotNull("failure - expected not null", createdEntity);
        Assert.assertNotNull("failure - expected id attribute not null",
                createdEntity.getId());
        Assert.assertEquals("failure - expected text attribute match", "test",
                createdEntity.getName());

        Collection<Cause> list = service.findAll();

        Assert.assertEquals("failure - expected size", 16, list.size());

    }

    @Test
    public void testCreateWithId() {

        Exception exception = null;

        Cause entity = new Cause();
        entity.setId(Long.MAX_VALUE);
        entity.setName("test");

        try {
            service.create(entity);
        } catch (EntityExistsException e) {
            exception = e;
        }

        Assert.assertNotNull("failure - expected exception", exception);
        Assert.assertTrue("failure - expected EntityExistsException",
                exception instanceof EntityExistsException);

    }

    @Test
    public void testUpdate() {

        Long id = new Long(1);

        Cause entity = service.findOne(id);

        Assert.assertNotNull("failure - expected not null", entity);

        String updatedText = entity.getName() + " test";
        entity.setName(updatedText);
        Long updatedFrequency = entity.getFrequency() + 100L;
        entity.setFrequency(updatedFrequency);
        Cause updatedEntity = service.update(entity);

        Assert.assertNotNull("failure - expected not null", updatedEntity);
        Assert.assertEquals("failure - expected id attribute match", id,
                updatedEntity.getId());
        Assert.assertEquals("failure - expected text attribute match",
                updatedText, updatedEntity.getName());
        Assert.assertEquals("failure - expected frequency attribute match",
                updatedFrequency, updatedEntity.getFrequency());

    }

    @Test
    public void testUpdateNotFound() {

        Exception exception = null;

        Cause entity = new Cause();
        entity.setId(Long.MAX_VALUE);
        entity.setName("test");

        try {
            service.update(entity);
        } catch (NoResultException e) {
            exception = e;
        }

        Assert.assertNotNull("failure - expected exception", exception);
        Assert.assertTrue("failure - expected NoResultException",
                exception instanceof NoResultException);

    }

    @Test
    public void testDelete() {

        Long id = new Long(1);

        Cause entity = service.findOne(id);

        Assert.assertNotNull("failure - expected not null", entity);

        service.delete(id);

        Collection<Cause> list = service.findAll();

        Assert.assertEquals("failure - expected size", 14, list.size());

        Cause deletedEntity = service.findOne(id);

        Assert.assertNull("failure - expected null", deletedEntity);

    }

}