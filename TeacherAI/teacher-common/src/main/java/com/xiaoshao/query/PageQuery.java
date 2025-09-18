package com.xiaoshao.query;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
@Schema(name = "PageQuery", description = "分页查询对象")
public class PageQuery {
    @Schema(description = "页码")
    private Integer pageNo;
    @Schema(description = "每页条数")
    private Integer pageSize;
    @Schema(description = "排序字段")
    private String sort;
    @Schema(description = "排序方式")
//    默认是升序
    private Boolean isAsc=true;
//TODO这个地方还是需要修改，前端应该有排序的按钮，目前这个函数没有什么作用
    public <T> Page<T> toMpPage(OrderItem... orders) {
        Page<T> page = Page.of(this.pageNo, this.pageSize);
        // 处理 sort 参数（优先级高于 orders）
        if (StringUtils.isNotBlank(sort)) {
            OrderItem primaryOrder = isAsc ?
                    OrderItem.asc(sort) : OrderItem.desc(sort);
            page.addOrder(primaryOrder);
        }
        // 添加额外排序条件
        if (orders != null && orders.length > 0) {
            page.addOrder(orders);
        }
        return page;
    }

    public <T> Page<T> toMpPage(String defaultSortBy, boolean isAsc) {
        if (StringUtils.isBlank(defaultSortBy)) {
            throw new IllegalArgumentException("默认排序字段不能为空");
        }
        OrderItem orderItem = new OrderItem();
        orderItem.setColumn(defaultSortBy);  // 使用实体类属性名（如 updateTime）
        orderItem.setAsc(isAsc);
        return this.toMpPage(orderItem);
    }

    public <T> Page<T> toMpPageDefaultSortByCreateTime() {
        return this.toMpPage("create_time", false);  // 实体类属性名 createTime
    }

    public <T> Page<T> toMpPageDefaultSortByUpdateTime() {
        return this.toMpPage("update_time", false);  // 实体类属性名 updateTime
    }

}
