package my.kimilm.springtest.comment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import my.kimilm.springtest.comment.parent.Comment;

import javax.persistence.Entity;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentC extends Comment {

    private Character cValue;
}

