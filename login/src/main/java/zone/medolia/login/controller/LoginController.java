package zone.medolia.login.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import zone.medolia.common.result.CodeMsg;
import zone.medolia.common.result.Result;
import zone.medolia.common.vo.LoginVo;
import zone.medolia.login.redis.RedisService;
import zone.medolia.login.service.SeckillUserService;

import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/login")
@Slf4j
public class LoginController {

    SeckillUserService userService;
    RedisService redisService;

    @Autowired
    public LoginController(SeckillUserService userService,
                           RedisService redisService) {
        this.userService = userService;
        this.redisService = redisService;
    }

    @RequestMapping("/test")
    @ResponseBody
    public Result<String> test() {
        String str = "Login service feigned by web service";
        return Result.success(str);
    }

    @RequestMapping("/to_login")
    public String toLogin() {
        return "login";
    }

    @PostMapping(value = "/do_login")
    @ResponseBody
    public String doLogin(@RequestBody LoginVo loginVo) {
        log.info("loginVo: {}", loginVo);
        String token = userService.login(loginVo);
        if (token == null)
            return null;
        else {
            log.info("success login!");
            return token;
        }
    }

    @RequestMapping("/success")
    public String loginSuccess() {
        return "loginSuccess";
    }
}
