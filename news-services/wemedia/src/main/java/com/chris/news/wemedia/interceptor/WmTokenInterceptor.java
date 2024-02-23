package com.chris.news.wemedia.interceptor;

import com.chris.news.model.wemedia.pojo.WmUser;
import com.chris.news.utils.thread.WmThreadLocalUtil;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.HandlerInterceptor;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class WmTokenInterceptor implements HandlerInterceptor {
    /**
     * 得到header中的用户信息，并且存入到当前线程中
     */
    @Override
    public boolean preHandle(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {
        String userId = request.getHeader("userId");
        if(userId != null){
            //存入到当前线程中
            WmUser wmUser = new WmUser();
            wmUser.setId(Integer.valueOf(userId));
            WmThreadLocalUtil.setUser(wmUser);

        }
        return true;
    }

    /**
     * 清理线程中的数据
     */
    @Override
    public void afterCompletion(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler, Exception ex) {
        WmThreadLocalUtil.clear();
    }
}
