package com.smart4y.cloud.core.mapper.base;

import com.github.pagehelper.PageInfo;
import com.smart4y.cloud.core.message.page.Page;
import com.smart4y.cloud.core.constant.CommonConstants;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 实体与DTO转换 基类
 *
 * @author Youtao
 *         Created by youtao on 2018/11/7.
 */
public abstract class AbstractConverter<Source, Target> {

    public abstract Target convert(Source source, Map<String, Object> parameters);

    public Target convert(Source source) {
        return this.convert(source, Collections.emptyMap());
    }

    public List<Target> convertList(Collection<Source> sources) {
        return this.convertList(sources, Collections.emptyMap());
    }

    public final List<Target> convertList(Collection<Source> sources, Map<String, Object> parameters) {
        if (sources == null) {
            return Collections.emptyList();
        }
        List<Target> targets = new ArrayList<>(sources.size());
        for (Source source : sources) {
            targets.add(this.convert(source, parameters));
        }
        return targets;
    }

    public Page<Target> convertPage(PageInfo<Source> sources) {
        return this.convertPage(sources, Collections.emptyMap());
    }

    public Page<Target> convertPage(PageInfo<Source> sources, Map<String, Object> parameters) {
        if (null == sources) {
            return new Page<>();
        }
        int total = (int) sources.getTotal();
        List<Target> targets = sources.getList().stream()
                .map(s -> this.convert(s, parameters))
                .collect(Collectors.toList());
        Page<Target> page = new Page<>();
        page.setTotal(total);
        page.setRecords(targets);
        return page;
    }

    public String toLocalDateTime(LocalDateTime localDateTime) {
        if (null == localDateTime) {
            return "";
        }
        return localDateTime.format(CommonConstants.DATE_TIME_FORMATTER);
    }

    public String toLocalDate(LocalDate localDate) {
        if (null == localDate) {
            return "";
        }
        return localDate.format(CommonConstants.DATE_FORMATTER);
    }
}