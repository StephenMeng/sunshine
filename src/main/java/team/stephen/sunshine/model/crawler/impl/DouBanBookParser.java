package team.stephen.sunshine.model.crawler.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import team.stephen.sunshine.dto.book.BookDto;
import team.stephen.sunshine.model.crawler.Parser;

import java.util.List;

public class DouBanBookParser implements Parser<BookDto> {
    @Override
    public List<BookDto> parse(String html) {
        JSONObject jsonObject = JSONObject.parseObject(html);
        JSONArray jsonArray = jsonObject.getJSONArray("books");
        return jsonArray.toJavaList(BookDto.class);
    }
}
