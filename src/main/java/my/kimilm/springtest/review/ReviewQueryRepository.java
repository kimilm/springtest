package my.kimilm.springtest.review;

import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static my.kimilm.springtest.comment.parent.QComment.comment;
import static my.kimilm.springtest.review.QReview.review;

@Repository
@RequiredArgsConstructor
public class ReviewQueryRepository {

    private final JPAQueryFactory queryFactory;

    public List<Review> findReviewByCommentInstanceTwoQuery(List<String> filters) {
        List<Long> reviewIds = queryFactory.select(comment.review.reviewId)
                .from(comment)
                .where(comment.dtype.in(filters)).fetch();

        return queryFactory.selectFrom(review)
                .leftJoin(review.comments, comment).fetchJoin()
                .where(review.reviewId.notIn(reviewIds))
                .fetch();
    }

    public List<Review> findBySubQuery(List<String> filters) {
        return queryFactory.selectFrom(review)
                .leftJoin(review.comments, comment).fetchJoin()
                .where(review.reviewId.notIn(
                        JPAExpressions.select(comment.review.reviewId)
                                .from(comment)
                                .where(comment.dtype.in(filters)))
                ).fetch();
    }
}
