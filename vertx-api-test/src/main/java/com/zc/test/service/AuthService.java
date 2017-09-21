package com.zc.test.service;

import io.vertx.core.json.JsonObject;
import org.rayeye.util.exception.TransactionException;
import org.rayeye.vertx.annotations.VerticleMapping;
import org.rayeye.vertx.result.ResultOb;
import org.springframework.stereotype.Service;

/**
 * 简单测试登录
 *
 * @ProjectName: test
 * @Package: com.zc.service
 * @ClassName: ${TYPE_NAME}
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
public class AuthService {
    public JsonObject doLogin(JsonObject param) throws TransactionException {
        return new JsonObject(ResultOb.build().setMsg("测试登录地址...").toString());
    }
}
