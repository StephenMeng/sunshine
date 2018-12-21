package team.stephen.sunshine.service.other;

import java.util.List;

/**
 * Created by stephen on 2017/10/22.
 */
public interface Parser<T> {
    public List<T> parse(String html);

    T parseDetail(String html);
}
