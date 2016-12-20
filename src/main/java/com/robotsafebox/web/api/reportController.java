package com.robotsafebox.web.api;

import com.robotsafebox.base.json.JsonResult;
import com.robotsafebox.entity.*;
import com.robotsafebox.framework.properties.Constant;
import com.robotsafebox.framework.push.jpush.JPushUtils;
import com.robotsafebox.framework.utils.DateUtil;
import com.robotsafebox.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    protected BoxRecordService boxRecordService;

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

                            //财盒创建人的动态中需要显示  报警 和 电量不足
                            if (actiontype == 2 || actiontype == 3) {
                                BoxRecord boxRecord = new BoxRecord();
                                boxRecord.setBoxId(box.getId());
                                boxRecord.setUserId(user.getId());
                                boxRecord.setType((byte) (actiontype == 2 ? 7 : 8));
                                boxRecord.setCreateTime(DateUtil.getCurrentDateTime());
                                boxRecord.setRemark(alertContent);
                                boxRecordService.saveBoxRecord(boxRecord);

                                //需要记录 未读报警记录条数（包含 报警 和 电量不足）
                                user.setAlarmNum((user.getAlarmNum() == null ? 0 : user.getAlarmNum()) + 1);
                                userService.saveUser(user);

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

    @RequestMapping(value = "/helpCenter", method = RequestMethod.GET)
    public String helpCenter(Model model) {
        return "public/helpCenter";
    }

    @RequestMapping(value = "/aboutICH", method = RequestMethod.GET)
    public String aboutICH(Model model) {
        return "public/aboutICH";
    }


}
