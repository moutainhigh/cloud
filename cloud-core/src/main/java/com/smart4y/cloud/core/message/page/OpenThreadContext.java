package com.smart4y.cloud.core.message.page;

/**
 * 本地线程数据
 *
 * @author Youtao
 *         Created by youtao on 2019-06-04.
 */
public class OpenThreadContext {

    /**
     * 当前第几页
     */
    private static ThreadLocal<Integer> currentPage = new ThreadLocal<>();
    /**
     * 每页显示条数
     */
    private static ThreadLocal<Integer> pageSize = new ThreadLocal<>();

    public static int getPage() {
        Integer cp = currentPage.get();
        if (cp == null) {
            return 0;
        }
        return cp;
    }

    public static int getLimit() {
        Integer ps = pageSize.get();
        if (ps == null) {
            return 0;
        }
        return ps;
    }

    public static void setPageLimit(int page, int limit) {
        currentPage.set(page > 0 ? page : 1);
        pageSize.set(limit < 0 ? 10 : limit);
    }

    public static void removePageLimit() {
        currentPage.remove();
        pageSize.remove();
    }
}