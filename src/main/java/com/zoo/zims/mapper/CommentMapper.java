package com.zoo.zims.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zoo.zims.entity.Comment;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommentMapper extends BaseMapper<Comment> {
}