package team.stephen.sunshine.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import team.stephen.sunshine.constant.enu.Topic;
import team.stephen.sunshine.exception.NullParamException;
import team.stephen.sunshine.exception.WrongClassTypeException;
import team.stephen.sunshine.model.common.HistoryLog;
import team.stephen.sunshine.service.common.KafkaService;
import team.stephen.sunshine.util.Response;

/**
 * @author Stephen
 */
@RestController
@RequestMapping("message")
public class MessageController extends BaseController {
    @Autowired
    private KafkaService kafkaService;

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
}
