package cn.gson.oasys.controller.daymanage;

import cn.gson.oasys.model.dao.daymanagedao.DaymanageDao;
import cn.gson.oasys.model.dao.system.StatusDao;
import cn.gson.oasys.model.dao.system.TypeDao;
import cn.gson.oasys.model.dao.user.UserDao;
import cn.gson.oasys.model.entity.schedule.ScheduleList;
import cn.gson.oasys.model.entity.system.SystemStatusList;
import cn.gson.oasys.model.entity.system.SystemTypeList;
import cn.gson.oasys.model.entity.user.User;
import cn.gson.oasys.services.daymanage.DaymanageServices;
import cn.gson.oasys.services.process.ProcessService;
import com.alibaba.fastjson.JSONObject;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.cp.api.impl.WxCpMessageServiceImpl;
import me.chanjar.weixin.cp.api.impl.WxCpServiceImpl;
import me.chanjar.weixin.cp.bean.message.WxCpMessage;
import me.chanjar.weixin.cp.bean.message.WxCpMessageSendResult;
import me.chanjar.weixin.cp.config.impl.WxCpDefaultConfigImpl;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

@Controller
@RequestMapping("/")
public class DaymanageController {

    private static final String GET_TOKEN_URL = "https://qyapi.weixin.qq.com/cgi-bin/gettoken";

    @Autowired
    DaymanageDao daydao;
    @Autowired
    UserDao udao;
    @Autowired
    DaymanageServices dayser;
    @Autowired
    StatusDao statusdao;
    @Autowired
    TypeDao typedao;
    @Autowired
    ProcessService ps;

    @Value("${wechat.corpId}")
    String corpId;
    @Value("${wechat.secret}")
    String secret;
    @Value("${wechat.agentId}")
    Integer agentId;

    WxCpDefaultConfigImpl config;

    WxCpServiceImpl wxCpService;


    WxCpMessageServiceImpl service;

    String accessToken;

    @PostConstruct
    public void init() throws IOException {
        getAccessToken();
        config = new WxCpDefaultConfigImpl();
        config.setCorpId(corpId);
        config.setCorpSecret(secret);
        config.setAgentId(agentId);
        config.setAccessToken(accessToken);
        //WxCpService会自己刷新token
        wxCpService = new WxCpServiceImpl();
        wxCpService.setWxCpConfigStorage(config);
        service = new WxCpMessageServiceImpl(wxCpService);
    }

