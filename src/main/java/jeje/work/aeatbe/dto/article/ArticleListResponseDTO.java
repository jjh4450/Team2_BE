package jeje.work.aeatbe.dto.article;

import java.util.List;
import lombok.*;

/**
 * 칼럼 리스트 반환할 때 사용하는 반환 형식입니다.
 */
public record ArticleListResponseDTO(
    List<ArticleResponseDTO> columns,
    String nextPageToken,
    PageInfoDTO pageInfo
) {
    @Builder
    public ArticleListResponseDTO {}
}