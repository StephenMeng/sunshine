package team.stephen.sunshine.service.common;


import team.stephen.sunshine.constant.enu.Topic;
import team.stephen.sunshine.exception.NullParamException;
import team.stephen.sunshine.exception.WrongClassTypeException;

public interface KafkaService {
    void produce(Topic topic, Object obj) throws WrongClassTypeException, NullParamException;
}
