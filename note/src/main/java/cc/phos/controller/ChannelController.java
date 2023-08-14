package cc.phos.controller;

import cc.phos.core.response.R;
import cc.phos.entity.ChannelEntity;
import cc.phos.mapper.IChannelMapper;
import cc.phos.service.IChannelService;
import cc.phos.service.impl.IChannelServiceImpl;
import cc.phos.vo.channel.CreateChannelVO;
import cc.phos.vo.channel.SortChannelVO;
import cc.phos.vo.channel.UpdateChannelVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/channel")
@AllArgsConstructor
@Validated
public class ChannelController {

    private final IChannelMapper channelMapper;
    private final IChannelServiceImpl channelService;

    @PostMapping("/create")
    public R create(@Valid @RequestBody CreateChannelVO vo) {
        ChannelEntity channelEntity = new ChannelEntity();
        // 检查数据库中是否已经存在相同名称的频道
        LambdaQueryWrapper<ChannelEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ChannelEntity::getChannelName, vo.getChannelName());
        Long count = channelMapper.selectCount(queryWrapper);
        if (count != 0) {
            return R.error("频道名称已存在");
        }
        BeanUtils.copyProperties(vo, channelEntity);
        channelMapper.insert(channelEntity);
        return R.ok();
    }

    @DeleteMapping("/delete/{id}")
    public R delete(@PathVariable("id") Long id) {
        ChannelEntity channelEntity = channelMapper.selectById(id);
        if (channelEntity == null) {
            return R.error("频道不存在");
        }
        channelMapper.deleteById(id);
        return R.ok();
    }

    @PatchMapping("/update/{id}")
    public R update(@Valid @RequestBody UpdateChannelVO vo, @PathVariable("id") Long id) {
        ChannelEntity channelEntity = channelMapper.selectById(id);
        if (channelEntity == null) {
            return R.error("频道不存在");
        }
        // 检查数据库中是否已经存在相同名称的频道
        LambdaQueryWrapper<ChannelEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ChannelEntity::getChannelName, vo.getChannelName());
        Long count = channelMapper.selectCount(queryWrapper);
        if (count != 0) {
            return R.error("频道名称已存在");
        }
        BeanUtils.copyProperties(vo, channelEntity);
        channelMapper.updateById(channelEntity);
        return R.ok();
    }

    @GetMapping("/{id}")
    public R getById(@PathVariable("id") Long id) {
        ChannelEntity channelEntity = channelMapper.selectById(id);
        if (channelEntity == null) {
            return R.error("频道不存在");
        }
        return R.ok(channelEntity);
    }

    @GetMapping("/list")
    public R list() {
        LambdaQueryWrapper<ChannelEntity> queryWrapper = new LambdaQueryWrapper<>();
        List<ChannelEntity> list = channelService.list();
        // sort list by sort field
        list.sort((o1, o2) -> (int) (o1.getSort() - o2.getSort()));
        return R.ok(list);
    }

    @Transactional
    @PatchMapping("/sort")
    public R sort(@Validated @RequestBody SortChannelVO[] vos) {
        List<ChannelEntity> list = Arrays.stream(vos).map(vo -> {
            ChannelEntity channelEntity = new ChannelEntity();
            BeanUtils.copyProperties(vo, channelEntity);
            return channelEntity;
        }).toList();
        channelService.updateBatchById(list);
        return R.ok();
    }

}
