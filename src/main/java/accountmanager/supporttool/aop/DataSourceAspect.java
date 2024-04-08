package accountmanager.supporttool.aop;

import accountmanager.supporttool.annotation.SwitchDataSource;
import accountmanager.supporttool.configuration.AbstractRoutingDataSourceImpl;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
@Order(-10)
public class DataSourceAspect {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    //defininining where the jointpoint need to be applied
    @Pointcut("@annotation(accountmanager.supporttool.annotation.SwitchDataSource)")
    public void annotationPointCut() {
    }

    // setting the lookup key using the annotation passed value
    @Before("annotationPointCut()")
    public void before(JoinPoint joinPoint){
        MethodSignature sign =  (MethodSignature)joinPoint.getSignature();
        Method method = sign.getMethod();
        SwitchDataSource annotation = method.getAnnotation(SwitchDataSource.class);
        if(annotation != null && !AbstractRoutingDataSourceImpl.getDatabaseName().equalsIgnoreCase(annotation.value())){
            AbstractRoutingDataSourceImpl.setDatabaseName(annotation.value());
            logger.info("Switch DataSource to [{}] in Method [{}]",
                    annotation.value(), joinPoint.getSignature());
        }
    }

    // restoring to default datasource after the execution of the method
    @After("annotationPointCut()")
    public void after(JoinPoint point){
        if(null != AbstractRoutingDataSourceImpl.getDatabaseName()) {
            AbstractRoutingDataSourceImpl.removeDatabaseName();
        }
    }
}