    void getAccessToken() throws IOException {
        String url = GET_TOKEN_URL + "?corpid=" + corpId + "&corpsecret=" + secret;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .get()
                .url(url)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonStr = response.body().string();
                JSONObject jsonObject = JSONObject.parseObject(jsonStr);
                Integer errcode = jsonObject.getInteger("errcode");
                if (errcode == 0) {
                    accessToken = jsonObject.getString("access_token");
                } else {
                    System.err.println(jsonObject.getString("errmsg"));
                }
            }
        });
    }


    @RequestMapping("daymanage")
    private String daymanage(@SessionAttribute("userId") Long userid,
                             Model model, @RequestParam(value = "page", defaultValue = "0") int page,
                             @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        List<SystemTypeList> types = typedao.findByTypeModel("aoa_schedule_list");
        List<SystemStatusList> statuses = statusdao.findByStatusModel("aoa_schedule_list");
        List<Order> orders = new ArrayList<>();
        orders.add(new Order(Direction.DESC, "statusId"));
        orders.add(new Order(Direction.DESC, "createTime"));
        Sort sort = new Sort(orders);
        Pageable pa = new PageRequest(page, size, sort);
        User user = udao.findOne(userid);
        Page<ScheduleList> myday = daydao.findByUser(user, pa);

        List<ScheduleList> scheduleLists = myday.getContent();

        model.addAttribute("schedules", scheduleLists);
        model.addAttribute("types", types);
        model.addAttribute("statuses", statuses);
        model.addAttribute("page", myday);
        model.addAttribute("url", "daymanagepaging");
        model.addAttribute("ismyday", 1);
        return "daymanage/daymanage";
    }

    @RequestMapping("daymanagepaging")
    private String daymanagepaging(@SessionAttribute("userId") Long userid,
                                   Model model, @RequestParam(value = "page", defaultValue = "0") int page,
                                   @RequestParam(value = "size", defaultValue = "10") int size
    ) {

        List<SystemTypeList> types = typedao.findByTypeModel("aoa_schedule_list");
        List<SystemStatusList> statuses = statusdao.findByStatusModel("aoa_schedule_list");

        Sort sort = new Sort(new Order(Direction.ASC, "user"));
        Pageable pa = new PageRequest(page, size, sort);
        User user = udao.findOne(userid);
        Page<ScheduleList> myday = daydao.findByUser(user, pa);

        List<ScheduleList> scheduleLists = myday.getContent();
        model.addAttribute("types", types);
        model.addAttribute("statuses", statuses);
        model.addAttribute("schedules", scheduleLists);
        model.addAttribute("page", myday);
        model.addAttribute("url", "daymanagepaging");
        model.addAttribute("ismyday", 1);
        return "daymanage/daymanagepaging";
    }

    @RequestMapping("aboutmeday")
    private String aboutmeday(@SessionAttribute("userId") Long userid,
                              Model model, @RequestParam(value = "page", defaultValue = "0") int page,
                              @RequestParam(value = "size", defaultValue = "10") int size
    ) {

        List<SystemTypeList> types = typedao.findByTypeModel("aoa_schedule_list");
        List<SystemStatusList> statuses = statusdao.findByStatusModel("aoa_schedule_list");

        Sort sort = new Sort(new Order(Direction.ASC, "user"));
        Pageable pa = new PageRequest(page, size, sort);
        User user = udao.findOne(userid);
        List<User> users = new ArrayList<>();
        users.add(user);
        Page<ScheduleList> aboutmeday = daydao.findByUsers(users, pa);

        List<ScheduleList> scheduleLists = aboutmeday.getContent();

        model.addAttribute("schedules", scheduleLists);
        model.addAttribute("types", types);
        model.addAttribute("statuses", statuses);
        model.addAttribute("page", aboutmeday);
        model.addAttribute("url", "aboutmedaypaging");

        return "daymanage/daymanage";
    }

    @RequestMapping("aboutmedaypaging")
    public String aboutmedaypaging(@SessionAttribute("userId") Long userid,
                                   Model model, @RequestParam(value = "page", defaultValue = "0") int page,
                                   @RequestParam(value = "size", defaultValue = "10") int size
    ) {

        List<SystemTypeList> types = typedao.findByTypeModel("aoa_schedule_list");
        List<SystemStatusList> statuses = statusdao.findByStatusModel("aoa_schedule_list");

        Sort sort = new Sort(new Order(Direction.ASC, "user"));
        Pageable pa = new PageRequest(page, size, sort);
        User user = udao.findOne(userid);
        List<User> users = new ArrayList<>();
        users.add(user);
        Page<ScheduleList> aboutmeday = daydao.findByUsers(users, pa);

        List<ScheduleList> scheduleLists = aboutmeday.getContent();

        model.addAttribute("schedules", scheduleLists);
        model.addAttribute("types", types);
        model.addAttribute("statuses", statuses);
        model.addAttribute("page", aboutmeday);

        model.addAttribute("url", "aboutmedaypaging");

        return "daymanage/daymanagepaging";
    }

    @RequestMapping("dayedit")
    private String dayedit(@RequestParam(value = "rcid", required = false) Long rcid,
                           @RequestParam(value = "page", defaultValue = "0") int page,
                           @RequestParam(value = "size", defaultValue = "10") int size,
                           Model model
    ) {
        ps.user(page, size, model);
        List<SystemTypeList> types = typedao.findByTypeModel("aoa_schedule_list");
        List<SystemStatusList> statuses = statusdao.findByStatusModel("aoa_schedule_list");
        ScheduleList rc = null;
        if (rcid != null) {
            rc = daydao.findOne(rcid);
            System.out.println(rc);
        }

        model.addAttribute("types", types);
        model.addAttribute("statuses", statuses);
        model.addAttribute("rc", rc);
        return "daymanage/editday";
    }

    @RequestMapping("addandchangeday")
    public String addandchangeday(ScheduleList scheduleList, @RequestParam("shareuser") String shareuser, BindingResult br,
                                  @SessionAttribute("userId") Long userid) {
        User user = udao.findOne(userid);
        System.out.println(shareuser);
        List<User> users = new ArrayList<>();

        System.out.println(users.size());
        StringTokenizer st = new StringTokenizer(shareuser, ";");
        StringBuilder toUsers = new StringBuilder();
        while (st.hasMoreElements()) {
            User toUser = udao.findByUserName(st.nextToken());
            toUsers.append(toUser.getEamil() + "|");
            users.add(toUser);
        }
        toUsers.deleteCharAt(toUsers.lastIndexOf("|"));
        scheduleList.setUser(user);
        if (users.size() > 0) {
            scheduleList.setUsers(users);
        }
        System.out.println(scheduleList);
        //理论上应该加上toUsers，为了演示出效果先注掉
        //WxCpMessage wxCpMessage = WxCpMessage.TEXT().agentId(agentId).content(scheduleList.getTitle()+"\n 起止时间:"+scheduleList.getCreateTime()
        //				+"--"+scheduleList.getEndTime()+"\n"+"详细信息:"+scheduleList.getDescribe()).toUser(toUsers.toString()).build();
        WxCpMessage wxCpMessage = WxCpMessage.TEXT().toUser("@all").agentId(agentId).content(scheduleList.getTitle() + "\n 起止时间:" + scheduleList.getCreateTime()
                + "--" + scheduleList.getEndTime() + "\n" + "详细信息:" + scheduleList.getDescribe()).build();
        try {
            WxCpMessageSendResult result = service.send(wxCpMessage);
            //发送成功，持久化
            if (result.getErrCode() == 0) {
                daydao.save(scheduleList);
            }
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
        return "/daymanage";
    }

    @RequestMapping("dayremove")
    public String dayremove(@RequestParam(value = "rcid") Long rcid) {
        ScheduleList rc = daydao.findOne(rcid);

        daydao.delete(rc);

        return "/daymanage";
    }

    /**
     * 一下是日历controller
     *
     * @return
     */
    @RequestMapping("daycalendar")
    private String daycalendar() {
        return "daymanage/daycalendar";
    }

//	@RequestMapping("mycalendarload")
//	public void mycalendarload(@SessionAttribute("userId") Long userid,HttpServletResponse response) throws IOException{
//		List<ScheduleList> se = dayser.aboutmeschedule(userid);
//		
//		for (ScheduleList scheduleList : se) {
//			System.out.println(scheduleList);
//		}
//		
//		String json = JSONObject.toJSONString(se);
//		response.setHeader("Cache-Control", "no-cache");
//		response.setContentType("text/json;charset=UTF-8");
//		response.getWriter().write(json);
//		
//	}

    @RequestMapping("mycalendarload")
    public @ResponseBody
    List<ScheduleList> mycalendarload(@SessionAttribute("userId") Long userid, HttpServletResponse response) throws IOException {
        List<ScheduleList> se = dayser.aboutmeschedule(userid);

        return se;
    }
}
