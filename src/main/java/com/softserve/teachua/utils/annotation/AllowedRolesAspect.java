package com.softserve.teachua.utils.annotation;

import com.softserve.teachua.constants.RoleData;
import com.softserve.teachua.exception.WrongAuthenticationException;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
@Slf4j
public class AllowedRolesAspect {
    private static final String PERMIT_EXCEPTION = "You have no necessary permissions (role)";

    @Around("@annotation(com.softserve.teachua.utils.annotation.AllowedRoles)")
    public Object doSomething(ProceedingJoinPoint jp) throws Throwable {
        Set<RoleData> roles = Arrays
                .stream(((MethodSignature) jp.getSignature()).getMethod().getAnnotation(AllowedRoles.class).value())
                .collect(Collectors.toSet());
        log.debug("Allowed roles: {}", roles);
        HttpServletRequest httpServletRequest = getRequest();
        for (RoleData role : roles) {
            if (httpServletRequest.isUserInRole(role.getDBRoleName())) {
                return jp.proceed();
            }
        }

        throw new WrongAuthenticationException(PERMIT_EXCEPTION);
    }

    private HttpServletRequest getRequest() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();
        return servletRequestAttributes.getRequest();
    }
}
