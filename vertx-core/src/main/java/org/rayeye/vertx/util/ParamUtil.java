package org.rayeye.vertx.util;

import io.vertx.core.MultiMap;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.apache.commons.lang3.StringUtils;
import org.rayeye.common.log.Log;
import org.rayeye.common.log.LogFactory;
import org.rayeye.util.html.HtmlFilter;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 参数 工具类
 * @ProjectName:
 * @Package:        org.rayeye.vertx.util
 * @ClassName:      ParamUtil
 * @Description:    参数 工具类
 * @Author:         Neil.Zhou
 * @CreateDate:     2017/9/20 19:12
 * @UpdateUser:     Neil.Zhou
 * @UpdateDate:     2017/9/20 19:12
 * @UpdateRemark:   The modified content
 * @Version:        1.0
 * <p>Copyright: Copyright (c) 2017/9/20</p>
 *
 */
public class ParamUtil {
    private static Log logger = LogFactory.get(ParamUtil.class);
    /**
     * 请求参数转换
     * @method      getRequestParams
     * @author      Neil.Zhou
     * @param paramMap 将vertx中的MultiMap 转成JsonObject
     * @return      io.vertx.core.json.JsonObject
     * @exception
     * @date        2017/9/20 19:13
     */
    private static JsonObject getRequestParams(MultiMap paramMap){
        JsonObject param=new JsonObject();
        if (paramMap!=null){
            Iterator iter = paramMap.entries().iterator();
            while(iter.hasNext()) {
                Map.Entry<String, Object> entry = (Map.Entry<String, Object>) iter.next();
                if(entry.getValue() instanceof String){
                    if (param.containsKey(entry.getKey())){//多值
                        if (!(param.getValue(entry.getKey()) instanceof JsonArray)) {
                            List<String> arry= (List<String>)paramMap.getAll((String) entry.getKey());
                            for(int i=0;i<arry.size();i++){
                                arry.set(i, HtmlFilter.transcoding(arry.get(i)));
                            }
                            param.put(entry.getKey(),arry);
                            //直接转
                            continue;
                        }
                    }else{
                        param.put(entry.getKey(), HtmlFilter.transcoding((String)entry.getValue()));
                    }
                }else{
                    param.put(entry.getKey(),entry.getValue());
                }
            }
        }
        return getParamPage(param);
    }

