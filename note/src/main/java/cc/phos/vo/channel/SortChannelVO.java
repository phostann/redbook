package cc.phos.vo.channel;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SortChannelVO {
    @NotNull(message = "频道ID不能为空")
    private Long id;
    @NotNull(message = "排序值不能为空")
    private Long sort;
}
