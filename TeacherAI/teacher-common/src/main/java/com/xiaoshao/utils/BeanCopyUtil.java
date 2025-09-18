package com.xiaoshao.utils;

import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * 工具类，进行列表复制
 */
public class BeanCopyUtil {
    public static <S, T> List<T> copyListProperties(List<S> sources, Supplier<T> targetSupplier) {
        return sources.stream()
                .map(source -> {
                    T target = targetSupplier.get();
                    BeanUtils.copyProperties(source, target);
                    return target;
                })
                .collect(Collectors.toList());
    }
}