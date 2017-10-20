package org.rayeye.vertx.result;

import com.github.miemiedev.mybatis.paginator.domain.Paginator;
import io.vertx.core.json.Json;

/**
 *  response响应集合消息
 * @projectName:    vertx-core
 * @package:        org.rayeye.vertx.result
 * @className:      ResultList
 * @description:    响应集合消息
 * @author:         Neil.Zhou
 * @createDate:     2017/9/20 13:34
 * @updateUser:     Neil.Zhou
 * @updateDate:     2017/9/20 13:34
 * @updateRemark:   相应消息
 * @version:        1.0
 * <p>Copyright: Copyright (c) 2017/9/20</p>
 *
 */
public class ResultList<T> extends ResultOb{
    /** 共多少条 **/
    private int total;
    /** 每页多少条 **/
    private int count;
    /** 共多少页 **/
    private int pagetotal;
    /** 有下一页 **/
    private boolean hasNextPage;
    /** 有上一页 **/
    private boolean hasPrePage;
    /** 第几页 **/
    private int nopage;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getPagetotal() {
        return pagetotal;
    }

    public void setPagetotal(int pagetotal) {
        this.pagetotal = pagetotal;
    }

    public boolean isHasNextPage() {
        return hasNextPage;
    }

    public void setHasNextPage(boolean hasNextPage) {
        this.hasNextPage = hasNextPage;
    }

    public boolean isHasPrePage() {
        return hasPrePage;
    }

    public void setHasPrePage(boolean hasPrePage) {
        this.hasPrePage = hasPrePage;
    }

    public int getNopage() {
        return nopage;
    }

    public void setNopage(int nopage) {
        this.nopage = nopage;
    }

    public ResultList(Paginator p) {
        //共多少条
        total = p.getTotalCount();
        //每页多少条
        count = p.getLimit();
        //共多少页
        pagetotal = p.getTotalPages();
        //第几页
        nopage = p.getPage();
        //有下一页
        hasNextPage = p.isHasNextPage();
        //有上一页
        hasPrePage = p.isHasPrePage();
    }
    @Override
    public String toString(){
        return Json.encode(this);
    }
}
