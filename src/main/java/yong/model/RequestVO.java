package yong.model;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import yong.constants.ContentsTypeCode;

@Getter
@ToString
@Builder
public class RequestVO {
    @NotNull
    private ContentsTypeCode contentsTypeCode;
    private String title;
    private String contents;
}
