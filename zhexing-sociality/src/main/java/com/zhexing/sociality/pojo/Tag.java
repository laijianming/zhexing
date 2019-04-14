package com.zhexing.sociality.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tag implements Serializable {

    private Long tagId;

    private String tname;

    private Long tcreater;

    private Integer tstatus;

    private Date createTime;
}
