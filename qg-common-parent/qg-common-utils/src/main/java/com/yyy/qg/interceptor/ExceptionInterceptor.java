package com.yyy.qg.interceptor;


import com.yyy.qg.utils.EmptyUtils;
import com.yyy.qg.utils.PrintUtil;
import com.alibaba.fastjson.JSONObject;
import com.yyy.qg.dto.ReturnResult;
import com.yyy.qg.dto.ReturnResultUtils;
import com.yyy.qg.dto.exception.CommonException;
import com.yyy.qg.utils.YYY;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/***
 * 异常统一处理的拦截器
 */
public class ExceptionInterceptor implements HandlerInterceptor {



    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        httpServletResponse.setHeader("Access-Control-Allow-Origin", "*");
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }
    /***
     * @param request
     * @param response
     * @param o
     * @param e
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object o, Exception e) throws Exception {
        //出现异常进行处理
        if(EmptyUtils.isNotEmpty(e)){
            YYY.print("异常信息>>>>>>>>>"+e.getMessage());
            YYY.print("堆栈跟踪>>>>>>>>>"+ e.getStackTrace());
            YYY.print(">>>>>"+e.getCause());
            ModelAndView m = new ModelAndView();
            m.addObject("error",e);
            m.setViewName("error");
            response.setContentType("text/html;charset=UTF-8");
            PrintUtil printUtil=new PrintUtil(response);
            ReturnResult returnResult= ReturnResultUtils.returnFail(CommonException.SYSTEM_EXCEPTION.getCode(),CommonException.SYSTEM_EXCEPTION.getMessage());
            printUtil.print(JSONObject.toJSON(returnResult));
        }
    }
}
