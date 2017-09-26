package org.rayeye.vertx.address;

import java.util.Objects;

/**
 * 事件格式类
 * @ProjectName:
 * @Package:        org.rayeye.vertx.base
 * @ClassName:      EventBusAddress
 * @Description:    Describes the function of the class
 * @Author:         Neil.Zhou
 * @CreateDate:     2017/9/20 18:50
 * @UpdateUser:     Neil.Zhou
 * @UpdateDate:     2017/9/20 18:50
 * @UpdateRemark:   The modified content
 * @Version:        1.0
 * <p>Copyright: Copyright (c) 2017/9/20</p>
 *
 */
public class EventBusAddress {
    /**
     * 地址正向编,解决集群中“/”的问题,默认是以点的形式
     * @method      positiveFormate
     * @author      Neil.Zhou
     * @version
     * @param clazz
     * @return      java.lang.String
     * @exception
     * @date        2017/9/20 19:07
     */
    public static String positiveFormate(Class<?> clazz){
        Objects.requireNonNull(clazz, "clazz must not be null.");
        return clazz.getName();
    }
    /**
     * 地址正向编,解决集群中“/”的问题,默认是以点的形式
     * (适用于集群，将自定义的router api中“/” 转换成“.”)
     * @method      positiveFormate
     * @author      Neil.Zhou
     * @param address
     * @return      java.lang.String
     * @exception
     * @date        2017/9/20 19:07
     */
    public static String positiveFormate(String address){
        Objects.requireNonNull(address, "address must not be null.");
        if(address.startsWith("/")){
            address=address.substring(1);
        }
        return address.trim().replace("/",".");
    }
    /**
     * 反向解析,还原地址
     * 适用于根据api router去寻址对应的class
     * @method      negativeFormate
     * @author      Neil.Zhou
     * @param address
     * @return      java.lang.String
     * @exception
     * @date        2017/9/20 19:10
     */
    public static String negativeFormate(String address){
        Objects.requireNonNull(address, "address must not be null.");
        return address.trim().replace(".","/");
    }
    /**
     * 将类名首字母转成小写
     * @method      toLowerCaseFirstOne
     * @author      Neil.Zhou
     * @param s
     * @return      java.lang.String
     * @exception
     * @date        2017/9/26 15:01
     */
    public static String toLowerCaseFirstOne(String className){
        if(Character.isLowerCase(className.charAt(0))) {
            return className;
        }else {
            return (new StringBuilder()).append(Character.toLowerCase(className.charAt(0))).append(className.substring(1)).toString();
        }
    }
}
