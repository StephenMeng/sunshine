package team.stephen.sunshine.service.common.impl;


import com.alibaba.fastjson.JSONObject;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.stephen.sunshine.constant.enu.Topic;
import team.stephen.sunshine.exception.NullParamException;
import team.stephen.sunshine.exception.WrongClassTypeException;
import team.stephen.sunshine.service.common.KafkaService;
import team.stephen.sunshine.util.common.LogRecod;

/**
 * @author Stephen
 */
@Service
public class KafkaServiceImpl implements KafkaService {

    @Autowired
    private Producer producer;

    @Override
    public void produce(Topic topic, Object obj) throws WrongClassTypeException, NullParamException {
        if (obj == null || topic == null) {
            throw new NullParamException("参数不能为空");
        }
        if (!topic.getCls().isAssignableFrom(obj.getClass())) {
            throw new WrongClassTypeException(obj.getClass().getName() + " 不是 topic[" + topic.getCls().getName() + "] 的类或子类");
        }
        ProducerRecord<String, String> record = new ProducerRecord<>(topic.getName(), "key", JSONObject.toJSONString(obj));
        try {
            producer.send(record);
            LogRecod.print("produce success:" + obj.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
