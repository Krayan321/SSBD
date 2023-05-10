package pl.lodz.p.it.ssbd2023.ssbd01.interceptors;

import jakarta.annotation.Resource;
import jakarta.ejb.SessionContext;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.InvocationContext;
import lombok.extern.java.Log;
import java.util.Arrays;

@Log
public class TrackerInterceptor {

    @Resource
    private SessionContext sctx;

    @AroundInvoke
    public Object InvokeForMethod(InvocationContext invocation) throws Exception {
        StringBuilder sb = new StringBuilder("User: ").
                append(sctx.getCallerPrincipal().getName());
        Object result;
        try{
            sb.append("Intercepted method: ").
                    append(invocation.getMethod().toString());
            String parameters;
            if (invocation.getParameters() != null)
                parameters = String.format(" Parameters : %s",
                        Arrays.toString(invocation.getParameters()));
            else parameters = " none ";
            sb.append(parameters);

            result = invocation.proceed();
        } catch (Exception e) {
            sb.append(String.format("Exception : %s", e));
            log.severe(sb.toString());
            throw e;
        }
        String nextResult;
        if (result != null) nextResult =
                String.format(" Returned value: %s", result);
        else nextResult = " No value";
        sb.append(nextResult);
        log.info(sb.toString());
        return nextResult;
    }
}
