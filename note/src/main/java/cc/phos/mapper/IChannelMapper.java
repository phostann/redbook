package cc.phos.mapper;


import cc.phos.entity.ChannelEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IChannelMapper extends BaseMapper<ChannelEntity> {
//    void batchUpdate(@Param("channels") ChannelEntity[] channels);
}