package eg.dicomparison.test;

import eg.dicomparison.domain.TestBean;
import org.jboss.weld.context.DependentContext;
import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;
import org.jboss.weld.literal.DefaultLiteral;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.enterprise.inject.Instance;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.util.AnnotationLiteral;
import java.util.Set;

/**
 * Test Weld instance lookup.
 * <br/>
 * Created by IntelliJ IDEA.
 * User: josh
 * Date: 6/24/11
 * Time: 7:50 AM
 */
@Test
public class WeldLookupTest {
    private static final Logger log = LoggerFactory.getLogger(WeldLookupTest.class);

    public void lookupNoDeps() {
        WeldContainer weldContainer = new Weld().initialize();
        BeanManager beanManager = weldContainer.getBeanManager();
        Set<Bean<?>> beans = beanManager.getBeans(TestBean.class, DefaultLiteral.INSTANCE);
        Assert.assertEquals(beans.size(),1);
        Bean<?> bean = beans.iterator().next();
        log.info("bean=" + bean + " scope=" + bean.getScope());

        DependentContext dependentContext = weldContainer.instance().select(DependentContext.class).get();
        log.info("dependentContext=" + dependentContext);
        Instance<TestBean> instance = weldContainer.instance().select(TestBean.class,DefaultLiteral.INSTANCE);
        log.info("instance=" + instance);
        TestBean b1 = instance.get();
        log.info("b1=" + b1.toString());
        TestBean b2 = instance.get();
        log.info("b2=" + b2.toString());
        // The creational context in the 'instance' object has both TestBean instances in it! [jsd]
    }
}
