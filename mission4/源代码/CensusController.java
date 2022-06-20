package cn.gson.oasys.controller.attendce;

import cn.gson.oasys.model.dao.attendcedao.AttendceDao;
import cn.gson.oasys.model.dao.attendcedao.CensusService;
import cn.gson.oasys.model.entity.attendce.Attends;
import cn.gson.oasys.model.entity.attendce.Census;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.write.metadata.WriteSheet;
import javassist.*;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.AttributeInfo;
import javassist.bytecode.ConstPool;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.ArrayMemberValue;
import javassist.bytecode.annotation.StringMemberValue;
import net.bytebuddy.agent.ByteBuddyAgent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.PostConstruct;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.security.ProtectionDomain;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author 86156
 * Controller层
 */
@Controller
public class CensusController {
    private static final Map<String,String> filedMap = new HashMap<>(10);

    private static final String FILE_PATH = "D:\\programming\\oasys\\oa_system\\tmp";
    @Autowired
    CensusService censusService;

    @Autowired
    AttendceDao attendceDao;

    private Date dynamicStartTime;
    private Date dynamicEndTime;

    private Lock lock = new ReentrantLock();

    @PostConstruct
    void init(){
        filedMap.put("attendsTime","考勤时间");
        filedMap.put("attendHmtime","考勤时分");
        filedMap.put("attendsIp","登录IP");
        filedMap.put("weekOfday","星期");
        filedMap.put("attendsRemark","考勤备注");
    }

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

    @GetMapping("/toDynamicTable")
    public String toDynamicTable(@RequestParam("startTime") String startTime, @RequestParam("endTime") String endTime) {
        //startTime = startTime + " 0:00:00";
        startTime = "2017-1-1" + " 00:00:00";
        endTime = endTime + " 23:59:59";
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            dynamicStartTime =dateFormat.parse(startTime);
            dynamicEndTime = dateFormat.parse(endTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "attendce/customerCensusTable";
    }


    /**
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

    /**
     * 直接对类结构进行操作，需要加锁
     *
     * @param request
     * @return
     */
    @RequestMapping("/dynamicTable")
    public void createTempTable(HttpServletRequest request, HttpServletResponse resp) {
        Set<String> fieldName = new HashSet<>();
        Enumeration<String> names = request.getParameterNames();
        if(!names.hasMoreElements()){
            return;
        }
        for (int i = 1; i < Integer.MAX_VALUE; i++) {
            String field = request.getParameter(i + "");
            if (field == null) {
                break;
            }
            fieldName.add(field);
        }
        resp.setContentType("application/x-download");
        resp.setCharacterEncoding("UTF-8");
        resp.addHeader("Content-Disposition", "attachment;filename=weekTable.xlsx");
        //先加锁
        lock.lock();
        try {
            addAnnotation(fieldName, Attends.class);
            List<Attends> data = attendceDao.findByDate(dynamicStartTime, dynamicEndTime);
            String fileName = FILE_PATH + "/" + System.currentTimeMillis() + Math.random() + ".xlsx";
            EasyExcel.write(fileName, Attends.class).sheet().doWrite(data);
            File file = new File(fileName);
            FileInputStream fin = null;
            ServletOutputStream respWriter = null;
            byte[] length = new byte[(int) file.length()];
            try {
                fin = new FileInputStream(fileName);
                fin.read(length);
                respWriter = resp.getOutputStream();
                respWriter.write(length);
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
        } finally {
            lock.unlock();
        }
    }

    private void addAnnotation(Set<String> fields, Class<?> clazz) {

        byte[] update = null;
        //需要忽略的字段
        List<String> ignoreField = new ArrayList<>();
        ClassPool classPool = ClassPool.getDefault();
        for(String key : filedMap.keySet()){
            if(!fields.contains(key)){
                ignoreField.add(key);
            }
        }

        try {
            CtClass ctClass = classPool.getCtClass(clazz.getName());
            //转化过后会将类冻结，需要手动解冻
            ctClass.defrost();
            ConstPool constPool = ctClass.getClassFile().getConstPool();
            //为不在集合中的字段加上@ExcelIgnore注解
            for (String field : fields) {
                CtField ctField = ctClass.getDeclaredField(field);
                AnnotationsAttribute annotationsAttribute = getAnnotationsAttribute(ctField);
                if (annotationsAttribute == null) {
                    annotationsAttribute = new AnnotationsAttribute(constPool, AnnotationsAttribute.visibleTag);
                }
                for(Annotation annotation:annotationsAttribute.getAnnotations()){
                    if(annotation.getTypeName().equals("com.alibaba.excel.annotation.ExcelIgnore")){
                        annotationsAttribute.removeAnnotation(annotation.getTypeName());
                    }
                }
                Annotation annotation = new Annotation(ExcelProperty.class.getName(), constPool);
                ArrayMemberValue arrayMemberValue = new ArrayMemberValue(constPool);
                arrayMemberValue.setValue(new StringMemberValue[]{new StringMemberValue(filedMap.get(field), constPool)});
                annotation.addMemberValue("value",arrayMemberValue);
                annotationsAttribute.addAnnotation(annotation);
            }
            //为需要忽略的字段加上@ExcelIgnore注解
            for(String ignore : ignoreField){
                CtField ctField = ctClass.getDeclaredField(ignore);
                AnnotationsAttribute annotationsAttribute = getAnnotationsAttribute(ctField);
                if(annotationsAttribute != null){
                    Annotation[] annotations = annotationsAttribute.getAnnotations();
                    for(Annotation annotation : annotations){
                        if(annotation.getTypeName().equals("com.alibaba.excel.annotation.ExcelProperty")){
                            annotationsAttribute.removeAnnotation(annotation.getTypeName());
                        }
                    }
                }
                annotationsAttribute.addAnnotation(new Annotation(ExcelIgnore.class.getName(),constPool));
            }
            //得到新的类结构
            update = ctClass.toBytecode();
            ctClass.defrost();
        } catch (NotFoundException e) {
            e.printStackTrace();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        } catch (CannotCompileException e) {
            e.printStackTrace();
        }
        //更新类结构
        reloadClass(clazz, update);

    }

    private AnnotationsAttribute getAnnotationsAttribute(CtField ctField) {
        List<AttributeInfo> attrs = ctField.getFieldInfo().getAttributes();
        AnnotationsAttribute attr = null;
        if (attrs != null) {
            Optional<AttributeInfo> optional = attrs.stream()
                    .filter(AnnotationsAttribute.class::isInstance)
                    .findFirst();
            if (optional.isPresent()) {
                attr = (AnnotationsAttribute) optional.get();
            }
        }
        return attr;
    }

    private void reloadClass(Class<?> clazz, byte[] byteCode) {
        Instrumentation instrumentation = ByteBuddyAgent.install();
        ClassFileTransformer classFileTransformer = new ClassFileTransformer() {
            @Override
            public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
                return byteCode;
            }
        };
        instrumentation.addTransformer(classFileTransformer, true);
        try {
            instrumentation.retransformClasses(clazz);
        } catch (UnmodifiableClassException e) {
            e.printStackTrace();
        } finally {
            instrumentation.removeTransformer(classFileTransformer);
        }
    }

}
