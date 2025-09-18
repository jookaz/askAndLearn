package com.xiaoshao.result;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaoshao.utils.BeanCopyUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 封装分页查询结果
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageResult<T> implements Serializable {

    private long total; //总记录数

    private List<T> records; //当前页数据集合
//返回空列表
    public static <V,P> PageResult<V> empty(Page<P> page){
        return new PageResult<>(page.getTotal(), Collections.emptyList());
    }
//    默认使用的转换方式利用BeanCopyUtil进行转换，这里P指的是实体类型，V指的是VO类型
    public static <V,P> PageResult<V> of(Page<P> page, Supplier<V> targetSupplier){
        List<P> records = page.getRecords();
        if (records == null || records.isEmpty()) {
            return empty(page);
        }
        List<V> vos = BeanCopyUtil.copyListProperties(records, targetSupplier);
        return new PageResult<>(page.getTotal(), vos);
    }
//    自定义分页结果转为vo分页结果的函数允许用户自定义PO-VO的转换方式
    public static <V,P> PageResult<V> of(Page<P> page, Function<P,V> convertor){
        List<P> records = page.getRecords();
//        非空检验
        if (records == null || records.isEmpty()) {
           return empty(page);
        }
//        进行转换
        List<V> list = records.stream().map(convertor).toList();
//        封装返回
        return new PageResult<>(page.getTotal(), list);
    }

}
