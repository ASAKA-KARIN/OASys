package cn.gson.oasys.controller.attendce;

import cn.gson.oasys.model.dao.attendcedao.CensusService;
import cn.gson.oasys.model.entity.attendce.Census;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

/**
 * @author 86156
 * Controller层
 */
@Controller
public class CensusController {
    private static final String FILE_PATH = "D:\\programming\\oasys\\oa_system\\tmp";
    @Autowired
    CensusService censusService;

    @RequestMapping("/census")
    public String census(@RequestParam("startTime") String startTime, @RequestParam("endTime") String endTime,
                         Model model, HttpServletRequest request) {
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

    @RequestMapping("/downloadExcel")
    public void downloadExcel(@RequestParam("startTime") String startTime, @RequestParam("endTime") String endTime,
                              HttpServletRequest request, HttpServletResponse resp) {
        startTime = startTime + " 0:00:00";
        //为了有数据
        //startTime = "2017-1-1" + " 0:00:00";
        endTime = endTime + " 23:59:59";
        resp.setContentType("application/x-download");
        resp.setCharacterEncoding("UTF-8");
        List<Census> censuses = censusService.getTop3EmpByDept(startTime, endTime);
        String templateFileName = getTemplateFile(0, request, resp);
        String fileName = FILE_PATH + "/" + System.currentTimeMillis() + Math.random() + ".xlsx";
        writeExcel(templateFileName, fileName, censuses);
        File file = new File(fileName);
        resp.addHeader("Content-Disposition", "attachment;filename=weekTable.xlsx");
        byte[] data = new byte[(int) file.length()];
        FileInputStream fin = null;
        ServletOutputStream respWriter = null;
        try {
            fin = new FileInputStream(file);
            fin.read(data);
            respWriter = resp.getOutputStream();
            respWriter.write(data);
            respWriter.flush();
            deleteFile();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fin.close();
                respWriter.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    /**
     * todo 动态模板
     *
     * @param flag    0 使用默认模板 1 使用动态模板
     * @param request
     * @param resp
     * @return
     */
    public String getTemplateFile(@RequestParam(name = "flag", defaultValue = "0") int flag, HttpServletRequest request, HttpServletResponse resp) {
        return "D:\\programming\\oasys\\oa_system\\template\\simpleTemp.xlsx";
    }

    public void writeExcel(String templateName, String fileName, Object data) {
        ExcelWriter excelWriter = EasyExcel.write(fileName).withTemplate(templateName).build();
        WriteSheet writeSheet = EasyExcel.writerSheet().build();
        excelWriter.fill(data, writeSheet);
        excelWriter.finish();
    }

    /**
     * 删除临时文件
     */
    public void deleteFile() {
        File baseFile = new File(FILE_PATH);
        File[] files = baseFile.listFiles();
        for (File file : files) {
            long now = System.currentTimeMillis();
            long modified = file.lastModified();
            if (now - modified >= 80) {
                file.delete();
            }
        }
    }


}
