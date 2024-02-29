package yong.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import yong.annotation.Enum;
import yong.constants.ContentsTypeCode;

@Getter
@ToString
@Builder
public class RequestVO {
    @Enum(enumClass = ContentsTypeCode.class
            , message = "유효하지 않은 ContentsTypeCode 입니다."
            , excludeEnumType = {"FEED"}
            , ignoreCase = true)
    private String contentsTypeCode;
    private String title;
    private String contents;
}
