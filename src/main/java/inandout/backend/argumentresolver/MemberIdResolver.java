package inandout.backend.argumentresolver;

import inandout.backend.dto.login.CustomMemberDetails;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class MemberIdResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(MemberId.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        System.out.println(((CustomMemberDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getMemberId());
        return ((CustomMemberDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getMemberId();
    }
}
