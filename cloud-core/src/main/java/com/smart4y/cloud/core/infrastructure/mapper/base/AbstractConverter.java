package com.smart4y.cloud.core.infrastructure.mapper.base;

import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 实体与DTO转换 基类
 *
 * @author Youtao
 *         Created by youtao on 2018/11/7.
 */
public abstract class AbstractConverter<Source, Target> {

    public Target convert(Source source) {
        return convert(source, Collections.emptyMap());
    }

    public abstract Target convert(Source source, Map<String, Object> parameters);

    public List<Target> convertToList(Collection<Source> sources) {
        return convertToList(sources, Collections.emptyMap());
    }

    public final List<Target> convertToList(Collection<Source> sources, Map<String, Object> parameters) {
        if (sources == null) {
            return null;
        }
        List<Target> targets = new ArrayList<>(sources.size());
        for (Source source : sources) {
            targets.add(convert(source, parameters));
        }
        return targets;
    }

    public PageInfo<Target> convertToPage(PageInfo<Source> sourcePage) {
        return convertToPage(sourcePage, Collections.emptyMap());
    }

    public PageInfo<Target> convertToPage(PageInfo<Source> sourcePage, Map<String, Object> parameters) {
        PageInfo<Target> targetPage = new PageInfo<>();
        BeanUtils.copyProperties(sourcePage, targetPage);
        targetPage.setList(null);

        List<Target> targets = sourcePage.getList().stream()
                .map(x -> convert(x, parameters))
                .collect(Collectors.toList());
        targetPage.setList(targets);

        return targetPage;
    }
}