package com.zc.test.service;

import io.vertx.core.json.JsonObject;
import org.rayeye.util.exception.TransactionException;
import org.rayeye.vertx.annotations.RouteMethod;
import org.rayeye.vertx.annotations.ServiceMethod;
import org.rayeye.vertx.annotations.VerticleMapping;
import org.rayeye.vertx.result.ResultOb;
import org.rayeye.vertx.spring.AbstractService;
import org.springframework.stereotype.Service;

/**
 * 简单测试登录
 * （测试时建议分环境部署）
 *
 * @projectName: test
 * @package: com.zc.service
 * @className: Auth1Service
 * @description: Describes the function of the class
 * @author: Neil.Zhou
 * @createDate: 2017/9/21 11:38
 * @updateUser: Neil.Zhou
 * @updateDate: 2017/9/21 11:38
 * @updateRemark: The modified content
 * @version: 1.0
 * <p>Copyright: Copyright (c) 2017</p>
 */
@VerticleMapping("/")
@Service
public class AuthClusterService extends AbstractService{
    /**
     * 集群测试
     * @method      doLogin
     * @author      Neil.Zhou
     * @param param
     * @return      io.vertx.core.json.JsonObject
     * @exception   TransactionException
     * @date        2017/9/26 17:15
     */
    @ServiceMethod(value ="test1",method = RouteMethod.GET,descript="测试")
    public JsonObject doLogin(JsonObject param) throws TransactionException {
        return new JsonObject(ResultOb.build().setMsg("测试登录地址...").toString());
    }
}
