package com.hao.weixintest.service;


import com.alibaba.fastjson.JSONObject;
import com.hao.weixintest.util.HttpUtil;
import com.hao.weixintest.util.WeixinUtil;
import com.hao.weixintest.vo.TemplateData;
import com.hao.weixintest.vo.WechatTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class weixinMessageService {

    private static final Logger log = LoggerFactory.getLogger(weixinMessageService.class);


    /**
     *  * 发送模板消息
     * appId 公众账号的唯一标识
     * appSecret 公众账号的密钥
     * openId 用户标识
     */
    public  String  sendTemplateMessage() {


        //需要用到  access_token
        String access_token = WeixinUtil.getSNSToken(WeixinUtil.appId,WeixinUtil.appsecret);

        String url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=" + access_token;

        TemplateData first = new TemplateData();
        TemplateData keynote1 = new TemplateData();
        TemplateData keynote2 = new TemplateData();
        TemplateData keynote3 = new TemplateData();
        TemplateData remark = new TemplateData();

        Map<String, TemplateData> data = new HashMap<String, TemplateData>();

        first.setValue("恭喜你购买成功！");
        first.setColor("#173177");
        data.put("first",first);

        keynote1.setValue("巧克力");
        keynote1.setColor("#173177");
        data.put("keynote1",keynote1);

        keynote2.setValue("39.8");
        keynote2.setColor("#173177");
        data.put("keynote2",keynote2);

        keynote3.setValue("2014年9月22日");
        keynote3.setColor("#173177");
        data.put("keynote3",keynote3);

        remark.setValue("请及时处理您的订单");
        remark.setColor("#173177");
        data.put("remark",remark);

        WechatTemplate wechat = new WechatTemplate();

        wechat.setData(data);//模板消息内容

        wechat.setTemplate_id(WeixinUtil.template_id);//模板id

        wechat.setTouser(WeixinUtil.opent_id);//用户id （发给谁）

        wechat.setUrl("liguanghao.top/wx/runUrl");//点击消息挑战到哪个页面  可不传

        String jsonString = JSONObject.toJSONString(wechat);

        //JSONObject jsonObject = httpRequest(url, "POST", jsonString);

        log.info("请求前参数："+jsonString+",url:"+url);
        Map<String,String> headers=new HashMap<>();
        String s = HttpUtil.sendPost(url, jsonString, headers, "utf-8");

        log.info("send_template_message:"+s);

        return s;


    }







}
