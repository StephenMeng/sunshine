package team.stephen.sunshine.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import team.stephen.sunshine.constant.enu.Topic;
import team.stephen.sunshine.exception.NullParamException;
import team.stephen.sunshine.exception.WrongClassTypeException;
import team.stephen.sunshine.model.common.Email;
import team.stephen.sunshine.model.common.HistoryLog;
import team.stephen.sunshine.service.common.KafkaService;
import team.stephen.sunshine.service.common.MailService;
import team.stephen.sunshine.util.common.Response;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Stephen
 */
@RestController
@RequestMapping("message")
public class MessageController extends BaseController {
    @Autowired
    private KafkaService kafkaService;
    @Autowired
    private MailService mailService;

    @RequestMapping(value = "produce", method = RequestMethod.GET)
    public Response produce() {

        for (int i = 0; i < 10; i++) {
            HistoryLog log = new HistoryLog();
            log.setId((long) i);
            log.setDescription("description : " + i);
            if (i / 2 == 0) {
                log = null;
            }
            try {
                kafkaService.produce(Topic.LOG, log);
            } catch (WrongClassTypeException | NullParamException e) {
                e.printStackTrace();
            }
        }
        return Response.success(true);
    }

    @RequestMapping(value = "mail", method = RequestMethod.GET)
    public Response produceMail() {
        for (int i = 0; i < 1; i++) {
            String address = "mengfansai147@163.com";
            String[] cc = {"1581284397@qq.com"};
            StringBuffer sb = new StringBuffer();
            sb.append("<h1>大标题-h1</h1>")
                    .append("<p style='color:#F00'>红色字</p>")
                    .append("<p style='text-align:right'>右对齐</p>");
            Email email = new Email();
            email.setFromAddress(address);
            email.setHtml(true);
            email.setToAddress(address);
            email.setCcAddress(cc);
            email.setSubject("测试： kafka  html邮件");
            Map<String, Object> content = new HashMap<>();
            content.put("name", email.getToAddress());
            email.setContent(mailService.template("mail/test", content));
            try {
                kafkaService.produce(Topic.EMAIL, email);
            } catch (WrongClassTypeException | NullParamException e) {
                e.printStackTrace();
            }
        }

        return Response.success(true);
    }
}
