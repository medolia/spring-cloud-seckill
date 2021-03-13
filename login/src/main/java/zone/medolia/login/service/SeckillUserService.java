package zone.medolia.login.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zone.medolia.common.domain.SeckillUser;
import zone.medolia.common.vo.LoginVo;
import zone.medolia.login.dao.SeckillUserDao;
import zone.medolia.login.redis.RedisService;
import zone.medolia.login.redis.key.SeckillUserKey;
import zone.medolia.login.util.MD5Util;
import zone.medolia.login.util.UUIDUtil;

@Service
@Slf4j
public class SeckillUserService {

    SeckillUserDao seckillUserDao;
    RedisService redisService;

    @Autowired
    public SeckillUserService(SeckillUserDao seckillUserDao, RedisService redisService) {
        this.seckillUserDao = seckillUserDao;
        this.redisService = redisService;
    }

    public SeckillUser getById(long id) {
        // 查询对象缓存
        SeckillUser user = redisService.get(SeckillUserKey.getById, ""+id, SeckillUser.class);
        if (user != null) {
            log.info("user found in cache: " + user.toString());
            return user;
        }

        // 若缓存未命中，查询数据库、更新缓存
        user = seckillUserDao.getById(id);
        if (user != null)
            redisService.set(SeckillUserKey.getById, ""+id, user);

        return user;
    }

    public String login(LoginVo loginVo) {
        log.info("loginVo: {}", loginVo);

        if (loginVo == null)
            return null;
        String mobile = loginVo.getMobile();
        String formPass = loginVo.getPassword();

        // 判断手机号是否存在
        SeckillUser user = getById(Long.parseLong(mobile));
        if (user == null)
            return null;

        // 核对密码
        String dbPass = user.getPassword();
        String dbSalt = user.getSalt();
        String calcPass = MD5Util.formPassToDBPass(formPass, dbSalt);
        if (!calcPass.equals(dbPass))
            return null;

        // 生成 cookie，即使用一个统一的标识键、一个 uuid 为值存入 cookie 中
        String token = UUIDUtil.uuid();
        log.info("new token created: {}", token);
        redisService.set(SeckillUserKey.token, token, user);
        return token;
    }
}
