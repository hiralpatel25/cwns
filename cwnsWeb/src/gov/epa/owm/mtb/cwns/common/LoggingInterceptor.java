package gov.epa.owm.mtb.cwns.common;

import org.apache.log4j.Logger;
import org.springframework.aop.MethodBeforeAdvice;

import org.apache.log4j.Logger;

import java.lang.reflect.Method;

public class LoggingInterceptor implements MethodBeforeAdvice {

    private Logger log = Logger.getLogger(this.getClass());

   public void before(Method method, Object[] objects, Object o) throws Throwable {
	   log.debug("LoggingInterceptor involked(-) ");
    }
}