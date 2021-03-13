package zone.medolia.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import zone.medolia.common.key.SeckillUserKey;
import zone.medolia.common.result.CodeMsg;
import zone.medolia.common.result.Result;
import zone.medolia.common.vo.LoginVo;
import zone.medolia.web.client.LoginClient;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/login")
@Slf4j
public class LoginController {

    LoginClient loginClient;

    @Autowired
    public void setLoginClient(LoginClient loginClient) {
        this.loginClient = loginClient;
    }

    @RequestMapping("/test")
    @ResponseBody
    public Result<String> test() {
        Result<String> result = loginClient.test();
        log.info(result.toString());
        return result;
    }

    @RequestMapping("/to_login")
    public String toLogin() {
        return "login";
    }

    @PostMapping("/do_login")
    @ResponseBody
    public Result<String> doLogin(HttpServletResponse response, LoginVo loginVo) {
        log.info("loginVo sent to login service: {}", loginVo);
        String token = loginClient.doLogin(loginVo);
        if (token == null || StringUtils.isEmpty(token) || token.equals(""))
            return Result.error(CodeMsg.PASSWORD_ERROR);

        log.info("token from feign: {}", token);
        Cookie cookie = new Cookie("token", token);
        cookie.setPath("/");
        cookie.setMaxAge(SeckillUserKey.token.expireSeconds());
        response.addCookie(cookie);
        return Result.success(token);
    }
}
