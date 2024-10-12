package jeje.work.aeatbe.column_dto;

import java.util.List;
import lombok.*;

/**
 * 칼럼 리스트 반환할 때 사용하는 반환 형식입니다.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ArticleListResponseDTO {
    private List<ArticleResponseDTO> columns;
    private String nextPageToken;
    private PageInfoDTO pageInfo;
}