package org.rayeye.vertx.standard;

import io.vertx.ext.web.Router;

/**
 * 获得全局的Router对象
 *
 * @ProjectName: vertx-core
 * @Package: org.rayeye.vertx.standard
 * @ClassName: SingleVertxRouter
 * @Description: 获得全局的Router对象
 * @Author: Neil.Zhou
 * @CreateDate: 2017/9/21 10:48
 * @UpdateUser: Neil.Zhou
 * @UpdateDate: 2017/9/21 10:48
 * @UpdateRemark: The modified content
 * @Version: 1.0
 * <p>Copyright: Copyright (c) 2017</p>
 */
public class SingleVertxRouter {
    private static Router router;
    private static SingleVertxRouter instance;
    public SingleVertxRouter() {
        router=Router.router(StandardVertxUtil.getStandardVertx());
    }
    public static SingleVertxRouter getInstance() {
        if (instance == null) {
            instance =new SingleVertxRouter();
        }
        return instance;
    }
    /**
     * 获得当前Router
     * @method      getRouter
     * @author      Neil.Zhou
     * @param
     * @return      io.vertx.ext.web.Router
     * @exception
     * @date        2017/9/21 10:54
     */
    public static Router getRouter(){
        if (instance == null) {
            instance =new SingleVertxRouter();
        }
        return router;
    }
}
