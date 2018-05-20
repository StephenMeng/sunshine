package team.stephen.sunshine.model.crawler;

import java.util.List;

public interface Parser<T> {
    List<T> parse(String html);
}
