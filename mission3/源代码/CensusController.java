package cn.gson.oasys.controller.attendce;

import cn.gson.oasys.model.dao.attendcedao.CensusService;
import cn.gson.oasys.model.entity.attendce.Census;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author 86156
 * Controller层
 */
@Controller
public class CensusController {

    @Autowired
    CensusService censusService;

    @RequestMapping("/census")
    public String census(@RequestParam("startTime") String startTime, @RequestParam("endTime") String endTime,
                        ,HttpServletRequest request) {
        startTime = startTime + " 0:00:00";
        //为了能查询出数据
        //startTime = "2017-1-1" + " 0:00:00";
        endTime = endTime + " 23:59:59";
        List<Census> emps = censusService.getTop3EmpByDept(startTime, endTime);
        request.setAttribute("emps", emps);
        request.setAttribute("url", "censusTable");
        request.setAttribute("test", "test");
        return "attendce/censusTable";

    }

}
