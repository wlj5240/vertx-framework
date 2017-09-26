package com.zc.test.service;

import io.vertx.core.json.JsonObject;
import org.rayeye.util.exception.TransactionException;
import org.rayeye.vertx.annotations.VerticleMapping;
import org.rayeye.vertx.result.ResultOb;
import org.rayeye.vertx.spring.AbstractService;
import org.springframework.stereotype.Service;

/**
 * 简单测试登录
 * 如果非降级API定义，可以继承AbstractService
 * 如果有API定一层， 不推荐继承AbstractService 或使用直接发送调用业务
 *
 * @ProjectName: test
 * @Package: com.zc.service
 * @ClassName: AuthService
 * @Description: Describes the function of the class
 * @Author: Neil.Zhou
 * @CreateDate: 2017/9/21 11:38
 * @UpdateUser: Neil.Zhou
 * @UpdateDate: 2017/9/21 11:38
 * @UpdateRemark: The modified content
 * @Version: 1.0
 * <p>Copyright: Copyright (c) 2017</p>
 */
@VerticleMapping
@Service
public class AuthService extends AbstractService {

    /**
     * 单项目测试
     * @method      doLogin
     * @author      Neil.Zhou
     * @param param
     * @return      io.vertx.core.json.JsonObject
     * @exception
     * @date        2017/9/26 17:16
     */
    public JsonObject doLogin(JsonObject param) throws TransactionException {
        return new JsonObject(ResultOb.build().setMsg("测试登录地址...").toString());
    }
}
