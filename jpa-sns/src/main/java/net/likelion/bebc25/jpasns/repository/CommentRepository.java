package net.likelion.bebc25.jpasns.repository;

import net.likelion.bebc25.jpasns.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
