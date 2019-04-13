package team.stephen.sunshine.service.other.parse;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import team.stephen.sunshine.util.common.LogRecord;

import java.util.function.Function;

/**
 * Created by stephen on 2017/10/22.
 */
public abstract class BaseParser implements Parser {

    protected String parseItem(Element o, Function<Element, String> fun) {
        try {
            return fun.apply(o);
        } catch (Exception e) {
//            LogRecord.warn("parse item warn :%s", e);
        }
        return null;
    }

    protected String parseItemFromList(Elements o, int index, Function<Element, String> fun) {
        try {
            return fun.apply(o.get(index));
        } catch (Exception e) {
            LogRecord.warn("parse item warn :%s", e);
        }
        return null;
    }
}
