package com.hao.weixintest.controller;

import com.hao.weixintest.service.weixinMessageService;
import com.hao.weixintest.util.CheckUtil;
import com.hao.weixintest.util.WeixinUtil;
import com.hao.weixintest.util.XmlUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Controller
@RequestMapping("/wx")
public class WeixinController {

    private static final Logger log = LoggerFactory.getLogger(WeixinController.class);


    @Autowired
    weixinMessageService weixinMessageService;


    @ResponseBody
    @RequestMapping("/runUrl")
    public String runUrl(){

        log.info("runUrl");
        return "hello weixin";

    }

    /**
     * 打开开发者模式签名认证
     * @param signature
     * @param timestamp
     * @param nonce
     * @param echostr
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/service", method = RequestMethod.GET)
    public Object defaultView(String signature, String timestamp, String nonce, String echostr) {

        log.info("service");
        log.info("signature:"+signature+",timestamp:"+timestamp+",nonce:"+nonce+",echostr:"+echostr);


        if (echostr == null || echostr.isEmpty()) {
            return nonce;
        }
        if (CheckUtil.checkSignature(signature, timestamp, nonce)) {
            log.info("echostr方法返回："+echostr);
            return echostr;
        }

        log.info("service方法返回："+nonce);
        return nonce;
    }

    /**
     * 接收、处理、响应由微信服务器转发的用户发送给公众帐号的消息
     * @param request
     * @param response
     */
    @RequestMapping(value = "/service", method = RequestMethod.POST)
    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        log.info("service--post");
        // 将请求、响应的编码均设置为UTF-8（防止中文乱码）
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        String responseMessage;

        try {
            //解析微信发来的请求,将解析后的结果封装成Map返回
            Map<String,String> map = XmlUtil.parseXml(request);
            log.info("开始构造响应消息");
            responseMessage = WeixinUtil.buildResponseMessage(map);
            log.info("responseMessage:"+responseMessage);
            if(responseMessage.equals("")){
                responseMessage ="未正确响应";
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("发生异常："+ e.getMessage());
            responseMessage ="未正确响应";
        }
        //发送响应消息
        response.getWriter().println(responseMessage);
    }



    /**
     * 发送模板消息
     * @return
     */
    @RequestMapping("/test")
    @ResponseBody
    public String testDemo() {


        String s = weixinMessageService.sendTemplateMessage();

        return s;

    }


}
