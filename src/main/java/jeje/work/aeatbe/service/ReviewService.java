package jeje.work.aeatbe.service;

import java.util.List;
import java.util.stream.Collectors;
import jeje.work.aeatbe.dto.ReviewDTO;
import jeje.work.aeatbe.dto.UserDTO;
import jeje.work.aeatbe.entity.Product;
import jeje.work.aeatbe.entity.Review;
import jeje.work.aeatbe.entity.User;
import jeje.work.aeatbe.repository.ProductRepository;
import jeje.work.aeatbe.repository.ReviewRepository;
import jeje.work.aeatbe.repository.UserRepository;
import org.springframework.stereotype.Service;

/**
 * 리뷰 서비스 레이어
 */
@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public ReviewService(ReviewRepository reviewRepository, UserRepository userRepository, ProductRepository productRepository) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    /**
     * 리뷰 조회
     *
     * @param searchByUser 마이페이지 여부(true = 마이페이지, false = 단순 상품 리뷰)
     * @param productId    상품 id
     * @param token        유저 토큰
     * @return list 형식의 reviewDTO
     */
    public List<ReviewDTO> getReviews(boolean searchByUser, Long productId, String token) {
        List<Review> reviews = List.of();
        if (searchByUser) {
            if (token == null || token.isEmpty()) {
                throw new IllegalStateException("로그인이 필요합니다.");
            }
            // 여기서 토큰을 디코딩하여 사용자 ID 추출하는 코드 추가 예정 (token)
            Long userId = 1L;
            reviews = reviewRepository.findByUserId(userId);

        } else{
            reviews = reviewRepository.findByProductId(productId);
        }

        if (reviews.isEmpty()) {
            throw new IllegalArgumentException("해당 product_id로 조회된 리뷰가 없습니다.");
        }

        return reviews.stream()
            .map(review -> new ReviewDTO(
                review.getId(),
                review.getRate(),
                review.getContent(),
                new UserDTO(
                    review.getUser().getId(),
                    review.getUser().getUserName(),
                    review.getUser().getUserImgUrl()
                ),
                review.getProduct().getId()
            ))
            .collect(Collectors.toList());
    }

    /**
     * 새 리뷰 생성
     *
     * @param reviewDTO 리뷰 DTO
     * @param token     유저 토큰
     */
    public void createReview(ReviewDTO reviewDTO, String token) {

        // 현재는 임시로 이렇게 해 두고 토큰이 생기면 수정하겠습니다
        Long userId = reviewDTO.getUser().getId();

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        Product product = productRepository.findById(reviewDTO.getProductId())
            .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));

        Review review = new Review(
            reviewDTO.getId(),
            reviewDTO.getRate(),
            reviewDTO.getContent(),
            user,
            product
        );

        reviewRepository.save(review);

    }

    /**
     * 리뷰 수정
     *
     * @param id        리뷰 id
     * @param reviewDTO 리뷰 DTO
     */
    public void updateReviews(Long id, ReviewDTO reviewDTO) {
        Review existingReview = reviewRepository.findById(id)
            .orElseThrow(()-> new IllegalArgumentException("해당 상품의 리뷰가 존재하지 않습니다."));

        Review updateReview = new Review(
            existingReview.getId(),
            reviewDTO.getRate(),
            reviewDTO.getContent(),
            existingReview.getUser(),
            existingReview.getProduct()
        );

        reviewRepository.save(updateReview);
    }

    /**
     * 리뷰 삭제
     *
     * @param id 리뷰 id
     */
    public void deleteReviews(Long id) {
        Review existingReview = reviewRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("ID: " + id + "에 대한 리뷰를 찾을 수 없습니다."));

        reviewRepository.delete(existingReview);
    }

}
