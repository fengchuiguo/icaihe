package com.robotsafebox.web.api;

import com.robotsafebox.base.json.JsonResult;
import com.robotsafebox.entity.HardwareReportLog;
import com.robotsafebox.framework.properties.Constant;
import com.robotsafebox.service.HardwareReportLogService;
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