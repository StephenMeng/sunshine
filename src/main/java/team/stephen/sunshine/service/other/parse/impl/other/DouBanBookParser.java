package team.stephen.sunshine.service.other.parse.impl.other;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import team.stephen.sunshine.service.other.parse.Parser;
import team.stephen.sunshine.web.dto.book.BookDto;

import java.util.List;

public class DouBanBookParser implements Parser<BookDto> {
    @Override
    public List<BookDto> parse(String html) {
        JSONObject jsonObject = JSONObject.parseObject(html);
        JSONArray jsonArray = jsonObject.getJSONArray("books");
        return jsonArray.toJavaList(BookDto.class);
    }
}