    /**
     * 请求参数转换
     * @method      getRequestParams
     * @author      Neil.Zhou
     * @param ctx 根据vertx-web的上下文来获取参数
     * @return      io.vertx.core.json.JsonObject
     * @exception
     * @date        2017/9/20 19:15
     */
    public static JsonObject getRequestParams(RoutingContext ctx){
        JsonObject params=new JsonObject();
        try{
            params=getRequestParams(ctx.request().params());
            //ip
            params.put("serverIp",ctx.request().localAddress().host());
            params.put("clientIp",ctx.request().remoteAddress().host());

            JsonObject param=ctx.getBodyAsJson();
            if(param!=null){
                logger.trace("参数来源body,为JSON....");
                Map<String,Object> paramMap=param.getMap();
                Iterator<String> iterator=paramMap.keySet().iterator();
                while (iterator.hasNext()){
                    String key=iterator.next();
                    if(paramMap.get(key) instanceof String){
                        params.put(key,HtmlFilter.transcoding((String)paramMap.get(key)));
                    }else if((paramMap.get(key) instanceof JsonArray)||(paramMap.get(key) instanceof List)){
                        JsonArray arry=new JsonArray();
                        if(paramMap.get(key) instanceof List){
                            arry=new JsonArray((List) paramMap.get(key));
                        }else{
                            arry=(JsonArray) paramMap.get(key);
                        }
                        params.put(key,new JsonArray(HtmlFilter.transcodingByOBJ(arry.encode())));
                    }else if((paramMap.get(key) instanceof JsonObject)||(paramMap.get(key) instanceof Map)){
                        JsonObject sub1=new JsonObject();
                        if(paramMap.get(key) instanceof JsonObject){
                            sub1=(JsonObject) paramMap.get(key);
                        }else{
                            sub1=new JsonObject((Map)paramMap.get(key));
                        }
                        params.put(key,new JsonObject(HtmlFilter.transcodingByOBJ(sub1.encode())));
                    }else{
                        params.put(key,paramMap.get(key));
                    }
                }
            }
        }catch (Exception e){
            logger.trace("请求body体中无参数!");
        }
        return params;
    }
    @Deprecated
    private static JsonObject getRequestParams(JsonObject jsonObject){
        JsonObject param=jsonObject;
        try{
            if(param!=null){
                logger.debug("参数来源body,为JSON....");
                Map<String,Object> paramMap=param.getMap();
                Iterator<String> iterator=paramMap.keySet().iterator();
                while (iterator.hasNext()){
                    String key=iterator.next();
                    if(paramMap.get(key) instanceof String){
                        param.put(key,HtmlFilter.transcoding((String)paramMap.get(key)));
                    }else if((paramMap.get(key) instanceof JsonArray)||(paramMap.get(key) instanceof List)){
                        JsonArray arry=new JsonArray();
                        if(paramMap.get(key) instanceof List){
                            arry=new JsonArray((List) paramMap.get(key));
                        }else{
                            arry=(JsonArray) paramMap.get(key);
                        }
                        param.put(key,new JsonArray(HtmlFilter.transcodingByOBJ(arry.encode())));
                        //param.put(key,arry);
                    }else if((paramMap.get(key) instanceof JsonObject)||(paramMap.get(key) instanceof Map)){
                        JsonObject sub1=new JsonObject();
                        if(paramMap.get(key) instanceof JsonObject){
                            sub1=(JsonObject) paramMap.get(key);
                        }else{
                            sub1=new JsonObject((Map)paramMap.get(key));
                        }
                        param.put(key,new JsonObject(HtmlFilter.transcodingByOBJ(sub1.encode())));
                    } else{
                        param.put(key,HtmlFilter.transcoding(paramMap.get(key).toString()));
                    }
                }
                //params=params.mergeIn(param);
            }
        }catch (Exception e){
            e.printStackTrace();
            logger.debug("参数来源body或query....");
        }
        return param;
    }

    /**
     * 根据http get请求，获取URL参数
     * @method      getQueryMap
     * @author      Neil.Zhou
     * @param query
     * @return      io.vertx.core.json.JsonObject
     * @exception
     * @date        2017/9/20 19:18
     */
    public static JsonObject getQueryMap(String query){
        String[] params = query.split("&");
        JsonObject map = new JsonObject();
        for (String param : params) {
            String name = param.split("=")[0];
            String value = "";
            try {
                value = URLDecoder.decode(param.split("=")[1], "UTF-8");
            } catch (Exception e) {
            }
            map.put(name, value);
        }
        return getParamPage(map);
    }

    /**
     * 默认处理分页
     * @method      getParamPage
     * @author      Neil.Zhou
     * @param params
     * @return      io.vertx.core.json.JsonObject
     * @exception
     * @date        2017/9/20 19:19
     */
    private static JsonObject getParamPage(JsonObject params){
        if(params!=null){
            if(!params.containsKey("limit")){
                params.put("limit",10) ;
            }else {
                if(params.getValue("limit") instanceof String){
                    if(StringUtils.isBlank(params.getString("limit"))){
                        params.put("limit",10) ;
                    }else {
                        params.put("limit",Integer.valueOf(params.getString("limit"))) ;
                    }
                }else if(params.getValue("limit") instanceof Number){
                    params.put("limit",Integer.valueOf(params.getValue("limit").toString())) ;
                }else{
                    params.put("limit",10) ;
                }
            }
            if(!params.containsKey("page")){
                params.put("page",1) ;
            }else {
                if(params.getValue("page") instanceof String){
                    if(StringUtils.isBlank(params.getString("page"))){
                        params.put("page",1) ;
                    }else {
                        params.put("page",Integer.valueOf(params.getString("page"))) ;
                    }
                }else if(params.getValue("page") instanceof Number){
                    params.put("page",Integer.valueOf(params.getValue("page").toString())) ;
                }else{
                    params.put("page",1) ;
                }
            }
        }
        return params;
    }

