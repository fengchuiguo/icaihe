package com.robotsafebox.web.api;

import com.robotsafebox.base.json.JsonResult;
import com.robotsafebox.entity.Box;
import com.robotsafebox.entity.BoxMessage;
import com.robotsafebox.entity.HardwareReportLog;
import com.robotsafebox.entity.User;
import com.robotsafebox.framework.properties.Constant;
import com.robotsafebox.framework.push.jpush.JPushUtils;
import com.robotsafebox.framework.utils.DateUtil;
import com.robotsafebox.service.BoxMessageService;
import com.robotsafebox.service.BoxService;
import com.robotsafebox.service.HardwareReportLogService;
import com.robotsafebox.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;


@Controller
//@RequestMapping(Constant.API_HEAD_URL)  // url:  /模块/资源/{id}细分
public class reportController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private HardwareReportLogService hardwareReportLogService;

    @Resource
    private BoxMessageService boxMessageService;

    @Resource
    private BoxService boxService;

    @Resource
    private UserService userService;

    @RequestMapping(value = "/report", method = RequestMethod.POST, produces = {Constant.CONTENT_TYPE_JSON})
    @ResponseBody
    public JsonResult report(@RequestParam("ICHID") String ichid, @RequestParam("action") int actiontype) {
        JsonResult jsonResult = new JsonResult();
        try {
            HardwareReportLog hardwareReportLog = new HardwareReportLog();
            hardwareReportLog.setIchId(ichid);
            hardwareReportLog.setActionType(actiontype);
            hardwareReportLog.setCreateTime(new Date());
            int result = hardwareReportLogService.saveHardwareReportLog(hardwareReportLog);
            if (result == 1) {
                jsonResult.setData(null);
                jsonResult.setMessage("保存成功！");
                jsonResult.setStateSuccess();

                //推送消息（说明：actiontype == 1 WIFI配置成功 此处不推送，改到 添加财盒 成功后推送）
                if (actiontype == 2 || actiontype == 3 || actiontype == 4 || actiontype == 5) {
                    Box box = boxService.getBoxByIchId(ichid);
                    if (box != null) {
                        User user = userService.getCreateUserByBoxId(box.getId());
                        if (user != null) {
                            String alertContent = JPushUtils.getAlertContent(box.getBoxName(), actiontype);
                            if (alertContent != null) {
                                Boolean pushOK = JPushUtils.sendPush(user.getId(), alertContent, actiontype);
                                if (pushOK) {
                                    BoxMessage boxMessage = new BoxMessage();
                                    boxMessage.setBoxId(box.getId());
                                    boxMessage.setUserId(user.getId());
                                    boxMessage.setType((byte) actiontype);
                                    boxMessage.setMessage(alertContent);
                                    boxMessage.setCreateTime(DateUtil.getCurrentDateTime());
                                    boxMessageService.saveBoxMessage(boxMessage);
                                }
                            }
                        }
                    }
                }

            }
        } catch (Exception ex) {
            jsonResult.setMessage(Constant.EXCEPTION_MESSAGE);
            logger.error(ex.getMessage(), ex);
        }
        return jsonResult;
    }

    @RequestMapping(value = "/report/list", method = RequestMethod.GET)
    public String list(Model model) {
        List<HardwareReportLog> logList = hardwareReportLogService.getHardwareReportLogList();
        model.addAttribute("list", logList);
        return "reportLog/reportLog";  /*   /WEB-INF/jsp/reportLog/"reportLog".jsp  */
    }


}
