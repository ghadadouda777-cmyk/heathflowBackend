package com.example.backendhealth.security;

import com.example.backendhealth.services.AbonnementService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class SubscriptionInterceptor implements HandlerInterceptor {

    private final AbonnementService abonnementService;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        if (!(handler instanceof HandlerMethod handlerMethod)) return true;

        boolean requiresSubscription =
                handlerMethod.hasMethodAnnotation(RequireSubscription.class) ||
                        handlerMethod.getBeanType().isAnnotationPresent(RequireSubscription.class);

        if (!requiresSubscription) return true;

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            response.sendError(401, "Non authentifié");
            return false;
        }

        if (!abonnementService.hasActiveSubscription(auth.getName())) {
            response.setStatus(403);
            response.setContentType("application/json");
            response.getWriter().write(
                    "{\"error\":\"SUBSCRIPTION_REQUIRED\"," +
                            "\"message\":\"Abonnement actif requis.\"}"
            );
            return false;
        }

        return true;
    }
}
