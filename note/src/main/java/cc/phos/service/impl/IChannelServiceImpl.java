package cc.phos.service.impl;

import cc.phos.entity.ChannelEntity;
import cc.phos.mapper.IChannelMapper;
import cc.phos.service.IChannelService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class IChannelServiceImpl extends ServiceImpl<IChannelMapper, ChannelEntity> implements IChannelService {
}
