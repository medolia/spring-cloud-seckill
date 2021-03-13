package zone.medolia.web.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import zone.medolia.common.domain.SeckillUser;
import zone.medolia.common.key.SeckillUserKey;
import zone.medolia.web.redis.RedisService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Component
@Slf4j
public class UserArgumentResolver implements HandlerMethodArgumentResolver {
    RedisService redisService;

    @Autowired
    public void setRedisService(RedisService redisService) {
        this.redisService = redisService;
    }

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        Class<?> clazz = methodParameter.getParameterType();
        return clazz == SeckillUser.class;
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter,
                                  ModelAndViewContainer modelAndViewContainer,
                                  NativeWebRequest nativeWebRequest,
                                  WebDataBinderFactory webDataBinderFactory)
            throws Exception {

        HttpServletRequest request = nativeWebRequest.getNativeRequest(HttpServletRequest.class);
        String paramToken = request.getParameter("token");
        String cookieToken = getCookieValue(request, "token");
        if (StringUtils.isEmpty(paramToken) && StringUtils.isEmpty(cookieToken))
            return null;

        String token = StringUtils.isEmpty(paramToken) ? cookieToken : paramToken;
        log.info("token FOUND: {}", token);

        SeckillUser user = redisService.get(SeckillUserKey.token,
                token, SeckillUser.class);
        log.info("user obtained by token in redis: {}", user);
        return user;
    }

    private String getCookieValue(HttpServletRequest request, String cookieNameToken) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null || cookies.length <= 0)
            return null;

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(cookieNameToken))
                return cookie.getValue();
        }
        return null;
    }
}
