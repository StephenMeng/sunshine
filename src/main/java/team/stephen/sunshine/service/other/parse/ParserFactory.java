package team.stephen.sunshine.service.other.parse;

import com.google.common.collect.Maps;
import team.stephen.sunshine.service.other.parse.impl.cssci.CssciPageParser;
import team.stephen.sunshine.service.other.parse.impl.cssci.CssciArticleOverViewParser;
import team.stephen.sunshine.service.other.parse.impl.weibo.WeiboParser;
import team.stephen.sunshine.service.other.parse.impl.weibo.WeiboSearchPageNumParserImpl;
import team.stephen.sunshine.service.other.parse.impl.weibo.WeiboSearchParser;

import java.util.Map;
import java.util.Optional;

/**
 * @author Stephen
 * @date 2019/03/18 23:25
 */
public final class ParserFactory {
    public static final ParserFactory INSTANCE = new ParserFactory();
    private static final Map<ParserType, Parser> PARSER_MAP = Maps.newHashMap();

    static {
        PARSER_MAP.put(ParserType.WEIBO, new WeiboParser());
        PARSER_MAP.put(ParserType.WEIBO_SEARCH, new WeiboSearchParser());
        PARSER_MAP.put(ParserType.WEIBO_SEARCH_PAGE_NUM, new WeiboSearchPageNumParserImpl());
        PARSER_MAP.put(ParserType.CSSCI_PAGE, new CssciPageParser());
        PARSER_MAP.put(ParserType.CSSCI_ARTICLE_OVERVIEW, new CssciArticleOverViewParser());

    }

    private ParserFactory() {

    }

    public Optional<Parser> getParser(ParserType type) {
        return Optional.ofNullable(PARSER_MAP.get(type));
    }
}
