package com.broadwave.toppos.configs;

import com.p6spy.engine.spy.appender.MessageFormattingStrategy;

public class P6spyPrettySqlFormatterProd implements MessageFormattingStrategy {
    @Override
    public String formatMessage(int connectionId, String now, long elapsed, String category, String prepared, String sql, String url) {
        return "[SQLLog] duration : "+ elapsed + "ms | " + sql;
    }

}
