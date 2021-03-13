package zone.medolia.web.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import zone.medolia.common.result.Result;
import zone.medolia.common.vo.LoginVo;

@FeignClient("login")
public interface LoginClient {

    @PostMapping(value = "/login/do_login")
    String doLogin(@RequestBody LoginVo loginVo);

    @RequestMapping("/login/test")
    Result<String> test();
}
