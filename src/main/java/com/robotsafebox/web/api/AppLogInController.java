package com.robotsafebox.web.api;

import com.robotsafebox.base.json.JsonResult;
import com.robotsafebox.base.web.BaseAppController;
import com.robotsafebox.framework.properties.Constant;
import com.robotsafebox.framework.tools.ApiTokenTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/appInit")  // url:  /模块/资源/{id}细分
public class AppLogInController extends BaseAppController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

//    @Autowired
//    private SeckillService seckillService;

    @RequestMapping(value = "/init", method = RequestMethod.GET, produces = {Constant.CONTENT_TYPE_JSON})
    @ResponseBody
    public JsonResult init() {
        JsonResult jsonResult = new JsonResult();
        try {
            //todo：与登陆一起
            String token=ApiTokenTool.getToken("156");
            jsonResult.setData(token);
            jsonResult.setMessage("获取数据成功！");
            jsonResult.setStateSuccess();
        } catch (Exception ex) {
            ex.printStackTrace();
            jsonResult.setMessage(Constant.EXCEPTION_MESSAGE);
        }
        return jsonResult;
    }


}
