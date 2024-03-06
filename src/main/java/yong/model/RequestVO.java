package yong.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import yong.constants.ContentsTypeCode;

@Getter
@ToString
@Builder
public class RequestVO {
    private ContentsTypeCode contentsTypeCode;
    private String title;
    private String contents;
}
