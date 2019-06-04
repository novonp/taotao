package com.taotao.search.exception;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 全局异常处理
 */
public class GlobalExceptionReslover implements HandlerExceptionResolver {

    Logger logger = LoggerFactory.getLogger(GlobalExceptionReslover.class);

    @Override
    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                         Object o, Exception e) {
    logger.error("系统错误",e);
    ModelAndView result = new ModelAndView();
    result.addObject("message","系统错误，请稍后在试！");
    result.setViewName("error/exception");
    return result;
    }
}