    public static void main(String[] arg){
        String ss = "0.87";
       // System.out.println(Integer.parseInt(ss));
        JsonObject ob=new JsonObject();
        ob.put("a","12");
        ob.put("a1","12s");
        ob.put("a2","12.34");
        //System.out.println(ob.getInteger("a",0));

        JsonObject params=new JsonObject("{\"html\":\"<script>alert('js嵌入');</script>dddd\",\"html1\":\"hdhdhd<a href='http://www.baidu.com'>点击</a>\"}");
        try{
            JsonObject param=new JsonObject("{\"key\":\"<script>alert('jddds嵌入');</script>dddd\",\"html1\":\"<a href='http://www.baidu.com'>点击</a>\"}");
            if(param!=null){
                logger.debug("参数来源body,为JSON....");
                params=params.mergeIn(param);
                params.put("list",new JsonArray(){{
                    add("<script>alert('list value 1');</script>");
                    add("<script>alert('list value 2');</script>");
                    add("<script>alert('list value 3');</script>");
                    add("<script>alert('list value 4');</script>");
                }});
                params.put("JsonObject",new JsonObject(){{
                    put("key1","<script>alert('JsonObject value 1');</script>");
                    put("key2","<script>alert('JsonObject value 2');</script>");
                    put("key3","<script>alert('JsonObject value 3');</script>");
                }});
                params.put("Map",new HashMap(){{
                    put("Map1","<script>alert('Map value 1');</script>");
                    put("Map2","<script>alert('Map value 2');</script>");
                    put("Map3","<script>alert('Map value 3');</script>");
                }});
                params.put("num1",12);
                params.put("num2",1.98);
                params.put("num3",87.00);
                params.put("num4",0.980);
            }
            System.out.println(getRequestParams(params));
        }catch (Exception e){
            logger.debug("参数来源body或query....");
        }
        //System.out.println(params.encode());
        /*try{
            Map<String,Object> paramMap=params.getMap();
            Iterator<String> iterator=paramMap.keySet().iterator();
           while (iterator.hasNext()){
               String key=iterator.next();
               if(paramMap.get(key) instanceof String){
                   params.put(key,HtmlFilter.transcoding((String)paramMap.get(key)));
               }else  if(paramMap.get(key) instanceof List){
                   List arry=(List) paramMap.get(key);
                   for(int i=0;i<arry.size();i++){
                       if(StringUtils.isNotBlank((String)arry.get(i))){
                           arry.set(i,HtmlFilter.transcoding((String)arry.get(i)));
                       }
                   }
                   params.put(key,arry);
               }else if((paramMap.get(key) instanceof JsonObject)||(paramMap.get(key) instanceof Map)){
                  JsonObject sub1=new JsonObject();
                   if(paramMap.get(key) instanceof JsonObject){
                       sub1=(JsonObject) paramMap.get(key);
                   }else{
                       sub1=new JsonObject((Map)paramMap.get(key));
                   }
                   params.put(key,new JsonObject(HtmlFilter.transcoding(sub1.encode())));
               }
           }
        }catch (Exception e){
            e.printStackTrace();
            logger.error("获取参数有误；{}");
        }*/
        String encodeStr=HtmlFilter.transcoding("http://192.168.10.9/ued/app-iot-ui/tree/master");
        System.out.println("encodeStr:"+encodeStr);
        System.out.println("decodeStr:"+HtmlFilter.restoreEscaped(encodeStr));

    }

}
