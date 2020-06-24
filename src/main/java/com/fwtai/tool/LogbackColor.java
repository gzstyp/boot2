package com.fwtai.tool;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.pattern.color.ANSIConstants;
import ch.qos.logback.core.pattern.color.ForegroundCompositeConverterBase;

/**
 * Logback自定义日志颜色
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2019-04-10 16:03
 * @QQ号码 444141300
 * @官网 http://www.fwtai.com
*/
public final class LogbackColor extends ForegroundCompositeConverterBase<ILoggingEvent>{

    @Override
    protected String getForegroundColorCode(final ILoggingEvent event){

        Level level = event.getLevel();
        switch (level.toInt()) {
            //ERROR等级为红色
            case Level.ERROR_INT:
                return ANSIConstants.RED_FG;
            //WARN等级为黄色
            case Level.WARN_INT:
                return ANSIConstants.YELLOW_FG;
            //INFO等级为蓝色
            case Level.INFO_INT:
                return ANSIConstants.BLUE_FG;
            //DEBUG等级为绿色
            case Level.DEBUG_INT:
                return ANSIConstants.GREEN_FG;
            //其他为默认颜色
            default:
                return ANSIConstants.DEFAULT_FG;
        }
    }
}