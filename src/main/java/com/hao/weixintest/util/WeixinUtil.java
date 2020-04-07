package com.hao.weixintest.util;

import com.alibaba.fastjson.JSONObject;
import com.hao.weixintest.vo.TemplateData;
import com.hao.weixintest.vo.WechatTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class WeixinUtil {

    private static final Logger log = LoggerFactory.getLogger(WeixinUtil.class);

    //模板id  内容为{{first.DATA}} 商品：{{keynote1.DATA}} 价格：{{keynote2.DATA}} 日期：{{keynote3.DATA}} {{remark.DATA}}
    public static String template_id = "6OfgzlGopnBaL4essWouHOCx_-hAAGKMnbudZoZsz-8";

    //用户id   微信号
    public static String opent_id = "oOlxiwVRByQQ6bxE-c9-ltQAy0EQ";

    //每个公众号都有的信息
    public static String appId = "wx4b338edda89d50fb";

    //每个公众号都有的信息
    public static String appsecret = "920e69beb66b3aef2bd650f185ab4553";

    /**
     * 获取微信公众号授权凭证(获取 access_token ) 这个必须要有，调用接口都要用到
     *
     * @param appId 公众账号的唯一标识
     * @param appSecret 公众账号的密钥
     */
    public static String getSNSToken(String appId, String appSecret) {


        String requestUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=SECRET";
        requestUrl = requestUrl.replace("APPID", appId);
        requestUrl = requestUrl.replace("SECRET", appSecret);
        Map<String,String> headers=new HashMap<>();
        String responsewechart = HttpUtil.sendGet(requestUrl, headers, "UTF-8");

        JSONObject jsonObject = JSONObject.parseObject(responsewechart);

        String access_token = jsonObject.getString("access_token");
        Integer expires_in = jsonObject.getInteger("expires_in");

        return access_token;
    }

    /**
     *  * 发送模板消息
     * appId 公众账号的唯一标识
     * appSecret 公众账号的密钥
     * openId 用户标识
     */
    public static String  send_template_message() {
        //需要用到  access_token
        String access_token = getSNSToken(appId,appsecret);

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

        wechat.setTemplate_id(template_id);//模板id

        wechat.setTouser(opent_id);//用户id （发给谁）

        wechat.setUrl("liguanghao.top/wx/runUrl");//点击消息挑战到哪个页面  可不传

        String jsonString = JSONObject.toJSONString(wechat);

        //JSONObject jsonObject = httpRequest(url, "POST", jsonString);

        log.info("请求前参数："+jsonString+",url:"+url);
        Map<String,String> headers=new HashMap<>();
        String s = HttpUtil.sendPost(url, jsonString, headers, "utf-8");

        log.info("send_template_message:"+s);

        return s;


    }


            /*TEXT,//文本消息
            IMAGE,//图片消息
            VOICE,//语音消息
            VIDEO,//视频消息
            SHORTVIDEO,//小视频消息
            LOCATION,//地理位置消息
            LINK,//链接消息
            EVENT;//事件消息*/


    /**
     * 根据消息类型构造返回消息
     * @param map 封装了解析结果的Map
     * @return responseMessage(响应消息)
     */
    public static String buildResponseMessage(Map map) {
        //响应消息
        String responseMessage = "";
        //得到消息类型
        String msgType = map.get("MsgType").toString();
        log.info("MsgType:" + msgType);
        //消息类型
        if("TEXT".equals(msgType)) {
            //处理文本消息
            responseMessage = MessageHandlerUtil.handleTextMessage(map);
        }else if("IMAGE".equals(msgType)) {
            //处理图片消息
            responseMessage = MessageHandlerUtil.handleImageMessage(map);
        }else if("VOICE".equals(msgType)){
            //处理语音消息
            responseMessage = MessageHandlerUtil.handleVoiceMessage(map);
        }else if("VIDEO".equals(msgType)){
            //处理视频消息
            responseMessage = MessageHandlerUtil.handleVideoMessage(map);
        }else if("SHORTVIDEO".equals(msgType)){
            //处理小视频消息
            responseMessage = MessageHandlerUtil.handleSmallVideoMessage(map);
        }else if("LOCATION".equals(msgType)){
            //处理位置消息
            responseMessage = MessageHandlerUtil.handleLocationMessage(map);
        }else if("LINK".equals(msgType)){
            //处理链接消息
            responseMessage = MessageHandlerUtil.handleLinkMessage(map);
        }else if("EVENT".equals(msgType)){
            ////处理事件消息,用户在关注与取消关注公众号时，微信会向我们的公众号服务器发送事件消息,开发者接收到事件消息后就可以给用户下发欢迎消息
            responseMessage = MessageHandlerUtil.handleEventMessage(map);
        }
        //返回响应消息
        return responseMessage;
    }

























}

