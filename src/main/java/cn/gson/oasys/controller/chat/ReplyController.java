package cn.gson.oasys.controller.chat;

import java.io.PrintWriter;
import java.util.Date;
import java.util.Objects;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

import cn.gson.oasys.model.dao.discuss.CommentDao;
import cn.gson.oasys.model.dao.discuss.DiscussDao;
import cn.gson.oasys.model.dao.discuss.ReplyDao;
import cn.gson.oasys.model.dao.discuss.VoteTitleListDao;
import cn.gson.oasys.model.dao.discuss.VoteTitlesUserDao;
import cn.gson.oasys.model.dao.user.UserDao;
import cn.gson.oasys.model.entity.discuss.Comment;
import cn.gson.oasys.model.entity.discuss.Discuss;
import cn.gson.oasys.model.entity.discuss.Reply;
import cn.gson.oasys.model.entity.discuss.VoteList;
import cn.gson.oasys.model.entity.discuss.VoteTitleUser;
import cn.gson.oasys.model.entity.discuss.VoteTitles;
import cn.gson.oasys.model.entity.user.User;
import cn.gson.oasys.services.discuss.CommentService;
import cn.gson.oasys.services.discuss.DiscussService;
import cn.gson.oasys.services.discuss.ReplyService;
import cn.gson.oasys.services.discuss.VoteService;

@Controller
@RequestMapping("/")
public class ReplyController {
    @Autowired
    private ReplyDao replyDao;
    @Autowired
    private ReplyService replyService;
    @Autowired
    private UserDao uDao;
    @Autowired
    private DiscussDao discussDao;
    @Autowired
    private DiscussService disService;
    @Autowired
    private CommentDao commentDao;
    @Autowired
    private CommentService commentservice;
    @Autowired
    private VoteTitleListDao voteTitleDao;
    @Autowired
    private VoteService voteService;
    @Autowired
    private VoteTitlesUserDao voteUserDao;

    /**
     * ????????????
     *
     * @param req
     * @return
     */
    @RequestMapping("replyhandle")
    public String reply(HttpServletRequest req,
                        @RequestParam(value = "page", defaultValue = "0") int page,
                        @RequestParam(value = "size", defaultValue = "5") int size,
                        @SessionAttribute("userId") Long userId, Model model) {
        System.out.println(size);
        Long num = null;

        Long discussId = Long.parseLong(req.getParameter("replyId"));
        String module = req.getParameter("module");    //?????????????????????????????????

        User user = uDao.findOne(userId);
        System.out.println(discussId);
        System.out.println(module);
        Discuss dis = null;
        if ("discuss".equals(module)) {
            dis = discussDao.findOne(discussId);
            num = dis.getDiscussId();
        } else {
            Reply replyyy = replyDao.findOne(discussId);
            dis = replyyy.getDiscuss();
            num = dis.getDiscussId();
        }
        if (!StringUtils.isEmpty(req.getParameter("comment"))) {
            String comment = req.getParameter("comment");
            System.out.println(comment);

            if ("discuss".equals(module)) {
                //???????????????-??????
                System.out.println("??????-??????");
                Discuss discuss = discussDao.findOne(discussId);
                Reply reply = new Reply(new Date(), comment, user, discuss);
                num = discuss.getDiscussId();
                replyService.save(reply);
            } else { //???????????????-??????
                System.out.println("??????-??????");
                Reply reply = replyDao.findOne(discussId);
                Comment comment2 = new Comment(new Date(), comment, user, reply);
                commentservice.save(comment2);
                num = reply.getDiscuss().getDiscussId();
            }
            Discuss discuss = discussDao.findOne(num);
            if (user.getSuperman()) {
                model.addAttribute("manage", "??????????????????");
            } else {
                if (Objects.equals(user.getUserId(), discuss.getUser().getUserId())) {
                    model.addAttribute("manage", "??????????????????");
                }
            }
        }
        disService.setDiscussMess(model, num, userId, page, size);
        return "chat/replytable";
    }

    //????????????
    @RequestMapping("likethis")
    public void likeThis(HttpServletRequest req, HttpServletResponse resp, @SessionAttribute("userId") Long userId) {
        resp.setCharacterEncoding("utf-8");
        PrintWriter out = null;
        likeThisFun(req, userId);
//		try {
//			out = resp.getWriter();
//			if(number==1){
//				out.println("??????("+(likenum+1)+")");
//			}else{
//				out.println("???("+(likenum-1)+")");
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
        out.flush();
        out.close();
    }

    private void likeThisFun(HttpServletRequest req, Long userId) {
        Long replyId = Long.parseLong(req.getParameter("replyId"));
        String module = req.getParameter("module");
        int number = 0;
        int likenum = 0;
        User user = uDao.findOne(userId);
        if ("discuss".equals(module)) {
            Discuss discuss = discussDao.findOne(replyId);
            Set<User> users = discuss.getUsers();
            likenum = discuss.getUsers().size();
            if (!discuss.getUsers().contains(user)) {
                System.out.println("??????????????????????????????");
                users.add(user);
                number = 1;
            } else {
                System.out.println("?????????????????????????????????");
                users.remove(user);
                number = -1;
            }
            discuss.setUsers(users);
            disService.save(discuss);
        } else if ("reply".equals(module)) {
            Reply reply = replyDao.findOne(replyId);
            Set<User> users = reply.getUsers();
            likenum = reply.getUsers().size();
            if (!reply.getUsers().contains(user)) {
                System.out.println("??????????????????????????????");
                users.add(user);
                number = 1;
            } else {
                System.out.println("?????????????????????????????????");
                users.remove(user);
                number = -1;
            }
            reply.setUsers(users);
            replyService.save(reply);
        }
    }

