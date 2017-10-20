package org.rayeye.vertx.standard;

import io.vertx.ext.web.Router;

/**
 * 获得全局的Router对象
 *
 * @projectName: vertx-core
 * @package: org.rayeye.vertx.standard
 * @className: SingleVertxRouter
 * @description: 获得全局的Router对象
 * @author: Neil.Zhou
 * @createDate: 2017/9/21 10:48
 * @updateUser: Neil.Zhou
 * @updateDate: 2017/9/21 10:48
 * @updateRemark: The modified content
 * @version: 1.0
 * <p>Copyright: Copyright (c) 2017</p>
 */
public class SingleVertxRouter {
    private volatile Router router;
    private static SingleVertxRouter instance;
    public SingleVertxRouter() {
        this.router=Router.router(StandardVertxUtil.getStandardVertx());
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
        return instance.router;
    }
    public static Router setRouter(Router router){
        if (instance == null) {
            instance =new SingleVertxRouter();
        }
        instance.router=router;
        return router;
    }
}
