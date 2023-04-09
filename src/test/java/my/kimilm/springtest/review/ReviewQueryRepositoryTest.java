package my.kimilm.springtest.review;

import my.kimilm.springtest.comment.CommentA;
import my.kimilm.springtest.comment.CommentB;
import my.kimilm.springtest.comment.CommentC;
import my.kimilm.springtest.comment.parent.Comment;
import my.kimilm.springtest.comment.parent.CommentRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@SpringBootTest
class ReviewQueryRepositoryTest {

    @Autowired
    public CommentRepository commentRepository;

    @Autowired
    public ReviewRepository reviewRepository;

    @Autowired
    public ReviewQueryRepository queryRepository;

    @BeforeEach
    void setUp() {
        Review review1 = new Review(null, new ArrayList<>());
        Review review2 = new Review(null, new ArrayList<>());
        Review review3 = new Review(null, new ArrayList<>());
        Review review4 = new Review(null, new ArrayList<>());

        CommentA commentA = new CommentA(1);
        CommentB commentB = new CommentB("One");
        CommentC commentC = new CommentC('1');

        CommentA commentA2 = new CommentA(2);
        CommentB commentB2 = new CommentB("Two");

        commentA.setReview(review1);
        review1.getComments().add(commentA);

        commentB.setReview(review2);
        review2.getComments().add(commentB);

        commentC.setReview(review3);
        review3.getComments().add(commentC);

        commentA2.setReview(review4);
        commentB2.setReview(review4);
        review4.getComments().addAll(List.of(commentA2, commentB2));

        reviewRepository.saveAll(List.of(review1, review2, review3, review4));
    }

    @Test
    void findAllReviews() {
        List<Review> reviews = reviewRepository.findAll();
        Assertions.assertThat(reviews.size()).isEqualTo(4);
    }

    @Test
    void findAllComments() {
        List<Comment> comments = commentRepository.findAll();
        Assertions.assertThat(comments.size()).isEqualTo(5);
    }

    @Test
    void findReviewByCommentInstanceTwoQuery() {
        List<Review> reviews = queryRepository.findReviewByCommentInstanceTwoQuery(List.of("CommentA"));
        Set<String> dTypes = reviews.stream()
                .flatMap(review -> review.getComments().stream())
                .map(Comment::getDtype)
                .collect(Collectors.toSet());

        Assertions.assertThat(reviews.size()).isEqualTo(2);
        Assertions.assertThat(dTypes).containsExactly("CommentB", "CommentC");
    }

    @Test
    void findReviewByCommentInstancesTwoQuery() {
        List<Review> reviews = queryRepository.findReviewByCommentInstanceTwoQuery(List.of("CommentA", "CommentB"));
        Set<String> dTypes = reviews.stream()
                .flatMap(review -> review.getComments().stream())
                .map(Comment::getDtype)
                .collect(Collectors.toSet());

        Assertions.assertThat(reviews.size()).isEqualTo(1);
        Assertions.assertThat(dTypes).containsExactly("CommentC");
    }

    @Test
    void findBySubQuery() {
        List<Review> reviews = queryRepository.findBySubQuery(List.of("CommentA"));
        Set<String> dTypes = reviews.stream()
                .flatMap(review -> review.getComments().stream())
                .map(Comment::getDtype)
                .collect(Collectors.toSet());

        Assertions.assertThat(reviews.size()).isEqualTo(2);
        Assertions.assertThat(dTypes).containsExactly("CommentB", "CommentC");
    }

    @Test
    void findBySubQueryTwo() {
        List<Review> reviews = queryRepository.findBySubQuery(List.of("CommentA", "CommentB"));
        Set<String> dTypes = reviews.stream()
                .flatMap(review -> review.getComments().stream())
                .map(Comment::getDtype)
                .collect(Collectors.toSet());

        Assertions.assertThat(reviews.size()).isEqualTo(1);
        Assertions.assertThat(dTypes).containsExactly("CommentC");
    }

    @AfterEach
    void tearDown() {
        reviewRepository.deleteAll();
    }
}