    //??????????????????
    @RequestMapping("/replypaging")
    public String replyPaging(HttpServletRequest req,
                              @RequestParam(value = "selecttype") Long selecttype,
                              @RequestParam(value = "selectsort") Long selectsort,
                              @RequestParam(value = "page", defaultValue = "0") int page,
                              @RequestParam(value = "size", defaultValue = "5") int size,
                              @SessionAttribute("userId") Long userId, Model model) {
        System.out.println(size);
        System.out.println(page);
        System.out.println("selecttype:" + selecttype);
        System.out.println("selectsort:" + selectsort);
        Long num = Long.parseLong(req.getParameter("num"));
        disService.discussHandle(model, num, userId, page, size, selecttype, selectsort);

        return "chat/replytable";
    }

    //????????????
    @RequestMapping("/replydelete")
    public String replyDelete(HttpServletRequest req,
                              @RequestParam(value = "page", defaultValue = "0") int page,
                              @RequestParam(value = "size", defaultValue = "5") int size,
                              @SessionAttribute("userId") Long userId, Model model) {
        User user = uDao.findOne(userId);
        System.out.println(size);
        Long num = Long.parseLong(req.getParameter("num"));
        Discuss discuss = discussDao.findOne(num);
        Long discussId = Long.parseLong(req.getParameter("replyId"));
        String module = req.getParameter("module");    //?????????????????????????????????
        if (user.getSuperman()) {
        } else {
            if (Objects.equals(user.getUserId(), discuss.getUser().getUserId())) {
            } else {
                System.out.println("??????????????????????????????");
                return "redirect:/notlimit";
            }
        }
        if ("reply".equals(module)) {
            System.out.println("???????????????");
            Reply reply = replyDao.findOne(discussId);
            replyService.deleteReply(reply);
        } else if ("comment".equals(module)) {
            System.out.println("???????????????");
            commentservice.deleteComment(discussId);
        }
        disService.setDiscussMess(model, num, userId, page, size);
        model.addAttribute("manage", "manage");
        System.out.println(num);
        System.out.println(discussId);
        System.out.println(module);
        return "chat/replytable";
    }

    //????????????
    @RequestMapping("votehandle")
    public String voteHandle(HttpServletRequest req, @SessionAttribute("userId") Long userId, Model model) {
        Long discussId = Long.parseLong(req.getParameter("discussId"));
        Long titleId = Long.parseLong(req.getParameter("titleId"));
        Integer selectOne = Integer.parseInt(req.getParameter("selectType"));
        Discuss discuss = discussDao.findOne(discussId);
        User user = uDao.findOne(userId);
        VoteTitles voteTitle = voteTitleDao.findOne(titleId);
        VoteTitleUser voteTitleUser = new VoteTitleUser(discuss.getVoteList().getVoteId(), voteTitle, user);
        VoteList vote = discuss.getVoteList();
        Date date = new Date();
        if (date.getTime() < vote.getStartTime().getTime()) {
            return "??????????????????";
        } else if (date.getTime() > vote.getEndTime().getTime()) {
            return "??????????????????";
        } else {
            System.out.println("????????????????????????");
            System.out.println("??????????????????");
            model.addAttribute("dateType", 3);
        }
        if (Objects.isNull(voteUserDao.findByVoteTitlesAndUser(voteTitle, user))) {
            voteService.savaVoteTitleUser(voteTitleUser);
        } else {
            return "?????????????????????";
        }

        voteService.voteServiceHandle(model, user, discuss);
        model.addAttribute("discuss", discuss);
        return "chat/votetable";
    }

    //???????????????????????????????????????????????????????????????
    //???????????????????????????????????????????????????id???
    @RequestMapping("likeuserload")
    public String likeUserLoad(HttpServletRequest req, Model model, @SessionAttribute("userId") Long userId) {
        Long replyId = Long.parseLong(req.getParameter("replyId"));
        String module = req.getParameter("module");
        Integer size = Integer.parseInt(req.getParameter("size"));
        User user = uDao.findOne(userId);
        if ("discuss".equals(module)) {
            //?????????????????????????????????
            likeThisFun(req, userId);
            disService.setDiscussMess(model, replyId, userId, 0, size);
//			Discuss discuss=discussDao.findOne(replyId);
//			int discussLikeNum=discuss.getUsers().size();
//			Set<User> setUsers=discuss.getUsers();
//			model.addAttribute("discuss", discuss);
//			model.addAttribute("discussLikeNum", discussLikeNum);
//			model.addAttribute("setUsers", setUsers);
            return "chat/discusslike";
        } else if ("reply".equals(module)) {
            //?????????????????????????????????
            String rightNum = req.getParameter("rightNum");
            likeThisFun(req, userId);
            Reply reply = replyDao.findOne(replyId);
            int likeNum = reply.getUsers().size();
            Set<User> users = reply.getUsers();
            model.addAttribute("rightNum", rightNum);
            model.addAttribute("comments", commentDao.findByReply(reply).size());    //???????????????
            model.addAttribute("reply", reply);                        //????????????????????????????????????
            model.addAttribute("contain", users.contains(user));    //????????????
            model.addAttribute("likeNum", likeNum);                    //???????????????
            model.addAttribute("users", users);                        //?????????????????????
            return "chat/replylike";
        } else {
            //????????????  ???????????????????????????
            return "????????????";
        }
    }

}
