package com.weather.entity.table;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "station")
public class Station {
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    private String station;
    private String name;
}
